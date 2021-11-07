package io.jokester.fullstack_playground.rdb_codegen

import com.typesafe.scalalogging.LazyLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.codegen.jdbc.SimpleJdbcCodegen
import io.getquill.codegen.model.{NameParser, SnakeCaseNames}
import io.getquill.{PostgresJdbcContext, SnakeCase}
import org.postgresql.ds.PGSimpleDataSource

object RdbCodegenMain extends App with LazyLogging {
  logger.debug("started")

  val destPkg = "io.jokester.fullstack_playground.quill.generated"

  def setupPostgres(configPrefix: String) = new PostgresJdbcContext(SnakeCase, configPrefix)

  def setupDefaultPostgres(): PostgresJdbcContext[SnakeCase.type] = {
    lazy val unpooledSrc = {
      val pgDataSource = new PGSimpleDataSource()
      pgDataSource.setURL(
        "jdbc:postgresql://localhost:61432/playground?user=pguser&password=secret&ssl=false",
      )
      pgDataSource
    }

    lazy val pooledSrc = {
      val config = new HikariConfig()
      config.setDataSource(unpooledSrc)
      new HikariDataSource(config)
    }

    val ctx = new PostgresJdbcContext(SnakeCase, pooledSrc)
    ctx
  }

  private val ctx = setupPostgres("devDB")

  private val gen: SimpleJdbcCodegen =
    new SimpleJdbcCodegen(ctx.dataSource, packagePrefix = destPkg) {
      override def nameParser: NameParser = SnakeCaseNames
    }

  gen.writeFiles(location =
    "stated-graphql-openapi/src/main/scala/" + destPkg.split("\\.").mkString("/"),
  )
}
