import Dependencies._

val scala2Version = "2.13.7"
val scala3Version = "3.0.0" // TODO: try compile with scala3

// "bare" definition, applies to all projects
ThisBuild / version := "current"
ThisBuild / organization := "io.jokester.fullstack_playground"
ThisBuild / organizationName := "gh/jokester/fullstack-playground"
ThisBuild / scalaVersion := scala2Version
ThisBuild / scalacOptions ++= Seq("-Xlint")
ThisBuild / coverageEnabled := true

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
    name := "stated-graphql-openapi",
    libraryDependencies ++= basicDeps ++ akkaDeps ++ circeDeps ++ tapirDeps ++ quillDeps ++ scalikeJdbcDeps ++ testDeps ++ buildDeps,
    Universal / target := file("target/universal"),
  )
  .enablePlugins(
    JavaAppPackaging,
    // see http://scalikejdbc.org/documentation/reverse-engineering.html
    // (not generating prefect code)
    ScalikejdbcPlugin,
  )
  .dependsOn(statelessAkkaHttp, statelessOpenapi % "compile->compile;test->test;")

lazy val rdbCodegen = (project in file("rdb-codegen"))
  .settings(
    name := "rdb-codegen",
    libraryDependencies ++= basicDeps ++ quillCodegenDeps,
    // dependencyOverrides ++= basicDeps, // required to force use of old SFJ4j api supported by logback-classic
  )

lazy val enableQuillLog = taskKey[Unit]("enable quill logs")
enableQuillLog := {
  System.err.println("enable quill log")
  sys.props.put("quill.macro.log", false.toString)
  sys.props.put("quill.binds.log", true.toString)
}
(statedGraphqlOpenapi / Compile / run) := ((statedGraphqlOpenapi / Compile / run) dependsOn enableQuillLog)
  .evaluated

{
  lazy val packageAllTxz = taskKey[Unit]("package all txz in parallel")
  packageAllTxz dependsOn (
    statelessAkkaHttp / Universal / packageXzTarball,
    statelessOpenapi / Universal / packageXzTarball,
    statedGraphqlOpenapi / Universal / packageXzTarball,
  )
  packageAllTxz := {}
}
