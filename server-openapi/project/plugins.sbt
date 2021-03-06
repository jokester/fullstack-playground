addSbtPlugin("org.scalameta" % "sbt-scalafmt"  % "2.4.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

// packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.6")

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.8"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.5.0")
