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

  private val hXw = s"${rows}x$cols"

  def patch: String = (
    for {
      relative <- toRelSrcNodes(matrix = m, dimensions = hXw)
      checker = TileType(tileType).toChecker(relative)
      absolute <- toAbsWithMargins(checker, 22, 22)
    } yield
      s"""<g>
          |  $createTag
          |  <g id='$groupId'>
          |    ${createConnectors(absolute)}
          |  </g>
          |</g>
          |""".stripMargin
    ).get

  def createTag: String = {
    val link = "https://d-bl.github.io/GroundForge/index.html" +
      "?matrix=" + m.grouped(cols).toArray.mkString("%0D") + s"&amp;tiles=$tileType"
    s"""
      |<text style='font-family:Arial;font-size:11pt'>
      | <tspan x='${offsetX + 15}' y='${offsetY - 20}'>$tileType, $hXw, $m</tspan>
      | <tspan x='${offsetX + 15}' y='${offsetY -  0}' style='fill:#008;'>
      |  <a xlink:href='$link'>pair/thread diagrams</a>
      | </tspan>
      |</text>"""
  }

  def createConnectors(m: M): String = {
    def createNode(row: Int, col: Int, cell: SrcNodes): String = if (cell.isEmpty) "" else {

      val nodeX = col * 10 + offsetX
      val nodeY = row * 10 + offsetY
      f"""  <circle
         |    style='fill:#00${(row % cols + 1) * 32}%2X${(col % rows + 1) * 32}%2X;stroke:none'
         |    id='${toNodeId(row, col)}'
         |    cx='$nodeX'
         |    cy='$nodeY'
         |    r='2'
         |  />""".stripMargin
    }
    def createTwoIn(row: Int, col: Int, cell: SrcNodes): String = if (cell.isEmpty) "" else {

      val nodeX = col * 10 + offsetX
      val nodeY = row * 10 + offsetY
      val (leftRow, leftCol)= cell(0)
      val (rightRow, rightCol) = cell(1)

      def createPath(startRow: Int, startCol: Int): String = if (m(startRow)(startCol).isEmpty) "" else
        s"""  <path
           |    style='stroke:#000000;fill:none'
           |    d='M ${startCol * 10 + offsetX},${startRow * 10 + offsetY} $nodeX,$nodeY'
           |    inkscape:connector-type='polyline'
           |    inkscape:connector-curvature='0'
           |    inkscape:connection-start='#${toNodeId(startRow, startCol)}'
           |    inkscape:connection-end='#${toNodeId(row, col)}'
           |  />""".stripMargin
      s"""  <!-- row=$row col=$col left : (row=$leftRow, col=$leftCol) right : (row=$rightRow, col=$rightCol)-->
         |${createPath(leftRow, leftCol)}
         |${createPath(rightRow, rightCol)}
         |""".stripMargin
    }
    m.indices.flatMap(row => m(row).indices.flatMap(col =>
      createNode(row, col, m(row)(col))
    )).toArray.mkString("") +
    m.indices.flatMap(row => m(row).indices.flatMap(col =>
      createTwoIn(row, col, m(row)(col))
    )).toArray.mkString("")
  }

  private def toNodeId(row: Int, col: Int): String =
    s"${groupId}r${row}c$col"
}
