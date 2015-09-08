import sbt.Keys._

// ··· Project Info ···

name := "spray-prerender"

organization := "com.github.jarlakxen"

crossScalaVersions := Seq("2.11.7")

scalaVersion <<= (crossScalaVersions) { versions => versions.head }

fork in run   := true

publishMavenStyle := true

publishArtifact in Test := false


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

val vAkka     = "2.3.12"
val vSpray    = "1.3.3"
val vSpec2    = "3.6.4"
val vJUnit    = "4.12"

libraryDependencies ++= Seq(
  // --- Akka ---
  "com.typesafe.akka"             %%  "akka-actor"            % vAkka     %  "provided",
  // --- Spray ---
  "io.spray"                      %%  "spray-can"             % vSpray    %  "provided",
  "io.spray"                      %%  "spray-routing"         % vSpray    %  "provided",
  "io.spray"                      %%  "spray-client"          % vSpray,
  // --- Testing ---
  "org.specs2"                    %%  "specs2-core"           % vSpec2    % "test",
  "org.specs2"                    %%  "specs2-junit"          % vSpec2    % "test",
  "io.spray"                      %%  "spray-testkit"         % vSpray    % "test",
  "junit"                         %   "junit"                 % vJUnit    % "test"
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
    <url>https://github.com/Jarlakxen/spray-prerender</url>
    <connection>scm:git:git@github.com:Jarlakxen/spray-prerender.git</connection>
    <developerConnection>scm:git:git@github.com:Jarlakxen/spray-prerender.git</developerConnection>
  </scm>
  <developers>
    <developer>
      <id>Jarlakxen</id>
      <name>Facundo Viale</name>
      <url>https://github.com/Jarlakxen/spray-prerender</url>
    </developer>
  </developers>
)

publishTo <<= version { v =>
  val nexus = "http://oss.sonatype.org/"
  if (v.endsWith("-SNAPSHOT"))
  Some("sonatype-nexus-snapshots" at nexus + "content/repositories/snapshots/")
  else
  Some("sonatype-nexus-staging" at nexus + "service/local/staging/deploy/maven2/")
}
