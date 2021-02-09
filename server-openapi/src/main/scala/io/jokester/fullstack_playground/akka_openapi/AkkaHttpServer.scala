package io.jokester.fullstack_playground.akka_openapi

import akka.actor.{ActorSystem, ClassicActorSystemProvider}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object AkkaHttpServer extends LazyLogging {

  def listen(inner: Route, interface: Option[String] = None, port: Option[Int] = None): Unit = {
    import scala.concurrent.duration._

    implicit val untypedSystem: ActorSystem                 = ActorSystem("ClassicToTypedSystem")
    implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

    val completeRoute: Route =
      withLogging {
        withCors {
          inner
        }
      }

    val bindingFuture = Http()(new ClassicActorSystemProvider {
      override def classicSystem: ActorSystem = untypedSystem
    }).newServerAt(interface = interface.getOrElse("0.0.0.0"), port = port.getOrElse(8080))
      .bind(completeRoute)
      .map(_.addToCoordinatedShutdown(10.seconds))
      .map(server => {
        logger.debug(s"Server online at http://localhost:8080/")
        server
      })

    for (
      bound   <- bindingFuture;
      stop    <- waitKeyboardInterrupt();
      unbound <- bound.unbind()
    ) {
      untypedSystem.terminate()
    }
  }

  def withLogging: Directive0 =
    extractRequest.flatMap(req => {
      logger.debug(s"Request: ${req.method.name} ${req.uri}")
      pass
    })

  def withCors: Directive0 = {
    extractActorSystem.flatMap((actorSystem: ActorSystem) =>
      cors(
        CorsSettings
          .default(actorSystem)
          .withAllowedMethods(
            Seq(
              HttpMethods.GET,
              HttpMethods.OPTIONS,
              HttpMethods.POST,
              HttpMethods.DELETE,
              HttpMethods.PUT,
              HttpMethods.PATCH,
            ),
          ),
      ),
    )
  }

  def waitKeyboardInterrupt()(implicit executionContext: ExecutionContext): Future[Unit] =
    Option(System.console())
      .map(console =>
        Future[Unit] {
          logger.debug(s"Press ENTER to stop")
          // let it run until user presses return
          console.readLine()
        },
      )
      .getOrElse({
        logger.debug(s"No TTY found. Ignoring console.")
        Future.never
      })
}
