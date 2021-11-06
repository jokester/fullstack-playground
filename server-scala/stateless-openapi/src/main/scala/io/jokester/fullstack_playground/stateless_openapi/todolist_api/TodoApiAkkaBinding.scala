package io.jokester.fullstack_playground.stateless_openapi.todolist_api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{concat, pathPrefix}

import scala.concurrent.Future

/**
  *
  */
object TodoApiAkkaBinding {

  def buildTodoApiRoute(impl: TodoApiImpl): Route = {
    import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
    val endpoints = TodoApi.endpoints

    val interpreter = AkkaHttpServerInterpreter()
    val serverImplRoute = pathPrefix("todos") {
      concat(
        interpreter.toRoute(endpoints.listTodo)(req =>
          Future.successful(impl.list().map(TodoApi.TodoList)),
        ),
        interpreter.toRoute(endpoints.showTodo)(todoId => Future.successful(impl.show(todoId))),
        interpreter.toRoute(endpoints.createTodo)(req => Future.successful(impl.create(req))),
        interpreter.toRoute(endpoints.deleteTodo)(todoId =>
          Future.successful(impl.remove(todoId).map(_ => ())),
        ),
        interpreter.toRoute(endpoints.updateTodo)(req =>
          Future.successful(impl.update(req._1, req._2)),
        ),
      )
    }

    serverImplRoute
  }
}
