name := "ThreeManChess"

version := "0.1"

organization := "pl.archiet"

scalaVersion := "2.12.6"
scalacOptions ++= Seq(
  "-language:_",
  "-Ypartial-unification",
  "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  "com.github.mpilquist" %% "simulacrum" % "0.13.0",
  "org.scalaz" %% "scalaz-core" % "7.2.26"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

libraryDependencies += "eu.timepit" %% "refined-scalaz" % "0.9.2"

libraryDependencies += "org.scalaz" %% "testz-core" % "0.0.5"

libraryDependencies += "org.scalaz" %% "testz-stdlib" % "0.0.5"

libraryDependencies += "org.scalaz" %% "testz-runner" % "0.0.5"

libraryDependencies += "org.scalaz" %% "testz-scalaz" % "0.0.5"

addSbtPlugin("org.ensime" % "sbt-ensime" % "2.5.1")