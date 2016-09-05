name := "groonga4s"

version := "0.6.0"

scalaVersion := "2.11.8"

organization := "com.naokia"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.8.4" % "test",
  "org.apache.httpcomponents" % "httpclient" % "4.5.+",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2"
)

resolvers ++= Seq("scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

scalacOptions in Test ++= Seq("-Yrangepos")
