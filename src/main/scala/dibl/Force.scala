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

import javax.script.{Invocable, ScriptEngine, ScriptEngineManager}
import jdk.nashorn.api.scripting.ScriptObjectMirror

import scala.util.Try

/** A dedicated JavaScript engine with a predefined D3js force simulation,
  * for batch execution in a JVM environment.
  */
object Force {
  private val invocable = {
    val engine: ScriptEngine = (new ScriptEngineManager).getEngineByName("nashorn")
    // TODO add source to errors, currently just:
    // ... in <eval> at line number ... at column number ...
    engine.eval(new FileReader("docs/js/d3.v4.min.js"))
    engine.eval(new FileReader("src/main/resources/event_loop.js"))
    engine.eval(new FileReader("src/main/resources/force.js"))
    engine.eval("print('JavaScript engine with D3-Force configuration started')")
    engine.asInstanceOf[Invocable]
  }

  /** Calculates new node positions in a dedicated JavaScript engine.
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
  def nudgeNodes(diagram: Diagram,
                 center: Point = Point(0, 0),
                 timeout: Long = 20,
                 timeUnit: TimeUnit = TimeUnit.SECONDS
                ): Try[Diagram] = Try {
    val nodes = toJS("nodesToJS", diagram.nodes)
    val links = toJS("linksToJS", diagram.links)

    def toScalaNode(i: Int): NodeProps = {
      val node = nodes.get(i.toString).asInstanceOf[ScriptObjectMirror]
      diagram.nodes(i).withLocation(
        node.get("x").asInstanceOf[Double],
        node.get("y").asInstanceOf[Double]
      )
    }

    invocable.invokeFunction("applyForce", barrier, center, nodes, links)
    println(s"waiting at most $timeout $timeUnit to simulate forces")
    barrier.await(timeout, timeUnit)
    println(s"done waiting")
    Diagram(
      diagram.nodes.indices.map(i => toScalaNode(i)),
      diagram.links
    )
  }

  /** turns the asynchronous calculation in a synchronous call */
  private val barrier = new CyclicBarrier(2)

  case class Point(x: Double, y: Double)

  private def toJS(jsFunction: String, props: Seq[Props]) = {
    invocable
      .invokeFunction(jsFunction, props)
      .asInstanceOf[ScriptObjectMirror]
  }
}
