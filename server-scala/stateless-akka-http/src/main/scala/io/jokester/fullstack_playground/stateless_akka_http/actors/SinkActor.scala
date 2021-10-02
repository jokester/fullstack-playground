package io.jokester.fullstack_playground.stateless_akka_http.actors

import akka.actor.typed.Behavior

import java.util.UUID

object SinkActor {
  trait SinkCommand

  def apply(props: UUID): Behavior[SinkActor.SinkCommand] = {
    ???
  }
}
class SinkActor {}
