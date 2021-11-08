package io.jokester.fullstack_playground.user_todolist_api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import cats.Id
import sttp.model.headers.CookieValueWithMeta

import scala.concurrent.{ExecutionContext, Future}

object UserTodoApiAkkaBinding {

  import UserTodoApi._

  private def setCookie(value: String): CookieValueWithMeta =
    CookieValueWithMeta
      .safeApply(value)
      .getOrElse(throw new Error(s"failed to encode cookie"))

  def buildRoute(impl: UserTodoService[Id])(implicit ec: ExecutionContext): Route = {
    import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

    def loadUser(accessToken: AccessToken) =
      impl
        .validateSession(accessToken)
        .map(user => user)

    def loadUserAndValidateUserId(accessToken: AccessToken, expectedUserId: Int) =
      for (
        user <- impl.validateSession(accessToken);
        _    <- Right(expectedUserId).filterOrElse(_ == user.userId, Unauthenticated("user"))
      ) yield user

    val interpreter = AkkaHttpServerInterpreter()

    def future[T](in: impl.ApiResult[T]): Future[impl.ApiResult[T]] = Future.successful(in)

    concat(
      interpreter.toRoute(endpoints.createUser)(req => future(impl.createUser(req))),
      interpreter.toRoute(endpoints.login)(req => future(impl.loginUser(req))),
      interpreter.toRoute(endpoints.refreshToken)(req => future(impl.refreshAccessToken(req))),
      interpreter.toRoute(endpoints.updateUserProfile)(params =>
        future {
          val (accessToken, userId, newProfile) = params
          for (
            user    <- loadUserAndValidateUserId(accessToken, userId);
            updated <- impl.updateProfile(user, newProfile)
          ) yield updated
        },
      ),
      interpreter.toRoute(endpoints.createTodo)(params =>
        future {
          val (accessToken, userId, req) = params
          for (
            user    <- loadUserAndValidateUserId(accessToken, userId);
            created <- impl.createTodo(user, req)
          ) yield created
        },
      ),
      interpreter.toRoute(endpoints.updateTodo)(params =>
        future {
          val (accessToken, userId, todoId, newTodo) = params
          for (
            user    <- loadUserAndValidateUserId(accessToken, userId);
            created <- impl.updateTodo(user, newTodo)
          ) yield created
        },
      ),
      interpreter.toRoute(endpoints.deleteTodo)(params =>
        future {
          val (accessToken, userId, todoId) = params
          for (
            user    <- loadUserAndValidateUserId(accessToken, userId);
            created <- impl.deleteTodo(user, todoId)
          ) yield ()
        },
      ),
      interpreter.toRoute(endpoints.listTodo)(params =>
        future {
          val (accessToken, userId) = params
          for (user <- loadUserAndValidateUserId(accessToken, userId); list <- impl.listTodo(user))
            yield ListTodoResponse(list)
        },
      ),
    )
  }

}
