package io.jokester.fullstack_playground.user_todolist_api

import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.{auth, _}

import java.time.OffsetDateTime
import scala.language.implicitConversions

object UserTodoApi {

  // entities
  case class User(userId: Int, profile: UserProfile)
  object UserProfile {
    implicit val jsonEncoder: Encoder[UserProfile] = deriveEncoder[UserProfile]
    implicit val jsonDecoder: Decoder[UserProfile] = deriveDecoder[UserProfile]
  }
  case class UserProfile(nickname: Option[String], avatarUrl: Option[String])
  case class TodoItem(
      todoId: Int,
      userId: Int,
      title: String,
      description: String,
      finishedAt: Option[OffsetDateTime],
  )

  // error body
  sealed trait ErrorInfo                     extends Throwable
  case class BadRequest(error: String)       extends ErrorInfo
  case class NotFound(error: String)         extends ErrorInfo
  case class Unauthorized(error: String)     extends ErrorInfo
  case class Unauthenticated(error: String)  extends ErrorInfo
  case class InternalError()                 extends ErrorInfo
  case class Unknown(code: Int, msg: String) extends ErrorInfo
  case object NoContent                      extends ErrorInfo

  // auth header
  case class AccessToken(value: String)
  case class RefreshToken(value: String)

  // jwt payload
  case class AccessTokenPayload(userId: Int)
  case class RefreshTokenPayload(userId: Int)

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

  case class CreateTodoRequest(title: String, description: String)
  type CreateTodoResponse = TodoItem
  type UpdateTodoRequest  = TodoItem
  type UpdateTodoResponse = TodoItem
  case class ListTodoResponse(todos: Seq[TodoItem])

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
        .in("users" / path[Int]("userId") / "profile")
        .in(jsonBody[UserProfile])
        .out(jsonBody[UserProfile])

    val createTodo =
      authedBasePath.post
        .in("users" / path[Int]("userId") / "todos")
        .in(jsonBody[CreateTodoRequest])
        .out(jsonBody[CreateTodoResponse])

    val updateTodo = authedBasePath.patch
      .in("users" / path[Int]("userId") / "todos" / path[Int]("todoId"))
      .in(jsonBody[CreateTodoResponse])
      .out(jsonBody[CreateTodoResponse])

    val deleteTodo =
      authedBasePath.delete.in("users" / path[Int]("userId") / "todos" / path[Int]("todoId"))

    val listTodo = authedBasePath.get
      .in("users" / path[Int]("userId") / "todos")
      .out(jsonBody[ListTodoResponse])
  }

  val endpointList = Seq(
    endpoints.createUser,
    endpoints.login,
    endpoints.refreshToken,
    endpoints.updateUserProfile,
    endpoints.createTodo,
    endpoints.deleteTodo,
    endpoints.updateTodo,
    endpoints.listTodo,
  )

  def asOpenAPI: OpenAPI = {
    import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
    OpenAPIDocsInterpreter().toOpenAPI(endpointList, "User Todos", "1.0")
  }

  def asOpenAPIYaml: String = {
    import sttp.tapir.openapi.circe.yaml._
    asOpenAPI.toYaml
  }
}
