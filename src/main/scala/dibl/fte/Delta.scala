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

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

case class Delta(dx: Double, dy: Double) {
  private val rX: Int = (dx * acc).toInt
  private val rY: Int = (dy * acc).toInt
  val rounded: (Double, Double) = (rX / acc, rY / acc)
}

@JSExportTopLevel("Delta") object Delta {
  private val acc = 100000d

  @JSExport
  def create(topoLinks: Seq[TopoLink], u: js.Array[js.Array[Double]], v: js.Array[js.Array[Double]], q: js.Array[Double]): Map[TopoLink, Delta] = {
    // JVM variant in src/test dibl.fte.Deltas (note the plural)
    val zeroes = q.zipWithIndex.filter(_._1 < 0.00001)
    println(zeroes)
    if (zeroes.size != 2) {
      println(s"no null space: $zeroes")
      Map.empty
    } else {
      // TODO how to know that x and y are not mixed up?
      val Seq(x, y) = zeroes.map(_._2).toSeq
      val m = v.toSeq.map(_.toSeq)
      q.indices.map(i => topoLinks(i) -> Delta(m(i)(x), m(i)(y))).toMap
    }
  }
}
