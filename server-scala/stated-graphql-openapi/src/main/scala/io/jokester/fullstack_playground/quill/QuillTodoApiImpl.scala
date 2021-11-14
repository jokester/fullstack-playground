package io.jokester.fullstack_playground.quill

import cats.syntax.either._
import io.jokester.fullstack_playground.todolist_api.{ApiConvention, TodoApi, TodoApiImpl}
import io.jokester.fullstack_playground.quill.generated.public.{PublicExtensions, Todos}
import io.getquill.{EntityQuery, PostgresDialect, PostgresJdbcContext, SnakeCase}

import java.time.{Clock, OffsetDateTime, OffsetTime}

class QuillTodoApiImpl(
    private val ctx: QuillCtxFactory.OurCtx,
) extends TodoApiImpl
    with QuillDatetimeEncoding {
  import ctx._
  import ApiConvention._

  override def create(req: TodoApi.CreateTodoIntent): ApiResult[TodoApi.Todo] = {
    val created = ctx.run(quote {
      query[Todos]
        .insert(_.title -> lift(req.title), _.desc -> lift(req.title))
        .returning(row => row)
    })
    resultRight(mapFromDB(created))
  }

  override def list(): ApiResult[Seq[TodoApi.Todo]] = {
    val q = quote { query[Todos] }
    val r = ctx.run(q).map(mapFromDB)
    resultRight(r)
  }

  override def show(todoId: Int): ApiResult[TodoApi.Todo] = {
    val found = run(quote {
      query[Todos].filter(_.todoId == lift(todoId))
    })
    found.headOption.map(mapFromDB) match {
      case Some(got) => resultRight(got)
      case _         => resultLeft(NotFound("not found"))
    }
  }

  override def update(todoId: Index, patch: TodoApi.Todo): ApiResult[TodoApi.Todo] = {
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

          val updated = run {
            findById(todoId)
              .update(
                lift(merged),
              )
              .returning(row => row)
          }
          resultRight(updated).map(mapFromDB)
        case _ => resultLeft(NotFound("not found"))

      }
    }

  }

  override def remove(todoId: Index): ApiResult[TodoApi.Todo] = {

    /**
      * FIXME: see what happens when nothing removed
      */
    val removed = run { findById(todoId).delete.returning(row => row) }

    resultRight(removed).map(mapFromDB)
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

  private def resultRight[T](orig: T): ApiResult[T]        = orig.asRight[ApiConvention.ErrorInfo]
  private def resultLeft(e: ErrorInfo): ApiResult[Nothing] = e.asLeft[Nothing]
  private val clock                                        = Clock.systemUTC()
  private def now                                          = OffsetDateTime.now(clock)
}
