(emitSourceMaps in fullOptJS) := false

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "GroundForge"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "org.scalactic" %%% "scalactic" % "3.0.0",
  "org.scalatest" %%% "scalatest" % "3.0.0" % "test"
)

bootSnippet := "dibl.D3Data().get('-437 34-7', 10, 10, 0, 0, '', 'bricks');"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)