package io.jokester.fullstack_playground.user_todolist_api

import io.jokester.fullstack_playground.quill.{QuillContextFactory, QuillDatetimeEncoding}
import io.jokester.http_api.{OpenAPIAuthConvention, OpenAPIConvention}
import io.jokester.http_api.OpenAPIConvention.Failable

class UserTodoServiceQuillImpl(protected override val ctx: QuillContextFactory.UserTodoCtx)
    extends UserTodoService
    with QuillDatetimeEncoding[QuillContextFactory.UserTodoCtx] {
  override def createUser(
      req: UserTodoApi.CreateUserRequest,
  ): Failable[UserTodoApi.CreateUserResponse] = ???

  override def updateProfile(
      userId: OpenAPIAuthConvention.UserId,
      newProfile: UserTodoApi.UserProfile,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = ???

  override def showUser(
      userId: OpenAPIAuthConvention.UserId,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = ???

  override def loginUser(
      req: UserTodoApi.LoginRequest,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = ???

  override def createTodo(
      userId: OpenAPIAuthConvention.UserId,
      req: UserTodoApi.CreateTodoRequest,
  ): OpenAPIConvention.Failable[UserTodoApi.CreateTodoResponse] = ???

  override def listTodo(
      userId: OpenAPIAuthConvention.UserId,
  ): OpenAPIConvention.Failable[UserTodoApi.TodoList] = ???

  override def updateTodo(
      userId: OpenAPIAuthConvention.UserId,
      req: UserTodoApi.TodoItem,
  ): OpenAPIConvention.Failable[UserTodoApi.TodoItem] = ???

  override def deleteTodo(
      userId: OpenAPIAuthConvention.UserId,
      todoId: Int,
  ): OpenAPIConvention.Failable[UserTodoApi.TodoItem] = ???
}
