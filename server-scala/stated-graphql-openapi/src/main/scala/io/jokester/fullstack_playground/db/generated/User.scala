package io.jokester.fullstack_playground.db.generated

import scalikejdbc._

case class User(email: String, passwordHash: String, nickname: String, userId: Int) {

  def save()(implicit session: DBSession): User = User.save(this)(session)

  def destroy()(implicit session: DBSession): Int = User.destroy(this)(session)

}

object User extends SQLSyntaxSupport[User] {

  override val schemaName = Some("chatroom")

  override val tableName = "user"

  override val columns = Seq("email", "password_hash", "nickname", "user_id")

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User =
    new User(
      email = rs.get(u.email),
      passwordHash = rs.get(u.passwordHash),
      nickname = rs.get(u.nickname),
      userId = rs.get(u.userId),
    )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(userId: Int)(implicit session: DBSession): Option[User] = {
    sql"""select ${u.result.*} from ${User as u} where ${u.userId} = ${userId}"""
      .map(User(u.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[User] = {
    sql"""select ${u.result.*} from ${User as u}""".map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${User.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[User] = {
    sql"""select ${u.result.*} from ${User as u} where ${where}"""
      .map(User(u.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[User] = {
    sql"""select ${u.result.*} from ${User as u} where ${where}"""
      .map(User(u.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${User as u} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(email: String, passwordHash: String, nickname: String)(implicit
      session: DBSession,
  ): User = {
    val generatedKey = sql"""
      insert into ${User.table} (
        ${column.email},
        ${column.passwordHash},
        ${column.nickname}
      ) values (
        ${email},
        ${passwordHash},
        ${nickname}
      )
      """.updateAndReturnGeneratedKey.apply()

    User(
      userId = generatedKey.toInt,
      email = email,
      passwordHash = passwordHash,
      nickname = nickname,
    )
  }

  def batchInsert(entities: collection.Seq[User])(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("email")        -> entity.email,
        Symbol("passwordHash") -> entity.passwordHash,
        Symbol("nickname")     -> entity.nickname,
      ),
    )
    SQL("""insert into user(
      email,
      password_hash,
      nickname
    ) values (
      {email},
      {passwordHash},
      {nickname}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: User)(implicit session: DBSession): User = {
    sql"""
      update
        ${User.table}
      set
        ${column.email} = ${entity.email},
        ${column.passwordHash} = ${entity.passwordHash},
        ${column.nickname} = ${entity.nickname},
        ${column.userId} = ${entity.userId}
      where
        ${column.userId} = ${entity.userId}
      """.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession): Int = {
    sql"""delete from ${User.table} where ${column.userId} = ${entity.userId}""".update.apply()
  }

}
