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

import dibl.Matrix._
import org.scalatest._

/** checks for typos in HashMap-s and more */
class MatrixSpec extends FlatSpec with Matchers {

  "chars in matrix strings" should "exist as keys for relSources" in {
    relSourcesMap.keys.toSet should contain
      matrixMap.values.flatten.toArray.flatten.sortWith(_ < _).distinct
  }

  "matrix string lengths" should "match dimensions in the key" in {
    matrixMap.keys.foreach{ key =>
      val (rows,cols) = Matrix.dims(key).get
      val valueLength = rows * cols
      matrixMap.get(key).get.foreach{ s =>
        if(s.length != valueLength)
          fail(s"length [${s.length}] of '$s' in set '$key' doesn't match dimensions $key ")
      }
    }
  }

  "predefined matrices" should "repeat without internal loose ends" in {

    val errors = new StringBuffer()
    matrixMap.keys.foreach { key =>
      for (i <- matrixMap.get(key).get.indices) {
        val m = Settings(key, i, absRows = 40, absCols = 12, shiftLeft = 1, shiftUp = 1,stitches = "").get.absM
        val nrOfLinks = countLinks(m)
        // println(nrOfLinks.deep.mkString("(",",",")").replaceAll("Array","\n").tail)
        val mID = s"$key.$i"
        val ok = Set[Int](0,2,3,4,5,6,7,8,9,10,11,13,14,15,21,22,23,24,25,44,55,75,77,80,81,82,88)
        // some matrices even don't have loose ends in the foot sides
        val topBottomMargin = 4
        val footsideMargin = if (
          (key == "4x2" && !ok.contains(i))
          || mID == "2x4.37" || mID == "2x4.39" || mID == "2x2.5"
          || mID == "2x4 cloth.1" || mID == "2x4 bias.2"
        ) 3 else 0
        // check the nodes have four or zero links but skip the margins
        for {
          row <- topBottomMargin until nrOfLinks.length - topBottomMargin
          col <- footsideMargin until nrOfLinks(0).length - footsideMargin
        } if (nrOfLinks(row)(col)%4 != 0)
          errors.append (s"${matrixMap.get(key).get(i)} $mID has ${nrOfLinks(row)(col)} links at ($row,$col)\n")
      }
    }
    errors.toString shouldBe ""
  }

  "shift" should "succeed" in {

    val m1 = Array(Array[String]("a", "b", "c", "d"), Array[String]("A", "B", "C", "D"))
    val m2 = Array(Array[String]("b", "c", "d", "a"), Array[String]("B", "C", "D", "A"))
    val m3 = Array(Array[String]("B", "C", "D", "A"), Array[String]("b", "c", "d", "a"))
    val m4 = Array(Array[String]("A", "B", "C", "D"), Array[String]("a", "b", "c", "d"))
    val mA: M = Array(
      Array(Array((11, 12)), Array((13, 14)), Array((15, 16)), Array((17, 18))),
      Array(Array((21, 22)), Array((23, 24)), Array((25, 26)), Array((27, 28)))
    )
    val mB: M = Array(
      Array(Array((23, 24)), Array((25, 26)), Array((27, 28)), Array((21, 22))),
      Array(Array((13, 14)), Array((15, 16)), Array((17, 18)), Array((11, 12)))
    )
    shift(m1, left = 1, up = 0) shouldBe m2
    shift(m1, left = 1, up = 1) shouldBe m3
    shift(m1, left = 0, up = 1) shouldBe m4
    shift(mA, left = 1, up = 1) shouldBe mB
  }
}