(emitSourceMaps in fullOptJS) := false

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "DiBL-ground"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

bootSnippet := "dibl.Graph().get();"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)