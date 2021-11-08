package io.jokester.fullstack_playground.quill

import io.jokester.fullstack_playground.quill.generated.public.{PublicExtensions, Todos}
import io.getquill.{EntityQuery, PostgresDialect, PostgresJdbcContext, SnakeCase}

class QuillTodoApiImpl(
    private val ctx: PostgresJdbcContext[SnakeCase.type]
      with PublicExtensions[PostgresDialect, SnakeCase.type],
) {
  import ctx.{query, quote}
  import ctx.PublicSchema.TodosDao

  /**
    * TODO: impl with quill
    */
}
