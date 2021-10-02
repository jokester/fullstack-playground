package io.jokester.fullstack_playground.stateless_akka_http

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

import java.lang.management.ManagementFactory
import java.time.Clock
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

object Main extends App with LazyLogging {

  case class IncomingHttpHeader(name: String, value: String)
  case class IncomingHttpHeaderDump(incomingHeaders: Seq[IncomingHttpHeader])

  val clock = Clock.systemUTC()

  def pingRoute: Route = {
    (get & path("ping")) {
      complete(
        s"Server running at ${clock.instant()}. Uptime= ${ManagementFactory.getRuntimeMXBean.getUptime}ms",
      )
    }
  }

  def echoHeadersRoute: Route = {
    (get & path("echoHeaders")) {
      extractRequest(req => {
        val dump = IncomingHttpHeaderDump(
          req.headers.map((a: HttpHeader) => IncomingHttpHeader(a.name(), a.value())),
        )
        complete(dump)
      })
    }
  }

  def fullRoute = concat(pingRoute, echoHeadersRoute)

  AkkaHttpServer.listen(
    (AkkaHttpServer.withCors & AkkaHttpServer.withLogging2)(fullRoute),
    port = Option(8082),
  )
}
