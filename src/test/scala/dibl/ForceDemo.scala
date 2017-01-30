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

import dibl.Force.{Point, simulate}

object ForceDemo {
  def main(args: Array[String]) {
    val pairDiagram1 = PairDiagram("5-", "bricks", stitches = "ct", absRows = 3, absCols = 3).get
    println(s"nodes: ${pairDiagram1.nodes}")
    println(s"links: ${pairDiagram1.links}")

    // demonstrating optional arguments
    println(
      simulate(pairDiagram1)
        .map(_.mkString(", "))
    )
    println(
      simulate(pairDiagram1, center = Point(200, 200))
        .map(_.mkString(", "))
    )
    println(
      simulate(pairDiagram1, interval = 100)
        .map(_.mkString(", "))
    )

    /*
      The first pair diagram has positions initialised from the matrix. These values are an estimate to prevent
      flipped and rotated diagrams. These positions are nudged by a force simulation.
      A thread diagram is created from a pair diagram by replacing each node with a couple of new nodes. These new
      nodes are all placed on the spot of their original node and are also nudged by a force simulation.
      A subsequent transition from a thread diagram to a pair diagram only restyles the nodes,
      their positions don't need nudging to optimise the next step.
    */
    val threadDiagram1 = ThreadDiagram(pairDiagram1, simulate(pairDiagram1))
    val pairDiagram2 = PairDiagram("ctc", threadDiagram1)
    val threadDiagram2a = ThreadDiagram(pairDiagram2)
    val threadDiagram2b = ThreadDiagram(threadDiagram2a, simulate(threadDiagram2a))
    val pairDiagram3 = PairDiagram("ctct", threadDiagram2b)

    /* TODO generate SVG in a scala way rather than feed a diagram to show-graph.js
       For now, you can view the thread diagrams and first pair diagram on
       https://d-bl.github.io/GroundForge/recursive.html
       though the force.simulation is not applied when creating one diagram from another
     */
    println(
      simulate(threadDiagram2a)
        .map(_.mkString(", "))
    )

    System.exit(0) // TODO terminate script engine more elegantly
  }
}
