import sbt._
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
    scalacOptions ++= Seq("-Xlint")
  )
  .enablePlugins(
    JavaAppPackaging,
  )
