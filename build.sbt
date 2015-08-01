name := "groonga4s"

version := "0.1.5"

scalaVersion := "2.11.6"

organization := "com.naokia"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.5" % "test",
  "org.apache.httpcomponents" % "httpclient" % "4.4.+",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.5.+"
)

resolvers ++= Seq("scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

scalacOptions in Test ++= Seq("-Yrangepos")


