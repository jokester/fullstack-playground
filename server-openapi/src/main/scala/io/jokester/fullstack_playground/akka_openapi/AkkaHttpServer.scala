package io.jokester.fullstack_playground.akka_openapi

import akka.actor.{ActorSystem, ClassicActorSystemProvider}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route, RouteResult}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.scalalogging.LazyLogging

import java.time.Clock
import java.util.UUID
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object AkkaHttpServer extends LazyLogging {

  def listen(inner: Route, interface: Option[String] = None, port: Option[Int] = None): Unit = {
    import scala.concurrent.duration._

    implicit val untypedSystem: ActorSystem                 = ActorSystem("ClassicToTypedSystem")
    implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

    val completeRoute: Route =
      withLogging2 {
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

  def withLogging2: Directive0 = {
    val reqId = UUID.randomUUID()
    logRequestResult((req: HttpRequest) =>
      (res: RouteResult) => {
        logger.debug(s"Request ${reqId}: ${req.method.name} ${req.uri}")
        logger.debug(s"Response ${reqId}: ${res}")
        None
      },
    )
  }

  def withLogging: Directive0 =
    extractRequest.flatMap(req => {
      val reqId    = UUID.randomUUID()
      val reqStart = Clock.systemUTC().millis()
      logger.debug(s"Request ${reqId}: ${req.method.name} ${req.uri}")

      mapResponse(res => {
        logger.debug(
          s"Request ${reqId}: ${res.status.value} in ${Clock.systemUTC().millis() - reqStart}ms",
        )
        res
      })
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
          logger.info(s"Press ENTER to stop")
          // let it run until user presses return
          console.readLine()
        },
      )
      .getOrElse({
        logger.info(s"No TTY found. Ignoring console.")
        Future.never
      })
}
