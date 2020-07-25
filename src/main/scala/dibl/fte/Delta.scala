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

  /** version for a java environment */
  def apply(data: Seq[Seq[Double]],
            topoLinks: Seq[TopoLink],
           ): Try[Map[TopoLink, Delta]] = {
    //    println("data " + data.map(_.map(_.toInt).mkString(",")).mkString("; "))
    //    println("summed data " + data.map(_.map(_.toInt).sum).mkString(","))

    val t0 = System.nanoTime
    val nullSpace = new SimpleMatrix(data.map(_.toArray).toArray).svd.nullSpace
    //    println("Elapsed time nullspace: " + (System.nanoTime - t0) * 0.000000001 + "s")

    val cols = nullSpace.numCols()
    val rows = nullSpace.numRows()
    if (cols != 2 || rows != topoLinks.size)
      Failure(new Exception(s"nullSpace dimensions are ($rows,$cols) expected (${ topoLinks.size },2)"))
    else Success((0 until rows)
      .map(i => topoLinks(i) -> Delta(nullSpace.get(i, 0), nullSpace.get(i, 1)))
      .toMap
    )
  }
}
