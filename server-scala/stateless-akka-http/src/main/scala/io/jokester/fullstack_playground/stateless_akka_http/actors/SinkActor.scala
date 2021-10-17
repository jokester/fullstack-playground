package io.jokester.fullstack_playground.stateless_akka_http.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.typesafe.scalalogging.LazyLogging

import java.time.Instant
import java.util.UUID

object SinkActor extends LazyLogging {
  trait Command

  final case class ReceivedMessage(msg: String, receivedAt: Instant)

  case class PostMessage(msg: ReceivedMessage, replyTo: ActorRef[MessageBatch]) extends Command
  case class GetMessage(replyTo: ActorRef[MessageBatch])                        extends Command
  case class WaitMessage(replyTo: ActorRef[MessageBatch])                       extends Command
  case class SubscribeMessage(replyTo: ActorRef[MessageBatch])                  extends Command
  // TODO: unsubscribe

  case class MessageBatch(sinkName: String, sinkId: UUID, messages: Seq[ReceivedMessage])

  def apply(sinkName: String, sinkId: UUID): Behavior[SinkActor.Command] = {

    def buildBatch(messages: Seq[ReceivedMessage]) = MessageBatch(sinkName, sinkId, messages)

    Behaviors.setup { ctx =>
      val maxMsgCount                          = 10
      var recentMessages: Seq[ReceivedMessage] = Seq.empty
      var subscribers: Set[SubscribeMessage]   = Set.empty
      var waiters: List[WaitMessage]           = List.empty

      Behaviors.receiveMessage({
        case GetMessage(replyTo) =>
          replyTo ! buildBatch(recentMessages)
          Behaviors.same

        case wm @ WaitMessage(_) =>
          waiters :+= wm
          Behaviors.same

        case sm @ SubscribeMessage(out) =>
          subscribers += sm
          out ! buildBatch(recentMessages)
          Behaviors.same

        case PostMessage(msg, replyTo) =>
          recentMessages = (msg +: recentMessages).take(maxMsgCount)
          logger
            .debug("sink={} received new message. {} messages present", sinkId, recentMessages.size)

          val forSubscribers = buildBatch(Seq(msg))
          subscribers.foreach(s => s.replyTo ! forSubscribers)

          waiters.foreach(s => s.replyTo ! forSubscribers)
          waiters = List.empty

          replyTo ! buildBatch(recentMessages)
          Behaviors.same
      })
    }
  }
}
