package io.jokester.fullstack_playground.user_todolist_api

import akka.NotUsed
import io.circe.generic.auto._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.{auth, _}

import java.time.OffsetDateTime

object UserTodoApi {

  import io.jokester.fullstack_playground.todolist_api.ApiConvention._

  // entities
  case class User(userId: Int, profile: UserProfile)
  object UserProfile {
    val jsonEncoder: Encoder[UserProfile] = deriveEncoder[UserProfile]
    val jsonDecoder: Decoder[UserProfile] = deriveDecoder[UserProfile]
  }
  case class UserProfile(nickname: Option[String], avatarUrl: Option[String])
  case class TodoItem(
      todoId: Int,
      userId: Int,
      title: String,
      description: String,
      finishedAt: Option[OffsetDateTime],
  )

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

    private def basePath: Endpoint[Unit, ErrorInfo, Unit, NotUsed] =
      endpoint
        .in("user_todo_api")
        .errorOut(defaultErrorOutputMapping)

    private def authedBasePath =
      basePath
        .in(
          auth
            .bearer[String]()
            .map[AccessToken](
              Mapping.from(f = AccessToken)(_.value),
            ),
        )

    val createUser: Endpoint[CreateUserRequest, ErrorInfo, CreateUserResponse, NotUsed] =
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

    val deleteTodo: Endpoint[(AccessToken, Int, Int), ErrorInfo, Unit, Any] =
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
