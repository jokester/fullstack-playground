package io.jokester.fullstack_playground

import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Directives.concat
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.quill.QuillCtxFactory
import io.jokester.fullstack_playground.todolist_api.{
  TodoApiQuillImpl,
  TodoApiAkkaBinding,
  TodoApiMemoryImpl,
}
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

import java.nio.file.{Files, Path}
import scala.concurrent.ExecutionContext

object StatedMain extends App with LazyLogging {

  args match {
    case Array("writeOpenApiSpec", yamlPath) =>
      logger.info(s"writing OpenAPI spec to ${yamlPath}")
      Files.writeString(Path.of(yamlPath), UserTodoApi.asOpenAPIYaml)

    case Array("server") =>
      val ctx        = QuillCtxFactory.createContext("database.default")
      val quillImpl  = new TodoApiQuillImpl(ctx)
      val memoryImpl = new TodoApiMemoryImpl()
      AkkaHttpServer
        .listenWithNewSystem(actorSystem => {
          implicit val ec: ExecutionContext = actorSystem.executionContext
          val rootRoute = concat(
            pathPrefix("stated-openapi") {
              TodoApiAkkaBinding.buildTodoApiRoute(quillImpl)
            },
            pathPrefix("stateless-openapi") {
              TodoApiAkkaBinding.buildTodoApiRoute(memoryImpl)
            },
          )
          (AkkaHttpServer.withCors & AkkaHttpServer.withRequestLogging) {
            rootRoute
          }
        })
        .andThen(_ => {
          logger.info("QuillCtx closing")
          ctx.close()
          logger.info("QuillCtx closed")
        })(scala.concurrent.ExecutionContext.global)

    case _ =>
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")

  }

}
