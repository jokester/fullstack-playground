package io.jokester.fullstack_playground.stateless_akka_http.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import com.typesafe.scalalogging.LazyLogging

import java.util.UUID

object SinkManagerActor extends LazyLogging {
  sealed trait Command
  case class GetSink(sinkId: UUID, replyTo: ActorRef[GotSink]) extends Command
  case class GotSink(sinkActor: ActorRef[SinkActor.Command])   extends Command

  def apply(): Behaviors.Receive[Command] = {
    var map: Map[UUID, ActorRef[SinkActor.Command]] = Map.empty

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
