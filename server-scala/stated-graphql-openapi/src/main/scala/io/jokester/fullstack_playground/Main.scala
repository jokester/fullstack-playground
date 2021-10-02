package io.jokester.fullstack_playground

import com.typesafe.scalalogging.LazyLogging

import java.nio.file.{Files, Path}

object Main extends App with LazyLogging {

  args.headOption.getOrElse("NONE") match {
    case "runServer" =>
      DatabaseInit.setupDefault
      AkkaHttpServer.listen(UserTodoApiAkkaBinding.buildRoute(new UserTodoServiceImpl()))

    case "writeOpenApiSpec" =>
      import sttp.tapir.openapi.circe.yaml._
      val yamlPath = Some(System.getenv("WRITE_OPENAPI_SPEC_TO_FILE"))
        .getOrElse(throw new IllegalArgumentException("$WRITE_OPENAPI_SPEC_TO_FILE not defined"))
      logger.info(s"writing OpenAPI spec to ${yamlPath}")
      Files.writeString(Path.of(yamlPath), UserTodoApi.asOpenAPI.toYaml)

    case _ =>
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")

  }

}
