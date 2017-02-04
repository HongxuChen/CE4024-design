name := """cecz4024"""

scalaVersion := "2.11.8"

javacOptions ++= Seq("-source", "1.6")

libraryDependencies ++= Seq(
  "org.bouncycastle" % "bcprov-jdk15on" % "1.56",
  "ch.qos.logback" % "logback-classic" % "1.1.9",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

crossPaths := false
autoScalaLibrary := false