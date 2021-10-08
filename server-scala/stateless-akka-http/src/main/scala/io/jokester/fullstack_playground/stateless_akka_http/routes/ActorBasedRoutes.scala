package io.jokester.fullstack_playground.stateless_akka_http.routes

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Route
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.server.Directives.reject
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

final class ActorBasedRoutes()(implicit val root: ActorSystem[Unit]) {

  def x(): Route = {
    implicit val timeout: Timeout = 3.seconds

    reject()

  }

}
