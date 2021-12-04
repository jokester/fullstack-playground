package io.jokester.fullstack_playground.todolist_api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{concat, pathPrefix}
import io.jokester.http_api.OpenAPIConvention

import scala.concurrent.ExecutionContext

/**
  *
  */
object TodoApiAkkaBinding extends OpenAPIConvention.Lifters {

  def buildTodoApiRoute(impl: TodoApiService)(implicit ec: ExecutionContext): Route = {
    import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
    val endpoints = TodoApi.endpoints

    val interpreter = AkkaHttpServerInterpreter()
    val serverImplRoute = concat(
      interpreter.toRoute(endpoints.listTodo) { req =>
        impl.list()
      },
      interpreter.toRoute(endpoints.showTodo) { todoId =>
        impl.show(todoId)
      },
      interpreter.toRoute(endpoints.createTodo) { req =>
        impl.create(req)
      },
      interpreter.toRoute(endpoints.deleteTodo) { todoId =>
        mapInner(
          impl.remove(todoId),
          (whatever: Any) => (),
        )
      },
      interpreter.toRoute(endpoints.updateTodo) { req =>
        impl.update(req._1, req._2)
      },
    )

    serverImplRoute
  }
}
