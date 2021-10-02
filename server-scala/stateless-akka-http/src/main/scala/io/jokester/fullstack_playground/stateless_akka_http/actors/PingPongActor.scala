package io.jokester.fullstack_playground.stateless_akka_http.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration.DurationInt

object PingActor extends LazyLogging {
  trait Command

  case class StartPing()                                    extends Command
  private case class WrappedPongRes(res: PongActor.PongRes) extends Command

  def apply(): Behavior[Command] = {
    Behaviors.setup[Command] { ctx =>
      val pongActor = ctx.spawn(PongActor(), "pong")
      // adapter: convert other actor's response to own known format
      val pongResponseMapper: ActorRef[PongActor.PongRes] =
        ctx.messageAdapter(res => WrappedPongRes(res))

      def active(): Behavior[Command] = {

        Behaviors.receiveMessage({
          case StartPing() =>
            logger.debug(s"received StartPing")
            pongActor ! PongActor.Pong(pongResponseMapper)
            Behaviors.same

          case WrappedPongRes(pong @ PongActor.PongRes(_)) =>
            logger.debug(s"received pong: ${pong}")
            ctx.scheduleOnce(
              1.seconds,
              pongActor,
              PongActor.Pong(pongResponseMapper),
            )
            Behaviors.same
        })
      }

      active()
    }
  }

}

object PongActor {
  trait Command
  case class Pong(replyTo: ActorRef[PongRes]) extends Command
  case class PongRes(count: Int)              extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup[Command] { ctx =>
      var count = 0

      Behaviors.receiveMessage[Command]({
        case Pong(replyTo) =>
          count += 1
          replyTo ! PongRes(count)
          Behaviors.same
      })
    }

}
