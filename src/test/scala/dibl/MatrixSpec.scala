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

/** checks for typos in HashMap-s */
class MatrixSpec extends FlatSpec with Matchers {

  "chars in matrix strings" should "exist as keys for relSources" in {
    relSourcesMap.keys.toSet should contain
      matrixMap.values.flatten.toArray.flatten.sortWith(_ < _).distinct
  }

  "matrix string lengths" should "match dimensions in the key" in {
    matrixMap.keys.foreach{ key =>
      val dim = Matrix.dim(key)
      val valueLength = dim(0) * dim(1)
      matrixMap.get(key).get.foreach{ s =>
        if(s.length != valueLength)
          fail(s"length [${s.length}] of '$s' in set '$key' doesn't match dimensions ${dim.deep} ")
      }
    }
  }

  "predefined matrices" should "repeat without internal loose ends" in {

    matrixMap.keys.foreach { key =>
      val errors = new StringBuffer()
      for (i <- matrixMap.get(key).get.indices) {
        val abs = Matrix(key, i, absRows = 12,absCols = 11, left=1 , up=1)
        val nrOfLinks = countLinks(abs)
        // visualize nodes in the margins
        //println(nrOfLinks.deep.mkString("(",",",")").replaceAll("Array","\n").tail)
        for { // assertions
          row <- 2 until nrOfLinks.length - 2
          col <- 3 until nrOfLinks(0).length - 3 // TODO fix foot sides to reduce 3 to 2
        } if (nrOfLinks(row)(col)%4 != 0)
          errors.append (s"$key.$i has ${nrOfLinks(row)(col)} links at ($row,$col)\n")
      }
      errors.toString shouldBe ""
      def s(xs: R) = println(xs.deep.mkString(",").replaceAll("Array",""))
    }
  }
}