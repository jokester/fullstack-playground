package io.jokester.fullstack_playground.db.generated

import scalikejdbc._
import java.time.{OffsetDateTime}

case class TodoappTodos(
    todoId: Long,
    userId: Int,
    title: String,
    description: String,
    finishedAt: Option[OffsetDateTime] = None,
    createdAt: OffsetDateTime,
    updatedAt: OffsetDateTime,
) {

  def save()(implicit session: DBSession): TodoappTodos = TodoappTodos.save(this)(session)

  def destroy()(implicit session: DBSession): Int = TodoappTodos.destroy(this)(session)

}

object TodoappTodos extends SQLSyntaxSupport[TodoappTodos] {

  override val schemaName = Some("public")

  override val tableName = "todoapp_todos"

  override val columns =
    Seq("todo_id", "user_id", "title", "description", "finished_at", "created_at", "updated_at")

  def apply(tt: SyntaxProvider[TodoappTodos])(rs: WrappedResultSet): TodoappTodos =
    apply(tt.resultName)(rs)
  def apply(tt: ResultName[TodoappTodos])(rs: WrappedResultSet): TodoappTodos =
    new TodoappTodos(
      todoId = rs.get(tt.todoId),
      userId = rs.get(tt.userId),
      title = rs.get(tt.title),
      description = rs.get(tt.description),
      finishedAt = rs.get(tt.finishedAt),
      createdAt = rs.get(tt.createdAt),
      updatedAt = rs.get(tt.updatedAt),
    )

  val tt = TodoappTodos.syntax("tt")

  override val autoSession = AutoSession

  def find(todoId: Long)(implicit session: DBSession): Option[TodoappTodos] = {
    sql"""select ${tt.result.*} from ${TodoappTodos as tt} where ${tt.todoId} = ${todoId}"""
      .map(TodoappTodos(tt.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[TodoappTodos] = {
    sql"""select ${tt.result.*} from ${TodoappTodos as tt}"""
      .map(TodoappTodos(tt.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${TodoappTodos.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[TodoappTodos] = {
    sql"""select ${tt.result.*} from ${TodoappTodos as tt} where ${where}"""
      .map(TodoappTodos(tt.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[TodoappTodos] = {
    sql"""select ${tt.result.*} from ${TodoappTodos as tt} where ${where}"""
      .map(TodoappTodos(tt.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${TodoappTodos as tt} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(
      userId: Int,
      title: String,
      description: String,
      finishedAt: Option[OffsetDateTime] = None,
      createdAt: OffsetDateTime,
      updatedAt: OffsetDateTime,
  )(implicit session: DBSession): TodoappTodos = {
    val generatedKey = sql"""
      insert into ${TodoappTodos.table} (
        ${column.userId},
        ${column.title},
        ${column.description},
        ${column.finishedAt},
        ${column.createdAt},
        ${column.updatedAt}
      ) values (
        ${userId},
        ${title},
        ${description},
        ${finishedAt},
        ${createdAt},
        ${updatedAt}
      )
      """.updateAndReturnGeneratedKey.apply()

    TodoappTodos(
      todoId = generatedKey,
      userId = userId,
      title = title,
      description = description,
      finishedAt = finishedAt,
      createdAt = createdAt,
      updatedAt = updatedAt,
    )
  }

  def batchInsert(
      entities: collection.Seq[TodoappTodos],
  )(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("userId")      -> entity.userId,
        Symbol("title")       -> entity.title,
        Symbol("description") -> entity.description,
        Symbol("finishedAt")  -> entity.finishedAt,
        Symbol("createdAt")   -> entity.createdAt,
        Symbol("updatedAt")   -> entity.updatedAt,
      ),
    )
    SQL("""insert into todoapp_todos(
      user_id,
      title,
      description,
      finished_at,
      created_at,
      updated_at
    ) values (
      {userId},
      {title},
      {description},
      {finishedAt},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: TodoappTodos)(implicit session: DBSession): TodoappTodos = {
    sql"""
      update
        ${TodoappTodos.table}
      set
        ${column.todoId} = ${entity.todoId},
        ${column.userId} = ${entity.userId},
        ${column.title} = ${entity.title},
        ${column.description} = ${entity.description},
        ${column.finishedAt} = ${entity.finishedAt},
        ${column.createdAt} = ${entity.createdAt},
        ${column.updatedAt} = ${entity.updatedAt}
      where
        ${column.todoId} = ${entity.todoId}
      """.update.apply()
    entity
  }

  def destroy(entity: TodoappTodos)(implicit session: DBSession): Int = {
    sql"""delete from ${TodoappTodos.table} where ${column.todoId} = ${entity.todoId}""".update
      .apply()
  }

}
