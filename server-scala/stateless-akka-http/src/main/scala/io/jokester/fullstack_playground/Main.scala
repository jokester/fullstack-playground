package io.jokester.fullstack_playground

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.concat
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.stateless_akka_http.actors.{PingActor, SinkManagerActor}
import io.jokester.fullstack_playground.stateless_akka_http.routes.StatelessRoutes
import io.jokester.fullstack_playground.stateless_akka_http.routes.ActorBasedRoutes
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

object Main extends App with LazyLogging {

  private val actorSystem: ActorSystem[SinkManagerActor.Command] =
    ActorSystem(SinkManagerActor(), "ping")
  private val sinkRoute = new ActorBasedRoutes(actorSystem)(actorSystem)

  AkkaHttpServer.listenWithSystem(
    (AkkaHttpServer.withCors & AkkaHttpServer.withRequestLogging)(
      concat(
        /**
          * GET /ping
          */
        StatelessRoutes.pingRoute,
        /**
          * GET /echo-headers
          */
        StatelessRoutes.echoHeadersRoute,
        /**
          * (any method) /echo-request
          */
        StatelessRoutes.echoRequestRoute,
        /**
          * GET /message-sink/{UUID}
          * POST /message-sink/{UUID}
          */
        sinkRoute.route,
      ),
    ),
    port = Option(8082),
  )(actorSystem.classicSystem)
}
