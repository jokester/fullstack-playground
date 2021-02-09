package io.jokester.fullstack_playground.quill_db

import io.getquill._

import java.time.OffsetDateTime

object QuillContext {
  def connect() = new PostgresJdbcContext(SnakeCase, "quill-ctx.pg")
}

class QuillTables(val ctx: PostgresJdbcContext[SnakeCase.type]) extends QuillDateTimeEncoders {

  import ctx._

  case class Todo(
      todoId: Int,
      title: String,
      desc: String,
      finishedAt: Option[OffsetDateTime],
      createdAt: OffsetDateTime,
      updatedAt: OffsetDateTime,
  )

  val todo: ctx.Quoted[EntityQuery[Todo]] = quote {
    querySchema[Todo](
      // for some reason this is not used
      "todo",
      _.todoId     -> "todo_id",
      _.title      -> "title",
      _.desc       -> """"desc"""",
      _.finishedAt -> "finished_at",
      _.createdAt  -> "created_at",
      _.updatedAt  -> "updated_at",
    )
  }

  def listTodo: Seq[Todo] = ctx.run(todo)

  def showTodo(todoId: Int): Option[Todo] =
    ctx.run {
      query[Todo].filter(_.todoId == lift(todoId)).take(1)
    }.headOption

  def createTodo(title: String, desc: String): Todo =
    (ctx.run {
      todo.insert(_.title -> lift(title), _.desc -> lift(desc)).returning(inserted => inserted)
    })

  def removeTodo(todoId: Int): Int =
    (ctx.run {
      todo.filter(_.todoId == lift(todoId)).delete
    }).toInt

  def updateTodoState(todoId: Int, finishedAt: Option[OffsetDateTime]) =
    ctx.run {
      todo
        .filter(_.todoId == lift(todoId))
        .update(
          _.finishedAt
            -> lift(finishedAt),
        )
    }
}
