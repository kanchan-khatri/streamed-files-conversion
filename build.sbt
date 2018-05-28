lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "op.assessment",
      scalaVersion := "2.12.4",
      scalacOptions += "-Ypartial-unification"
    )),
    name := "betest"
  )

libraryDependencies += "org.typelevel"  %% "cats-core"    % "1.1.0"
libraryDependencies += "org.typelevel"  %% "cats-effect"  % "1.0.0-RC"
libraryDependencies += "co.fs2"         %% "fs2-core"     % "0.10.4"
libraryDependencies += "co.fs2" %% "fs2-io" % "0.10.4"

libraryDependencies += "org.scalatest"  %% "scalatest"    % "3.0.5" % Test
