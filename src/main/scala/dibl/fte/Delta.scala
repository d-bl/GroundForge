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
package dibl.fte

import dibl.fte.Delta.acc
import org.ejml.simple.SimpleMatrix

import scala.util.{ Failure, Success, Try }

case class Delta(dx: Double, dy: Double) {
  private val rX: Int = (dx * acc).toInt
  private val rY: Int = (dy * acc).toInt
  val rounded: (Double, Double) = (rX / acc, rY / acc)
}

object Delta {
  private val acc = 100000d

  /** for a java environment */
  def apply(data: Seq[Seq[Double]],
            topoLinks: Seq[TopoLink],
           ): Try[Map[TopoLink, Delta]] = {
    //    println("data " + data.map(_.map(_.toInt).mkString(",")).mkString("; "))
    //    println("summed data " + data.map(_.map(_.toInt).sum).mkString(","))

    val t0 = System.nanoTime
    val svd = new SimpleMatrix(data.map(_.toArray).toArray).svd
    //    println("Elapsed time nullspace: " + (System.nanoTime - t0) * 0.000000001 + "s")

    val v = svd.getV
    val x = v.numCols() - 2
    val y = x + 1
    val secondLastOnDiagonalOfW = svd.getW.get(x, x)
    if (secondLastOnDiagonalOfW > 0.001)
        Failure(new Exception(s"no null space, second last diagonal element: $secondLastOnDiagonalOfW"))
    else Success((0 to y)
      .map(i => topoLinks(i) -> Delta(v.get(i, x), v.get(i, y)))
      .toMap
    )
  }
}
