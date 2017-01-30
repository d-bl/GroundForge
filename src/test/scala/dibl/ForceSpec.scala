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

import scala.util.Success

class ForceSpec extends FlatSpec with Matchers {

  private val pairDiagram1 = PairDiagram("5-", "bricks", stitches = "ct", absRows = 3, absCols = 3).get

  "points" should "should be spread around the center" in {
    round(accumulate(simulate(pairDiagram1).get)) shouldBe Point(0, 0)
  }

  "simulate" should "succeed" in {
    val threadDiagram1 = ThreadDiagram(pairDiagram1)
    val threadDiagram2 = ThreadDiagram(PairDiagram("ctc", threadDiagram1))
    simulate(pairDiagram1) shouldBe a[Success[_]]
    simulate(threadDiagram2) shouldBe a[Success[_]]
  }

  private def round(b: Point) = Point(Math.round(b.x), Math.round(b.y))

  private def accumulate(points: Array[Point]) = points
    .foldLeft(Point(0, 0)) { case (Point(ax, ay), Point(bx, by)) => Point(ax + bx, ay + by) }
}