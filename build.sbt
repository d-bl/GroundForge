(emitSourceMaps in fullOptJS) := false

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "GroundForge"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"

bootSnippet := "dibl.D3Data().get('-437 34-7', 10, 10, 0, 0, '', 'bricks');"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)