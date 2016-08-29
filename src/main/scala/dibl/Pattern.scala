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

abstract class Pattern (m:String,
                        tileType: String,
                        rows: Int,
                        cols: Int,
                        groupId: String = "GFP1",
                        offsetX: Int = 80,
                        offsetY: Int = 120) {

  require(rows * cols == m.length, "invalid matrix dimensions")
  require(offsetX > 0 && offsetY > 0, "invalid patch dimensions")

  protected val hXw = s"${rows}x$cols"
  private val tt = TileType(tileType)

  protected def toX(col: Int): Int = col * 10 + offsetX
  protected def toY(row: Int): Int = row * 10 + offsetY
  protected def toNodeId(row: Int, col: Int): String = s"${groupId}r${row}c$col"
  protected def toColor(row: Int, col: Int): String = {
    val (r,c) = tt.toOriginal(row, col, rows, cols)
    f"00${c * (256/cols)}%02X${r * (256/rows)}%02X"
  }

  def patch(rows: Int = 22, cols: Int = 22): String = (
    for {
      relative <- toRelSrcNodes(matrix = m, dimensions = hXw)
      checker = tt.toChecker(relative)
      absolute <- toAbsWithMargins(checker, rows, cols)
    } yield absolute
    ).map(createGroup).getOrElse("whoops")

  protected def createGroup(m: M): String = {
    val q = s"matrix=${this.m}&amp;tiles=$tileType"
    val url = "https://d-bl.github.io/GroundForge/index.html"
    s"""<g>
       |  <text style='font-family:Arial;font-size:11pt'>
       |   <tspan x='${offsetX + 15}' y='${offsetY - 20}'>$tileType, $hXw, $m</tspan>
       |   <tspan x='${offsetX + 15}' y='${offsetY - 0}' style='fill:#008;'>
       |    <a xlink:href='$url?$q'>pair/thread diagrams</a>
       |   </tspan>
       |  </text>
       |  ${createDiagram(m)}
       |</g>
       |""".stripMargin
  }

  protected def createDiagram(absolute: M): String
}

case class ConnectedPattern(m: String,
                            tileType: String,
                            rows: Int,
                            cols: Int,
                            groupId: String = "GFP1",
                            offsetX: Int = 80,
                            offsetY: Int = 120)
  extends Pattern(m, tileType, rows, cols, groupId, offsetX, offsetY) {

  def createDiagram(m: M) = {
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
        val (startRow,
        startCol) = start
        if (m(startRow)(
          startCol).isEmpty) "" else
          s"""  <path
             |    style='stroke:#000000;fill:none'
             |    d='M ${toX(startCol)},${toY(startRow)} ${toX (col)},${toY(row)}'
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
}
class ClonedPattern (m:String, tileType: String, rows: Int, cols: Int,
               groupId: String = "GFP1", offsetX: Int = 80, offsetY: Int = 120)
  extends Pattern(m, tileType, rows, cols, groupId, offsetX, offsetY) {

  val relative = toRelSrcNodes(matrix = m, dimensions = hXw).get // TODO repeating a previous action
  def createDiagram(m: M): String = clones + original(relative)

  private def clones: String = {
    val brickOffset = if (tileType == "bricks") cols * 5 else 0
    def cloneRows(row1: Int): String = {
      val row2 = row1 + rows * 10 // TODO refactor into TileType
      List.range(start = -(if (tileType == "bricks")brickOffset else 10*cols), end = 250, step = cols * 10).
        map(w => {
          clone(w, row1) + clone(w - brickOffset, row2)
        }).mkString("")
    }
    val cloneAtOriginal = clone(0, 0)
    List.range(start = -rows * 10, end = 200, step = rows * 20)
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
    val sourceRow = (targetRow + dRow + rows) % rows
    val sourceCol = // TODO refactor into TileType
      if (tileType == "bricks" && targetRow == 0 && dRow != 0)
        (targetCol + dCol + cols/2) % cols
      else (targetCol + dCol + cols) % cols
    val tag = s"${s"${toNodeId(sourceCol, sourceRow)}"}-${toNodeId(targetCol, targetRow)}"
    val targetNode = s"${offsetX + (targetCol * 10)},${offsetY + (targetRow * 10)}"
    val sourceNode = s"${offsetX + (dCol + targetCol) * 10},${offsetY + (dRow + targetRow) * 10}"
    val pathData = s"M $sourceNode $targetNode"
    s"\t\t<path d='$pathData'><title>$tag</title></path>\n"
  }
}

