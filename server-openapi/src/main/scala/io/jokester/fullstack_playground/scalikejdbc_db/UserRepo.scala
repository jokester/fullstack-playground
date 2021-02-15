package io.jokester.fullstack_playground.scalikejdbc_db

import cats.Id
import io.jokester.fullstack_playground.genereated_scalikejdbc.{User, UserProfile}
import org.postgresql.util.PGobject
import scalikejdbc._

import java.sql.PreparedStatement

trait UserRepoApi[Result[_]] {

  import io.jokester.fullstack_playground.genereated_scalikejdbc.User

  def createUser(email: String, pass: String): Result[User]
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

class UserRepo(implicit session: DBSession) extends UserRepoApi[Id] {
  import User._
  override def createUser(email: String, pass: String): Id[User] = {
    val profileBinder = ParameterBinder(
      // FIXME: how to use binder factory?
      value = "{}",
      binder = (stmt: PreparedStatement, idx: Int) => {
        val jsonObject = new PGobject()
        jsonObject.setType("json")
        jsonObject.setValue("{}")
        stmt.setObject(idx, jsonObject)
      },
    )

    implicit val scalikeJDBCFactory: ParameterBinderFactory[UserProfile] =
      // FIXME: not used??
      ParameterBinderFactory[UserProfile] { value => (stmt, idx) =>
        val jsonObject = new PGobject()
        jsonObject.setType("json")
        jsonObject.setValue("{}")
        stmt.setObject(idx, jsonObject)
      }
    val userId = sql"""
          INSERT INTO "public"."user" (${User.column.userEmail}, ${User.column.userPassword}, ${User.column.userProfile})
           VALUES (${email}, ${pass}, ${profileBinder})
         """.updateAndReturnGeneratedKey().apply().toInt

    findUser(userId).get
  }

  override def listUser(): Id[Seq[User]] = {

    sql"""
          SELECT ${user.resultName.*} FROM ${user}
       """.map(User(user.resultName)).list().apply()
  }

  override def findUser(userId: Int): Id[Option[User]] = {
    sql"""
          SELECT ${user.resultName.*} FROM ${user}
          WHERE ${user.userId} = ${userId}
       """.map(User(user.resultName)).single().apply()
  }

  override def updateUser(updated: User): Id[User] = ???

  override def removeUser(u: User): Id[User] = ???
}
