package io.jokester.fullstack_playground.user_todolist_api.validation
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.data.NonEmptyList
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi._
import io.jokester.http_api.OpenAPIConvention.{BadRequest, Failable, Lifters}

import scala.language.implicitConversions

object UserValidators extends UserValidators with Lifters

trait UserValidators { self: Lifters =>

  private implicit def transformValidated[T](from: Validated[String, T]): Failable[T] =
    from.fold(
      l => liftError(BadRequest(l)),
      r => liftSuccess(r),
    )

  def validateCreateUserRequest(intent: CreateUserRequest): Failable[CreateUserRequest] = {
    Validated.Valid(intent.copy(email = intent.email.trim.toLowerCase))
  }
}
