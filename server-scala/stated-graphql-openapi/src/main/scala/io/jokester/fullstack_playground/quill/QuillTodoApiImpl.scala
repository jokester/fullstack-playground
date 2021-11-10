package io.jokester.fullstack_playground.quill

import cats.syntax.either._
import io.jokester.fullstack_playground.todolist_api.{TodoApi, TodoApiImpl, ApiConvention}
import io.jokester.fullstack_playground.quill.generated.public.{PublicExtensions, Todos}
import io.getquill.{EntityQuery, PostgresDialect, PostgresJdbcContext, SnakeCase}

import scala.concurrent.Future

class QuillTodoApiImpl(
    private val ctx: PostgresJdbcContext[SnakeCase.type]
      with PublicExtensions[PostgresDialect, SnakeCase.type],
) extends TodoApiImpl
    with QuillDatetimeEncoding {
  import ctx._

  override def create(req: TodoApi.TodoCreateRequest): ApiResult[TodoApi.Todo] = ???

  override def list(): ApiResult[Seq[TodoApi.Todo]] = {
    val q = quote { query[Todos] }
    val r = ctx.run(q).map(mapFromDB)
    resultRight(r)
  }

  override def show(todoId: Index): ApiResult[TodoApi.Todo] = ???

  override def update(todoId: Index, patch: TodoApi.Todo): ApiResult[TodoApi.Todo] = ???

  override def remove(todoId: Index): ApiResult[TodoApi.Todo] = ???

  private def mapFromDB(row: Todos): TodoApi.Todo = {
    TodoApi.Todo(
      id = row.todoId,
      title = row.title,
      desc = row.desc,
      finished = row.finishedAt.isDefined,
      updatedAt = row.updatedAt.toInstant,
    )
  }

  private def resultRight[T](orig: T): ApiResult[T] = (orig.asRight[ApiConvention.ErrorInfo])
}
