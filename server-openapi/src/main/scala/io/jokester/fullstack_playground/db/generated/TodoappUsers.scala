package io.jokester.fullstack_playground.db.generated

import scalikejdbc._
import java.time.{OffsetDateTime}

case class TodoappUsers(
    userId: Long,
    userEmail: String,
    userProfile: Any,
    passwordHash: String,
    createdAt: OffsetDateTime,
    updatedAt: OffsetDateTime,
) {

  def save()(implicit session: DBSession): TodoappUsers = TodoappUsers.save(this)(session)

  def destroy()(implicit session: DBSession): Int = TodoappUsers.destroy(this)(session)

}

object TodoappUsers extends SQLSyntaxSupport[TodoappUsers] {

  override val schemaName = Some("public")

  override val tableName = "todoapp_users"

  override val columns =
    Seq("user_id", "user_email", "user_profile", "password_hash", "created_at", "updated_at")

  def apply(tu: SyntaxProvider[TodoappUsers])(rs: WrappedResultSet): TodoappUsers =
    apply(tu.resultName)(rs)
  def apply(tu: ResultName[TodoappUsers])(rs: WrappedResultSet): TodoappUsers =
    new TodoappUsers(
      userId = rs.get(tu.userId),
      userEmail = rs.get(tu.userEmail),
      userProfile = rs.any(tu.userProfile),
      passwordHash = rs.get(tu.passwordHash),
      createdAt = rs.get(tu.createdAt),
      updatedAt = rs.get(tu.updatedAt),
    )

  val tu = TodoappUsers.syntax("tu")

  override val autoSession = AutoSession

  def find(userId: Long)(implicit session: DBSession): Option[TodoappUsers] = {
    sql"""select ${tu.result.*} from ${TodoappUsers as tu} where ${tu.userId} = ${userId}"""
      .map(TodoappUsers(tu.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[TodoappUsers] = {
    sql"""select ${tu.result.*} from ${TodoappUsers as tu}"""
      .map(TodoappUsers(tu.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${TodoappUsers.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[TodoappUsers] = {
    sql"""select ${tu.result.*} from ${TodoappUsers as tu} where ${where}"""
      .map(TodoappUsers(tu.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[TodoappUsers] = {
    sql"""select ${tu.result.*} from ${TodoappUsers as tu} where ${where}"""
      .map(TodoappUsers(tu.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${TodoappUsers as tu} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(
      userEmail: String,
      userProfile: Any,
      passwordHash: String,
      createdAt: OffsetDateTime,
      updatedAt: OffsetDateTime,
  )(implicit session: DBSession): TodoappUsers = {
    val generatedKey = sql"""
      insert into ${TodoappUsers.table} (
        ${column.userEmail},
        ${column.userProfile},
        ${column.passwordHash},
        ${column.createdAt},
        ${column.updatedAt}
      ) values (
        ${userEmail},
        ${userProfile},
        ${passwordHash},
        ${createdAt},
        ${updatedAt}
      )
      """.updateAndReturnGeneratedKey.apply()

    TodoappUsers(
      userId = generatedKey,
      userEmail = userEmail,
      userProfile = userProfile,
      passwordHash = passwordHash,
      createdAt = createdAt,
      updatedAt = updatedAt,
    )
  }

  def batchInsert(
      entities: collection.Seq[TodoappUsers],
  )(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("userEmail")    -> entity.userEmail,
        Symbol("userProfile")  -> entity.userProfile,
        Symbol("passwordHash") -> entity.passwordHash,
        Symbol("createdAt")    -> entity.createdAt,
        Symbol("updatedAt")    -> entity.updatedAt,
      ),
    )
    SQL("""insert into todoapp_users(
      user_email,
      user_profile,
      password_hash,
      created_at,
      updated_at
    ) values (
      {userEmail},
      {userProfile},
      {passwordHash},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: TodoappUsers)(implicit session: DBSession): TodoappUsers = {
    sql"""
      update
        ${TodoappUsers.table}
      set
        ${column.userId} = ${entity.userId},
        ${column.userEmail} = ${entity.userEmail},
        ${column.userProfile} = ${entity.userProfile},
        ${column.passwordHash} = ${entity.passwordHash},
        ${column.createdAt} = ${entity.createdAt},
        ${column.updatedAt} = ${entity.updatedAt}
      where
        ${column.userId} = ${entity.userId}
      """.update.apply()
    entity
  }

  def destroy(entity: TodoappUsers)(implicit session: DBSession): Int = {
    sql"""delete from ${TodoappUsers.table} where ${column.userId} = ${entity.userId}""".update
      .apply()
  }

}
