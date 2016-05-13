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

import dibl.Matrix.toRelSrcNodes

object Pattern {

  private val a4 = "height='1052' width='744'"
  private val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg'"

  def doc(patch: String) =
    s"${s"<svg version='1.1' id='svg2' $a4 $nameSpaces>"}\n$patch\n</svg>"
}

class Pattern (m:String, isBrick: Boolean, height: Int, width: Int,
               groupId: String = "g1", offsetX: Int = 80, offsetY: Int = 120) {

  private val hXw = s"${height}x$width"

  def patch: String = {
    val relative = toRelSrcNodes(matrix = m, dimensions = hXw).get
    val mType = if (isBrick) "brick wall" else "checker board"
    val link = "https://d-bl.github.io/GroundForge/advanced.html" +
      "?matrix=" + m.grouped(4).toArray.mkString("%0D") + (if (isBrick) "&amp;bricks=" else "")
    val tag = s"<text style='font-family:Arial;font-size:11pt'>\n" +
      s"\t <tspan x='${offsetX - 50}' y='${offsetY - 80}'>$mType, $hXw, $m</tspan>\n" +
      s"\t <tspan x='${offsetX - 50}' y='${offsetY - 60}' style='fill:#008;'>\n" +
      s"\t  <a xlink:href='$link'>pair/thread diagrams</a>\n" +
      s"\t </tspan>\n" +
      s"\t</text>\n"
    s"\n<g>\n$clones\n\t$tag\n\t<g id='$groupId'>\n${original(relative)}\t</g>\n</g>\n"
  }

  private def clones: String = {
    val brickOffset = if (isBrick) width * 5 else 0
    def cloneRows(row1: Int): String = {
      val row2 = row1 + height * 10
      List.range(start = -(if (isBrick)brickOffset else 10*width), end = 250, step = width * 10).
        map(w => {
          clone(w, row1) + clone(w - brickOffset, row2)
        }).mkString("")
    }
    val cloneAtOriginal = clone(0, 0)
    List.range(start = -height * 10, end = 200, step = height * 20)
      .map(h => cloneRows(h)).mkString("")
      .replace(cloneAtOriginal, cloneAtOriginal.replace("#000", "#008"))
  }

  private def clone(i: Int, j: Int): String =
    s"\t<use transform='translate($i,$j)' xlink:href='#$groupId' ${"style='stroke:#000;fill:none'"}/>\n"

  private def original(m: M): String =
    m.indices.flatMap(row =>
      m(row).indices.flatMap(col =>
        createNode((row, col), m(row)(col))
      )
    ).mkString("")

  private def createNode(target: (Int, Int), n: SrcNodes): String =
    if (n.length < 2) ""
    else {
      createLink(target, n(0)) + createLink(target, n(1))
    }

  private def createLink(target: (Int, Int), source: (Int, Int)): String = {
    val (targetRow, targetCol) = target
    val (dRow, dCol) = source
    val sourceRow = (targetRow + dRow + height) % height
    val sourceCol =
      if (isBrick && targetRow == 0 && dRow != 0)
        (targetCol + dCol + width/2) % width
      else (targetCol + dCol + width) % width
    val tag = s"${s"${toNodeId(sourceCol, sourceRow)}"}-${toNodeId(targetCol, targetRow)}"
    val targetNode = s"${offsetX + (targetCol * 10)},${offsetY + (targetRow * 10)}"
    val sourceNode = s"${offsetX + (dCol + targetCol) * 10},${offsetY + (dRow + targetRow) * 10}"
    val pathData = s"M $sourceNode $targetNode"
    s"\t\t<path d='$pathData'><title>$tag</title></path>\n"
  }

  private def toNodeId(col: Int, row: Int): String =
    "ABCDEFGIIJKLMNOPQRSTUVWXYZ" (col) + row.toString
}
