package io.jokester.fullstack_playground.quill

import io.jokester.fullstack_playground.quill.generated.public.{PublicExtensions, Todos}
import io.getquill.{EntityQuery, PostgresDialect, PostgresJdbcContext, SnakeCase}

class QuillTodoApiImpl(
    private val ctx: PostgresJdbcContext[SnakeCase.type]
      with PublicExtensions[PostgresDialect, SnakeCase.type],
) extends QuillDatetimeEncoding {
  import ctx._

  def listTodos(): Seq[Todos] = {
    val q = quote {
      query[Todos]
    }
    val r = ctx.run(q)
    r
  }

  /**
    * TODO: impl with quill
    */
}
