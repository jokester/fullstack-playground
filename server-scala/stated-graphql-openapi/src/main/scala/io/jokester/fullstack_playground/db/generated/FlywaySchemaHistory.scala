package io.jokester.fullstack_playground.db.generated

import scalikejdbc._
import java.time.{OffsetDateTime}

case class FlywaySchemaHistory(
    installedRank: Int,
    version: Option[String] = None,
    description: String,
    `type`: String,
    script: String,
    checksum: Option[Int] = None,
    installedBy: String,
    installedOn: OffsetDateTime,
    executionTime: Int,
    success: Boolean,
) {

  def save()(implicit session: DBSession): FlywaySchemaHistory =
    FlywaySchemaHistory.save(this)(session)

  def destroy()(implicit session: DBSession): Int = FlywaySchemaHistory.destroy(this)(session)

}

object FlywaySchemaHistory extends SQLSyntaxSupport[FlywaySchemaHistory] {

  override val schemaName = Some("public")

  override val tableName = "flyway_schema_history"

  override val columns = Seq(
    "installed_rank",
    "version",
    "description",
    "type",
    "script",
    "checksum",
    "installed_by",
    "installed_on",
    "execution_time",
    "success",
  )

  def apply(fsh: SyntaxProvider[FlywaySchemaHistory])(rs: WrappedResultSet): FlywaySchemaHistory =
    apply(fsh.resultName)(rs)
  def apply(fsh: ResultName[FlywaySchemaHistory])(rs: WrappedResultSet): FlywaySchemaHistory =
    new FlywaySchemaHistory(
      installedRank = rs.get(fsh.installedRank),
      version = rs.get(fsh.version),
      description = rs.get(fsh.description),
      `type` = rs.get(fsh.`type`),
      script = rs.get(fsh.script),
      checksum = rs.get(fsh.checksum),
      installedBy = rs.get(fsh.installedBy),
      installedOn = rs.get(fsh.installedOn),
      executionTime = rs.get(fsh.executionTime),
      success = rs.get(fsh.success),
    )

  val fsh = FlywaySchemaHistory.syntax("fsh")

  override val autoSession = AutoSession

  def find(installedRank: Int)(implicit session: DBSession): Option[FlywaySchemaHistory] = {
    sql"""select ${fsh.result.*} from ${FlywaySchemaHistory as fsh} where ${fsh.installedRank} = ${installedRank}"""
      .map(FlywaySchemaHistory(fsh.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[FlywaySchemaHistory] = {
    sql"""select ${fsh.result.*} from ${FlywaySchemaHistory as fsh}"""
      .map(FlywaySchemaHistory(fsh.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${FlywaySchemaHistory.table}"""
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[FlywaySchemaHistory] = {
    sql"""select ${fsh.result.*} from ${FlywaySchemaHistory as fsh} where ${where}"""
      .map(FlywaySchemaHistory(fsh.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[FlywaySchemaHistory] = {
    sql"""select ${fsh.result.*} from ${FlywaySchemaHistory as fsh} where ${where}"""
      .map(FlywaySchemaHistory(fsh.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${FlywaySchemaHistory as fsh} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(
      installedRank: Int,
      version: Option[String] = None,
      description: String,
      `type`: String,
      script: String,
      checksum: Option[Int] = None,
      installedBy: String,
      installedOn: OffsetDateTime,
      executionTime: Int,
      success: Boolean,
  )(implicit session: DBSession): FlywaySchemaHistory = {
    sql"""
      insert into ${FlywaySchemaHistory.table} (
        ${column.installedRank},
        ${column.version},
        ${column.description},
        ${column.`type`},
        ${column.script},
        ${column.checksum},
        ${column.installedBy},
        ${column.installedOn},
        ${column.executionTime},
        ${column.success}
      ) values (
        ${installedRank},
        ${version},
        ${description},
        ${`type`},
        ${script},
        ${checksum},
        ${installedBy},
        ${installedOn},
        ${executionTime},
        ${success}
      )
      """.update.apply()

    FlywaySchemaHistory(
      installedRank = installedRank,
      version = version,
      description = description,
      `type` = `type`,
      script = script,
      checksum = checksum,
      installedBy = installedBy,
      installedOn = installedOn,
      executionTime = executionTime,
      success = success,
    )
  }

  def batchInsert(
      entities: collection.Seq[FlywaySchemaHistory],
  )(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("installedRank") -> entity.installedRank,
        Symbol("version")       -> entity.version,
        Symbol("description")   -> entity.description,
        Symbol("type")          -> entity.`type`,
        Symbol("script")        -> entity.script,
        Symbol("checksum")      -> entity.checksum,
        Symbol("installedBy")   -> entity.installedBy,
        Symbol("installedOn")   -> entity.installedOn,
        Symbol("executionTime") -> entity.executionTime,
        Symbol("success")       -> entity.success,
      ),
    )
    SQL("""insert into flyway_schema_history(
      installed_rank,
      version,
      description,
      type,
      script,
      checksum,
      installed_by,
      installed_on,
      execution_time,
      success
    ) values (
      {installedRank},
      {version},
      {description},
      {type},
      {script},
      {checksum},
      {installedBy},
      {installedOn},
      {executionTime},
      {success}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: FlywaySchemaHistory)(implicit session: DBSession): FlywaySchemaHistory = {
    sql"""
      update
        ${FlywaySchemaHistory.table}
      set
        ${column.installedRank} = ${entity.installedRank},
        ${column.version} = ${entity.version},
        ${column.description} = ${entity.description},
        ${column.`type`} = ${entity.`type`},
        ${column.script} = ${entity.script},
        ${column.checksum} = ${entity.checksum},
        ${column.installedBy} = ${entity.installedBy},
        ${column.installedOn} = ${entity.installedOn},
        ${column.executionTime} = ${entity.executionTime},
        ${column.success} = ${entity.success}
      where
        ${column.installedRank} = ${entity.installedRank}
      """.update.apply()
    entity
  }

  def destroy(entity: FlywaySchemaHistory)(implicit session: DBSession): Int = {
    sql"""delete from ${FlywaySchemaHistory.table} where ${column.installedRank} = ${entity.installedRank}""".update
      .apply()
  }

}
