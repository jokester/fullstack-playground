package io.jokester.fullstack_playground.user_todolist_api

import io.circe.generic.auto._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.jokester.http_api.OpenAPIBuilder
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{auth, _}

import java.time.OffsetDateTime

object UserTodoApi extends UserAuth with UserApi with TodoApi {

  import io.jokester.http_api.OpenAPIConvention._
  import io.jokester.http_api.OpenAPIAuthConvention._

  object endpoints {

    private val basePath =
      endpoint
        .in("user_todo_api")
        .errorOut(defaultErrorOutputMapping)

    /**
      *
      */
    private val authedBasePath =
      basePath
        .in(
          auth
            .bearer[String]()
            .map[BearerToken](
              Mapping.from(f = BearerToken)(_.value),
            ),
        )

    val createUser =
      basePath.post.in("users").in(jsonBody[CreateUserRequest]).out(jsonBody[CreateUserResponse])

    val login =
      basePath.post
        .in("auth" / "login")
        .in(jsonBody[LoginRequest])
        .out(jsonBody[UserAccount])

    val refreshToken = basePath.post
      .in("auth" / "refresh_token")
      .in(
        auth.bearer[String]().map[BearerToken](f = BearerToken)(_.value),
      )
      .out(jsonBody[UserAccount])

    val updateUserProfile =
      authedBasePath.put
        .in("users" / path[Int]("userId") / "profile")
        .in(jsonBody[UserProfile])
        .out(jsonBody[UserAccount])

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

    val listTodo =
      authedBasePath.get
        .in("users" / path[Int]("userId") / "todos")
        .out(jsonBody[TodoList])
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

  def asOpenAPIYaml: String = OpenAPIBuilder.buildOpenApiYaml(endpointList, "user-todo", "0.1")
}

trait UserApi {

  // entities
  case class UserAccount(userId: Int, profile: UserProfile)
  case class UserProfile(nickname: Option[String], avatarUrl: Option[String])
  object UserProfile {

    /**
      * FIXME: can we remove this?
      */
    val jsonEncoder: Encoder[UserProfile] = deriveEncoder[UserProfile]
    val jsonDecoder: Decoder[UserProfile] = deriveDecoder[UserProfile]
  }

  case class CreateUserRequest(email: String, initialPass: String, profile: UserProfile)
  case class CreateUserResponse(userId: Int, userProfile: UserProfile)
  case class LoginRequest(email: String, password: String)
}

trait UserAuth {
  case class UserId(value: Int)
}

trait TodoApi { self: UserApi =>

  case class TodoItem(
      todoId: Int,
      userId: Int,
      title: String,
      description: String,
      finishedAt: Option[OffsetDateTime],
  )
  case class TodoList(items: Seq[TodoItem])

  case class CreateTodoRequest(title: String, description: String)
  type CreateTodoResponse = TodoItem
  type UpdateTodoRequest  = TodoItem
  type UpdateTodoResponse = TodoItem
}
