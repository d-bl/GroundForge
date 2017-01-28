/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html
*/
package dibl

import java.io.FileReader
import javax.script.{Invocable, ScriptContext, ScriptEngine, ScriptEngineManager}

import jdk.nashorn.api.scripting.ScriptObjectMirror

object Batch {
  private val invocable = {
    val engine: ScriptEngine = (new ScriptEngineManager).getEngineByName("nashorn")
    val engineScope = engine.getBindings(ScriptContext.ENGINE_SCOPE)
    engineScope.put("window", engineScope)
    engine.eval(new FileReader("docs/js/d3.v4.min.js"))
    engine.eval(new FileReader("docs/js/event_loop.js"))
    engine.eval(
      s"""function nodesToJS(arrayOfMaps) {
        |  var result = new Array(arrayOfMaps.size())
        |  for ( i = arrayOfMaps.size() ; i-- > 0 ;  ){
        |    map = arrayOfMaps.apply(i)
        |    result[i] = {
        |      x: map.get('x').get(),
        |      y: map.get('y').get()
        |    }
        |  }
        |  return result
        |}
        |function linksToJS(nodes, arrayOfMaps) {
        |  var result = new Array(arrayOfMaps.size())
        |  for ( i = arrayOfMaps.size() ; i-- > 0 ;  ){
        |    map = arrayOfMaps.apply(i)
        |    result[i] = {
        |      source: map.get('source').get(),
        |      target: map.get('target').get(),
        |      weak: map.get('weak').get()
        |    }
        |  }
        |  return result
        |}
        |function strength(link){return link.weak? 5 : 50}
        |function applyForce(center, data, callback) {
        |  var nodes = nodesToJS(data.nodes())
        |  var links = linksToJS(nodes, data.links())
        |  d3.forceSimulation(nodes)
        |    .force("charge", d3.forceManyBody().strength(-1000))
        |    .force("link", d3.forceLink(links).strength(strength).distance(12).iterations(30))
        |    .force("center", d3.forceCenter(center.x(), center.y()))
        |    .alpha(0.0035)
        |    .on("end", function() {
        |      Java.type("dibl.Batch").onEnd(nodes, callback)
        |    })
        |}
        |print("javascript engine started")
        |""".stripMargin)
    engine.asInstanceOf[Invocable]
  }

  /** For internal use
    *
    * @param jsNodePositions converted to argument for callback
    * @param callback called when the D3js simulation’s timer stops
    */
  def onEnd(jsNodePositions: ScriptObjectMirror, callback: (Array[Point])=> Unit): Unit =
    callback(jsNodePositions
      .values()
      .toArray()
      .map(ps => {
        val props = ps.asInstanceOf[ScriptObjectMirror]
        Point(
          props.get("x").asInstanceOf[Double],
          props.get("y").asInstanceOf[Double]
        )
      })
    )

  case class Point(x: Double, y: Double)

  /**
    *
    * @param center used for https://github.com/d3/d3-force/#forceCenter
    * @param diagram collections of nodes and links for:
    *                https://github.com/d3/d3-force/#forceSimulation
    *                https://github.com/d3/d3-force/#links
    * @param callback called when the D3js simulation’s timer stops:
    *                 https://github.com/d3/d3-force/#simulation_on
    */
  def applyForce(diagram: Diagram,
                 center: Point = Point(0,0),
                 callback: (Array[Point]) => Unit = {ns => println(ns.mkString(", "))}
                ): Unit = {
    invocable.invokeFunction("applyForce", center, diagram, callback)
    println()
    println(s"nodes: ${diagram.nodes}")
    println(s"links: ${diagram.links}")
  }

  /** usage example */
  def main(args: Array[String]) {
    val pairDiagram = PairDiagram("5-", "bricks", stitches = "ctc", absRows = 3, absCols = 3).get

    // TODO rather wait for the D3js threads than just sleep
    // event_loop.js implements setTimeout(func, delay) and setInterval(func, delay)
    // for D3js with java.util.Timer and java.util.concurrent.Phaser
    // it needs more hoops than onEnd to define them in scala
    applyForce(pairDiagram); Thread.sleep(2000)
    applyForce(pairDiagram); Thread.sleep(2000)
    applyForce(pairDiagram); Thread.sleep(2000)

    // TODO apply the nodePositions to a diagram before calculating the next
    // it might prevent https://github.com/d-bl/GroundForge/blob/87d706d/docs/images/bloopers.md#3
    // without needing the countDown loop at
    // https://github.com/d-bl/GroundForge/blob/2d85c7a/docs/API.md#functions-createsvg-createthreadsvg-createpairsvg
    val threadDiagram = ThreadDiagram(pairDiagram)
    PairDiagram("ctc",threadDiagram)
    // TODO at last generate SVG in a scala way rather than feed it to show-graph.js

    System.exit(0)
  }
}
