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
    val pairDiagram = PairDiagram("5-", "bricks", stitches = "ctc", absRows = 3, absCols = 3).get
    println(s"nodes: ${pairDiagram.nodes}")
    println(s"links: ${pairDiagram.links}")

    println(simulate(pairDiagram).map(_.mkString(", ")))
    println(simulate(pairDiagram, center = Point(200,200)).map(_.mkString(", ")))
    println(simulate(pairDiagram, interval = 100).map(_.mkString(", ")))

    val threadDiagram = ThreadDiagram(pairDiagram, simulate(pairDiagram))
    PairDiagram("ctc", threadDiagram, simulate(threadDiagram))
    // TODO at last generate SVG in a scala way rather than feed the diagram to show-graph.js

    System.exit(0) // TODO terminate script engine more elegantly
  }
}
