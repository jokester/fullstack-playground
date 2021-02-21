package io.jokester.fullstack_playground.user_todo_list

import cats.Id
import io.circe.generic.auto._
import io.jokester.fullstack_playground.genereated_scalikejdbc.{
  UserProfile,
  Todo => TodoRow,
  User => UserRow,
}
import io.jokester.fullstack_playground.scalikejdbc_db.UserTodoDb
import sttp.model.StatusCode
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.{auth, _}

object UserTodoApi {

  sealed trait ErrorInfo
  case class NotFound(what: String)          extends ErrorInfo
  case class Unauthorized(realm: String)     extends ErrorInfo
  case class Unknown(code: Int, msg: String) extends ErrorInfo
  case object NoContent                      extends ErrorInfo

  case class CreateUserRequest(email: String, initialPass: String, profile: UserProfile)
  case class CreateUserResponse(userId: Int, userProfile: UserProfile)

  case class LoginRequest(email: String, password: String)
  case class LoginResponse(userId: Int, userProfile: UserProfile)

  case class AuthedUser(jwtToken: String, userId: Int)

  case class AccessToken(value: String)
  case class RefreshToken(value: String)

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

    private def authedBasePath =
      basePath.in(
        auth.bearer[String]().map[AccessToken](f = AccessToken)(_.value),
      )

    val createUser =
      basePath.in("users").post.in(jsonBody[CreateUserRequest]).out(jsonBody[CreateUserResponse])

    val login =
      basePath.post
        .in("auth" / "login")
        .in(jsonBody[LoginRequest])
        .out(jsonBody[LoginResponse])
        .out(
          setCookie("refreshToken"),
        )

    val refreshToken = basePath.post
      .in("auth" / "refresh_token")
      .in(cookie[String]("refreshToken").map[RefreshToken](f = RefreshToken)(_.value))

    val updateUserProfile =
      authedBasePath.put
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

  type ApiResult[T] = Id[Either[ErrorInfo, T]]

  def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse]
  def loginUser(req: LoginRequest): ApiResult[(LoginResponse, RefreshToken)]

  def issueAccessToken(refreshToken: RefreshToken): ApiResult[AccessToken]
  def validateAccessToken(jwtToken: AccessToken): ApiResult[AuthedUser]
  def updateProfile(authed: AuthedUser, newProfile: UserProfile): ApiResult[UserProfile]

  def createTodo(authed: AuthedUser, req: CreateTodoRequest): ApiResult[CreateTodoResponse]
  def updateTodo(authed: AuthedUser, req: UpdateTodoRequest): ApiResult[UpdateTodoResponse]

}

class UserTodoServiceImpl extends UserTodoService with UserTodoDb {
  import UserTodoApi._

  private val dummyRefreshToken = RefreshToken("dummyRefresh")
  private val dummyAccessToken  = AccessToken("dummyAccess")

  private def successRight[T](value: => T): ApiResult[T]                     = Right(value)
  private def successEither[T](value: => Either[ErrorInfo, T]): ApiResult[T] = value
  private def fail(value: ErrorInfo): ApiResult[Nothing]                     = Left(value)

  private def issueRefreshToken(user: UserRow): RefreshToken = RefreshToken("123")

  override def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse] = {
    db().localTx(implicit session => {
      val created = userRepo.createUser(req.email, req.initialPass, req.profile)
      successRight(CreateUserResponse(created.userId, created.userProfile))
    })
  }

  override def loginUser(
      req: LoginRequest,
  ): ApiResult[(LoginResponse, RefreshToken)] = {
    val user = db().readOnly(implicit session => {
      val found = userRepo.findUserByEmail(req.email)
      found
        .filter(_.userPassword == req.password)
        .toRight(NotFound("authed user"))
    })
    user.map(u => (LoginResponse(u.userId, u.userProfile), issueRefreshToken(u)))
  }

  override def issueAccessToken(refreshToken: RefreshToken): ApiResult[AccessToken] =
    Right(dummyAccessToken)

  override def validateAccessToken(jwtToken: AccessToken): ApiResult[AuthedUser] = {
    if (jwtToken == dummyAccessToken)
      successRight(AuthedUser(userId = -1, jwtToken = jwtToken.value))
    else fail(Unauthorized("realm"))
  }

  override def updateProfile(
      authed: AuthedUser,
      newProfile: UserProfile,
  ): ApiResult[UserProfile] = {
    val result = db().localTx(implicit session => {
      val updated =
        for (
          orig <- userRepo.findUser(authed.userId).toRight(NotFound("user"));
          updated <-
            userRepo.updateUser(orig.copy(userProfile = newProfile)).toRight(NotFound("user"))
        ) yield updated

      updated.map(_.userProfile)
    })
    result
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
