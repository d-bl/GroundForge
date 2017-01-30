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

import scala.util.{Failure, Success, Try}

/** A dedicated JavaScript engine with a predefined D3js force simulation */
object Force {
  private val invocable = {
    val engine: ScriptEngine = (new ScriptEngineManager).getEngineByName("nashorn")
    val engineScope = engine.getBindings(ScriptContext.ENGINE_SCOPE)
    engineScope.put("window", engineScope)
    // TODO improve errors, currently: ... in <eval> at line number ... at column number ...
    engine.eval(new FileReader("docs/js/d3.v4.min.js")) // drop ".min" for debugging purposes
    engine.eval(new FileReader("src/main/resources/event_loop.js"))
    engine.eval(new FileReader("src/main/resources/force.js"))
    engine.eval(
      s"""var onEnd = Java.type("dibl.Force").onEnd
         |print("javascript engine started")
         |""".stripMargin)
    engine.asInstanceOf[Invocable]
  }

  /** For internal use, called when the D3js simulation’s timer stops:
    * https://github.com/d3/d3-force/#simulation_on
    *
    * @param jsNodePositions A copy of the x/y values of [[Diagram.nodes]] changed by D3js.
    */
  def onEnd(jsNodePositions: ScriptObjectMirror): Unit = try {
    points = Success(jsNodePositions
      .values()
      .toArray()
      .map(ps => {
        val props = ps.asInstanceOf[ScriptObjectMirror]
        Point(
          props.get("x").asInstanceOf[Double],
          props.get("y").asInstanceOf[Double]
        )
      }))
  } catch {
    case e: Throwable => points = Failure(e)
  } finally {
    busy = false
  }

  private var points: Try[Array[Point]] = _
  private var busy = false

  case class Point(x: Double, y: Double)

  /** Calculates new node positions in a dedicated JavaScript engine, NOT THREAD SAFE!
    *
    * D3js executes the calculation in a thread as it is time consuming
    * and the library is primarily intended for client-side animations in a browser.
    * This method waits until the calculation completes for sequential batch execution.
    *
    * @param diagram  collections that are converted to nodes (x: Int, y: Int)
    *                 for https://github.com/d3/d3-force/#simulation_nodes
    *                 and links (source: Int , target: Int)
    *                 for https://github.com/d3/d3-force/#links
    *                 the boolean link.weak is converted to a strength value
    * @param center   used for https://github.com/d3/d3-force/#forceCenter
    * @param interval time to wait to check whether D3js is ready,
    *                 balance the overhead of a small value against spilled idle time
    */
  def simulate(diagram: Diagram,
               center: Point = Point(0, 0),
               interval: Long = 200
              ): Try[Array[Point]] = {
    busy = true // set to false by onEnd when the invoked applyForce completes.
    try {
      invocable.invokeFunction("applyForce", center, diagram)
    } catch {
      case e: Throwable => return Failure(e)
    }
    while (busy) Thread.sleep(interval)
    points // set by onEnd()
  }
}
