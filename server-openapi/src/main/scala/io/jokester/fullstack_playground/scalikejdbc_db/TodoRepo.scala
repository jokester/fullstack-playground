package io.jokester.fullstack_playground.scalikejdbc_db

import cats.Id
import io.jokester.fullstack_playground.genereated_scalikejdbc.{Todo, User}
import scalikejdbc._

trait TodoRepoApi[Result[_]] {

  import io.jokester.fullstack_playground.genereated_scalikejdbc.{Todo, User}

  def createTodo(user: User, todoTitle: String, todoDesc: String): Result[Todo]
  def listTodo(user: User): Result[Seq[Todo]]
  def listTodo(user: User, where: SQLSyntax): Result[Seq[Todo]]
  def findTodo(todoId: Int): Result[Option[Todo]]
  def updateTodo(updated: Todo): Result[Todo]
  def removeTodo(toRemove: Todo): Result[Todo]
}

final case class TodoRepo()(implicit session: DBSession) extends TodoRepoApi[Id] {
  override def createTodo(user: User, todoTitle: String, todoDesc: String): Id[Todo] = {
    val todoId = withSQL {
      insert
        .into(Todo)
        .namedValues(
          Todo.column.userId      -> user.userId,
          Todo.column.title       -> todoTitle,
          Todo.column.description -> todoDesc,
        )

    }.updateAndReturnGeneratedKey().apply()

    findTodo(todoId.toInt).get
  }

  override def listTodo(user: User): Id[Seq[Todo]] = {
    val t = Todo.syntax("t")
    sql"""
         SELECT ${t.resultName.*} FROM ${Todo as t}
         WHERE ${t.userId} = ${user.userId}
       """.map(Todo(t.resultName)).list().apply()
  }

  override def listTodo(user: User, where: SQLSyntax): Id[Seq[Todo]] = ???

  override def findTodo(todoId: Int): Id[Option[Todo]] = {
    val t = Todo.syntax("t")
    withSQL {
      select.from(Todo as t).where(sqls"${t.todoId} = ${todoId}")
    }.map(Todo(t)).single().apply()
  }

  override def updateTodo(updated: Todo): Id[Todo] = {
    val count = withSQL {
      update(Todo).set(
        Todo.column.title       -> updated.title,
        Todo.column.description -> updated.description,
        Todo.column.finishedAt  -> updated.finishedAt,
      )

    }.executeUpdate().apply()

    findTodo(updated.todoId).get
  }

  override def removeTodo(toRemove: Todo): Id[Todo] = ???
}
