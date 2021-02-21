package io.jokester.fullstack_playground.scalikejdbc_db

import cats.Id
import io.jokester.fullstack_playground.genereated_scalikejdbc.{User, UserProfile}
import scalikejdbc._

/**
  * FIXME meaningless to support generic here?
  */
trait UserRepoApi[Result[_]] {

  import io.jokester.fullstack_playground.genereated_scalikejdbc.User

  def createUser(email: String, pass: String, p: UserProfile): Result[User]
  def listUser(): Result[Seq[User]]
  def findUser(userId: Int): Result[Option[User]]
  def updateUser(updated: User): Result[Option[User]]
  def removeUser(u: User): Result[User]
}

final case class UserRepo()(implicit session: DBSession) extends UserRepoApi[Id] with OurBinders {
  import User._
  override def createUser(email: String, pass: String, p: UserProfile): Id[User] = {
    if (3 > 2) {
      createUser2(email, pass, p)
    } else {
      createUser3(email, pass, p)
    }
  }

  private def createUser2(email: String, pass: String, p: UserProfile): User = {
    val userId = withSQL {
      insert
        .into(User)
        .namedValues(
          User.column.userEmail    -> email.toLowerCase(),
          User.column.userPassword -> pass,
          User.column.userProfile  -> p,
        )
    }.updateAndReturnGeneratedKey().apply().toInt

    findUser(userId).get
  }

  override def findUser(userId: Int): Id[Option[User]] = {
    sql"""
          SELECT ${user.result.*} FROM ${User as user}
          WHERE ${user.userId} = ${userId}
       """.map(User(user.resultName)).single().apply()
  }

  def findUserByEmail(email: String): Id[Option[User]] = {
    sql"""
          SELECT ${user.result.*} FROM ${User as user}
          WHERE ${user.userEmail} = LOWER(${email})
       """.map(User(user.resultName)).single().apply()
  }

  private def createUser3(email: String, pass: String, p: UserProfile): User = {
    // FIXME: for some reason binder is not used
    val userId = sql"""
          INSERT INTO ${User} (${User.column.userEmail}, ${User.column.userPassword}, ${User.column.userProfile})
           VALUES (${email}, ${pass}, ${p})
         """.updateAndReturnGeneratedKey().apply().toInt
    findUser(userId).get
  }

  override def listUser(): Id[Seq[User]] = {

    sql"""
          SELECT ${user.resultName.*} FROM ${user}
       """.map(User(user.resultName)).list().apply()
  }

  override def updateUser(updated: User): Option[User] = {
    val updatedCount = withSQL {
      update(User)
        .set(
          User.column.userProfile  -> updated.userProfile,
          User.column.userPassword -> updated.userPassword,
        )
        .where(sqls"${User.column.userId} = ${updated.userId}")
    }.update.apply()

    findUser(updated.userId)
  }

  override def removeUser(u: User): Id[User] = {
    val removed = withSQL {
      delete.from(User).where.eq(User.column.userId, u.userId)
    }.update().apply()
    u
  }
}
