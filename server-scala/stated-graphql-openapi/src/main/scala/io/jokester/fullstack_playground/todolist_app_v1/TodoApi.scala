package io.jokester.fullstack_playground.todolist_app_v1

import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI

object TodoApi {

  def endpointList =
    Seq(
      endpoints.listTodo,
      endpoints.showTodo,
      endpoints.createTodo,
      endpoints.updateTodo,
      endpoints.deleteTodo,
    )

  def asOpenAPIYaml: String = {
    import sttp.tapir.openapi.circe.yaml._
    asOpenAPI.toYaml
  }

  def asOpenAPI: OpenAPI = {
    import sttp.tapir.docs.openapi._
    TodoApi.endpointList.toOpenAPI("Todos", "1.0")
  }

  sealed trait ErrorInfo

  case class Todo(id: Int, title: String, desc: String, finished: Boolean)

  case class TodoCreateRequest(title: String, desc: String)

  case class NotFound(what: String) extends ErrorInfo

  case class Unauthorized(realm: String) extends ErrorInfo

  case class Unknown(code: Int, msg: String) extends ErrorInfo

  case object NoContent extends ErrorInfo

  object endpoints {
    val listTodo: Endpoint[Unit, ErrorInfo, Seq[Todo], Any] = basePath.get
      .out(jsonBody[Seq[Todo]])
    val showTodo: Endpoint[Int, ErrorInfo, Todo, Any] =
      basePath.post.in(path[Int]).out(jsonBody[Todo])
    val createTodo: Endpoint[TodoCreateRequest, ErrorInfo, Todo, Any] = basePath.post
      .in(jsonBody[TodoCreateRequest])
      .out(jsonBody[Todo])
    val updateTodo: Endpoint[(Int, Todo), ErrorInfo, Todo, Any] =
      basePath.patch
        .in("todo" / path[Int])
        .in(jsonBody[Todo])
        .out(jsonBody[Todo])
    val deleteTodo: Endpoint[Int, ErrorInfo, Unit, Any] =
      basePath.delete.in(path[Int])
    private def basePath =
      endpoint
        .in("todos")
        .errorOut(
          oneOf[ErrorInfo](
            statusMapping(StatusCode.NotFound, jsonBody[NotFound].description("not found")),
          ),
        )
  }

}

trait TodoApiImpl {

  import TodoApi._

  type Failable[T] = Either[ErrorInfo, T]

  def create(req: TodoCreateRequest): Failable[Todo]

  def list(): Failable[Seq[Todo]]

  def show(todoId: Int): Failable[Todo]

  def update(todoId: Int, updated: Todo): Failable[Todo]

  def remove(todoId: Int): Failable[Todo]
}
