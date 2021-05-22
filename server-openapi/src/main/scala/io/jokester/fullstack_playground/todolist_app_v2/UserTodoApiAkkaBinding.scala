package io.jokester.fullstack_playground.todolist_app_v2

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import io.jokester.fullstack_playground.akka_openapi.AkkaOpenAPIServer
import sttp.model.headers.CookieValueWithMeta
import sttp.tapir.server.akkahttp.RichAkkaHttpEndpoint

import scala.concurrent.Future

object UserTodoApiAkkaBinding {

  import UserTodoApi._

  private implicit def mapRight[R](e: Either[ErrorInfo, R]): Future[Either[ErrorInfo, R]] =
    Future.successful(e)

  private def setCookie(value: String): CookieValueWithMeta =
    CookieValueWithMeta
      .safeApply(value)
      .getOrElse(throw new Error(s"failed to encode cookie"))

  def buildRoute(impl: UserTodoService): Route = {

    def loadUser(accessToken: AccessToken) =
      impl
        .validateSession(accessToken)
        .map(user => user)

    def loadUserAndValidateUserId(accessToken: AccessToken, expectedUserId: Int) =
      for (
        user <- impl.validateSession(accessToken);
        _    <- Right(expectedUserId).filterOrElse(_ == user.userId, Unauthenticated("user"))
      ) yield user

    Seq[Route](
      endpoints.createUser.toRoute(req => impl.createUser(req)),
      endpoints.login.toRoute(req => impl.loginUser(req)),
      endpoints.refreshToken.toRoute(req => impl.refreshAccessToken(req)),
      endpoints.updateUserProfile.toRoute(params => {
        val (accessToken, userId, newProfile) = params
        for (
          user    <- loadUserAndValidateUserId(accessToken, userId);
          updated <- impl.updateProfile(user, newProfile)
        ) yield updated
      }),
      endpoints.createTodo.toRoute(params => {
        val (accessToken, userId, req) = params
        for (
          user    <- loadUserAndValidateUserId(accessToken, userId);
          created <- impl.createTodo(user, req)
        ) yield created
      }),
      endpoints.updateTodo.toRoute(params => {
        val (accessToken, userId, todoId, newTodo) = params
        for (
          user    <- loadUserAndValidateUserId(accessToken, userId);
          created <- impl.updateTodo(user, newTodo)
        ) yield created
      }),
      endpoints.deleteTodo.toRoute(params => {
        val (accessToken, userId, todoId) = params
        for (
          user    <- loadUserAndValidateUserId(accessToken, userId);
          created <- impl.deleteTodo(user, todoId)
        ) yield ()
      }),
      endpoints.listTodo.toRoute(params => {
        val (accessToken, userId) = params
        for (user <- loadUserAndValidateUserId(accessToken, userId); list <- impl.listTodo(user))
          yield ListTodoResponse(list)
      }),
      AkkaOpenAPIServer.openapiRoute(UserTodoApi.asOpenAPI),
    ).reduce(_ ~ _)
  }

}
