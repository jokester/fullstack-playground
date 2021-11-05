package io.jokester.fullstack_playground

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.stateless_akka_http.actors.SinkManagerActor
import io.jokester.fullstack_playground.stateless_akka_http.routes.SimpleRoutes
import io.jokester.fullstack_playground.stateless_akka_http.routes.MessageSinkHandler
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

object StatelessAkkaHttpMain extends App with LazyLogging {

  private val actorSystem: ActorSystem[SinkManagerActor.Command] =
    ActorSystem(SinkManagerActor(), "ping")
  private val sinkRoute = new MessageSinkHandler(actorSystem)(actorSystem)

  AkkaHttpServer.listenWithSystem(
    (AkkaHttpServer.withCors & AkkaHttpServer.withRequestLogging) {
      path("stateless-akka-http") {
        concat(
          /**
            * GET /stateless-akka-http/simple/ping
            * GET /stateless-akka-http/simple/echo-headers
            * (any method) /stateless-akka-http/simple/echo-request
            */
          sinkRoute.route,
          /**
            * @see `ActorBasedRoutes`
            */
          SimpleRoutes.route,
        )
      }
    },
    port = Option(8082),
  )(actorSystem.classicSystem)
}
