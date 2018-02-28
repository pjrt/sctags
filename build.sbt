import java.nio.file.{Files, Paths}
import java.nio.file.attribute.{PosixFilePermission => Perm}

import scala.collection.JavaConverters._

import ReleaseTransformations._

lazy val stags =
  (project in file("stags"))
    .settings(
      libraryDependencies += "org.scalameta" %% "scalameta" % "3.3.1"
    )

lazy val cli =
  (project in file("cli"))
    .enablePlugins(BuildInfoPlugin)
    .dependsOn(stags % "compile->compile;test->test")
    .settings(
      name := "stags-cli",
      libraryDependencies += "com.github.scopt" %% "scopt" % "3.7.0",
      mainClass in assembly := Some("co.pjrt.stags.cli.Main"),
      buildInfoKeys := Seq[BuildInfoKey](version),
      buildInfoPackage := "co.pjrt.stags.cli.build",
      assemblyJarName in assembly := s"stags-${version.value}"
    )

lazy val root = (project in file("."))
  .aggregate(stags, cli)
  .settings(
    // Don't publish useless root artifacts
    packagedArtifacts := Map.empty,
    //--prevent project publishing https://github.com/sbt/sbt/issues/3136
    publish := {},
    publishLocal := {},
    publishM2 := {},
    publishArtifact := false //prevent publishing the root project in sbt0.13
  )
