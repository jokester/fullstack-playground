package io.jokester.fullstack_playground.todolist_api

import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.quill.generated.public.Todos
import io.jokester.fullstack_playground.quill.{
  QuillCtxFactory,
  QuillDatetimeEncoding,
  QuillWorkarounds,
}
import io.jokester.fullstack_playground.todolist_api.TodoApi.{CreateTodoIntent, TodoList}
import io.jokester.http_api.OpenAPIConvention._

import java.time.{Clock, OffsetDateTime}

class TodoApiQuillImpl(
    protected override val ctx: QuillCtxFactory.OurCtx,
) extends TodoApiService
    with QuillDatetimeEncoding
    with QuillWorkarounds
    with LazyLogging
    with Lifters {
  import ctx._

  override def create(req: CreateTodoIntent): ApiResult[TodoApi.Todo] = {
    val created = ctx.run(quote {
      query[Todos]
        .insert(_.title -> lift(req.title), _.desc -> lift(req.desc))
        .returning(row => row)
    })
    liftSuccess(mapFromDB(created))
  }

  override def list(): ApiResult[TodoList] =
    liftSuccess {
      val q = quote { query[Todos] }
      val r = ctx.run(q).map(mapFromDB)
      r
    }

  override def show(todoId: Int): ApiResult[TodoApi.Todo] = {
    val found = run(quote {
      query[Todos].filter(_.todoId == lift(todoId))
    })
    found.headOption.map(mapFromDB) match {
      case Some(got) => liftSuccess(got)
      case _         => liftError(NotFound(s"Todo(id=$todoId) not found"))
    }
  }

  override def update(todoId: Int, patch: TodoApi.Todo): ApiResult[TodoApi.Todo] = {
    transaction {

      val existed: Option[Todos] = run(findById(todoId)).headOption

      existed match {
        case Some(found) =>
          val merged = found.copy(
            title = patch.title,
            desc = patch.desc,
            finishedAt = found.finishedAt match {
              case None if patch.finished     => Some(now)
              case Some(_) if !patch.finished => None
              case _                          => found.finishedAt
            },
          )
          logger.debug("updating {} with {}", existed, merged)

          val updated = run {

            findById(todoId)
              .update(
                _.title      -> lift(merged.title),
                _.desc       -> lift(merged.desc),
                _.finishedAt -> lift(merged.finishedAt: Option[OffsetDateTime]),
//                lift(merged),
              )
              .returning(row => row)
          }
          liftSuccess(mapFromDB(updated))
        case _ => liftError(NotFound(s"Todo(id=$todoId) not found"))

      }
    }

  }

  override def remove(todoId: Int): ApiResult[TodoApi.Todo] = {

    try {

      /**
        * @note delete.returning() throws when the query returns NO or MULTI rows
        */
      val removed = run { findById(todoId).delete.returning(row => row) }
      logger.debug("remove returned {}", removed)

      liftSuccess(mapFromDB(removed))

    } catch {
      case e: Throwable =>
        logger.error("remove failed", e)
        handleRemovedRowNotUnique(e, liftError(NotFound(s"Todo(id=$todoId) not found")))
    }
  }

  private def findById(todoId: Int) = quote(query[Todos].filter(_.todoId == lift(todoId)))

  private def mapFromDB(row: Todos): TodoApi.Todo = {
    TodoApi.Todo(
      id = row.todoId,
      title = row.title,
      desc = row.desc,
      finished = row.finishedAt.isDefined,
      updatedAt = row.updatedAt.toInstant,
    )
  }

  private val clock = Clock.systemUTC()
  private def now   = OffsetDateTime.now(clock)

  /**
    * @note updateMeta for some reason doesn't work
    * this WAS SUPPOSED TO allows us to lift(todo_value) , and exclude todoId columns in update
    *
    * @see https://getquill.io/#extending-quill-meta-dsl-update-meta
    */
//  private implicit val todoUpdateMeta: UpdateMeta[Todos] =
//    updateMeta[Todos](_.todoId, _.updatedAt, _.createdAt)
}
