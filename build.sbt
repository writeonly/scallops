enablePlugins(com.lucidchart.sbt.scalafmt.ScalafmtPlugin)

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

evictionWarningOptions in update := EvictionWarningOptions.default
  .withWarnTransitiveEvictions(false)
  .withWarnDirectEvictions(false)
  .withWarnScalaVersionEviction(false)

scapegoatVersion in ThisBuild := "1.3.4"
wartremoverErrors ++= Warts.unsafe

scalacOptions ++= Seq(
  "-deprecation",
  "-explaintypes",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-target:jvm-1.8",
  "-unchecked",
  "-Xcheckinit",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Xverify",
  "-Ypartial-unification",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ypartial-unification",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
)

scalastyleFailOnWarning := true
scalastyleFailOnError := true

val ScalaticVersion = "3.0.4"
val ScalaLibraryVersion = "2.12.4"
val AkkaVersion = "2.5.12"

lazy val versionSnapshot = s"$ScalaticVersion-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "pl.writeonly.scalaops",
  scalaVersion := ScalaLibraryVersion,
  version := versionSnapshot
)

lazy val IntegrationTest = config("it") extend  Test
lazy val End2EndTest = config("et") extend Test

logBuffered in Test := false
testOptions in Test ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-o"),
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
)

lazy val integrationInConfig = inConfig(IntegrationTest)(Defaults.testTasks)
lazy val end2endInConfig = inConfig(End2EndTest)(Defaults.testTasks)

def whiteFilter(name: String): Boolean = (name endsWith "AssertSpec") || (name endsWith "FutureSpec")
def grayFilter(name: String): Boolean = (name endsWith "ScalarSpec") || (name endsWith "VectorSpec")
def blackFilter(name: String): Boolean = (name endsWith "FunSpec") || (name endsWith "FeatureSpec")

lazy val whiteSetting = testOptions in Test := Seq(Tests.Filter(whiteFilter))
lazy val graySetting = testOptions in IntegrationTest := Seq(Tests.Filter(grayFilter))
lazy val blackSetting = testOptions in End2EndTest := Seq(Tests.Filter(blackFilter))

lazy val inConfigs = Seq(integrationInConfig, end2endInConfig)
lazy val settings = Seq(whiteSetting, graySetting, blackSetting)

lazy val scallops = (project in file("."))
  //  .enablePlugins(JacocoItPlugin)
  .aggregate(specs, ops, pipe, monoidSpecs, monoid, monoidExternal, logging)
  .dependsOn(specs, ops, pipe, monoidSpecs, monoid, monoidExternal, logging)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scalaops",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    coverageEnabled := true,
    coverageMinimum := 0,
    coverageFailOnMinimum := true,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
    )
  )

lazy val logging = (project in file("scallops-logging"))
  .dependsOn(specs, pipe, ops)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scallops-logging",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
      "org.scalactic" %% "scalactic" % ScalaticVersion,
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  )

lazy val monoidExternal = (project in file("scalalops-monoid-external"))
  .dependsOn(specs, pipe, ops, monoidSpecs, monoid)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scala-for-future-external",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
      "org.scalactic" %% "scalactic" % ScalaticVersion,
      "org.typelevel" %% "cats-core" % "1.1.0",
      "org.scalaz" %% "scalaz-core" % "7.2.22"
    )
  )

lazy val monoid = (project in file("scallops-monoid"))
  .dependsOn(specs, pipe, ops, monoidSpecs)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scala-for-future",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
    )
  )

lazy val monoidSpecs = (project in file("scallops-monoid-specs"))
  .dependsOn(specs, pipe, ops)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scala-for-future-specs",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
    )
  )

lazy val ops = (project in file("scala-ops"))
  .dependsOn(specs, pipe)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scala-ops",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
//      "org.scalactic" %% "scalactic" % ScalaticVersion,
//      "org.typelevel" %% "cats-core" % "1.1.0"
    )
  )

lazy val pipe = (project in file("scala-pipe"))
  .dependsOn(specs)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scala-pipe",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
    )
  )

lazy val specs = (project in file("scala-specs"))
  .settings(
    name := "scala-specs",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0",
      "org.scalacheck" %% "scalacheck" % "1.13.5",
      "org.pegdown" % "pegdown" % "1.6.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  )
