package io.jokester.fullstack_playground.user_todolist_api

import cats.Id
import com.typesafe.scalalogging.LazyLogging
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef

import scala.concurrent.Future

class UserTodoServiceImpl extends UserTodoService[Id] with UserTodoDb with LazyLogging {
  import UserTodoApi._

  private def successRight[T](value: => T): ApiResult[T] = Right(value)
  private def fail(value: ErrorInfo): ApiResult[Nothing] = Left(value)

  override def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse] = ???

  override def loginUser(
      req: LoginRequest,
  ): ApiResult[LoginResponse] = ???

  override def refreshAccessToken(refreshToken: RefreshToken): ApiResult[LoginResponse] = ???

  override def validateSession(jwtToken: AccessToken): ApiResult[AuthedUser] = ???

  override def updateProfile(
      authed: AuthedUser,
      newProfile: UserProfile,
  ): ApiResult[UserProfile] = ???

  override def createTodo(
      authed: AuthedUser,
      req: CreateTodoRequest,
  ): ApiResult[CreateTodoResponse] = ???

  override def updateTodo(
      authed: AuthedUser,
      req: UpdateTodoRequest,
  ): ApiResult[UpdateTodoResponse] = ???

  override def deleteTodo(
      authed: AuthedUser,
      todoId: Int,
  ): ApiResult[TodoItem] = ???

  override def listTodo(authed: AuthedUser): ApiResult[Seq[TodoItem]] = ???
}
