package io.jokester.fullstack_playground.todolist_app_v2

import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.db.util.ScalikeJdbcJson
import io.jokester.fullstack_playground.db.generated.{TodoappTodos, TodoappUsers}
import io.jokester.fullstack_playground.utils.JwtHelpers
import scalikejdbc.scalikejdbcSQLInterpolationImplicitDef

class UserTodoServiceImpl
    extends UserTodoService
    with UserTodoDb
    with JwtHelpers
    with LazyLogging
    with DatabaseRowConverter
    with ScalikeJdbcJson {
  import UserTodoApi._

  private def successRight[T](value: => T): ApiResult[T] = Right(value)
  private def fail(value: ErrorInfo): ApiResult[Nothing] = Left(value)

  private def issueAccessToken(user: User): AccessToken =
    AccessToken(signAccessToken(user.userId))
  private def issueRefreshToken(user: User): RefreshToken =
    RefreshToken(signRefreshToken(user.userId))

  override def createUser(req: CreateUserRequest): ApiResult[CreateUserResponse] = {
    db.localTx(implicit session => {
      val created = TodoappUsers.create(
        userEmail = req.email,
        userProfile = encodeJson(req.profile),
        passwordHash = req.initialPass,
        updatedAt = now,
        createdAt = now,
      )
      successRight(
        CreateUserResponse(
          created.userId.toInt,
          userProfile = decodeJson[UserProfile](created.userProfile),
        ),
      )
    })
  }

  override def loginUser(
      req: LoginRequest,
  ): ApiResult[LoginResponse] = {
    val user = db.readOnly(implicit session => {
      val found = TodoappUsers.findBy(sqls""" ${TodoappUsers.column.userEmail} = ${req.email} """)
      found
        .filter(_.passwordHash == req.password)
        .toRight(NotFound("authed user"))
    })
    user.map(u =>
      LoginResponse(
        u.userId.toInt,
        decodeJson[UserProfile](u.userProfile),
        issueRefreshToken(u),
        issueAccessToken(u),
      ),
    )
  }

  override def refreshAccessToken(refreshToken: RefreshToken): ApiResult[LoginResponse] = {
    for (
      tokenPayload <-
        validateRefreshToken(refreshToken.value).toRight(Unauthorized("invalid refresh token"));
      user <- db.readOnly(implicit session =>
        TodoappUsers
          .findBy(sqls"""${TodoappUsers.column.userId} = ${tokenPayload.userId}""")
          .toRight(NotFound("user")),
      )
    )
      yield LoginResponse(
        userId = user.userId.toInt,
        userProfile = decodeJson[UserProfile](user.userProfile),
        accessToken = issueAccessToken(user),
        refreshToken = issueRefreshToken(user),
      )
  }

  override def validateSession(jwtToken: AccessToken): ApiResult[AuthedUser] = {
    for (
      tokenPayload <-
        validateAccessToken(jwtToken.value).toRight(Unauthorized("invalid access token"));

      user <- db.readOnly(implicit session =>
        TodoappUsers
          .findBy(sqls"""${TodoappUsers.column.userId} = ${tokenPayload.userId}""")
          .toRight(Unauthenticated("user not found")),
      )
    ) yield AuthedUser(jwtToken = jwtToken.value, userId = user.userId.toInt)
  }

  override def updateProfile(
      authed: AuthedUser,
      newProfile: UserProfile,
  ): ApiResult[UserProfile] = {
    db.localTx(implicit session => {
      for (
        orig <-
          TodoappUsers
            .findBy(sqls"""${TodoappUsers.column.userId} = ${authed.userId}""")
            .toRight(Unauthenticated("user not found"))
      ) yield {

        TodoappUsers.save(orig.copy( /* FIXME set userProfile */ ))
        newProfile
      }
    })
  }

  override def createTodo(
      authed: AuthedUser,
      req: CreateTodoRequest,
  ): ApiResult[CreateTodoResponse] = {
    Right(db.localTx(implicit session => {

      TodoappTodos.create(
        userId = authed.userId,
        title = req.title,
        description = req.description,
        finishedAt = None,
        createdAt = now,
        updatedAt = now,
      )
    }))
  }

  override def updateTodo(
      authed: AuthedUser,
      req: UpdateTodoRequest,
  ): ApiResult[UpdateTodoResponse] = {
    db.localTx(implicit session => {
      for (u <- TodoappTodos.findBy(sqls"""
        ${TodoappTodos.column.todoId} = ${req.todoId} AND ${TodoappUsers.column.userId} = ${authed.userId}
        """).toRight(NotFound("todo item"))) yield {
        TodoappTodos.save(
          u.copy(title = req.title, description = req.description, finishedAt = req.finishedAt),
        )
      }
    })
  }

  override def deleteTodo(
      authed: AuthedUser,
      todoId: Int,
  ): ApiResult[TodoItem] = {
    db.localTx(implicit session => {
      for (
        u <-
          TodoappTodos
            .findBy(
              sqls"${TodoappTodos.column.todoId} = ${todoId} AND ${TodoappUsers.column.userId} = ${authed.userId}",
            )
            .toRight(NotFound("todo item"))
      ) yield {
        TodoappTodos.destroy(u)
        u
      }
    })
  }

  override def listTodo(authed: AuthedUser): ApiResult[Seq[TodoItem]] = {
    Right(db.readOnly(implicit session => {
      TodoappTodos.findAllBy(sqls"${TodoappTodos.column.userId} = ${authed.userId}").map(fromDB)
    }))
  }
}
