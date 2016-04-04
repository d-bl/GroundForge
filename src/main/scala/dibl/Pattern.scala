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

import dibl.Matrix._

import scala.scalajs.js.annotation.JSExport

@JSExport
object Pattern {

  @JSExport
  def get(key: String, nr: Int): String = {
    val s = matrixMap.get(key).map(_ (nr)).get // TODO error handling
    val m = toRelSrcNodes(matrix = s, dimensions = key).get
    createDoc(key, m)
  }

  def createDoc(key: String, m: M): String = {
    val (height, width) = dims(key).get
    def cloneRows(i: Int): String = {
      val ii = i + height * 10
      List.range(start = -width * 5, end = 500, step = width * 10).
        map(w => clone(w, i) + clone(w - width * 5, ii)).mkString("")
    }
    val clones = List.range(start = -height * 10, end = 500, step = height * 20).
      map(h => cloneRows(h)).mkString("").replace(clone(0, 0), clone(0, 0).replace("#000", "#008"))
    val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg'"
    val a4 = "height='1052' width='744'"
    s"<svg version='1.1' id='svg2' $a4 $nameSpaces>\n$clones\t<g id='g1'>\n${createOriginal(m)}\t</g>\n</svg>"
  }

  def clone(i: Int, j: Int): String = {
    val id = createId(i, j)
    s"\t<use transform='translate($i,$j)' xlink:href='#g1' id='u$id' height='100%' width='100%' y='0' x='0' style='stroke:#000;fill:none'/>\n"
  }

  def createOriginal(m: M): String = {
    var paths = ""
    for {
      i <- m.indices
      j <- m(0).indices
    } paths = paths + createNode((i, j), m(i)(j))
    paths
  }

  def createNode(target: (Int, Int), n: SrcNodes) = {
    if (n.length < 2) ""
    else {
      val (i, j) = target
      val id = createId(i, j)
      createLink(s"p1$id", target, n(0)) +
        createLink(s"p2$id", target, n(1))
    }
  }

  def createId(i: Int, j: Int): String =
    f"${i + 500}%03d${j + 500}%03d"

  def createLink(id: String, target: (Int, Int), src: (Int, Int)): String = {
    val (y, x) = target
    val (dy, dx) = src
    val offset = 120
    val s = s"${offset + (x * 10)},${offset + (y * 10)} ${offset + (dx + x) * 10},${offset + (dy + y) * 10}"
    s"\t\t<path id='$id' d='M $s'/>\n"
  }
}
