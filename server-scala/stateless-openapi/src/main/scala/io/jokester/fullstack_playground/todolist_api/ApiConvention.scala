package io.jokester.fullstack_playground.todolist_api

import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{EndpointOutput, oneOf, oneOfDefaultMapping, oneOfMapping}

import scala.concurrent.Future

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

  trait LifterHelpers {
    type ApiResult[T] = Either[ErrorInfo, T]

    def liftResult[T](t: ApiResult[T]): Future[ApiResult[T]] = Future.successful(t)
    def liftSuccess[T](t: T): Future[ApiResult[T]]           = Future.successful(Right(t))
    def liftError(r: ErrorInfo): Future[ApiResult[Nothing]]  = Future.successful(Left(r))
  }
}
