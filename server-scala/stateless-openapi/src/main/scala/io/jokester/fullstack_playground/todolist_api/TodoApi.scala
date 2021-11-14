package io.jokester.fullstack_playground.todolist_api

import cats.Id
import io.circe.generic.auto._
import sttp.model.StatusCode
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI

import java.time.Instant
import scala.concurrent.Future

object TodoApi {
  import ApiConvention._

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
    import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
    OpenAPIDocsInterpreter().toOpenAPI(endpointList, "TodoAPI", "1.0")
  }

  case class Todo(
      id: Int,
      title: String,
      desc: String,
      finished: Boolean,
      updatedAt: Instant,
  )

  case class CreateTodoIntent(title: String, desc: String)

  /**
    * @note must not "extends AnyVal" or there will be mismatch between OpenAPI / Scala
    * @fixme this does not make "todos" required in OpenAPI
    */
  case class TodoList(todos: Seq[Todo])

  object endpoints {
    val listTodo: Endpoint[Unit, ErrorInfo, TodoList, Any] = basePath.get
      .out(statusCode(StatusCode.Ok))
      .out(jsonBody[TodoList])
      .name("list TODO")
      .description("get list of TODOs")

    val showTodo: Endpoint[Int, ErrorInfo, Todo, Any] =
      basePath.get
        .in(path[Int]("todoId"))
        .out(statusCode(StatusCode.Ok))
        .out(jsonBody[Todo])
        .name("show TODO")
        .description("get TODO by id")

    val createTodo: Endpoint[CreateTodoIntent, ErrorInfo, Todo, Any] = basePath.post
      .in(jsonBody[CreateTodoIntent])
      .out(statusCode(StatusCode.Created))
      .out(jsonBody[Todo])
      .name("create TODO")
      .description("create TODO item")

    val updateTodo: Endpoint[(Int, Todo), ErrorInfo, Todo, Any] =
      basePath.patch
        .in(path[Int]("todoId"))
        .in(jsonBody[Todo])
        .out(statusCode(StatusCode.Ok))
        .out(jsonBody[Todo])
        .name("update TODO")
        .description("update todoItem")

    val deleteTodo: Endpoint[Int, ErrorInfo, Unit, Any] =
      basePath.delete
        .in(path[Int]("todoId"))
        .out(statusCode(StatusCode.Ok))
        .name("delete TODO")
        .description("delete todoItem")

    private def basePath =
      endpoint
        .in("todos")
        .errorOut(defaultErrorOutputMapping)
  }

}

trait TodoApiImpl {
  import ApiConvention._
  import TodoApi._

  type ApiResult[T] = Either[ErrorInfo, T]

  def create(req: CreateTodoIntent): ApiResult[Todo]

  def list(): ApiResult[Seq[Todo]]

  def show(todoId: Int): ApiResult[Todo]

  def update(todoId: Int, patch: Todo): ApiResult[Todo]

  def remove(todoId: Int): ApiResult[Todo]
}
