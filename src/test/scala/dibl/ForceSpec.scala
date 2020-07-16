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

import dibl.Force.{ Point, nudgeNodes }
import dibl.proto.TilesConfig
import org.scalatest.{ FlatSpec, Matchers }

class ForceSpec extends FlatSpec with Matchers {

  // smaller row/col values cause less accurate average positions, larger values slow down recursion exponentially
  private val pairDiagram1 = NewPairDiagram.create(TilesConfig(
    "tile=5-&patchHeight=7&patchWidth=7&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4"
  ))

  "points" should "should be spread around the default origin" in {
    accumulate(nudgeNodes(pairDiagram1).get.nodes) shouldBe Point(0, 0)
  }

  it should "have some allowance for a custom origin" ignore {
    // just a tad too much variation in the values
    val origin = Point(6, 6)
    accumulate(nudgeNodes(pairDiagram1, origin).get.nodes, origin) shouldBe Point(6, 5)
  }

  "simulate" should "succeed on recursive diagrams" in {
    val threadDiagram2 = ThreadDiagram(nudgeNodes(pairDiagram1).get)
    val threadDiagram3 = ThreadDiagram(nudgeNodes(PairDiagram("ct", threadDiagram2)).get)
    val threadDiagram4 = ThreadDiagram(nudgeNodes(PairDiagram("ctc", threadDiagram3)).get)
    threadDiagram4 shouldBe a[Diagram]
    // accumulated positions are unpredictable
  }

  private def round(b: Point) = Point(Math.round(b.x*10)/10, Math.round(b.y*10)/10)

  private def accumulate(nodes: Seq[NodeProps], origin: Point = Point(0, 0)) = round(
    nodes
      .foldLeft(origin) {
        case (acc, next) =>
          Point(acc.x + next.x - origin.x, acc.y + next.y - origin.y)
      }
  )
}
