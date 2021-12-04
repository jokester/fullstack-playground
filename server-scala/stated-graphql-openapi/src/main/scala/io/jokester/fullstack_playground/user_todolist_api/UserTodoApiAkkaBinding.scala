package io.jokester.fullstack_playground.user_todolist_api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import cats.syntax.either._
import sttp.model.headers.CookieValueWithMeta

import scala.concurrent.{ExecutionContext, Future}
import io.jokester.http_api.OpenAPIConvention._
import io.jokester.http_api.OpenAPIAuthConvention._
import UserTodoApi._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

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

  private def validateUser(
      accessToken: BearerToken,
      expectedUserId: Int,
  ): Failable[UserId] =
    liftResult {
      validateAccessToken(accessToken.value).flatMap {
        case AccessTokenPayload(userId, _) if userId == expectedUserId =>
          UserId(userId).asRight
        case _ => Unauthorized("userId mismatch").asLeft
      }
    }

  def buildRoute: Route = {

    concat(
      interpreter.toRoute(endpoints.createUser)(req => impl.createUser(req)),
      interpreter.toRoute(endpoints.login)(req => (impl.loginUser(req))),
//      interpreter.toRoute(endpoints.refreshToken)(req => (impl.refreshAccessToken(req))),
      interpreter.toRoute(endpoints.updateUserProfile)(params => {
        val (accessToken, userId, newProfile) = params

        for (
          user <- validateUser(accessToken, userId); updated <- impl.updateProfile(user, newProfile)
        ) yield updated
      }),
      interpreter.toRoute(endpoints.createTodo)(params => {
        val (accessToken, userId, req) = params
        val created =
          for (
            user    <- validateUser(accessToken, userId);
            created <- impl.createTodo(user, req)
          ) yield created
        created.asFuture
      }),
      interpreter.toRoute(endpoints.updateTodo)(params => {
        val (accessToken, userId, todoId, newTodo) = params
        for (
          user    <- validateUser(accessToken, userId);
          created <- impl.updateTodo(user, newTodo)
        ) yield created
      }),
      interpreter.toRoute(endpoints.deleteTodo)(params => {
        val (accessToken, userId, todoId) = params
        for (
          user    <- validateUser(accessToken, userId);
          created <- impl.deleteTodo(user, todoId)
        ) yield ()
      }),
      interpreter.toRoute(endpoints.listTodo)(params => {
        val (accessToken, userId) = params
        for (user <- validateUser(accessToken, userId); list <- impl.listTodo(user))
          yield list
      }),
    )
  }

}
