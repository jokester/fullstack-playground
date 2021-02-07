package io.jokester.fullstack_playground

import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.akka_openapi.AkkaHttpServer
import io.jokester.fullstack_playground.quill_db.{QuillContext, QuillTables}
import io.jokester.fullstack_playground.slick_db.{Connection, Todos}
import io.jokester.fullstack_playground.todo_list.{TodoApi, TodoApiImpl, TodoApiMemoryImpl}
import org.slf4j.LoggerFactory

import java.time.OffsetDateTime
import scala.concurrent.Future
import scala.util.Using

object Main extends App with LazyLogging {

  def buildTodoApiRoute(impl: TodoApiImpl): Route = {

    import akka.http.scaladsl.server.Directives._
    import sttp.tapir.server.akkahttp._

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

  def runQuillSelect(): Unit = {
    Using(QuillContext.connect()) { ctx =>
      val table = new QuillTables(ctx)
      logger.debug("listTodo(): {}", table.listTodo)
      logger.debug("showTodo(1): {}", table.showTodo(1))
      logger.debug("showTodo(-1): {}", table.showTodo(-1))

      val created = table.createTodo("title", "desc")
      logger.debug("createTodo(): {}", created)

      logger.debug("removeTodo(-1): {}", table.removeTodo(-1))
      // FIXME: cant get update working
      logger.debug(
        "updateTodo({}): {}",
        created.todoId,
        table.updateTodoState(created.todoId, None),
      )
      logger.debug(
        "updateTodo({}): {}",
        created.todoId,
        table.updateTodoState(created.todoId, Some(OffsetDateTime.now())),
      )

      logger.debug("removeTodo({}): {}", created.todoId, table.removeTodo(created.todoId))
    }
    Thread.sleep(5000)
  }

  def runSlick(): Unit = {
    Using(Connection.connectPg()) { db =>
      val logger2 = LoggerFactory.getLogger("io.jokester.some")
      logger2.debug(s"slick db: ${db}")
      logger2.debug(s"lazylogging logger: ${logger}")
      logger.debug(s"lazylogging logger: ${logger}")
      Todos.runQueries(db)

      logger.debug(s"lazylogging logger: ${logger}")

    }
    Thread.sleep(1000)

  }

  args.headOption.getOrElse("NONE") match {
    case "runServer" =>
      AkkaHttpServer.listen(buildTodoApiRoute(new TodoApiMemoryImpl()))

    case "dumpQuillConfig" =>
      println(ConfigFactory.load().withOnlyPath("quill-ctx.pg"))
    case "runQuillSelect" =>
      runQuillSelect()
    case "runSlick" =>
      runSlick()
    case "openapi" =>
      println(TodoApi.asOpenAPIYaml)

    case _ => {
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")
    }

  }

}
