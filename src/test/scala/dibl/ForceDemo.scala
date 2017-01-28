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

import java.lang.Thread.sleep

import dibl.Force.Point

object ForceDemo {
  def main(args: Array[String]) {
    val pairDiagram = PairDiagram("5-", "bricks", stitches = "ctc", absRows = 3, absCols = 3).get

    // TODO rather wait for the D3js threads than just sleep
    // event_loop.js implements setTimeout(func, delay) and setInterval(func, delay)
    // for D3js with java.util.Timer and java.util.concurrent.Phaser
    // it needs more hoops than Force.onEnd to define them in scala
    Force.simulate(pairDiagram)
    sleep(2000)
    Force.simulate(pairDiagram, center = Point(200,200))
    sleep(2000)
    Force.simulate(pairDiagram, callback = applyNodePositions)
    sleep(2000)

    // TODO apply scaled nodePositions to a diagram before calculating the next
    // it might prevent https://github.com/d-bl/GroundForge/blob/87d706d/docs/images/bloopers.md#3
    // without needing the countDown loop at
    // https://github.com/d-bl/GroundForge/blob/2d85c7a/docs/API.md#functions-createsvg-createthreadsvg-createpairsvg
    val threadDiagram = ThreadDiagram(pairDiagram)
    PairDiagram("ctc",threadDiagram)
    // TODO at last generate SVG in a scala way rather than feed the diagram to show-graph.js

    System.exit(0)
  }

  private def applyNodePositions (points: Array[Point]) = println(points.mkString(", "))
}
