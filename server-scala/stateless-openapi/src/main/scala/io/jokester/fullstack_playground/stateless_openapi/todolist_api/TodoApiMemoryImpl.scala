package io.jokester.fullstack_playground.stateless_openapi.todolist_api

import com.typesafe.scalalogging.LazyLogging

import java.time.Clock
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

class TodoApiMemoryImpl extends TodoApiImpl with LazyLogging {
  import ApiConvention._
  import TodoApi._

  private val _nextId = new AtomicInteger(0)
  private val _todos  = new AtomicReference[Map[Int, Todo]](Map.empty)
  private val clock   = Clock.systemUTC()

  override def list(): Either[ErrorInfo, Seq[Todo]] = Right(_todos.get.values.toSeq)

  override def create(req: TodoCreateRequest): Either[ErrorInfo, Todo] = {
    val newItem =
      Todo(
        id = _nextId.incrementAndGet(),
        finished = false,
        title = req.title,
        desc = req.desc,
        updatedAt = clock.instant(),
      )
    _todos.getAndUpdate(todos => {
      todos.updated(newItem.id, newItem)
    })
    Right(newItem)
  }

  override def show(todoId: Int): Either[ErrorInfo, Todo] =
    _todos.get().get(todoId).toRight(NotFound(s"Todo(id=$todoId) not found"))

  override def update(todoId: Int, patch: Todo): Either[ErrorInfo, Todo] = {
    var ret: Either[ErrorInfo, Todo] = Left(NotFound(s"Todo(id=$todoId) not found"))

    _todos.getAndUpdate(todos => {
      todos
        .get(todoId)
        .map(existed => {
          val merged =
            existed.copy(
              title = patch.title,
              desc = patch.desc,
              finished = patch.finished,
              updatedAt = clock.instant(),
            )
          ret = Right(merged)
          todos.updated(todoId, merged)
        })
        .getOrElse(todos)
    })

    ret
  }

  def remove(todoId: Int): Either[ErrorInfo, Todo] = {
    var found: Either[ErrorInfo, Todo] = Left(NotFound(s"Todo(id=$todoId) not found"))

    _todos.getAndUpdate(todos => {
      todos
        .get(todoId)
        .map(existed => {
          found = Right(existed)
          todos.removed(todoId)
        })
        .getOrElse({
          todos
        })
    })

    found
  }

}
