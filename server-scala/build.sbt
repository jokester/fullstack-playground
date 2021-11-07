import Dependencies._

val scala2Version = "2.13.7"
val scala3Version = "3.0.0" // TODO: try compile with scala3

// "bare" definition, applies to all projects
ThisBuild / version := "current"
ThisBuild / organization := "io.jokester.fullstack_playground"
ThisBuild / organizationName := "gh/jokester/fullstack-playground"
ThisBuild / scalaVersion := scala2Version
ThisBuild / scalacOptions ++= Seq("-Xlint")

lazy val statelessAkkaHttp = (project in file("stateless-akka-http"))
  .settings(
    name := "stateless-akka-http",
    libraryDependencies ++= basicDeps ++ akkaDeps ++ circeDeps,
    Universal / target := file("target/universal"),
  )
  .enablePlugins(JavaAppPackaging)

lazy val statelessOpenapi = (project in file("stateless-openapi"))
  .settings(
    name := "stateless-openapi",
    libraryDependencies ++= basicDeps ++ akkaDeps ++ circeDeps ++ tapirDeps ++ testDeps ++ buildDeps,
    Universal / target := file("target/universal"),
  )
  .enablePlugins(JavaAppPackaging)
  .dependsOn(statelessAkkaHttp)

lazy val statedGraphqlOpenapi = (project in file("stated-graphql-openapi"))
  .settings(
    name := "server-openapi",
    libraryDependencies ++= basicDeps ++ akkaDeps ++ circeDeps ++ tapirDeps ++ quillDeps ++ testDeps ++ buildDeps,
    Universal / target := file("target/universal"),
  )
  .enablePlugins(
    JavaAppPackaging,
    // see http://scalikejdbc.org/documentation/reverse-engineering.html
    // (not generating prefect code)
    ScalikejdbcPlugin,
  )
  .dependsOn(statelessAkkaHttp, statelessOpenapi)

lazy val rdbCodegen = (project in file("rdb-codegen"))
  .settings(
    name := "rdb-codegen",
    libraryDependencies ++= basicDeps ++ quillDeps ++ testDeps ++ buildDeps,
  )
