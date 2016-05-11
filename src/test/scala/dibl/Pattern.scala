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

object Pattern {

  def get(key: String, nr: Int): String = {
    val m = matrixMap.get(key).map(_ (nr)).get
    createDoc(m)(isBrick = true, dims(key).get)
  }

  def createDoc(m:String)
               (implicit isBrick: Boolean, dims: (Int, Int)) = {
    val (height, width) = dims
    val hXw = s"${height}x$width"
    val relative = toRelSrcNodes(matrix = m, dimensions = hXw).get

    val a4 = "height='1052' width='744'"
    val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg'"
    val root = s"<svg version='1.1' id='svg2' $a4 $nameSpaces>"
    val tag = s"<text x='80' y='60'><tspan>$hXw $m</tspan></text>"
    s"$root\n$clones\n\t$tag\n\t<g id='g1'>\n${original(relative)}\t</g>\n</svg>"
  }

  def clones()(implicit isBrick: Boolean, dims: (Int, Int)): String = {
    val (height, width) = dims
    val brickOffset = if (isBrick) width * 5 else 0
    def cloneRows(row1: Int): String = {
      val row2 = row1 + height * 10
      List.range(start = -(if (isBrick)brickOffset else 10*width), end = 250, step = width * 10).
        map(w => {
          clone(w, row1) + clone(w - brickOffset, row2)
        }).mkString("")
    }
    val clones = List.range(start = -height * 10, end = 250, step = height * 20).
      map(h => cloneRows(h)).mkString("").replace(clone(0, 0), clone(0, 0).replace("#000", "#008"))
    clones
  }

  def clone(i: Int, j: Int): String = {
    val id = createId(i, j)
    val props = "height='100%' width='100%' y='0' x='0' style='stroke:#000;fill:none'"
    val cloneOf = "g1"
    s"\t<use transform='translate($i,$j)' xlink:href='#$cloneOf' id='u$id' $props/>\n"
  }

  def original(m: M)
              (implicit isBrick: Boolean, dims: (Int, Int)): String = {
    var paths = ""
    for {
      row <- m.indices
      col <- m(0).indices
    } paths = paths + createNode((row, col), m(row)(col))
    paths
  }

  def createNode(target: (Int, Int), n: SrcNodes)
                (implicit isBrick: Boolean, dims: (Int, Int)) = {
    if (n.length < 2) ""
    else {
      val (targetRow, targetCol) = target
      val id = createId(targetRow, targetCol)
      createLink(s"p1$id", target, n(0)) +
        createLink(s"p2$id", target, n(1))
    }
  }

  def createId(i: Int, j: Int): String = f"${i + 500}%03d${j + 500}%03d"

  def createLink(id: String, target: (Int, Int), source: (Int, Int))
                (implicit isBrick: Boolean, dims: (Int, Int)): String = {
    val offset = 120
    val (height, width) = dims
    val (targetRow, targetCol) = target
    val (dRow, dCol) = source
    val sourceCol = (targetCol + dCol + (if (isBrick) width/2 else width)) % width // FIXME
    val sourceRow = (targetRow + dRow + height) % height
    val tag = s"${s"${toNodeId(sourceCol, sourceRow)}"}-${toNodeId(targetCol, targetRow)}"
    val targetNode = s"${offset + (targetCol * 10)},${offset + (targetRow * 10)}"
    val sourceNode = s"${offset + (dCol + targetCol) * 10},${offset + (dRow + targetRow) * 10}"
    val pathData = s"M $sourceNode $targetNode"
    s"\t\t<path id='$id' d='$pathData'><title>$tag</title></path>\n"
  }

  def toNodeId(col: Int, row: Int)
              (implicit isBrick: Boolean, dims: (Int, Int)): String =
    "ABCDEFGIIJKLMNOPQRSTUVWXYZ" (col) + row.toString
}
