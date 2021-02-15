package io.jokester.fullstack_playground.scalikejdbc_db

import cats.Id
import io.jokester.fullstack_playground.genereated_scalikejdbc.{User, UserProfile}
import scalikejdbc._

trait UserRepoApi[Result[_]] {

  import io.jokester.fullstack_playground.genereated_scalikejdbc.User

  def createUser(email: String, pass: String, p: UserProfile): Result[User]
  def listUser(): Result[Seq[User]]
  def findUser(userId: Int): Result[Option[User]]
  def updateUser(updated: User): Result[User]
  def removeUser(u: User): Result[User]
}

trait TodoRepoApi[Result[_]] {

  import io.jokester.fullstack_playground.genereated_scalikejdbc.{Todo, User}

  def createTodo(user: User, todoTitle: String, todoDesc: String): Result[Todo]
  def listTodo(user: User): Result[Seq[Todo]]
  def listTodo(user: User, where: SQLSyntax): Result[Seq[Todo]]
  def findTodo(userId: Int): Result[Seq[Todo]]
  def updateTodo(updated: Todo): Result[Todo]
}

class UserRepo(implicit session: DBSession) extends UserRepoApi[Id] with OurBinders {
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
          User.column.userEmail    -> email,
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

  override def updateUser(updated: User): Id[User] = ???

  override def removeUser(u: User): Id[User] = {
    val removed = withSQL {
      delete.from(User).where.eq(User.column.userId, u.userId)
    }.update().apply()
    u
  }
}
