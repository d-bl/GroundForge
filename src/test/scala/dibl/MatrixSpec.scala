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

import org.scalatest._
import scala.collection.mutable.ListBuffer
import dibl.Matrix._

/** checks for typos in HashMap-s */
class MatrixSpec extends FlatSpec with Matchers {

  "chars in matrix strings" should "exist as keys for relSources" in {
    relSourcesMap.keys.toSet should contain
      matrixMap.values.flatten.toArray.flatten.sortWith(_ < _).distinct
  }

  "matrix string lengths" should "match dimensions in the key" in {
    matrixMap.keys.foreach{ key => 
      val dim = key.split("x").map(_.toInt)
      matrixMap.get(key).get.foreach{ s =>
        s.length shouldBe dim(0)*dim(1)//TODO let failure message show key and index
      }
    }
  }

  "nr of source nodes" should "match nr of target nodes" in {
    matrixMap.keys.foreach{ key =>
      for( i <- matrixMap.get(key).get.indices) {
        val src = toCheckerboard(getRelSources(key,i))
        val rows = src.length
        val cols = src(0).length
        val target: Array[Array[ListBuffer[(Int, Int)]]] = Array.fill(rows, cols)(ListBuffer[(Int,Int)]())
        for {
          row <- src.indices
          col <- src(0).indices
        } {
          for ((dRow, dCol) <- src(row)(col)) {
            target((row+dRow+rows)%rows)((col+dCol+cols)%cols) += ((row,col))
          }
        }
        println(src.deep.toString().replaceAll("Array","").replaceAll("-",""))
        println(target.deep.toString().replaceAll("Array","").replaceAll("ListBuffer","").replaceAll("-",""))
        // TODO yield both matrices to nr of nodes and compare those
        for {
          row <- src.indices
          col <- src(0).indices
        } {
          if (src(row)(col).length != target(row)(col).length)
            fail(s"row=$row, col=$col ${src(row)(col).length}!=${target(row)(col).length}")
        }
      }
    }
   }
}