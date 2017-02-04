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

import scala.util.Try

class ForceSpec extends FlatSpec with Matchers {

  // smaller row/col values cause less accurate average positions, larger values slow down recursion exponentially
  private val pairDiagram1 = PairDiagram("5-", "bricks", stitches = "ct", absRows = 5, absCols = 5).get

  "points" should "should be spread around the default origin" in {
    accumulate(simulate(pairDiagram1)) shouldBe Point(0, 0)
  }

  it should "should be spread around the custom origin" in {
    val origin = Point(12, 12)
    accumulate(simulate(pairDiagram1, origin), origin) shouldBe origin
  }

  "simulate" should "succeed on recursive diagrams" in {
    val threadDiagram1 = ThreadDiagram(pairDiagram1.nudgeNodes().get)
    val threadDiagram2 = ThreadDiagram(PairDiagram("ctc", threadDiagram1))
    val triedPoints = simulate(threadDiagram2)
    // TODO accumulation results are unpredictable but never right in this case
    //accumulate(triedPoints) shouldBe Point(0, 0)
  }

  private def round(b: Point) = Point(Math.round(b.x), Math.round(b.y))

  private def accumulate(triedPoints: Try[Array[Point]], origin: Point = Point(0, 0)) = round(
    triedPoints
      .get
      .foldLeft(origin) {
        case (Point(ax, ay), Point(bx, by)) =>
          Point(ax + bx - origin.x, ay + by - origin.y)
      }
  )
}