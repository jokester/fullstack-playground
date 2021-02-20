package io.jokester.fullstack_playground.user_todo_list

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import io.jokester.fullstack_playground.akka_openapi.AkkaOpenAPIServer
import sttp.tapir.server.akkahttp._

object UserTodoApiAkkaBinding {

  import UserTodoApi.endpoints

  def buildRoute(impl: UserTodoService): Route = {
    Seq[Route](
      endpoints.login.toRoute(req => impl.loginUser(req)),
      endpoints.createUser.toRoute(req => impl.createUser(req)),
      AkkaOpenAPIServer.openapiRoute(UserTodoApi.asOpenAPI),
    ).reduce(_ ~ _)
  }

}
