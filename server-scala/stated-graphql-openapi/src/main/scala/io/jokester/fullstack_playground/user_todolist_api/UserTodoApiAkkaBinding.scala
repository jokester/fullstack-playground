package io.jokester.fullstack_playground.user_todolist_api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.syntax.either._
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi._
import io.jokester.http_api.OpenAPIAuthConvention._
import io.jokester.http_api.OpenAPIConvention._
import sttp.model.headers.CookieValueWithMeta
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.ExecutionContext
import scala.language.implicitConversions

object UserTodoApiAkkaBinding {

  private def setCookie(value: String): CookieValueWithMeta =
    CookieValueWithMeta
      .safeApply(value)
      .getOrElse(throw new Error(s"failed to encode cookie"))

  def buildRoute(impl: UserTodoService)(implicit ec: ExecutionContext): Route = {
    new RouteBuilder(impl, "jwtSecret").buildRoute
  }

}
private class RouteBuilder(impl: UserTodoService, override val jwtSecret: String)(implicit
    ec: ExecutionContext,
) extends JwtHelper
    with Lifters {
  private val interpreter = AkkaHttpServerInterpreter()

  def buildRoute: Route = {

    concat(
      interpreter.toRoute(endpoints.createUser)(req => impl.createUser(req)),
      interpreter.toRoute(endpoints.login)(req =>
        for (authedAccount <- impl.loginUser(req))
          yield AuthSuccess(
            userId = authedAccount.userId,
            profile = authedAccount.profile,
            accessToken = signAccessToken(authedAccount.userId),
            refreshToken = signRefreshToken(authedAccount.userId),
          ),
      ),
      interpreter.toRoute(endpoints.refreshToken)(req => {
        for (
          userId        <- validateRefreshToken(req);
          authedAccount <- impl.showUser(userId)
        )
          yield AuthSuccess(
            userId = authedAccount.userId,
            profile = authedAccount.profile,
            accessToken = signAccessToken(authedAccount.userId),
            refreshToken = signRefreshToken(authedAccount.userId),
          )
      }),
      interpreter.toRoute(endpoints.updateUserProfile)(params => {
        val (accessToken, userId, newProfile) = params

        for (
          user    <- validateAccessToken(accessToken, userId);
          updated <- impl.updateProfile(user, newProfile)
        ) yield updated
      }),
      interpreter.toRoute(endpoints.createTodo)(params => {
        val (accessToken, userId, req) = params
        for (
          user    <- validateAccessToken(accessToken, userId);
          created <- impl.createTodo(user, req)
        ) yield created
      }),
      interpreter.toRoute(endpoints.updateTodo)(params => {
        val (accessToken, userId, todoId, newTodo) = params
        for (
          user    <- validateAccessToken(accessToken, userId);
          created <- impl.updateTodo(user, newTodo)
        ) yield created
      }),
      interpreter.toRoute(endpoints.deleteTodo)(params => {
        val (accessToken, userId, todoId) = params
        for (
          user    <- validateAccessToken(accessToken, userId);
          created <- impl.deleteTodo(user, todoId)
        ) yield ()
      }),
      interpreter.toRoute(endpoints.listTodo)(params => {
        val (accessToken, userId) = params
        for (user <- validateAccessToken(accessToken, userId); list <- impl.listTodo(user))
          yield list
      }),
    )
  }

}
