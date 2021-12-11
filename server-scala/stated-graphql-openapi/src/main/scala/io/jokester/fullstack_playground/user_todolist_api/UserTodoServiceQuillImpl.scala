package io.jokester.fullstack_playground.user_todolist_api
import scala.util.chaining._
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
  QuillWorkarounds,
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
    with QuillWorkarounds
    with LazyLogging {
  import ctx._
  import UserTodoSchema._

  override def createUser(
      req: UserTodoApi.CreateUserRequest,
  ): Failable[UserTodoApi.UserAccount] = {

    try {

      val created: UserRow = run(quote {
        UsersDao.query
          .insert(
            _.email        -> lift(req.email.toLowerCase),
            _.passwordHash -> lift(createPasswordHash(req.initialPass)),
            _.profile      -> lift(req.profile.asJson),
          )
          .returning(row => row)
      })
      liftSuccess(fromRow(created))
    } catch {
      case UniqueKeyViolation(_) =>
        liftError(BadRequest(s"Email address ${req.email} already occupied"))
    }

  }

  override def updateProfile(
      userId: OpenAPIAuthConvention.UserId,
      newProfile: UserTodoApi.UserProfile,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = {
    val updated = run({

      UsersDao.query
        .filter(u => u.userId == lift(userId.value))
        .update(_.profile -> lift(newProfile.asJson))
    })
    if (!(updated == 1)) {
      liftError(BadRequest("user not found"))
    } else {
      showUser(userId)
    }
  }

  override def showUser(
      userId: OpenAPIAuthConvention.UserId,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = {
    val found: Option[UserAccount] = run({
      UsersDao.query.filter(u => u.userId == lift(userId.value))
    }).headOption.map(fromRow)

    found.map(liftSuccess).getOrElse(liftError(BadRequest("user not found")))
  }

  override def loginUser(
      req: UserTodoApi.LoginRequest,
  ): OpenAPIConvention.Failable[UserTodoApi.UserAccount] = {
    val loaded = run(quote {
      UsersDao.query.filter(row => row.email == lift(req.email))
    })
    loaded.headOption
      .filter(f => validatePassword(req.password, f.passwordHash))
      .map(fromRow)
      .map(liftSuccess)
      .getOrElse(liftError(BadRequest("invalid cred")))
  }

  override def createTodo(
      userId: OpenAPIAuthConvention.UserId,
      req: UserTodoApi.CreateTodoRequest,
  ): OpenAPIConvention.Failable[UserTodoApi.CreateTodoResponse] = {
    val created: UserTodoRow = run(quote {
      UserTodosDao.query
        .insert(
          _.userId      -> lift(userId.value),
          _.title       -> lift(req.title),
          _.description -> lift(req.description),
          _.finishedAt  -> None,
        )
        .returning(row => row)
    })

    created
      .pipe(fromRow)
      .pipe(liftSuccess)
  }

  override def listTodo(
      userId: OpenAPIAuthConvention.UserId,
  ): OpenAPIConvention.Failable[UserTodoApi.TodoList] = {
    val found = run(quote {
      UserTodosDao.query.filter(_.userId == lift(userId.value))
    })

    TodoList(found.map(fromRow))
      .pipe(liftSuccess)
  }

  override def updateTodo(
      userId: OpenAPIAuthConvention.UserId,
      req: UserTodoApi.TodoItem,
  ): OpenAPIConvention.Failable[UserTodoApi.TodoItem] = {
    val found = run(findTodo(userId, req.todoId)).headOption.map(fromRow)

    val merged = found.map(existed => {
      existed.copy(title = req.title, description = req.description, finishedAt = req.finishedAt)
    })

    val updated = merged
      .map(m =>
        run(quote({
          findTodo(userId, req.todoId)
            .update(
              _.title       -> lift(m.title),
              _.description -> lift(m.description),
              _.finishedAt  -> lift(m.finishedAt),
            )
            .returning(row => row)

        })),
      )
      .map(v => v.pipe(fromRow))

    updated match {
      case None    => liftError(BadRequest("not found"))
      case Some(v) => liftSuccess(v)
    }

  }

  override def deleteTodo(
      userId: OpenAPIAuthConvention.UserId,
      todoId: Int,
  ): OpenAPIConvention.Failable[UserTodoApi.TodoItem] = {
    try {
      val removed = run {
        findTodo(userId, todoId).delete.returning(row => row)
      }
      removed.pipe(fromRow).pipe(liftSuccess)
    } catch {
      case RowNotFoundViolation(_) => liftError(BadRequest("not found"))
    }
  }

  private def findTodo(userId: OpenAPIAuthConvention.UserId, todoId: Int) =
    quote {
      UserTodosDao.query.filter(row =>
        row.userId == lift(userId.value) && row.todoId == lift(todoId),
      )
    }

  private def fromRow(row: UserRow): UserAccount = {
    UserAccount(
      userId = row.userId,
      profile = row.profile.as[UserProfile].getOrElse(UserProfile(None, None)),
    )
  }

  private def fromRow(row: UserTodoRow): UserTodoApi.TodoItem = {
    UserTodoApi.TodoItem(
      todoId = row.todoId,
      userId = row.userId,
      finishedAt = row.finishedAt,
      title = row.title,
      description = row.description,
    )

  }
}
