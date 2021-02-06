package io.jokester.tapir_todoapi

import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

object AkkaHttpServer extends LazyLogging {

  private val routes = {
    import akka.http.scaladsl.server.Directives._
    import akka.http.scaladsl.server.Route
    import sttp.tapir.server.akkahttp._

    Seq[Route](
      TodoApi.endpoints.listTodo.toRoute(_ => Future.successful(ServerLogic.list())),
      TodoApi.endpoints.createTodo.toRoute(req => Future.successful(ServerLogic.create(req))),
      TodoApi.endpoints.deleteTodo.toRoute(req =>
        Future.successful(ServerLogic.remove(req).map(_ => ()))),
      TodoApi.endpoints.updateTodo.toRoute(req =>
        Future.successful(ServerLogic.update(req._1, req._2))),
      openapiRoute
    ).reduce(_ ~ _)
  }

  def openapiRoute: Route = {
    import akka.http.scaladsl.server.Directives._
    import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
    import akka.http.scaladsl.model.ContentTypes._
    import akka.http.scaladsl.model.headers.`Content-Type`

    (get & path("openapi.yaml")) {

      complete(200, Seq(`Content-Type`(ContentTypes.`text/plain(UTF-8)`)), TodoApi.asOpenAPIYaml)
    }

  }

  def main(): Unit = {
    import akka.actor.{ActorSystem, ClassicActorSystemProvider}
    import akka.http.scaladsl.Http
    import akka.http.scaladsl.model.HttpMethods
    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
    import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
    import scala.concurrent.duration._

    implicit val untypedSystem = ActorSystem("ClassicToTypedSystem")

    implicit val executionContext = ExecutionContext.global

    val loggingEnabledRoutes: Route = ctx => {
      logger.debug(s"Request: ${ctx.request.method.name} ${ctx.request.uri}")
      routes(ctx)
    }

    val corsEnabledRoutes = cors(
      CorsSettings.default.withAllowedMethods(
        Seq(
          HttpMethods.GET,
          HttpMethods.OPTIONS,
          HttpMethods.POST,
          HttpMethods.DELETE,
          HttpMethods.PUT,
          HttpMethods.PATCH
        )
      )
    ) {
      loggingEnabledRoutes
    }
    val bindingFuture = Http()(new ClassicActorSystemProvider {
      override def classicSystem: ActorSystem = untypedSystem
    }).newServerAt("0.0.0.0", 8080)
      .bind(
        corsEnabledRoutes
      )
      .map(_.addToCoordinatedShutdown(10.seconds))
      .map(server => {
        logger.debug(s"Server online at http://localhost:8080/")
        server
      })

    for (bound  <- bindingFuture;
         stop   <- waitKeyboardInterrupt();
         unbound <- bound.unbind()) {
      untypedSystem.terminate()
    }
  }

  def waitKeyboardInterrupt()(implicit executionContext: ExecutionContext): Future[Unit] =
    Option(System.console())
      .map(console =>
        Future[Unit] {
          logger.debug(s"Press ENTER to stop")
          // let it run until user presses return
          console.readLine()
      })
      .getOrElse({
        logger.debug(s"No TTY found. Ignoring console.")
        Future.never
      })
}
