name := "groonga4s"

version := "0.8.0"

scalaVersion := "2.11.8"

organization := "com.naokia"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.4" % "test",
  "org.apache.httpcomponents" % "httpclient" % "4.5.+",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2"
)

scalacOptions in Test ++= Seq("-Yrangepos")
