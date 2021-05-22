import Dependencies._

// "bare" definition, applies to all projects
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "current"
ThisBuild / organization := "io.jokester.fullstack_playground"
ThisBuild / organizationName := "gh/jokester/fullstack-playground"

lazy val root = (project in file("."))
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
