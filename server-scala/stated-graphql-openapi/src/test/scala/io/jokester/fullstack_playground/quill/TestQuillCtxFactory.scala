package io.jokester.fullstack_playground.quill

import io.getquill.{PostgresDialect, PostgresJdbcContext, SnakeCase}
import io.jokester.fullstack_playground.quill.generated.public.PublicExtensions

object TestQuillCtxFactory {
  private def createTestContext = QuillCtxFactory.createContext("database.test")

  def withTestContext(
      testCase: (
          PostgresJdbcContext[SnakeCase.type] with PublicExtensions[PostgresDialect, SnakeCase.type],
      ) => Unit,
  ): Unit = {
    val created = createTestContext
    try {
      testCase(created)
    } finally {
      created.close()
    }
  }
}
