package io.jokester.fullstack_playground.quill

import io.getquill.{PostgresDialect, PostgresJdbcContext, SnakeCase}
import io.jokester.fullstack_playground.quill.generated.public.PublicExtensions

object QuillCtxFactory {
  def setupPostgres(configPrefix: String) = {
    new PostgresJdbcContext(SnakeCase, configPrefix)
      with PublicExtensions[PostgresDialect, SnakeCase.type]
  }

}
