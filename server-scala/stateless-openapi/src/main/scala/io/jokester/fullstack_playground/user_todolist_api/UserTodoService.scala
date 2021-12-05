package io.jokester.fullstack_playground.user_todolist_api

import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi.CreateTodoResponse
import io.jokester.http_api.OpenAPIAuthConvention.UserId
import io.jokester.http_api.OpenAPIConvention.Failable
import UserTodoApi._

/**
  * Auth-agnostic part
  */
trait UserTodoService {

  // user / auth
  def createUser(req: CreateUserRequest): Failable[CreateUserResponse]
  def updateProfile(userId: UserId, newProfile: UserProfile): Failable[UserAccount]
  def showUser(userId: UserId): Failable[UserAccount]
  def loginUser(req: LoginRequest): Failable[UserAccount]

  // todos: CRUD
  def createTodo(userId: UserId, req: CreateTodoRequest): Failable[CreateTodoResponse]
  def listTodo(userId: UserId): Failable[TodoList]
  def updateTodo(userId: UserId, req: TodoItem): Failable[TodoItem]
  def deleteTodo(userId: UserId, todoId: Int): Failable[TodoItem]
}
