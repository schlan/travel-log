fullResolvers := Seq(
  Resolver.url("Typesafe repository", url("https://repo.typesafe.com/typesafe/releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("foo", url("https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns),
  "foo1" at "https://oss.sonatype.org/content/repositories/public",
  "foo2" at "https://repo.typesafe.com/typesafe/releases",
  Resolver.url("foo3", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns),
  "foobar" at "https://jcenter.bintray.com/"
)

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.3")