package io.jokester.fullstack_playground

import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.user_todolist_api.UserTodoApi

import java.nio.file.{Files, Path}

object Main extends App with LazyLogging {

  args.headOption.getOrElse("NONE") match {
    case "writeOpenApiSpec" =>
      val yamlPath = Some(System.getenv("WRITE_OPENAPI_SPEC_TO_FILE"))
        .getOrElse(throw new IllegalArgumentException("$WRITE_OPENAPI_SPEC_TO_FILE not defined"))
      logger.info(s"writing OpenAPI spec to ${yamlPath}")
      Files.writeString(Path.of(yamlPath), UserTodoApi.asOpenAPIYaml)

    case _ =>
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")

  }

}
