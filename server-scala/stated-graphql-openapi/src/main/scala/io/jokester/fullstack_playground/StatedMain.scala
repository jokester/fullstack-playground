package io.jokester.fullstack_playground

import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Directives.concat
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.quill.QuillContextFactory
import io.jokester.fullstack_playground.todolist_api.{
  TodoApiQuillImpl,
  TodoApiAkkaBinding,
  TodoApiMemoryImpl,
}
import io.jokester.fullstack_playground.user_todolist_api.{UserTodoApi, UserTodoServiceQuillImpl, UserTodoApiAkkaBinding}
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

import java.nio.file.{Files, Path}
import scala.concurrent.ExecutionContext

object StatedMain extends App with LazyLogging {

  args match {
    case Array("writeOpenApiSpec", yamlPath) =>
      logger.info(s"writing OpenAPI spec to ${yamlPath}")
      Files.writeString(Path.of(yamlPath), UserTodoApi.asOpenAPIYaml)

    case Array("server") =>
      val ctx        = QuillContextFactory.createPublicContext("database.default")
      val quillImpl  = new TodoApiQuillImpl(ctx)
      val memoryImpl = new TodoApiMemoryImpl()

      val ctx2        = QuillContextFactory.createUserTodoContext("database.default")
      val quillImpl2 = new UserTodoServiceQuillImpl(ctx2)
      AkkaHttpServer
        .listenWithNewSystem(actorSystem => {
          implicit val ec: ExecutionContext = actorSystem.executionContext
          val todoApiRoute = concat(
            pathPrefix("stated-openapi/todo") {
              TodoApiAkkaBinding.buildTodoApiRoute(quillImpl)
            },
            pathPrefix("stateless-openapi/todo") {
              TodoApiAkkaBinding.buildTodoApiRoute(memoryImpl)
            },
          )

          val userTodoApiRoute = concat(
            pathPrefix("stated-openapi/user_todo") {
              UserTodoApiAkkaBinding.buildRoute(quillImpl2)
            }
          )

          (AkkaHttpServer.withCors & AkkaHttpServer.withRequestLogging) {
            concat( todoApiRoute, userTodoApiRoute )
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
