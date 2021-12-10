package io.jokester.fullstack_playground.user_todolist_api

import io.circe.generic.auto._
import io.circe.syntax._
import io.jokester.fullstack_playground.quill.generated.userTodo.{
  UserTodos => UserTodoRow,
  Users => UserRow,
}
import io.jokester.fullstack_playground.quill.{
  QuillCirceJsonEncoding,
  QuillContextFactory,
  QuillDatetimeEncoding,
}
import io.jokester.http_api.{OpenAPIAuthConvention, OpenAPIConvention}
import io.jokester.http_api.OpenAPIConvention.{BadRequest, Failable}
import io.jokester.utils.security.BCryptHelpers
import UserTodoApi._
import com.typesafe.scalalogging.LazyLogging

class UserTodoServiceQuillImpl(protected override val ctx: QuillContextFactory.UserTodoCtx)
    extends UserTodoService
    with QuillDatetimeEncoding[QuillContextFactory.UserTodoCtx]
    with QuillCirceJsonEncoding[QuillContextFactory.UserTodoCtx]
    with BCryptHelpers
    with OpenAPIConvention.Lifters
    with LazyLogging {
  import ctx._
  import UserTodoSchema._
  override def createUser(
      req: UserTodoApi.CreateUserRequest,
  ): Failable[UserTodoApi.UserAccount] = {
    val created: UserRow = run(quote {
      UsersDao.query
        .insert(
          _.email        -> lift(req.email.toLowerCase),
          _.passwordHash -> lift(createPasswordHash(req.initialPass)),
          _.profile      -> lift(req.profile.asJson),
        )
        .returning(row => row)
    })

    liftSuccess(transform(created))
  }

  override def updateProfile(
      userId: OpenAPIAuthConvention.UserId,
      newProfile: UserTodoApi.UserProfile,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = ???

  override def showUser(
      userId: OpenAPIAuthConvention.UserId,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = ???

  override def loginUser(
      req: UserTodoApi.LoginRequest,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = {
    val loaded = run(quote {
      UsersDao.query.filter(row => row.email == lift(req.email))
    })
    loaded.headOption
      .filter(f => validatePassword(req.password, f.passwordHash))
      .map(transform)
      .map(liftSuccess)
      .getOrElse(liftError(BadRequest("invalid cred")))
  }

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

  private def transform(row: UserRow): UserAccount = {
    val profileJson = row.profile.as[UserProfile]
    UserAccount(
      userId = row.userId.toInt,
      profile = profileJson.getOrElse(UserProfile(None, None)),
    )

  }
}
