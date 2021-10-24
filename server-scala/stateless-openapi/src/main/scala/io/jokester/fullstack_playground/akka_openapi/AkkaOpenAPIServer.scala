package io.jokester.fullstack_playground.akka_openapi

import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.Directives._
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml.RichOpenAPI

object AkkaOpenAPIServer {
  def optionalPrefix(prefix: Option[String]): Directive0 = {
    prefix.map(str => pathPrefix(str)).getOrElse(pass)
  }

  def openapiRoute(apiDecl: OpenAPI): Route = {
    (get & path("openapi.yaml")) {
      complete(200, Seq(`Content-Type`(ContentTypes.`application/octet-stream`)), apiDecl.toYaml)
    }

  }

}
