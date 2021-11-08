package io.jokester.fullstack_playground.todolist_api

import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{EndpointOutput, oneOf, oneOfDefaultMapping, oneOfMapping}

object ApiConvention {
  sealed trait ErrorInfo

  case class Unauthorized(message: String)   extends ErrorInfo
  case class NotFound(message: String)       extends ErrorInfo
  case class BadRequest(message: String)     extends ErrorInfo
  case class NotImplemented(message: String) extends ErrorInfo
  case class ServerError(message: String)    extends ErrorInfo

  val defaultErrorOutputMapping: EndpointOutput[ErrorInfo] =
    oneOf[ErrorInfo](
      oneOfMapping(
        StatusCode.Unauthorized,
        jsonBody[Unauthorized].description("login required"),
      ),
      oneOfMapping(StatusCode.NotFound, jsonBody[NotFound].description("not found")),
      oneOfMapping(StatusCode.BadRequest, jsonBody[BadRequest].description("caller's bad")),
      oneOfMapping(
        StatusCode.NotImplemented,
        jsonBody[NotImplemented].description("not implemented"),
      ),
      oneOfDefaultMapping(jsonBody[ServerError]),
    )

}
