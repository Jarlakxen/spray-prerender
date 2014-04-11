resolvers ++= Seq(
	"Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
	"Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8")
