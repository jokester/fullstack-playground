package io.jokester.fullstack_playground.user_todolist_api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi._
import io.jokester.http_api.OpenAPIAuthConvention._
import io.jokester.http_api.OpenAPIConvention._
import sttp.model.headers.CookieValueWithMeta
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.{ExecutionContext, Future}
import scala.language.{existentials, implicitConversions}

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
) extends JwtHelper {
  private val interpreter = AkkaHttpServerInterpreter()

  def buildRoute: Route =
    interpreter.toRoute(
      List(
        endpoints.createTodo
          .serverSecurityLogicPure(decodeAccessToken)
          .serverLogic(accessToken =>
            req =>
              Future {
                val (providedUid, payload) = req
                for (
                  uid     <- validateAccessToken(accessToken, providedUid);
                  created <- impl.createTodo(uid, payload)
                ) yield created
              },
          ),
      ),
    )

  def buildPublicRoute: Route =
    interpreter.toRoute(
      List(
        endpoints.createUser.serverLogic(req => Future(impl.createUser(req))),
        endpoints.login.serverLogic(req =>
          Future {
            for (authedAccount <- impl.loginUser(req))
              yield AuthSuccess(
                userId = authedAccount.userId,
                profile = authedAccount.profile,
                accessToken = signAccessToken(authedAccount.userId),
                refreshToken = signRefreshToken(authedAccount.userId),
              )
          },
        ),
      ),
    )

  def buildAuthedUserRoute: Route =
    interpreter.toRoute(
      List(
        endpoints.updateUserProfile
          .serverSecurityLogicPure(decodeAccessToken)
          .serverLogic(accessToken =>
            req =>
              Future {
                val (providedUid, payload) = req
                for (
                  uid     <- validateAccessToken(accessToken, providedUid);
                  updated <- impl.updateProfile(uid, payload)
                ) yield updated
              },
          ),
        endpoints.refreshToken
          .serverSecurityLogicPure(decodeBearerToken)
          .serverLogic(token =>
            req => {
              Future {
                for (
                  authedAccount <- impl.showUser(UserId(token.userId)))
                  yield AuthSuccess(
                    userId = authedAccount.userId,
                    profile = authedAccount.profile,
                    accessToken = signAccessToken(authedAccount.userId),
                    refreshToken = signRefreshToken(authedAccount.userId),
                  )
              }

            },
          ),
      ),
    )

  def buildAuthedTodoRoute: Route =
    interpreter.toRoute(
      List(
        endpoints.listTodo
          .serverSecurityLogicPure(decodeAccessToken)
          .serverLogic(accessToken =>
            req =>
              Future {
                val (providedUid) = req
                for (
                  uid   <- validateAccessToken(accessToken, providedUid);
                  todos <- impl.listTodo(uid)
                ) yield todos
              },
          ),
        endpoints.createTodo
          .serverSecurityLogicPure(decodeAccessToken)
          .serverLogic(accessToken =>
            req =>
              Future {
                val (providedUid, payload) = req
                for (
                  uid     <- validateAccessToken(accessToken, providedUid);
                  created <- impl.createTodo(uid, payload)
                ) yield created
              },
          ),
        endpoints.updateTodo
          .serverSecurityLogicPure(decodeAccessToken)
          .serverLogic(accessToken =>
            req =>
              Future {
                val (providedUid, providedTodoId, payload) = req
                for (
                  uid     <- validateAccessToken(accessToken, providedUid);
                  updated <- impl.updateTodo(uid, payload)
                ) yield updated
              },
          ),
        endpoints.deleteTodo
          .serverSecurityLogicPure(decodeAccessToken)
          .serverLogic(accessToken =>
            req =>
              Future {
                val (providedUid, providedTodoId) = req
                for (
                  uid     <- validateAccessToken(accessToken, providedUid);
                  deleted <- impl.deleteTodo(uid, providedTodoId)
                ) yield ()
              },
          ),
      ),
    )
}
