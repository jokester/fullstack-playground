import sbt._

object Dependencies {
  lazy val runtimeDeps: Seq[ModuleID] = Seq(
    Seq(
      // logging
      "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2",
      "ch.qos.logback"              % "logback-classic" % "1.2.3", // this provides SLJ4J backend
      "com.typesafe.akka"          %% "akka-slf4j"      % Versions.akkaStreams,
    ),
    Seq(
      // circe
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser",
    ).map(_ % Versions.circe),
    Seq(
      // sttp / tapir
      "com.softwaremill.sttp.tapir" %% "tapir-core",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
      "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server",
      //      "com.softwaremill.sttp.client3" %% "core"            % Versions.sttp
    ).map(_ % "0.17.10"),
    Seq(
      // akka
      "com.typesafe.akka" %% "akka-stream"      % Versions.akkaStreams,
      "com.typesafe.akka" %% "akka-http"        % Versions.akkaHttp,
      "com.typesafe.akka" %% "akka-actor-typed" % Versions.akkaStreams,
      "ch.megard"         %% "akka-http-cors"   % "1.1.0",
    ),
    Seq(
      Seq(
        // pgsql / hikariCP
        "org.postgresql" % "postgresql" % Versions.postgresql,
        //      "com.zaxxer"     % "HikariCP"   % "4.0.1", // this resolves to bad version
      ),
      Seq(
        "org.scalikejdbc" %% "scalikejdbc" % Versions.scalikeJDBC,
      ),
      Seq(
        "org.flywaydb" % "flyway-core" % Versions.flyway,
      ),
    ).flatten,
    Seq(
      "com.typesafe" % "config" % "1.4.1",
      // rest
      // auth0's jwt impl
      //      "com.auth0" % "java-jwt" % "3.13.0",
      "com.pauldijou" %% "jwt-circe" % "5.0.0",
    ),
  ).flatten

  lazy val testDeps: Seq[ModuleID] = Seq(
    "org.scalatest"       %% "scalatest"        % Versions.scalaTest,
    "org.scalikejdbc"     %% "scalikejdbc-test" % Versions.scalikeJDBC,
    "com.github.javafaker" % "javafaker"        % "1.0.2",
  ).map(_ % Test)

  lazy val buildDeps: Seq[ModuleID] = Seq.empty
}

private object Versions {
  val http4s      = "0.21.8"
  val catsEffect  = "2.2.0"
  val circe       = "0.13.0"
  val circeYaml   = "0.13.1"
  val sttp        = "3.0.0-RC5"
  val sttpModel   = "1.2.0-RC4"
  val sttpShared  = "1.0.0-RC6"
  val akkaHttp    = "10.2.3"
  val akkaStreams = "2.6.12"
  val swaggerUi   = "3.35.2"
  val upickle     = "1.2.2"
  val playJson    = "2.9.1"
  val silencer    = "1.7.1"
  val finatra     = "20.9.0"
  val catbird     = "20.9.0"
  val sprayJson   = "1.3.5"
  val scalaCheck  = "1.14.3"

  // testing
  val scalaTest               = "3.2.2"
  val scalaTestPlusScalaCheck = "3.2.2.0"

  // slick
  val slick   = "3.3.3"
  val slickPg = "0.19.3"

  // postgres
  val postgresql = "42.2.8"

  val scalikeJDBC = "3.5.0"

  val flyway = "7.8.2"

  // wtf
  val refined        = "0.9.17"
  val enumeratum     = "1.6.1"
  val zio            = "1.0.3"
  val zioInteropCats = "2.2.0.1"
  val playServer     = "2.8.2"
  val tethys         = "0.11.0"
}
