package io.jokester.fullstack_playground.todolist_app_v2

import cats.Id

trait UserTodoService {
  import UserTodoApi._

  type ApiResult[T] = Id[Either[ErrorInfo, T]]

  // signup
  def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse]

  // auth
  def loginUser(req: LoginRequest): ApiResult[LoginResponse]
  def validateSession(accessToken: AccessToken): ApiResult[AuthedUser]
  def refreshAccessToken(refreshToken: RefreshToken): ApiResult[LoginResponse]

  // user
  def updateProfile(authed: AuthedUser, newProfile: UserProfile): ApiResult[UserProfile]

  // todo
  def createTodo(authed: AuthedUser, req: CreateTodoRequest): ApiResult[CreateTodoResponse]
  def updateTodo(authed: AuthedUser, req: UpdateTodoRequest): ApiResult[UpdateTodoResponse]
  def deleteTodo(authed: AuthedUser, todoId: Int): ApiResult[TodoItem]
  def listTodo(authed: AuthedUser): ApiResult[Seq[TodoItem]]
}
