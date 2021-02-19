package io.jokester.fullstack_playground.user_todo_list

import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import io.jokester.fullstack_playground.genereated_scalikejdbc.{UserProfile, Todo => TodoRow}
import io.jokester.fullstack_playground.user_todo_list.UserTodoApi.{
  CreateUserRequest,
  CreateUserResponse,
}
import sttp.model.StatusCode
import sttp.tapir._
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

  case class CreateTodoRequest(title: String, description: String)
  type CreateTodoResponse = TodoRow

  object endpoints {

    private def basePath =
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
    import sttp.tapir.docs.openapi._
    OpenAPIDocsInterpreter.serverEndpointsToOpenAPI(endpointList, "User Todos", "1.0")
  }

  def buildRoute(impl: UserTodoApi[Future]): Route = {}

  def wtf[M[_]](input: M[LoginRequest]) = {
    input match {
      case _ => ???
    }

  }
}

trait UserTodoApi[Result[_]] {

  def createUser(req: CreateUserRequest): Result[CreateUserResponse]
}
