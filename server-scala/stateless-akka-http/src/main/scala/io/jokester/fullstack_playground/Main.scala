package io.jokester.fullstack_playground

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives.concat
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.stateless_akka_http.actors.PingActor
import io.jokester.fullstack_playground.stateless_akka_http.routes.StatelessRoutes
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

object Main extends App with LazyLogging {

  def initActorSystem(): ActorSystem[Nothing] = {

    val a = ActorSystem(PingActor(), "ping")
    a ! PingActor.StartPing()

    a
  }

  val actorSystem = initActorSystem()

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
      ),
    ),
    port = Option(8082),
  )(actorSystem.classicSystem)
}
