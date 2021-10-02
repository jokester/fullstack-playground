package io.jokester.fullstack_playground.todolist_app_v2

import com.typesafe.scalalogging.LazyLogging

import java.time.OffsetDateTime
import scala.language.implicitConversions

trait DatabaseRowConverter extends ScalikeJdbcJson with LazyLogging {
  import UserTodoApi._
  protected implicit def fromDB(row: TodoappUsers): User = {
    logger.debug("got userProfile object: {}", row.userProfile)
    User(userId = row.userId.toInt, profile = decodeJson[UserProfile](row.userProfile))
  }

  protected implicit def fromDB(row: TodoappTodos): TodoItem =
    TodoItem(
      title = row.title,
      description = row.description,
      todoId = row.todoId.toInt,
      userId = row.userId,
      finishedAt = row.finishedAt,
    )
  protected def now: OffsetDateTime = OffsetDateTime.now()
}
