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
import java.util.concurrent.{CyclicBarrier, TimeUnit}
import javax.script.{Invocable, ScriptContext, ScriptEngine, ScriptEngineManager}

import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.util.{Failure, Success, Try}

/** A dedicated JavaScript engine with a predefined D3js force simulation,
  * intended for single threaded batch execution in a JVM environment.
  */
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
      // because of this (not state of the art) declaration
      // each thread needs its own instance of the engine
      s"""var onEnd = Java.type("dibl.Force").onEnd
         |print("JavaCcript engine with D3-Force configuration started")
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
    barrier.await()
  }

  // the result of the asynchronous calculation
  private var points: Try[Array[Point]] = _

  // turns the asynchronous calculation in a synchronous call
  private val barrier = new CyclicBarrier(2)

  case class Point(x: Double, y: Double)

  /** Calculates new node positions in a dedicated JavaScript engine, NOT THREAD SAFE!
    *
    * The calculations are executed in a thread as it is time consuming
    * and the D3js library is primarily intended for client-side animations in a browser.
    * This method waits until the calculation completes for sequential batch execution.
    *
    * @param diagram  collections that are converted to nodes (x: Int, y: Int)
    *                 for https://github.com/d3/d3-force/#simulation_nodes
    *                 and links (source: Int , target: Int)
    *                 for https://github.com/d3/d3-force/#links
    *                 the boolean link.weak is converted to a predefined value for
    *                 https://github.com/d3/d3-force/#link_strength
    * @param center   used for https://github.com/d3/d3-force/#forceCenter
    * @param timeout  maximum time allowed for the D3js calculations
    * @param timeUnit the time unit of the timeout parameter
    */
  def simulate(diagram: Diagram,
               center: Point = Point(0, 0),
               timeout: Long = 20,
               timeUnit: TimeUnit = TimeUnit.SECONDS
              ): Try[Array[Point]] = {
    try {
      invocable.invokeFunction("applyForce", center, diagram)
    } catch {
      case e: Throwable => return Failure(e)
    }
    Try {
      println(s"waiting $timeout $timeUnit")
      barrier.await(timeout, timeUnit)
      println(s"done waiting")
      // at this moment another thread with the same instance
      // could start a new calculation and overwrite points before it is used
    }.flatMap(_ => points)
  }
}
