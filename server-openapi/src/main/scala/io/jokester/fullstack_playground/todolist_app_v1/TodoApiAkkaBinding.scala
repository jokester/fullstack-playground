package io.jokester.fullstack_playground.todolist_app_v1

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import sttp.tapir.server.akkahttp.RichAkkaHttpEndpoint

import scala.concurrent.Future

object TodoApiAkkaBinding {

  def buildTodoApiRoute(impl: TodoApiImpl): Route = {

    val serverImplRoute = {

      Seq[Route](
        TodoApi.endpoints.listTodo.toRoute(_ => Future.successful(impl.list())),
        TodoApi.endpoints.createTodo.toRoute(req => Future.successful(impl.create(req))),
        TodoApi.endpoints.deleteTodo.toRoute(req =>
          Future.successful(impl.remove(req).map(_ => ())),
        ),
        TodoApi.endpoints.updateTodo.toRoute(req => Future.successful(impl.update(req._1, req._2))),
      ).reduce(_ ~ _)

    }

    serverImplRoute
  }
}
