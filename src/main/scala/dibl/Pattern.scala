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

import dibl.Matrix.{toAbsWithMargins, toRelSrcNodes}

class Pattern (m:String, tileType: String, rows: Int, cols: Int,
               groupId: String = "GFP1", offsetX: Int = 80, offsetY: Int = 120) {

  require(rows * cols == m.length, "invalid matrix dimensions")

  private val hXw = s"${rows}x$cols"
  private val tt = TileType(tileType)

  def patch(rows: Int = 22, cols: Int = 22): String = {

    require(offsetX > 0 && offsetY > 0, "invalid patch dimensions")

    (for {
      relative <- toRelSrcNodes(matrix = m, dimensions = hXw)
      checker = tt.toChecker(relative)
      absolute <- toAbsWithMargins(checker, rows, cols)
      q = "matrix=" + m.grouped(this.cols).toArray.mkString("%0D") + s"&amp;tiles=$tileType"
      url = "https://d-bl.github.io/GroundForge/index.html"
    } yield
      s"""<g>
         |  <text style='font-family:Arial;font-size:11pt'>
         |   <tspan x='${offsetX + 15}' y='${offsetY - 20}'>$tileType, $hXw, $m</tspan>
         |   <tspan x='${offsetX + 15}' y='${offsetY -  0}' style='fill:#008;'>
         |    <a xlink:href='$url?$q'>pair/thread diagrams</a>
         |   </tspan>
         |  </text>
         |  ${createDiagram(absolute)}
         |</g>
         |""".stripMargin
    ).get}

  def createDiagram(m: M): String = {
    def createNode(row: Int, col: Int) =
      s"""  <circle
         |    style='fill:#${toColor(row, col)};stroke:none'
         |    id='${toNodeId(row, col)}'
         |    cx='${toX(col)}'
         |    cy='${toY(row)}'
         |    r='2'
         |  />
         |""".stripMargin

    def createTwoIn(row: Int, col: Int): String = {
      val srcNodes = m(row)(col)
      def createPath(start: (Int, Int)): String = {
        val (startRow, startCol) = start
        if (m(startRow)(startCol).isEmpty) "" else
          s"""  <path
             |    style='stroke:#000000;fill:none'
             |    d='M ${toX(startCol)},${toY(startRow)} ${toX(col)},${toY(row)}'
             |    inkscape:connector-type='polyline'
             |    inkscape:connector-curvature='0'
             |    inkscape:connection-start='#${toNodeId(startRow, startCol)}'
             |    inkscape:connection-end='#${toNodeId(row, col)}'
             |  />
             |""".stripMargin
      }
      s"""${createPath(srcNodes(0))}
         |${createPath(srcNodes(1))}""".stripMargin
    }
    m.indices.flatMap(row => m(row).indices.filter(m(row)(_).nonEmpty).flatMap(col => createNode(row, col))).toArray.mkString("") +
    m.indices.flatMap(row => m(row).indices.filter(m(row)(_).nonEmpty).flatMap(col => createTwoIn(row, col))).toArray.mkString("")
  }

  private def toX(col: Int): Int = col * 10 + offsetX
  private def toY(row: Int): Int = row * 10 + offsetY
  private def toNodeId(row: Int, col: Int): String = s"${groupId}r${row}c$col"
  private def toColor(row: Int, col: Int): String = {
    val (r,c) = tt.toOriginal(row, col, rows, cols)
    f"00${c * (256/cols)}%02X${r * (256/rows)}%02X"
  }
}
