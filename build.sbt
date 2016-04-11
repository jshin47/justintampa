import sbtassembly.Plugin.AssemblyKeys._

lazy val commonSettings = Seq(
  organization := "com.jshin",
  version := "0.0.1",
  scalaVersion := "2.11.8"
)

val akkaV = "2.4.3"

lazy val serverSettings = Seq(
  name := "justintampa-client-access",
  libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % "3.1.1",
    "com.h2database" % "h2" % "1.4.191",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "commons-codec" % "commons-codec" % "1.8",
    "commons-io" % "commons-io" % "2.4",
    "org.bouncycastle" % "bcprov-jdk15on" % "1.52",
    "org.jsoup" % "jsoup" % "1.8.3",
    "com.lihaoyi" %% "upickle" % "0.3.8",
    "com.lihaoyi" %% "scalatags" % "0.5.4"
  ),
  mainClass in Compile := Some("com.jshin.jt.ca.Main"),
  mainClass in assembly := (mainClass in Compile).value,
  jarName in assembly := "justintampa-client-access.jar",
  test in assembly := {},
  mappings in Universal := {
    val universalMappings = (mappings in Universal).value
    val fatJar = (assembly in Compile).value
    val filtered = universalMappings filter {
      case (file, name) ⇒ !name.endsWith(".jar")
    }
    filtered :+ (fatJar → ("lib/" + fatJar.getName))
  },
  scriptClasspath := Seq((jarName in assembly).value),
  scalaJsBundlerCompile in Compile <<= (scalaJsBundlerCompile in Compile).dependsOn(fullOptJS in Compile in client),
  scalaJsBundlerAssets in Compile += {
    import com.karasiq.scalajsbundler.dsl._

    val bootstrap = github("twbs", "bootstrap", "v3.3.6") / "dist"
    val fontAwesome = github("FortAwesome", "Font-Awesome", "v4.5.0")
    val videoJs = github("videojs", "video.js", "v5.8.0") / "dist"
    val highlightJs = "org.webjars" % "highlightjs" % "9.2.0"

    val jsDeps = Seq(
      // jQuery
      Script from url("https://code.jquery.com/jquery-2.1.4.min.js"),

      // Bootstrap
      Style from bootstrap / "css" / "bootstrap.css",
      Script from bootstrap / "js" / "bootstrap.js",

      // Font Awesome
      Style from fontAwesome / "css" / "font-awesome.css",

      // Video.js
      Script from videoJs / "video.min.js",
      Style from videoJs / "video-js.min.css",
      Static("video-js.swf") from videoJs / "video-js.swf",

      // Plugins
      Script from github("eXon", "videojs-youtube", "v2.0.8") / "dist" / "Youtube.min.js",

      // Noty.js
      Script from github("needim", "noty", "v2.3.8") / "js" / "noty" / "packaged" / "jquery.noty.packaged.min.js",

      // Moment.js
      Script from github("moment", "moment", "2.12.0") / "min" / "moment-with-locales.min.js",

      // Marked
      Script from "org.webjars.bower" % "marked" % "0.3.5" / "marked.min.js",

      // Tab Override
      Script from github("wjbryant", "taboverride", "4.0.3") / "build" / "output" / "taboverride.min.js",

      // Highlight.js
      Script from highlightJs / "highlight.min.js",
      Style from highlightJs / s"styles/${ClientStaticAssets.highlightJsStyle}.css"
    )

    val staticDir = (baseDirectory in client)(_ / "files").value
    val staticFiles = Seq(
      Html from ClientStaticAssets.index,
      Style from ClientStaticAssets.style,
      Script from staticDir / "img2base64.js",
      Script from staticDir / "modernizr.js",
      Image("favicon.ico") from staticDir / "favicon.ico",
      Image("img/muon_bg.jpg") from staticDir / "muon_bg.jpg",
      Image("img/muon_posts.jpg") from staticDir / "muon_posts.jpg",
      Image("img/muon_inputs.jpg") from staticDir / "muon_inputs.jpg"
    )

    val fonts =
      (fontAwesome / "fonts" / "fontawesome-webfont").fonts() ++
        (videoJs / "font" / "VideoJS").fonts(dir = "font", extensions = Seq("eot", "svg", "ttf", "woff"))

    Bundle("index", jsDeps, staticFiles, fonts, scalaJsApplication(client).value)
  }
)

lazy val clientSettings = Seq(
  persistLauncher in Compile := true,
  name := "justintampa-client",
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies ++= Seq(
    "com.chuusai" %%% "shapeless" % "2.2.5",
    "com.github.karasiq" %%% "parboiled" % "2.1.1-SNAPSHOT",
    "com.github.karasiq" %%% "scalajs-bootstrap" % "1.0.6-SNAPSHOT",
    "com.github.karasiq" %%% "scalajs-videojs" % "1.0.2",
    "com.github.karasiq" %%% "scalajs-marked" % "1.0.1",
    "io.github.widok" %%% "scala-js-momentjs" % "0.1.4"
  )
)

lazy val shared = crossProject.in(file("shared"))
  .settings(commonSettings:_*)
  .settings(
    name := "justintampa-shared"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "me.chrons" %% "boopickle" % "1.1.2"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "me.chrons" %%% "boopickle" % "1.1.2"
    )
  )

lazy val sharedJVM = shared.jvm

lazy val sharedJS = shared.js

lazy val server = Project("justintampa-client-access", file("."))
  .dependsOn(sharedJVM)
  .settings(assemblySettings, commonSettings, serverSettings)
  .enablePlugins(ScalaJSBundlerPlugin, JavaAppPackaging)

lazy val client = Project("justintampa-client", file("client"))
  .dependsOn(sharedJS)
  .settings(commonSettings, clientSettings)
  .enablePlugins(ScalaJSPlugin)
