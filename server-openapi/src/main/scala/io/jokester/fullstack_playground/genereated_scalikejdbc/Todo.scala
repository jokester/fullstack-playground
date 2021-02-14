package io.jokester.fullstack_playground.genereated_scalikejdbc

import io.jokester.fullstack_playground.scalikejdbc_db.ScalikeJDBCConnection
import scalikejdbc._

import java.time.OffsetDateTime

case class Todo(
    todoId: Int,
    userId: Int,
    title: String,
    description: String,
    finishedAt: Option[OffsetDateTime] = None,
    createdAt: OffsetDateTime,
    updatedAt: OffsetDateTime,
)

object Todo extends SQLSyntaxSupport[Todo] {
  override val schemaName = Some("public")

  override val tableName = "todo"

  override val columns =
    Seq("todo_id", "user_id", "title", "description", "finished_at", "created_at", "updated_at")

  def apply(t: SyntaxProvider[Todo])(rs: WrappedResultSet): Todo = apply(t.resultName)(rs)
  def apply(t: ResultName[Todo])(rs: WrappedResultSet): Todo =
    Todo(
      todoId = rs.get(t.todoId),
      userId = rs.get(t.userId),
      title = rs.get(t.title),
      description = rs.get(t.description),
      finishedAt = rs.get(t.finishedAt),
      createdAt = rs.get(t.createdAt),
      updatedAt = rs.get(t.updatedAt),
    )

  val t = Todo.syntax("t")

//  override val autoSession = AutoSession
  override val connectionPoolName = ScalikeJDBCConnection.poolName

}
