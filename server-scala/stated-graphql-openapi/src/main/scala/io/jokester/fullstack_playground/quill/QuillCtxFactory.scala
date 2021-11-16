package io.jokester.fullstack_playground.quill

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.util.LoadConfig
import io.getquill.{PostgresDialect, PostgresJdbcContext, SnakeCase}
import io.jokester.fullstack_playground.quill.generated.public.PublicExtensions
import org.postgresql.ds.PGSimpleDataSource

import javax.sql.DataSource

object QuillCtxFactory {

  /**
    * how table/columns are escaped in SQL
    */
  object FixedPostgresNaming extends SnakeCase {
    override def table(s: String): String  = s"""\"${super.table(s)}\""""
    override def column(s: String): String = s"""\"${super.column(s)}\""""
  }
  type OurCtx = PostgresJdbcContext[FixedPostgresNaming.type]
    with PublicExtensions[PostgresDialect, FixedPostgresNaming.type]

  def createContext(
      configPrefix: String,
  ): PostgresJdbcContext[FixedPostgresNaming.type]
    with PublicExtensions[PostgresDialect, FixedPostgresNaming.type] = {
    val dbConf = LoadConfig(configPrefix)
    val pgSource = simplePgDataSource(
      dbConf.getString("url"),
      dbConf.getString("user"),
      dbConf.getString("password"),
    )
    val hikariSource = pooledDataSource(pgSource)

    new PostgresJdbcContext[FixedPostgresNaming.type](FixedPostgresNaming, hikariSource)
      with PublicExtensions[PostgresDialect, FixedPostgresNaming.type]
  }

  /**
    * @param url JDBC URL like "jdbc:postgresql://localhost:61432/playground"
    *            (user / password in URL gets ignored)
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
    * @note must be closed manually
    */
  private def pooledDataSource(wrapped: DataSource): HikariDataSource = {
    val config = new HikariConfig()
    config.setDataSource(wrapped)
//    config.setConnectionInitSql("set time zone 'UTC'")
    new HikariDataSource(config)
  }

}
