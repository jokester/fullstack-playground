package io.jokester.fullstack_playground.rdb_codegen

import com.typesafe.scalalogging.LazyLogging
import io.getquill.codegen.jdbc.SimpleJdbcCodegen
import io.getquill.codegen.model.SnakeCaseNames
import io.getquill.{PostgresJdbcContext, SnakeCase}

object RdbCodegenMain extends App with LazyLogging {
  logger.debug("started")
  private val ctx = new PostgresJdbcContext(SnakeCase, "devDB")

  val destPkg = "io.jokester.fullstack_playground.stated"

  private val gen = new SimpleJdbcCodegen(ctx.dataSource, packagePrefix = destPkg) {
    override def nameParser = SnakeCaseNames
  }
  gen.writeFiles(location =
    "stated-graphql-openapi/src/main/scala/" + destPkg.split("\\.").mkString("/"),
  )
}
