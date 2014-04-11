import sbt.Keys._
import sbtrelease.ReleasePlugin._

// ··· Settings ···

seq(releaseSettings: _*)

// ··· Project Info ···

name := "spray-prerender"

organization := "com.github.jarlakxen"

crossScalaVersions := Seq("2.10.4")

fork in run   := true

publishMavenStyle := true

publishArtifact in Test := false

// ··· Project Enviroment ···

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala

EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE17)

unmanagedSourceDirectories in Compile := (scalaSource in Compile).value :: Nil

unmanagedSourceDirectories in Test := (scalaSource in Test).value :: Nil

resourceDirectory in Compile := baseDirectory.value / "src" / "main" / "resources"


// ··· Project Options ···

javacOptions ++= Seq(
    "-source", "1.7",
    "-target", "1.7"
)

scalacOptions ++= Seq(
    "-encoding",
    "utf8",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-unchecked",
    "-deprecation"
)

// ··· Project Repositories ···


resolvers ++= Seq(
    "spray repo"                     at "http://repo.spray.io/",
    "OSS"                            at "http://oss.sonatype.org/content/repositories/releases/")

// ··· Project Dependancies···

libraryDependencies ++= Seq(
  // --- Akka ---
  "com.typesafe.akka"             %%  "akka-actor"            % "2.2.3"		%  "provided",
  // --- Spray ---
  "io.spray"                      %   "spray-can"             % "1.2.1"		%  "provided",
  "io.spray"                      %   "spray-routing"         % "1.2.1"		%  "provided",
  "io.spray"                      %   "spray-client"          % "1.2.1",
  // --- Testing ---
  "org.specs2"                    %%  "specs2-core"           % "2.3.10"  % "test",
  "org.specs2"                    %%  "specs2-junit"          % "2.3.10"  % "test",
  "io.spray"                      %   "spray-testkit"         % "1.2.1"   % "test",
  "junit"                         %   "junit"                 % "4.11"    % "test"
)

pomExtra := (
  <url>https://github.com/Jarlakxen/spray-prerender</url>
  <licenses>
    <license>
      <name>GPL v2</name>
      <url>https://github.com/Jarlakxen/spray-prerender/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
  <url>git@github.com:Jarlakxen/spray-prerender.git</url>
  <connection>scm:git:git@github.com:Jarlakxen/spray-prerender.git</connection>
  </scm>
  <developers>
    <developer>
      <id>Jarlakxen</id>
      <name>Facundo Viale</name>
      <url>https://github.com/Jarlakxen/spray-prerender</url>
    </developer>
  </developers>
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

publishTo <<= version { v =>
  val nexus = "http://oss.sonatype.org/"
  if (v.endsWith("-SNAPSHOT"))
  Some("sonatype-nexus-snapshots" at nexus + "content/repositories/snapshots/")
  else
  Some("sonatype-nexus-staging" at nexus + "service/local/staging/deploy/maven2/")
}
