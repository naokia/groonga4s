name := "groonga4s"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core" % "3.5" % "test",
  "org.apache.httpcomponents" % "httpclient" % "4.4.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.5.2"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

scalacOptions in Test ++= Seq("-Yrangepos")
