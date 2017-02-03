name := """crypto"""

scalaVersion := "2.11.8"

javacOptions ++= Seq("-source", "1.6")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

crossPaths := false
autoScalaLibrary := false