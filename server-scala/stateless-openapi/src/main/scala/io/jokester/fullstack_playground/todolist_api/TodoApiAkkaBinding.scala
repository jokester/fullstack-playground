package io.jokester.fullstack_playground.todolist_api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{concat, pathPrefix}

/**
  *
  */
object TodoApiAkkaBinding extends ApiConvention.LifterHelpers {

  def buildTodoApiRoute(impl: TodoApiImpl): Route = {
    import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
    val endpoints = TodoApi.endpoints

    val interpreter = AkkaHttpServerInterpreter()
    val serverImplRoute = concat(
      interpreter.toRoute(endpoints.listTodo) { req =>
        liftResult(impl.list().map(TodoApi.TodoList))
      },
      interpreter.toRoute(endpoints.showTodo) { todoId =>
        liftResult(impl.show(todoId))
      },
      interpreter.toRoute(endpoints.createTodo)(req => liftResult(impl.create(req))),
      interpreter.toRoute(endpoints.deleteTodo) { todoId =>
        liftResult(impl.remove(todoId).map(_ => ()))
      },
      interpreter.toRoute(endpoints.updateTodo)(req => liftResult(impl.update(req._1, req._2))),
    )

    serverImplRoute
  }
}
