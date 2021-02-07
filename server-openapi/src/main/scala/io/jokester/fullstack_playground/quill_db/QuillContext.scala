package io.jokester.fullstack_playground.quill_db

import io.getquill._

import java.time.OffsetDateTime

object QuillContext {
  lazy val ctx = new PostgresJdbcContext(SnakeCase, "quill-ctx.pg")
}

object QuillTables extends QuillDateTimeEncoders {

  import QuillContext.ctx
  import ctx._

  case class TodoV1(
      todoId: Int,
      title: String,
      desc: String,
      finishedAt: Option[OffsetDateTime],
      createdAt: OffsetDateTime,
      updatedAt: OffsetDateTime
  )

  val todoV1: ctx.Quoted[EntityQuery[TodoV1]] = quote {
    querySchema[TodoV1](
      // for some reason this is not used
      "todo_v1",
      _.todoId     -> "todo_id",
      _.title      -> "title",
      _.desc       -> """"desc"""",
      _.finishedAt -> "finished_at",
      _.createdAt  -> "created_at",
      _.updatedAt  -> "updated_at"
    )
  }

  def listTodo: Seq[TodoV1] = ctx.run(todoV1)

  def showTodo(todoId: Int): Option[TodoV1] =
    (ctx.run {
      query[TodoV1].filter(_.todoId == lift(todoId)).take(1)
    }).headOption

  def createTodo(title: String, desc: String): TodoV1 =
    (ctx.run {
      todoV1.insert(_.title -> lift(title), _.desc -> lift(desc)).returning(inserted => inserted)
    })

  def removeTodo(todoId: Int) =
    (ctx.run {
      todoV1.filter(_.todoId == lift(todoId)).delete
    }).toInt

  def updateTodoState(todoId: Int, finishedAt: Option[OffsetDateTime]) =
    (
      ctx.run {
        todoV1
          .filter(_.todoId == lift(todoId))
          .update(
            _.finishedAt
              -> lift(finishedAt)
          )

      }
    )
}
