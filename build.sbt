// Scala versions
val scala3 = "3.3.0"

val scala213 = "2.13.11"

name := "dd-scala"
organization := "com.outr"
version := "1.2.13"

scalaVersion := scala213
crossScalaVersions := List(scala3, scala213)

scalacOptions ++= Seq("-unchecked", "-deprecation")
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
publishTo := sonatypePublishToBundle.value
sonatypeProfileName := "com.outr"
licenses := Seq("MIT" -> url("https://github.com/outr/dd-scala/blob/master/LICENSE"))
sonatypeProjectHosting := Some(xerial.sbt.Sonatype.GitHubHosting("outr", "dd-scala", "matt@matthicks.com"))
homepage := Some(url("https://github.com/outr/dd-scala"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/outr/dd-scala"),
    "scm:git@github.com:outr/dd-scala.git"
  )
)
developers := List(
  Developer(id="darkfrog", name="Matt Hicks", email="matt@matthicks.com", url=url("https://matthicks.com"))
)

outputStrategy := Some(StdoutOutput)

fork := true

libraryDependencies ++= Seq(
  "com.outr" %% "spice-client" % "0.1.9"
)