import sbt.Keys.libraryDependencies
//enablePluqgins(com.lucidchart.sbt.scalafmt.ScalafmtPlugin)

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

evictionWarningOptions in update := EvictionWarningOptions.default
  .withWarnTransitiveEvictions(false)
  .withWarnDirectEvictions(false)
  .withWarnScalaVersionEviction(false)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Ywarn-unused-import",
  "-Xfatal-warnings",
)

val ScalaticVersion = "3.0.4"
val ScalaLibraryVersion = "2.12.4"

lazy val versionSnapshot = s"$ScalaticVersion-SNAPSHOT"

lazy val commonSettings = Seq(
  organization := "pl.writeonly.addons",
  scalaVersion := ScalaLibraryVersion,
  version := versionSnapshot
)
lazy val IntegrationTest = config("it") extend(Test)
lazy val End2EndTest = config("et") extend(Test)

lazy val integrationInConfig = inConfig(IntegrationTest)(Defaults.testTasks)
lazy val end2endInConfig = inConfig(End2EndTest)(Defaults.testTasks)

def whiteFilter(name: String): Boolean = name endsWith "AssertSpec"
def grayFilter(name: String): Boolean = (name endsWith "ScalarSpec") || (name endsWith "VectorSpec")
def blackFilter(name: String): Boolean = (name endsWith "FunSpec") || (name endsWith "FeatureSpec")

lazy val whiteSetting = testOptions in Test := Seq(Tests.Filter(whiteFilter))
lazy val graySetting = testOptions in IntegrationTest := Seq(Tests.Filter(grayFilter))
lazy val blackSetting = testOptions in End2EndTest := Seq(Tests.Filter(blackFilter))

lazy val inConfigs = Seq(integrationInConfig, end2endInConfig)
lazy val settings = Seq(whiteSetting, graySetting, blackSetting)

lazy val scalaaddons = (project in file("."))
//  .enablePlugins(JacocoItPlugin)
  .aggregate(specs, utils)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scalaaddons",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    coverageEnabled := true,
    coverageMinimum := 60,
    coverageFailOnMinimum := true,
      libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
      )
  )

lazy val utils = (project in file("scalaaddon-utils"))
  .dependsOn(specs)
  .configs(IntegrationTest, End2EndTest)
  .settings(
    name := "scalaaddon-utils",
    commonSettings,
    integrationInConfig, end2endInConfig,
    whiteSetting, graySetting, blackSetting,
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % ScalaLibraryVersion,
      "org.scalactic" %% "scalactic" % ScalaticVersion
    )
  )

lazy val specs = (project in file("scalaaddon-specs"))
  .settings(
    name := "scalaaddon-specs",
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
