import Dependencies._

val scala2Version = "2.13.6"

// "bare" definition, applies to all projects
ThisBuild / version := "current"
ThisBuild / organization := "io.jokester.fullstack_playground"
ThisBuild / organizationName := "gh/jokester/fullstack-playground"

lazy val statelessAkkaHttp = (project in file("stateless-akka-http"))
  .settings(
    name := "stateless-akka-http",
    libraryDependencies ++= loggingDeps ++ akkaDeps ++ circeDeps,
    scalaVersion := scala2Version,
    scalacOptions ++= Seq("-Xlint"),
    Universal / target := file("target/universal"),
  )
  .enablePlugins(
    JavaServerAppPackaging,
  )

lazy val statelessOpenapi = (project in file("stateless-openapi"))
  .settings(
    name := "",
    libraryDependencies ++= loggingDeps ++ akkaDeps ++ runtimeDeps ++ testDeps ++ buildDeps,
    scalaVersion := scala2Version,
    scalacOptions ++= Seq("-Xlint"),
  )

lazy val statedGraphqlOpenapi = (project in file("stated-graphql-openapi"))
  .settings(
    name := "server-openapi",
    libraryDependencies ++= runtimeDeps ++ testDeps ++ buildDeps,
    scalacOptions ++= Seq("-Xlint"),
    // flyway
    flywayUrl := "jdbc:postgresql://127.0.0.1:61432/fullstack_playground_dev",
    flywayUser := "pguser",
    flywayPassword := "secret",
    flywayLocations += "db/migration",
  )
  .enablePlugins(
    // see http://scalikejdbc.org/documentation/reverse-engineering.html
    // (not generating prefect code)
    ScalikejdbcPlugin,
    FlywayPlugin,
  )
