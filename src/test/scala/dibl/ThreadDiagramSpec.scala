/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Try

class ThreadDiagramSpec extends FlatSpec with Matchers {
  "ThreadDiagram" should "not throw exceptions" in {
    // what it seems to do at the first row of stitches for most patterns
    nrOfNodes(Settings()) should be > 10
    nrOfNodes(Settings(key = "2x4", nr = 3, absRows = 8, absCols = 5)) should be > 10
    nrOfNodes(Settings(key = "2x4 rose ground", nr = 1, absRows = 8, absCols = 5, shiftLeft = 1)) should be > 10
    nrOfNodes(Settings(key = "2x4 rose ground", nr = 5, absRows = 8, absCols = 5, shiftLeft = 1)) should be > 10

    // TODO break the circle of start pins
    nrOfNodes(Settings(key = "2x4 bias", nr = 0, absRows = 8, absCols = 5, shiftLeft = 1)) should be > 10
    nrOfNodes(Settings(key = "2x4", nr = 3, absRows = 8, absCols = 9)) should be > 10
  }

  def nrOfNodes(s : Try[Settings]) = {
    val d = ThreadDiagram(PairDiagram(Settings().get))
    // mimic D3Data
    traverse(d.nodes)
    traverse(d.links)
    d.nodes.length
  }

  def traverse(items: Seq[Props]) = {

    val a = new Array[Any](items.length)
    println(s"${items.length} ${a.length}")
    for {i <- items.indices} {
      for {key <- items(i).keys} {
        a(i) = items(i).get(key).get
      }
    }
  }
}
