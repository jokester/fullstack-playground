import sbt._

object Dependencies {
  lazy val basicDeps: Seq[ModuleID] = Seq(
    // logging
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.4",
    "ch.qos.logback"              % "logback-classic" % "1.2.6", // this provides SLJ4J backend
    // config
    "com.typesafe" % "config" % "1.4.1",
  )
  lazy val akkaDeps: Seq[ModuleID] = Seq(
    // akka
    "com.typesafe.akka" %% "akka-stream"        % Versions.akka,
    "com.typesafe.akka" %% "akka-actor-typed"   % Versions.akka,
    "com.typesafe.akka" %% "akka-stream-typed"  % Versions.akka,
    "com.typesafe.akka" %% "akka-slf4j"         % Versions.akka,
    "com.typesafe.akka" %% "akka-cluster-typed" % Versions.akka,
    "com.typesafe.akka" %% "akka-http"          % Versions.akkaHttp,
    "ch.megard"         %% "akka-http-cors"     % "1.1.2",
    "de.heikoseeberger" %% "akka-http-circe"    % "1.38.2",
  )
  lazy val circeDeps: Seq[ModuleID] = Seq(
    // circe
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
  ).map(_ % Versions.circe)

  lazy val tapirDeps: Seq[ModuleID] = Seq(
    // tapir as server
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server",
  ).map(_ % Versions.tapir) :+ ("com.pauldijou" %% "jwt-circe" % "5.0.0")
  //      "com.softwaremill.sttp.client3" %% "core"            % Versions.sttp

  lazy val rdbDeps: Seq[ModuleID] = Seq(
    // pgsql / hikariCP
    "org.postgresql" % "postgresql" % Versions.postgresql,
    //      "com.zaxxer"     % "HikariCP"   % "4.0.1", // this resolves to bad version
    "org.scalikejdbc" %% "scalikejdbc" % Versions.scalikeJDBC,
    "org.flywaydb"     % "flyway-core" % Versions.flyway,
  )

  lazy val testDeps: Seq[ModuleID] = Seq(
    "org.scalatest"       %% "scalatest"        % Versions.scalaTest,
    "org.scalikejdbc"     %% "scalikejdbc-test" % Versions.scalikeJDBC,
    "com.github.javafaker" % "javafaker"        % "1.0.2",
  ).map(_ % Test)

  lazy val buildDeps: Seq[ModuleID] = Seq.empty
}

private object Versions {
  val circe     = "0.14.1"
  val circeYaml = "0.13.1"

  val tapir = "0.18.3"

  val akkaHttp  = "10.2.6"
  val akka      = "2.6.17"
  val swaggerUi = "3.35.2"
  val upickle   = "1.2.2"

  // rdbms
  val postgresql  = "42.2.24"
  val scalikeJDBC = "3.5.0"
  val flyway      = "8.0.1"

  // testing
  val scalaTest               = "3.2.2"
  val scalaTestPlusScalaCheck = "3.2.2.0"

}
