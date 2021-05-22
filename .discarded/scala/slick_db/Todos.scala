package discarded.slick_db

import com.typesafe.scalalogging.LazyLogging

import java.time.OffsetDateTime
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Todos extends LazyLogging {
  import slick.jdbc.PostgresProfile.api._

  case class TodoRow2(
      todoId: Int,
      title: String,
      desc: String,
      finishedAt: Option[OffsetDateTime],
      updatedAt: OffsetDateTime,
      createdAt: OffsetDateTime,
  )
  class Todo(tag: Tag)
      extends Table[(Int, String, String, Option[OffsetDateTime], OffsetDateTime, OffsetDateTime)](
        tag,
        "todo",
      ) {
    def todoId     = column[Int]("todo_id", O.AutoInc, O.PrimaryKey)
    def title      = column[String]("title")
    def desc       = column[String]("desc")
    def finishedAt = column[Option[OffsetDateTime]]("finished_at")
    def updatedAt  = column[OffsetDateTime]("updated_at")
    def createdAt  = column[OffsetDateTime]("created_at")

    def * = (todoId, title, desc, finishedAt, updatedAt, createdAt)

  }

  val todos = TableQuery[Todo]

  def findTodo(todoId: Int): Query[
    Todo,
    (Int, String, String, Option[OffsetDateTime], OffsetDateTime, OffsetDateTime),
    Seq,
  ] = {
    for (found <- todos if found.todoId === todoId) yield found
  }

  def createTodo(title: String, desc: String) =
    todos.map(row => (row.title, row.desc, row.finishedAt)) ++= Seq((title, desc, None))

  def runQueries(db: Database): Unit = {
    // list
    val allTodos = get(db.run(todos.result))
    logger.info("all todos: {}", allTodos)

    val foundTodo1 = get(db.run(findTodo(1).result))
    logger.info(s"todo(id=1): ${foundTodo1}")

    val createdTodo = get(db.run(createTodo("newTitle", "newDesc")))
    logger.info(s"new todo: ${createdTodo}")
  }

  private def get[T](value: Future[T]): T = Await.result(value, 1.minutes)

}
