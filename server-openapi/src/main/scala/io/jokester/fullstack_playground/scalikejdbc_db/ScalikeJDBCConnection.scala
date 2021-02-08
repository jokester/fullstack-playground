package io.jokester.fullstack_playground.scalikejdbc_db
import com.typesafe.scalalogging.LazyLogging
import scalikejdbc._

import java.time.OffsetDateTime
import scala.collection.mutable.ListBuffer

object ScalikeJDBCConnection extends LazyLogging {
  def initSingleton(): Unit = {
    ConnectionPool.singleton(
      "jdbc:postgresql://127.0.0.1:61432/try_hasura",
      "pguser",
      "secret",
    )
  }

  def initNamed(): Unit = {
    ConnectionPool.add(
      Symbol("pg"),
      "jdbc:postgresql://127.0.0.1:61432/try_hasura",
      "pguser",
      "secret",
    )
  }

  def showPools(): Unit = {
    logger.debug(s"initialized(default): ${ConnectionPool.isInitialized(Symbol("default"))}")
    logger.debug(s"initialized(pg): ${ConnectionPool.isInitialized(Symbol("pg"))}")
  }

  def db: DB.type = DB

  // FIXME: for unknown reason this fails in 2nd call
  def db2(): NamedDB = NamedDB(Symbol("pg"))

  def db3(): DB = DB(ConnectionPool.borrow(Symbol("pg")))

  def db4() = DB

}

object ScalikeTodoRepository extends LazyLogging {

  case class Todo(
      todoId: Int,
      title: String,
      desc: String,
      finishedAt: Option[OffsetDateTime],
      updatedAt: OffsetDateTime,
      createdAt: OffsetDateTime,
  )

  object Todo extends TodoMeta(Symbol("pg"))

  class TodoMeta(pool: Symbol) extends SQLSyntaxSupport[Todo] {
    override val tableName                  = "todo"
    override val connectionPoolName: Symbol = pool // required to use non-default pool
    def apply(g: ResultName[Todo])(rs: WrappedResultSet): Todo = {
      Todo(
        todoId = rs.int(g.todoId),
        title = rs.string(g.title),
        desc = rs.string(g.desc),
        finishedAt = rs.offsetDateTimeOpt(g.finishedAt),
        updatedAt = rs.offsetDateTime(g.updatedAt),
        createdAt = rs.offsetDateTime(g.createdAt),
      )
    }
  }
  def list()(implicit session: DBSession): Seq[Todo] = {
    val todo = Todo.syntax("t")
    val query =
      sql"""
        SELECT ${todo.result.*} FROM ${Todo as todo}
         """.map(Todo(todo.resultName)).list.apply()
    query
  }

  def find(todoId: Int)(implicit session: DBSession): Option[Todo] = {
    val t = Todo.syntax("t")

    sql"""
        SELECT ${t.result.*} FROM ${Todo as t} WHERE ${t.todoId} = ${todoId}
   """.map(Todo(t.resultName)).single.apply()
  }

  def findDesc(todoId: Int)(implicit session: DBSession): Option[String] = {
    val t = Todo.syntax("t")
    sql"""
          SELECT ${t.desc} FROM ${Todo as t} WHERE ${t.todoId} = ${todoId}
         
       """.map(rs => rs.string(1)).single().apply()
  }

  def update(todoId: Int, finishedAt: Option[OffsetDateTime])(implicit
      session: DBSession,
  ): Option[Todo] = {
    val updated = sql"""
      UPDATE "todo" SET "finished_at" = ${finishedAt} WHERE "todo_id" = ${todoId}
       """.update().apply()
    if (updated > 0) {
      find(todoId)
    } else {
      None
    }
  }

  def findByIds(ids: Seq[Int])(implicit session: DBSession): Seq[Todo] = {
    val t = Todo.syntax("t")

    sql"""
         SELECT ${t.result.*} FROM ${Todo as t} WHERE ${t.todoId} in (${ids})
       """.map(Todo(t.resultName)).list.apply()
  }

  def createAndReturn(title: String, desc: String)(implicit session: DBSession): Seq[Todo] = {
    findByIds(createAndReturnIds(title, desc))
  }

  def createAndReturnIds(title: String, desc: String)(implicit session: DBSession): Seq[Int] = {
    val newIds = ListBuffer[Int]()
    val inserted =
      sql"""
        INSERT INTO "todo" ("title", "desc") VALUES (${title}, ${desc}), (${title}, ${desc})
        RETURNING "todo_id"
         """.foreach(rs => {

        // seeming
        logger.debug(s"create in rs: ${rs} / ${rs.get[Int](1)}")
        newIds.addOne(rs.get[Int](1))

      })

    newIds.toSeq
  }
}
