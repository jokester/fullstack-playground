package io.jokester.fullstack_playground.akka_openapi

import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._

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
