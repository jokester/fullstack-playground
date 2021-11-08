package io.jokester.fullstack_playground.rdb_codegen

import com.typesafe.scalalogging.LazyLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.codegen.jdbc.model.JdbcTypes.JdbcQuerySchemaNaming
import io.getquill.codegen.jdbc.{ComposeableTraitsJdbcCodegen, SimpleJdbcCodegen}
import io.getquill.codegen.model._
import io.getquill.{PostgresJdbcContext, SnakeCase}
import org.postgresql.ds.PGSimpleDataSource

/**
  * A to run code generation (before I learned better way)
  */
object RdbCodegenMain extends App with LazyLogging {
  logger.debug("started")

  val destPkg = "io.jokester.fullstack_playground.quill.generated"

  lazy val simplePgDataSource = {
    val pgDataSource = new PGSimpleDataSource()
    pgDataSource.setURL(
      "jdbc:postgresql://localhost:61432/playground?user=pguser&password=secret&ssl=false",
    )
    pgDataSource
  }

  /**
    * @deprecated DO NOT USE not required or useful in codegen
    */
  lazy val pooledPgDataSource = {
    val config = new HikariConfig()
    config.setDataSource(simplePgDataSource)
    new HikariDataSource(config)
  }

  lazy val simpleCodeGen =
    new SimpleJdbcCodegen(simplePgDataSource, packagePrefix = destPkg) {
      override def nameParser: NameParser = SnakeCaseNames
    }

  lazy val traitsCodeGen = {
    new ComposeableTraitsJdbcCodegen(
      simplePgDataSource,
      packagePrefix = destPkg,
      nestedTrait = true,
    ) {
//      override def defaultNamespace: String = "public"

      /**
        * CustomNames() for explicit postgres schema
        */
      override def nameParser: NameParser = CustomNames()

      override def packagingStrategy: PackagingStrategy = {
        PackagingStrategy.ByPackageHeader
          .TablePerSchema(destPkg)
          .copy(
            packageNamingStrategyForQuerySchemas =
              PackageHeaderByNamespace(destPkg, _.table.namespace),
          )
      }

      /**
        * use `ThrowTypingError` to fail early
        */
      override def unrecognizedTypeStrategy: UnrecognizedTypeStrategy = ThrowTypingError

      override def typer: Typer = new OurPostgresTyper(unrecognizedTypeStrategy, numericPreference)

//      override def namespacer: Namespacer[JdbcTableMeta] = ts => ts.tableSchem.getOrElse("public")

      override def querySchemaNaming: JdbcQuerySchemaNaming = super.querySchemaNaming
    }
  }

  traitsCodeGen.writeFiles(location =
    "stated-graphql-openapi/src/main/scala/" + destPkg.split("\\.").mkString("/"),
  )
}
