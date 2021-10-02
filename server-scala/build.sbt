import Dependencies._

val scala2Version = "2.13.6"

// "bare" definition, applies to all projects
ThisBuild / version := "current"
ThisBuild / organization := "io.jokester.fullstack_playground"
ThisBuild / organizationName := "gh/jokester/fullstack-playground"

lazy val statelessAkkaHttp = (project in file("stateless-akka-http"))
  .settings(
    name := "",
    libraryDependencies ++= logDeps ++ akkaDeps,
    scalaVersion := scala2Version,
    scalacOptions ++= Seq("-Xlint"),
  )

lazy val statelessOpenapi = (project in file("statless-openapi"))
  .settings(
    name := "",
    libraryDependencies ++= runtimeDeps ++ testDeps ++ buildDeps,
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
    JavaAppPackaging,
    // see http://scalikejdbc.org/documentation/reverse-engineering.html
    // (not generating prefect code)
    ScalikejdbcPlugin,
    FlywayPlugin,
  )
