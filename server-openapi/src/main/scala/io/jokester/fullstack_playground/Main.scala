package io.jokester.fullstack_playground

import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.akka_openapi.AkkaHttpServer
import io.jokester.fullstack_playground.todo_list.{TodoApi, TodoApiImpl, TodoApiMemoryImpl}

import scala.concurrent.Future

object Main extends App with LazyLogging {

  def buildTodoApiRoute(impl: TodoApiImpl): Route = {

    import akka.http.scaladsl.server.Directives._
    import sttp.tapir.server.akkahttp._

    val serverImplRoute = {

      Seq[Route](
        TodoApi.endpoints.listTodo.toRoute(_ => Future.successful(impl.list())),
        TodoApi.endpoints.createTodo.toRoute(req => Future.successful(impl.create(req))),
        TodoApi.endpoints.deleteTodo.toRoute(req =>
          Future.successful(impl.remove(req).map(_ => ()))
        ),
        TodoApi.endpoints.updateTodo.toRoute(req => Future.successful(impl.update(req._1, req._2)))
      ).reduce(_ ~ _)

    }

    serverImplRoute
  }

  args.headOption.getOrElse("NONE") match {
    case "runServer" =>
      AkkaHttpServer.listen(buildTodoApiRoute(new TodoApiMemoryImpl()))

    case "openapi" =>
      println(TodoApi.asOpenAPIYaml)

    case _ => {
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")
    }

  }

}
