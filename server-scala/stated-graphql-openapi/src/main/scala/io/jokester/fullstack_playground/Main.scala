package io.jokester.fullstack_playground

import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.quill.QuillCtxFactory
import io.jokester.fullstack_playground.todolist_api.QuillTodoApiImpl
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi

import java.nio.file.{Files, Path}

object Main extends App with LazyLogging {

  args match {
    case Array("writeOpenApiSpec", yamlPath) =>
      logger.info(s"writing OpenAPI spec to ${yamlPath}")
      Files.writeString(Path.of(yamlPath), UserTodoApi.asOpenAPIYaml)

    case Array("quill") =>
      val ctx  = QuillCtxFactory.createContext("database.default")
      val impl = new QuillTodoApiImpl(ctx)
      logger.info("got todos: {}", impl.list())
      ctx.close()

    case _ =>
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")

  }

}
