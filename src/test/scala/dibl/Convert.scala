/*
 Copyright 2016 Jo Pol
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

import dibl.Matrix.relSourcesMap

object Convert {

  def swap(nodes: Array[(Int, Int)]) = (nodes(1), nodes(0))

  def toTuple(nodes: Array[(Int, Int)]) = (nodes(0), nodes(1))

  def swapSrcNodes: ((Int, Int)) => (Int, Int) = x => (x._2, x._1)

  def toChar: (Array[Array[(Int, Int)]]) => Array[Char] =
    _.map(srcNodes => if (srcNodes.isEmpty) '-' else nodeMap.getOrElse(toTuple(srcNodes), '@'))

  def toChars(relative: Array[Array[Array[(Int, Int)]]]): String =
    relative.map(toChar).map(_.mkString("")).mkString(" ")

  val nodeMap: Map[((Int, Int), (Int, Int)), Char] = {
    val keys = "0123456789ABCD".toCharArray
    val invertedMap = keys.map(key => toTuple(relSourcesMap(key)) -> key)
    val swappedInvertedMap = keys.map(key => swap(relSourcesMap(key)) -> key)
    (invertedMap ++ swappedInvertedMap).toMap
  }

  def main (args: Array[String]) = {

    // a single list in one object is too large for the compiler
    for (input <- M4x4a.matrices ++ M4x4b.matrices) {
      val s = toChars(toRelative(4,4,input))
      if (s.matches(".*[ABCD].*"))
        println(s)
    }
    for (input <- Misc.m1x1) {
      val s = toChars(toRelative(1,1,input))
      if (!s.contains("@"))
        println(s)
    }
    for (input <- Misc.m2x1) {
      val s = toChars(toRelative(2,1,input))
      if (!s.contains("@"))
        println(s)
    }
    for (input <- Misc.m2x2) {
      val s = toChars(toRelative(2,2,input))
      if (!s.contains("@"))
        println(s)
    }
    for (input <- Misc.m2x4) {
      val s = toChars(toRelative(2,4,input))
      if (!s.contains("@"))
        println(s)
    }
    for (input <- Misc.m3x3) {
      val s = toChars(toRelative(3,3,input))
      if (!s.contains("@"))
        println(s)
    }
  }

  /**
    * @param m https://i1.wp.com/web.uvic.ca/~vmi/laceTools/Inkscape1/template.png
    * https://tesselace.com/tools/inkscape-extension/
    * @return each cell(row,col) represents a node pointing to both ancestors with relative coordinates
    *         order of left and right ancestor is not determined
    */
  def toRelative(rows: Int, cols: Int, m: Array[(Int, Int, Int, Int, Int, Int)]) = {
    val matrix = Array.fill(rows, cols)(List[(Int, Int)]())
    for (t <- m) {
      val (x1, y1, x2, y2, x3, y3) = t
      val dCol2 = -(x2 - x1)
      val dRow2 = -(y2 - y1)
      val dCol3 = -(x3 - x1)
      val dRow3 = -(y3 - y1)
      val col2 = (x2 + cols) % cols
      val row2 = (y2 + rows) % rows
      val col3 = (x3 + cols) % cols
      val row3 = (y3 + rows) % rows
      // println(s"$t has arrows arriving at ($row2,$col2) from ($dRow2,$dCol2) and at ($row3,$col3) from ($dRow3,$dCol3)")
      // swap (dx,dy) into (dRow,dCol)
      matrix(row2)(col2) = matrix(row2)(col2) ++ List((dRow2, dCol2))
      matrix(row3)(col3) = matrix(row3)(col3) ++ List((dRow3, dCol3))
    }
    //println("relative: "+matrix.deep.toString.replaceAll("Array","\n").replaceAll("List","").replaceAll(" ",""))
    matrix.map(_.map(_.toArray))
  }
}

