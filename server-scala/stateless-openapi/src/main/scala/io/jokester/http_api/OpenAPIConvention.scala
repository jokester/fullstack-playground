package io.jokester.http_api

import sttp.model.StatusCode
import sttp.tapir.EndpointOutput
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir._
import cats.syntax.either._

import scala.concurrent.{ExecutionContext, Future}

object OpenAPIConvention {
  sealed trait ApiError extends Throwable

  // http 400
  case class BadRequest(message: String) extends ApiError
  // http 401
  case class Unauthorized(message: String) extends ApiError
  // http 403
  case class Forbidden(message: String) extends ApiError
  // http 404
  case class NotFound(message: String) extends ApiError
  // http 500
  case class ServerError(message: String) extends ApiError
  // http 501
  case class NotImplemented(message: String) extends ApiError

  val defaultErrorOutputMapping: EndpointOutput[ApiError] =
    oneOf[ApiError](
      oneOfMapping(
        StatusCode.Unauthorized,
        jsonBody[Unauthorized].description("login required"),
      ),
      oneOfMapping(StatusCode.NotFound, jsonBody[NotFound].description("resource not found")),
      oneOfMapping(
        StatusCode.Forbidden,
        jsonBody[Forbidden].description("user identity known but action not permitted"),
      ),
      oneOfMapping(
        StatusCode.BadRequest,
        jsonBody[BadRequest].description("client's bad in general"),
      ),
      oneOfMapping(
        StatusCode.NotImplemented,
        jsonBody[NotImplemented].description("not implemented"),
      ),
      oneOfDefaultMapping(jsonBody[ServerError].description("server's bad in general")),
    )

  case class ApiR[T](value: Future[Either[ApiError, T]]) {}

  type ApiResultSync[T] = Either[ApiError, T]
  type ApiResult[T]     = Future[Either[ApiError, T]]

  case class Failable[A](asFuture: ApiResult[A]) extends AnyVal {

    implicit def toFuture: ApiResult[A] = asFuture

    def map[B](t: A => B)(implicit ec: ExecutionContext): Failable[B] = {
      Failable(asFuture.map(either => either.map(t)))
    }
    def flatMap[B](t: A => Failable[B])(implicit ec: ExecutionContext): Failable[B] = {
      Failable(asFuture.flatMap({
        case Right(value) => t(value).asFuture
        case Left(l)      => Future.successful(l.asLeft)
      }))
    }
  }

  trait Lifters {
//    type Failable[T] = ApiResult[T]

    def liftResult[T](t: ApiResultSync[T]): Failable[T] = Failable(Future.successful(t))
    def liftSuccess[T](t: T): Failable[T]               = Failable(Future.successful(Right(t)))
    def liftError[A](r: ApiError): Failable[A]          = Failable(Future.successful(Left(r)))

    def mapInner[T, A](i: ApiResult[T], f: T => A)(implicit ec: ExecutionContext): ApiResult[A] = {
      i.map(resultSync => resultSync.map(f))
    }
  }
}
