package io.jokester.fullstack_playground.user_todo_list

import cats.Id
import com.typesafe.scalalogging.LazyLogging
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
  case class BadRequest(error: String)       extends ErrorInfo
  case class NotFound(error: String)         extends ErrorInfo
  case class Unauthorized(error: String)     extends ErrorInfo
  case class Unauthenticated(error: String)  extends ErrorInfo
  case class InternalError()                 extends ErrorInfo
  case class Unknown(code: Int, msg: String) extends ErrorInfo
  case object NoContent                      extends ErrorInfo

  case class CreateUserRequest(email: String, initialPass: String, profile: UserProfile)
  case class CreateUserResponse(userId: Int, userProfile: UserProfile)

  case class LoginRequest(email: String, password: String)
  case class LoginResponse(
      userId: Int,
      userProfile: UserProfile,
      refreshToken: RefreshToken,
      accessToken: AccessToken,
  )

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
            statusMapping(StatusCode.Unauthorized, jsonBody[Unauthenticated]),
            statusMapping(StatusCode.Forbidden, jsonBody[Unauthorized]),
          ),
        )

    private def authedBasePath =
      basePath
        .in(
          auth.bearer[String]().map[AccessToken](f = AccessToken)(_.value),
        )

    val createUser =
      basePath.post.in("users").in(jsonBody[CreateUserRequest]).out(jsonBody[CreateUserResponse])

    val login =
      basePath.post
        .in("auth" / "login")
        .in(jsonBody[LoginRequest])
        .out(jsonBody[LoginResponse])

    val refreshToken = basePath.post
      .in("auth" / "refresh_token")
      .in(
        auth.bearer[String]().map[RefreshToken](f = RefreshToken)(_.value),
      )
      .out(jsonBody[LoginResponse])

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
    endpoints.refreshToken,
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

  // signup
  def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse]

  // auth
  def loginUser(req: LoginRequest): ApiResult[LoginResponse]
  def validateAccessToken(accessToken: AccessToken): ApiResult[AuthedUser]
  def refreshAccessToken(refreshToken: RefreshToken): ApiResult[LoginResponse]

  // user
  def updateProfile(authed: AuthedUser, newProfile: UserProfile): ApiResult[UserProfile]

  // todo
  def createTodo(authed: AuthedUser, req: CreateTodoRequest): ApiResult[CreateTodoResponse]
  def updateTodo(authed: AuthedUser, req: UpdateTodoRequest): ApiResult[UpdateTodoResponse]

}

class UserTodoServiceImpl extends UserTodoService with UserTodoDb with LazyLogging {
  import UserTodoApi._

  private val dummyRefreshToken = RefreshToken("dummyRefresh")
  private val dummyAccessToken  = AccessToken("dummyAccess")

  private def successRight[T](value: => T): ApiResult[T] = Right(value)
  private def fail(value: ErrorInfo): ApiResult[Nothing] = Left(value)

  private def issueRefreshToken(user: UserRow): RefreshToken = dummyRefreshToken

  override def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse] = {
    db().localTx(implicit session => {
      val created = userRepo.createUser(req.email, req.initialPass, req.profile)
      successRight(CreateUserResponse(created.userId, created.userProfile))
    })
  }

  override def loginUser(
      req: LoginRequest,
  ): ApiResult[LoginResponse] = {
    val user = db().readOnly(implicit session => {
      val found = userRepo.findUserByEmail(req.email)
      found
        .filter(_.userPassword == req.password)
        .toRight(NotFound("authed user"))
    })
    user.map(u => LoginResponse(u.userId, u.userProfile, issueRefreshToken(u), dummyAccessToken))
  }

  override def refreshAccessToken(refreshToken: RefreshToken): ApiResult[LoginResponse] =
    if (refreshToken == dummyRefreshToken) {
      Right(
        LoginResponse(
          userId = -1,
          userProfile = UserProfile(),
          accessToken = dummyAccessToken,
          refreshToken = dummyRefreshToken,
        ),
      )
    } else {
      Left(Unauthorized("invalid refresh token"))
    }

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
