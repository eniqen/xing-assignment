name := "xing-assignment"

version := "0.1"

scalaVersion := "2.12.9"

lazy val ScalaTest = "3.0.8"
lazy val DoobieVersion = "0.8.0-RC1"
lazy val EnumeratumVersion = "1.5.13"
lazy val KindProjectorVersion = "0.10.3"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % ScalaTest,
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "com.beachape" %% "enumeratum" % EnumeratumVersion,
  "org.scalatest" %% "scalatest" % ScalaTest % "test"
)

resolvers += Resolver.sonatypeRepo("snapshots")

addCompilerPlugin(
  "org.typelevel" %% "kind-projector" % KindProjectorVersion cross CrossVersion.binary)
