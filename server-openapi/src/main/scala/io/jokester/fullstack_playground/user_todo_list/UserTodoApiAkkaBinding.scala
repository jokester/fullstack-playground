package io.jokester.fullstack_playground.user_todo_list

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.jokester.fullstack_playground.akka_openapi.AkkaOpenAPIServer
import sttp.model.headers.CookieValueWithMeta
import sttp.tapir.server.akkahttp._

import scala.concurrent.Future

object UserTodoApiAkkaBinding {

  import UserTodoApi._

  private implicit def mapRight[R](e: Either[ErrorInfo, R]): Future[Either[ErrorInfo, R]] =
    Future.successful(e)

  private def setCookie(value: String): CookieValueWithMeta =
    CookieValueWithMeta
      .safeApply(value)
      .getOrElse(throw new Error(s"failed to encode cookie"))

  def buildRoute(impl: UserTodoService): Route =
    Seq[Route](
      endpoints.createUser.toRoute(req => impl.createUser(req)),
      endpoints.login.toRoute(req => impl.loginUser(req)),
      endpoints.refreshToken.toRoute(req => impl.refreshAccessToken(req)),
      endpoints.updateUserProfile.toRoute(params => {
        val (accessToken, userId, newProfile) = params
        for (
          user    <- impl.validateSession(accessToken);
          updated <- impl.updateProfile(user, newProfile)
        ) yield updated
      }),
      AkkaOpenAPIServer.openapiRoute(UserTodoApi.asOpenAPI),
    ).reduce(_ ~ _)

}
