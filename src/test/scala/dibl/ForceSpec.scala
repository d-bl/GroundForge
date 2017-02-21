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

import dibl.Force.{Point, simulate}
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Success, Try}

class ForceSpec extends FlatSpec with Matchers {

  // smaller row/col values cause less accurate average positions, larger values slow down recursion exponentially
  private val pairDiagram1 = PairDiagram("5-", "bricks", absRows = 7, absCols = 7, stitches = "ct").get

  "points" should "should be spread around the default origin" in {
    accumulate(simulate(pairDiagram1)) shouldBe Point(0, 0)
  }

  it should "have some allowance for a custom origin" in {
    val origin = Point(6, 6)
    accumulate(simulate(pairDiagram1, origin), origin) shouldBe Point(5, 5)
  }

  "simulate" should "succeed on recursive diagrams" in {
    val threadDiagram2 = ThreadDiagram(pairDiagram1.nudgeNodes().get)
    val threadDiagram3 = ThreadDiagram(PairDiagram("ct", threadDiagram2)).nudgeNodes().get
    val threadDiagram4 = ThreadDiagram(PairDiagram("ctc", threadDiagram3)).nudgeNodes()
    threadDiagram4 shouldBe a[Success[_]]
    // accumulated positions are unpredictable
  }

  private def round(b: Point) = Point(Math.round(b.x*10)/10, Math.round(b.y*10)/10)

  private def accumulate(triedPoints: Try[Array[Point]], origin: Point = Point(0, 0)) = round(
    triedPoints
      .get
      .foldLeft(origin) {
        case (Point(ax, ay), Point(bx, by)) =>
          Point(ax + bx - origin.x, ay + by - origin.y)
      }
  )
}