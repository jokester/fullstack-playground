package io.jokester.fullstack_playground.stateless_akka_http.actors

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import java.util.UUID

object SinkActor {
  trait Command

  case class PostMessage(msg: String, replyTo: ActorRef[MessageUpdateBatch]) extends Command
  case class GetMessage(replyTo: ActorRef[MessageUpdateBatch])               extends Command
  case class WaitMessage(replyTo: ActorRef[MessageUpdateBatch])              extends Command
  case class SubscribeMessage(out: ActorRef[MessageUpdateBatch])             extends Command

  case class MessageUpdateBatch(recentMessages: Seq[String]) extends AnyVal

  def apply(props: UUID): Behavior[SinkActor.Command] =
    Behaviors.setup { ctx =>
      val maxMsgCount                                    = 100
      var recentMessages: Seq[String]                    = Seq("1", "2", "3")
      var subscribers: Set[ActorRef[MessageUpdateBatch]] = Set.empty

      Behaviors.receiveMessage({
        case PostMessage(msg, replyTo) =>
          val forSubscribers = MessageUpdateBatch(Seq(msg))
          subscribers.foreach(s => s ! forSubscribers)
          recentMessages = (recentMessages :+ msg).takeRight(maxMsgCount)
          replyTo ! MessageUpdateBatch(recentMessages)
          Behaviors.same

        case GetMessage(replyTo) =>
          replyTo ! MessageUpdateBatch(recentMessages)
          Behaviors.same

      })

    }
}
class SinkActor {}
