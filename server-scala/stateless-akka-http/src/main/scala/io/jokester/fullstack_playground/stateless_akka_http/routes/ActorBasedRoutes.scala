package io.jokester.fullstack_playground.stateless_akka_http.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import akka.util.Timeout
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.jokester.fullstack_playground.stateless_akka_http.actors.{SinkActor, SinkManagerActor}

import java.util.UUID
import scala.concurrent.duration.DurationInt

class ActorBasedRoutes(val sinkManagerActor: ActorRef[SinkManagerActor.Command])(implicit
    val actorSystem: ActorSystem[Nothing],
) {

  def route: Route = concat(getSinkRoute, postSinkRoute)

  def getSinkRoute: Route = {
    (get & path("message-sink" / JavaUUID)) { uuid =>
      sinkByUUID(uuid) { sinkActor =>
        askSink[SinkActor.MessageUpdateBatch](sinkActor, asker => SinkActor.GetMessage(asker)) {
          messages =>
            complete(messages)
        }
      }
    }
  }

  def postSinkRoute: Route = {
    (post & path("message-sink")) {
      reject()
    }
  }

  def waitSinkRoute: Route = ???

  def subscribeSinkRoute: Route = ???

  private def sinkByUUID(uuid: UUID): Directive1[ActorRef[SinkActor.Command]] = {
    implicit val timeout: Timeout = 1.seconds
    onSuccess(
      sinkManagerActor.ask[SinkManagerActor.GotSink](asker => SinkManagerActor.GetSink(uuid, asker)),
    ).map(gotSink => gotSink.sinkActor)
  }

  private def askSink[Res](
      sinkActor: ActorRef[SinkActor.Command],
      buildMsg: ActorRef[Res] => SinkActor.Command,
  ): Directive1[Res] = {
    implicit val timeout: Timeout = 1.seconds
    onSuccess(sinkActor.ask(buildMsg)).map(tuple => tuple)
  }
}
