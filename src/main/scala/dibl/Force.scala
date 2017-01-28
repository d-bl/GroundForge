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

object Force {
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
         |      Java.type("dibl.Force").onEnd(nodes, callback)
         |    })
         |}
         |print("javascript engine started")
         |""".stripMargin)
    engine.asInstanceOf[Invocable]
  }

  /** For internal use by the evaluated script
    *
    * @param jsNodePositions converted to a callback argument
    * @param callback        called when the D3js simulation’s timer stops
    */
  def onEnd(jsNodePositions: ScriptObjectMirror,
            callback: (Array[Point]) => Unit
           ): Unit =
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

  /** Calculates new node position with D3js in a background process.
    *
    * @param diagram  collections converted to nodes (x: Int, y: Int)
    *                 for https://github.com/d3/d3-force/#forceSimulation
    *                 and links (source: Int , target: Int)
    *                 for https://github.com/d3/d3-force/#links
    *                 the boolean link.weak is converted to a strength value
    * @param center   used for https://github.com/d3/d3-force/#forceCenter
    * @param callback called when the D3js simulation’s timer stops:
    *                 https://github.com/d3/d3-force/#simulation_on
    */
  def simulate(diagram: Diagram,
               center: Point = Point(0, 0),
               callback: (Array[Point]) => Unit = { points => println(points.mkString(", ")) }
              ): Unit = {
    invocable.invokeFunction("applyForce", center, diagram, callback)
    println()
    println(s"nodes: ${diagram.nodes}")
    println(s"links: ${diagram.links}")
  }
}
