import sbt._

name := """play-slick-quickstart"""

version := "1.0-SNAPSHOT"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"


libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.2.2",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.5",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.1.0",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.webjars" % "bootstrap" % "3.2.0",
  "org.webjars" % "jquery" % "2.1.1",
  "org.webjars" % "bootstrap-modal" % "2.2.5",
  "org.webjars" % "leaflet" % "0.7.3",
  "org.webjars" % "flot" % "0.8.3",
  "org.webjars" % "font-awesome" % "4.1.0",
  "org.webjars" % "outdated-browser" % "1.0.2"
)


play.Project.playScalaSettings