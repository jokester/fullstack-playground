package io.jokester.fullstack_playground.stateless_akka_http.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.jokester.fullstack_playground.stateless_akka_http.actors.{SinkActor, SinkManagerActor}

import java.time.Clock
import java.util.concurrent.TimeoutException
import scala.concurrent.duration.DurationInt

class ActorBasedRoutes(val sinkManagerActor: ActorRef[SinkManagerActor.Command])(implicit
    val actorSystem: ActorSystem[Nothing],
) extends LazyLogging {

  private val clock = Clock.systemUTC()

  def route: Route = concat(getSinkRoute, postSinkRoute, waitSinkRoute)

  private def getSinkRoute: Route = {
    (get & path("message-sink" / Segment)) { sinkName =>
      sinkByName(sinkName) { sinkActor =>
        askSink[SinkActor.MessageBatch](sinkActor, asker => SinkActor.GetMessage(asker)) {
          messages =>
            complete(messages)
        }
      }
    }
  }

  private def postSinkRoute: Route = {
    (post & path("message-sink" / Segment / "append")) { sinkName =>
      sinkByName(sinkName) { sinkActor =>
        readBodyAsString { msgText =>
          askSink[SinkActor.MessageBatch](
            sinkActor,
            asker =>
              SinkActor.PostMessage(SinkActor.ReceivedMessage(msgText, clock.instant()), asker),
          ) { messages =>
            complete(messages)
          }
        }
      }
    }
  }

  private def waitSinkRoute: Route = {
    (get & path("message-sink" / Segment / "next")) { sinkName =>
      sinkByName(sinkName) { sinkActor =>
        // NOTE akka has a 20s timeout
        implicit val askTimeout: Timeout = 10.seconds
        onComplete(
          sinkActor.ask[SinkActor.MessageBatch](asker => SinkActor.WaitMessage(asker)),
        ) {
          case Success(reply) => complete(reply)
          case Failure(e: TimeoutException) =>
            logger.debug("timeout waiting for reply", e)
            complete(StatusCodes.NotFound, "no new message in 10 seconds")
        }
      }
    }
  }

  private def subscribeSinkRoute: Route = ???

  private def readBodyAsString: Directive1[String] = {
    extractStrictEntity(5.seconds, 16 * 1_024L).map(entity => entity.data.utf8String)
  }

  private def sinkByName(sinkName: String): Directive1[ActorRef[SinkActor.Command]] = {
    implicit val timeout: Timeout = 1.seconds
    onSuccess(
      sinkManagerActor.ask[SinkManagerActor.GotSink](asker =>
        SinkManagerActor.GetSink(sinkName, asker),
      ),
    ).map(gotSink => gotSink.sinkActor)
  }

  private def askSink[Res](
      sinkActor: ActorRef[SinkActor.Command],
      buildMsg: ActorRef[Res] => SinkActor.Command,
  ): Directive1[Res] = {
    implicit val timeout: Timeout = 2.seconds
    onSuccess(sinkActor.ask(buildMsg)).map(tuple => tuple)
  }
}
