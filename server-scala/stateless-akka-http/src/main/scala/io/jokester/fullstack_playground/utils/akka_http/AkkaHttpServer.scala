package io.jokester.fullstack_playground.utils.akka_http

import akka.actor.typed.ActorSystem
import akka.actor.{ActorSystem => UntypedActorSystem}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives.{
  extractActorSystem,
  extractLog,
  extractRequest,
  logResult,
}
import akka.http.scaladsl.server.directives.LoggingMagnet
import akka.http.scaladsl.server.{Directive0, Route, RouteResult}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.scalalogging.LazyLogging

import java.time.Clock
import java.util.UUID
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object AkkaHttpServer extends LazyLogging {

  /**
    * Listen with an ad-hoc `ActorSystem`
    * and block until keypress
    */
  def listen(rootRoute: Route, interface: Option[String] = None, port: Option[Int] = None): Unit = {
    val typedSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "adhoc-system")
    listenWithSystem(rootRoute, interface, port)(untypedSystem = typedSystem.classicSystem)
  }

  /**
    * Listen with provided `ActorSystem`
    * and block until keypress
    */
  def listenWithSystem(
      rootRoute: Route,
      interface: Option[String] = None,
      port: Option[Int] = None,
  )(implicit untypedSystem: UntypedActorSystem): Unit = {
    import scala.concurrent.duration._

    implicit val executionContext: ExecutionContextExecutor = untypedSystem.getDispatcher

    val bindingFuture = Http()
      .newServerAt(interface = interface.getOrElse("0.0.0.0"), port = port.getOrElse(8080))
      .bind(rootRoute)
      .map(_.addToCoordinatedShutdown(10.seconds))
      .map(server => {
        logger.debug(s"Server online at http://localhost:${port.getOrElse(8080)}/")
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

  def withRequestLogging: Directive0 = {
    extractRequest.flatMap(req => {
      extractLog.flatMap(logger => {
        val reqId      = UUID.randomUUID()
        val reqStartAt = System.currentTimeMillis()
        logger.debug(s"req[${reqId}]: ${req.method.value} ${req.uri}")
        logResult(
          LoggingMagnet(_logger =>
            _res => {
              logger.debug(
                s"req[${reqId}]: got ${_res} in ${System.currentTimeMillis() - reqStartAt}ms",
              )
            },
          ),
        )
      })
    })
  }

  def withCors: Directive0 = {
    extractActorSystem.flatMap(actorSystem =>
      cors(
        CorsSettings
          .default(actorSystem.classicSystem)
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
