import sbt._

name := """play-slick-quickstart"""

version := "1.0-SNAPSHOT"

fullResolvers := Seq(
  Resolver.url("Typesafe repository", url("https://repo.typesafe.com/typesafe/releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("foo", url("https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns),
  "foo1" at "https://oss.sonatype.org/content/repositories/public",
  "foo2" at "https://repo.typesafe.com/typesafe/releases",
  Resolver.url("foo3", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns),
  "foobar" at "https://jcenter.bintray.com/"
)

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
  "org.webjars" % "outdated-browser" % "1.0.2",
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0"
)


play.Project.playScalaSettings