package io.jokester.fullstack_playground

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import discarded.quill_db.{QuillContext, QuillTables}
import discarded.slick_db.{Connection, Todos}
import io.jokester.fullstack_playground.akka_openapi.AkkaHttpServer
import io.jokester.fullstack_playground.scalikejdbc_db.{
  ScalikeJDBCConnection,
  ScalikeTodoRepository,
}
import io.jokester.fullstack_playground.user_todo_list.{
  UserTodoApi,
  UserTodoApiAkkaBinding,
  UserTodoServiceImpl,
}
import org.slf4j.LoggerFactory
import sttp.tapir.openapi.circe.yaml._

import java.time.OffsetDateTime
import scala.util.Using

object Main extends App with LazyLogging {

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

  def runScalikeJDBC() = {
    ScalikeJDBCConnection.showPools()
    ScalikeJDBCConnection.defaultDB()
    ScalikeJDBCConnection.showPools()
    ScalikeJDBCConnection.namedDB()
    ScalikeJDBCConnection.showPools()

    ScalikeJDBCConnection
      .namedDB()
      .localTx(implicit session => {

        val a = Seq()

        logger.debug(s"findAll(): ${a}")

//        val created =
//          models.Todo.create("ti##tle", "des$$$c", None, OffsetDateTime.MIN, OffsetDateTime.MAX)
//        logger.debug(s"create(): ${created}")

      })

    ScalikeJDBCConnection
      .namedDB()
      .localTx(implicit session => {
        logger.debug(s"findDesc(1) = ${ScalikeTodoRepository.findDesc(1)}")

      })
  }

  args.headOption.getOrElse("NONE") match {
    case "runServer" =>
      AkkaHttpServer.listen(UserTodoApiAkkaBinding.buildRoute(new UserTodoServiceImpl()))

    case "dumpQuillConfig" =>
      println(ConfigFactory.load().withOnlyPath("quill-ctx.pg"))
    case "runQuillSelect" =>
      runQuillSelect()
    case "runSlick" =>
      runSlick()
    case "scalikeJDBC" =>
      runScalikeJDBC()

    case "openapi" =>
      println(UserTodoApi.asOpenAPI.toYaml)

    case _ => {
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")
    }

  }

}
