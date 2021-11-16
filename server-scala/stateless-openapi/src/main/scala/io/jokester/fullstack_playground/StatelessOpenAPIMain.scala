package io.jokester.fullstack_playground

import akka.http.scaladsl.server.Directives.pathPrefix
import com.typesafe.scalalogging.LazyLogging
import io.jokester.fullstack_playground.todolist_api.{
  TodoApi,
  TodoApiAkkaBinding,
  TodoApiImpl,
  TodoApiMemoryImpl,
}
import io.jokester.fullstack_playground.utils.akka_http.AkkaHttpServer

import java.nio.file.{Files, Path}

object StatelessOpenAPIMain extends App with LazyLogging {

  args match {
    case Array("writeApiSpec", outputFilename: String) =>
      logger.info(s"Writing ApiSpec to ${outputFilename}")
      Files.writeString(Path.of(outputFilename), TodoApi.asOpenAPIYaml)

    case Array("server") =>
      /**
        * CRUD
        */
      val todoRoute = TodoApiAkkaBinding.buildTodoApiRoute(new TodoApiMemoryImpl())

      val rootRoute = pathPrefix("stateless-openapi") {
        todoRoute
      }
      AkkaHttpServer.listen(
        (AkkaHttpServer.withCors & AkkaHttpServer.withRequestLogging) {
          rootRoute
        },
      )

    case _ =>
      logger.error(s"Command Not Recognized: ${args}")
      System.exit(1)

  }
}
