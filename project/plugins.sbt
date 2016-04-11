logLevel := Level.Warn

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.8")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.0-RC2")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")
addSbtPlugin("com.github.karasiq" % "sbt-scalajs-bundler" % "1.0.7")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "scalatags" % "0.5.4",
  "org.webjars.bower" % "marked" % "0.3.5",
  "org.webjars" % "highlightjs" % "9.2.0"
)