package io.jokester.fullstack_playground.stateless_akka_http.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.typesafe.scalalogging.LazyLogging

import java.util.UUID

object SinkManagerActor extends LazyLogging {
  sealed trait SinkManagerCommand
  case class GetSink(sinkId: UUID, replyTo: ActorRef[GotSink]) extends SinkManagerCommand
  case class GotSink(x: ActorRef[SinkActor.SinkCommand])       extends SinkManagerCommand

  def apply(): Behaviors.Receive[SinkManagerCommand] = {
    var map: Map[UUID, ActorRef[SinkActor.SinkCommand]] = Map.empty

    Behaviors.receive({
      case (ctx, GetSink(sinkId, replyTo)) =>
        val existed = map.get(sinkId)
        val c       = existed.getOrElse(ctx.spawn(SinkActor(sinkId), s"sink-${sinkId}"))
        map += sinkId -> c
        replyTo ! SinkManagerActor.GotSink(c)

        Behaviors.same

    })
  }
}
