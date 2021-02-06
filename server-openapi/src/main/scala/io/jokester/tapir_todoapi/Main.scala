package io.jokester.tapir_todoapi

import java.io.File
import java.nio.file.{Files, Path}

import com.typesafe.scalalogging.LazyLogging

object Main extends App with LazyLogging {

  args.headOption.getOrElse("NONE") match {
    case "runServer" =>
      AkkaHttpServer.main()

    case "openapi" =>
      println(TodoApi.asOpenAPIYaml)

    case _ => {
      logger.debug("ARGV: {}", args.toList)
      throw new IllegalArgumentException(s"cannot recognize args: ${args.toList}")
    }

  }

}
