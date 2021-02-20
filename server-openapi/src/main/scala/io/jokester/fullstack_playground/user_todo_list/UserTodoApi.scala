package io.jokester.fullstack_playground.user_todo_list

import io.circe.generic.auto._
import io.jokester.fullstack_playground.genereated_scalikejdbc.{UserProfile, Todo => TodoRow}
import io.jokester.fullstack_playground.scalikejdbc_db.UserTodoDb
import io.jokester.fullstack_playground.user_todo_list.UserTodoApi._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI

import scala.concurrent.Future

object UserTodoApi {

  sealed trait ErrorInfo
  case class NotFound(what: String)          extends ErrorInfo
  case class Unauthorized(realm: String)     extends ErrorInfo
  case class Unknown(code: Int, msg: String) extends ErrorInfo
  case object NoContent                      extends ErrorInfo

  case class CreateUserRequest(email: String, initialPass: String, profile: UserProfile)
  case class CreateUserResponse(userId: Int, userProfile: UserProfile, jwtToken: String)

  case class LoginRequest(email: String, password: String)
  case class LoginResponse(userId: Int, userProfile: UserProfile, jwtToken: String)

  case class AuthedUser(jwtToken: String, userId: Int)

  case class CreateTodoRequest(title: String, description: String)
  type CreateTodoResponse = TodoRow
  type UpdateTodoRequest  = TodoRow
  type UpdateTodoResponse = TodoRow

  object endpoints {

    private def basePath: Endpoint[Unit, ErrorInfo, Unit, Any] =
      endpoint
        .in("user_todo_api")
        .errorOut(
          oneOf[ErrorInfo](
            statusMapping(StatusCode.NotFound, jsonBody[NotFound]),
          ),
        )

    val createUser =
      basePath.in("users").post.in(jsonBody[CreateUserRequest]).out(jsonBody[CreateUserResponse])

    val login =
      basePath.post.in("auth" / "login").in(jsonBody[LoginRequest]).out(jsonBody[LoginResponse])

    val updateUserProfile =
      basePath.put
        .in("users" / path[Int] / "profile")
        .in(jsonBody[UserProfile])
        .out(jsonBody[UserProfile])

    val createTodo =
      basePath.post.in("todos").in(jsonBody[CreateTodoRequest]).out(jsonBody[CreateTodoResponse])

    val updateTodo = basePath.patch
      .in("todos" / path[Int])
      .in(jsonBody[CreateTodoResponse])
      .out(jsonBody[CreateTodoResponse])
  }

  val endpointList = Seq(
    endpoints.createUser,
    endpoints.login,
    endpoints.updateUserProfile,
    endpoints.createUser,
    endpoints.updateTodo,
  )

  def asOpenAPI: OpenAPI = {
    OpenAPIDocsInterpreter.toOpenAPI(endpointList, "User Todos", "1.0")
  }
}

trait UserTodoService {
  import UserTodoApi._

  type ApiResult[T] = Future[Either[ErrorInfo, T]]

  def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse]
  def loginUser(req: LoginRequest): ApiResult[LoginResponse]
  def validateAuth(jwtToken: String): ApiResult[AuthedUser]
  def updateProfile(authed: AuthedUser, newProfile: UserProfile): ApiResult[UserProfile]

  def createTodo(authed: AuthedUser, req: CreateTodoRequest): ApiResult[CreateTodoResponse]
  def updateTodo(authed: AuthedUser, req: UpdateTodoRequest): ApiResult[UpdateTodoResponse]

}

class UserTodoServiceImpl extends UserTodoService with UserTodoDb {

  private def success[T](value: => T): Future[Either[ErrorInfo, T]] =
    Future.successful(Right(value))
  private def success[T](value: => Either[ErrorInfo, T]): Future[Either[ErrorInfo, T]] =
    Future.successful(value)
  private def fail(value: ErrorInfo): Future[Either[ErrorInfo, Nothing]] =
    Future.successful(Left(value))

  override def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse] = {
    db().localTx(implicit session => {
      val created = userRepo.createUser(req.email, req.initialPass, req.profile)
      success(CreateUserResponse(created.userId, created.userProfile, "dummy"))
    })
  }

  override def loginUser(req: LoginRequest): ApiResult[LoginResponse] = {
    val result = db().readOnly(implicit session => {
      val found = userRepo.findUserByEmail(req.email)
      found
        .filter(_.userPassword == req.password)
        .map(user => LoginResponse(user.userId, user.userProfile, "dummyJwt"))
        .toRight(NotFound("authed user"))
    })
    success(result)
  }

  override def validateAuth(jwtToken: String): ApiResult[AuthedUser] =
    success(AuthedUser(userId = -1, jwtToken = jwtToken))

  override def updateProfile(
      authed: AuthedUser,
      newProfile: UserProfile,
  ): ApiResult[UserProfile] = {
    db().localTx(implicit session => {
      val updated =
        for (
          orig <- userRepo.findUser(authed.userId).toRight(NotFound("user"));
          updated <-
            userRepo.updateUser(orig.copy(userProfile = newProfile)).toRight(NotFound("user"))
        ) yield updated

      success(updated.map(_.userProfile))
    })
  }

  override def createTodo(
      authed: AuthedUser,
      req: CreateTodoRequest,
  ): ApiResult[CreateTodoResponse] = ???

  override def updateTodo(
      authed: AuthedUser,
      req: UpdateTodoRequest,
  ): ApiResult[UpdateTodoResponse] = ???
}
