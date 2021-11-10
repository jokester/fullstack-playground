package io.jokester.fullstack_playground.quill

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.util.LoadConfig
import io.getquill.{PostgresDialect, PostgresJdbcContext, SnakeCase}
import io.jokester.fullstack_playground.quill.generated.public.PublicExtensions
import org.postgresql.ds.PGSimpleDataSource

import javax.sql.DataSource

object QuillCtxFactory {

  def setupPostgres(
      configPrefix: String,
  ): PostgresJdbcContext[SnakeCase.type] with PublicExtensions[PostgresDialect, SnakeCase.type] = {
    val dbConf = LoadConfig(configPrefix)
    val pgSource = simplePgDataSource(
      dbConf.getString("url"),
      dbConf.getString("user"),
      dbConf.getString("password"),
    )
    val hikariSource = pooledDataSource(pgSource)

    new PostgresJdbcContext[SnakeCase.type](SnakeCase, hikariSource)
      with PublicExtensions[PostgresDialect, SnakeCase.type]
  }

  /**
    * @param url JDBC URL like "jdbc:postgresql://localhost:61432/playground?user=pguser&password=secret&ssl=false"
    * @return
    */
  private def simplePgDataSource(
      url: String,
      user: String,
      password: String,
  ): PGSimpleDataSource = {
    val pgDataSource = new PGSimpleDataSource()
    pgDataSource.setURL(url)
    pgDataSource.setUser(user)
    pgDataSource.setPassword(password)
    pgDataSource
  }

  /**
    * @deprecated DO NOT USE not required or useful in codegen
    */
  private def pooledDataSource(wrapped: DataSource): HikariDataSource = {
    val config = new HikariConfig()
    config.setDataSource(wrapped)
    new HikariDataSource(config)
  }

}
