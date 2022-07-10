package io.jokester.fullstack_playground.todolist_api

import io.jokester.http_api.OpenAPIConvention

trait TodoApiService {
  import TodoApi._
  import io.jokester.http_api.OpenAPIConvention._

  def create(req: CreateTodoIntent): Failable[Todo]

  def list(): Either[OpenAPIConvention.ApiError, TodoList]

  def show(todoId: Int): Failable[Todo]

  def update(todoId: Int, patch: Todo): Failable[Todo]

  def remove(todoId: Int): Failable[Todo]
}
