package io.jokester.fullstack_playground.genereated_scalikejdbc

import scalikejdbc._
import java.time.{OffsetDateTime}

case class Todo(
    todoId: Int,
    title: String,
    desc: String,
    finishedAt: Option[OffsetDateTime] = None,
    createdAt: OffsetDateTime,
    updatedAt: OffsetDateTime,
) {

//  def save()(implicit session: DBSession): Todo = Todo.save(this)(session)

//  def destroy()(implicit session: DBSession): Int = Todo.destroy(this)(session)

}

object Todo extends SQLSyntaxSupport[Todo] {

  override val schemaName = Some("public")

  override val tableName = "todo"

  override val columns = Seq("todo_id", "title", "desc", "finished_at", "created_at", "updated_at")

  def apply(t: SyntaxProvider[Todo])(rs: WrappedResultSet): Todo = apply(t.resultName)(rs)
  def apply(t: ResultName[Todo])(rs: WrappedResultSet): Todo =
    new Todo(
      todoId = rs.get(t.todoId),
      title = rs.get(t.title),
      desc = rs.get(t.desc),
      finishedAt = rs.get(t.finishedAt),
      createdAt = rs.get(t.createdAt),
      updatedAt = rs.get(t.updatedAt),
    )

  val t = Todo.syntax("t")

  override val autoSession = AutoSession

  def find(todoId: Int)(implicit session: DBSession): Option[Todo] = {
    sql"""select ${t.result.*} from ${Todo as t} where ${t.todoId} = ${todoId}"""
      .map(Todo(t.resultName))
      .single
      .apply()
  }

  def findAll()(implicit session: DBSession): List[Todo] = {
    sql"""select ${t.result.*} from ${Todo as t}""".map(Todo(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    sql"""select count(1) from ${Todo.table}""".map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Todo] = {
    sql"""select ${t.result.*} from ${Todo as t} where ${where}"""
      .map(Todo(t.resultName))
      .single
      .apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Todo] = {
    sql"""select ${t.result.*} from ${Todo as t} where ${where}"""
      .map(Todo(t.resultName))
      .list
      .apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    sql"""select count(1) from ${Todo as t} where ${where}"""
      .map(_.long(1))
      .single
      .apply()
      .get
  }

  def create(
      title: String,
      desc: String,
      finishedAt: Option[OffsetDateTime] = None,
  )(implicit session: DBSession): Todo = {
    val generatedKey = sql"""
      insert into ${Todo.table} (
        ${column.title},
        "${column.desc}",
        ${column.finishedAt}
      ) values (
        ${title},
        ${desc},
        ${finishedAt}
      )
      """.updateAndReturnGeneratedKey.apply()

    find(generatedKey.toInt).get
  }

  def batchInsert(entities: collection.Seq[Todo])(implicit session: DBSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        Symbol("title")      -> entity.title,
        Symbol("desc")       -> entity.desc,
        Symbol("finishedAt") -> entity.finishedAt,
        Symbol("createdAt")  -> entity.createdAt,
        Symbol("updatedAt")  -> entity.updatedAt,
      ),
    )
    SQL("""insert into todo(
      title,
      "desc",
      finished_at,
      created_at,
      updated_at
    ) values (
      {title},
      {desc},
      {finishedAt},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: Todo)(implicit session: DBSession): Todo = {
    sql"""
      update
        ${Todo.table}
      set
        ${column.todoId} = ${entity.todoId},
        ${column.title} = ${entity.title},
        "${column.desc}" = ${entity.desc},
        ${column.finishedAt} = ${entity.finishedAt}
      where
        ${column.todoId} = ${entity.todoId}
      """.update.apply()
    entity
  }

  def destroy(entity: Todo)(implicit session: DBSession): Int = {
    sql"""delete from ${Todo.table} where ${column.todoId} = ${entity.todoId}""".update.apply()
  }

}
