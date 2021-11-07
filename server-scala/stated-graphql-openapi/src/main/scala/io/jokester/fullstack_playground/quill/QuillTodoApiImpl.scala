package io.jokester.fullstack_playground.quill

import io.jokester.fullstack_playground.quill.generated.public.Todos
import io.getquill.{EntityQuery, PostgresJdbcContext, SnakeCase}

class QuillTodoApiImpl(private val ctx: PostgresJdbcContext[SnakeCase.type]) {
  import ctx.{query, quote}

  val listTodoQuery = quote { query[Todos] }
  val constQuery    = quote(3.14)

  def listTodos(): Unit = {
    val queried = ctx.run(listTodoQuery)
  }
}
