package io.jokester.fullstack_playground.user_todolist_api
import io.jokester.http_api.OpenAPIConvention.{BadRequest, Failable, Lifters}
import UserTodoApi._
import io.jokester.fullstack_playground.user_todolist_api.validation.UserValidators

import scala.concurrent.ExecutionContext

trait UserTodoServiceValidation extends Lifters with UserValidators {
  self: UserTodoService =>

  implicit def ec: ExecutionContext

  override def createUser(req: CreateUserRequest): Failable[UserTodoApi.UserAccount] = {
    for (l <- validateCreateUserRequest(req); created <- self.createUser(l)) yield created
  }

}
