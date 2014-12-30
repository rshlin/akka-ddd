import Deps._
import sbt.Keys._
import sbtrelease.ReleasePlugin._

name := "akka-ddd"

version in ThisBuild := "1.0.0-SNAPSHOT"

organization in ThisBuild := "pl.newicom.dddd"

scalaVersion in ThisBuild := "2.11.4"

scalacOptions in ThisBuild := Seq("-encoding", "utf8", "-feature", "-language:postfixOps")

publishMavenStyle in ThisBuild := true

lazy val root = (project in file("."))
  .settings(`Pub&RelSettings`: _*)
  .aggregate(`akka-ddd-messaging`, `akka-ddd-core`, `akka-ddd-view`, `akka-ddd-test`)
  .settings(
    publishArtifact := false)

lazy val `akka-ddd-messaging` = project
  .settings(`Pub&RelSettings`: _*)
  .settings(
    licenses := Seq("MIT" -> url("http://raw.github.com/pawelkaczor/akka-ddd/master/LICENSE.md")),
    libraryDependencies ++= Seq(
      Akka.actor,
      "com.github.nscala-time" %% "nscala-time" % "1.4.0"
    ),
    startYear := Some(2014)
  )

lazy val `akka-ddd-core` = project
  .settings(`Pub&RelSettings`: _*)
  .settings(
    licenses := Seq("MIT" -> url("http://raw.github.com/pawelkaczor/akka-ddd/master/LICENSE.md")),
    startYear := Some(2014),
    publishArtifact in Test := true,
    libraryDependencies ++= Seq(
      Akka.actor, Akka.contrib, Akka.persistence, Akka.slf4j
    ))
  .dependsOn(`akka-ddd-messaging`)


lazy val `akka-ddd-view` = project
  .settings(`Pub&RelSettings`: _*)
  .settings(
    licenses := Seq("MIT" -> url("http://raw.github.com/pawelkaczor/akka-ddd/master/LICENSE.md")),
    libraryDependencies ++= Seq(
      Akka.actor
    ),
    startYear := Some(2014))
  .dependsOn(`akka-ddd-messaging`)

lazy val `akka-ddd-test` = project
  .settings(`Pub&RelSettings`: _*)
  .settings(
    licenses := Seq("MIT" -> url("http://raw.github.com/pawelkaczor/akka-ddd/master/LICENSE.md")),
    libraryDependencies ++= Seq(
      Akka.actor, Akka.contrib, Akka.persistence, Akka.slf4j,
      Akka.testkit, Akka.multiNodeTestkit,
      "org.scalacheck" %% "scalacheck" % "1.11.6",
      "org.scalatest" %% "scalatest" % "2.2.2",
      "commons-io" % "commons-io" % "2.4"
    ),
    startYear := Some(2014))
  .dependsOn(`akka-ddd-core`)

lazy val `eventstore-akka-persistence` = project
  .settings(`Pub&RelSettings`: _*)
  .settings(
    licenses := Seq("MIT" -> url("http://raw.github.com/pawelkaczor/akka-ddd/master/LICENSE.md")),
    libraryDependencies ++= Seq(
      Eventstore.client excludeAll ExclusionRule(organization = "com.typesafe.akka"),
      Eventstore.akkaJournal excludeAll ExclusionRule(organization = "com.typesafe.akka"),
      Json4s.native, Json4s.ext,
      Akka.slf4j, Akka.persistence
    ),
    startYear := Some(2014))
  .dependsOn(`akka-ddd-messaging`)

lazy val `Pub&RelSettings`: Seq[Def.Setting[_]] = Publish.settings ++ releaseSettings

