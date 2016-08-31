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

object Pattern {

  def apply (m:String,
             tileType: String,
             groupId: String = "GFP1",
             offsetX: Int = 80,
             offsetY: Int = 120,
             sheetType: String = "InkScapeConnectorsXXX"
            ): Pattern = sheetType match {
    case "InkScapeConnectors" =>
      ConnectedPattern(m, tileType, groupId, offsetX, offsetY)
    case _ =>
      ClonedPattern(m, tileType, groupId, offsetX, offsetY)
  }
}

abstract class Pattern (m:String,
                        tileType: String,
                        groupId: String,
                        offsetX: Int,
                        offsetY: Int) {
  require(offsetX > 0 && offsetY > 0, "invalid patch dimensions")

  protected val lines = Matrix.toMatrixLines(m).get
  protected val rows = lines.length
  protected val cols = lines(0).length
  protected val hXw = s"${rows}x$cols"

  protected val tt = TileType(tileType)
  protected def toX(col: Int): Int = col * 10 + offsetX
  protected def toY(row: Int): Int = row * 10 + offsetY
  protected def toNodeId(row: Int, col: Int): String = s"${groupId}r${row}c$col"
  protected def toColor(row: Int, col: Int): String = {
    val (r,c) = tt.toOriginal(row, col, rows, cols)
    val n = rows * cols + 0f
    val hue = (((r * cols ) + c) % n) / n
    hslToRgb(hue, 1f, 0.4f)
    //f"00${c * (256/cols)}%02X${r * (256/rows)}%02X"
    // see also http://ridiculousfish.com/blog/posts/colors.html
  }

  protected def createTag: String = {
    val options = Array(s"matrix=${lines.mkString("%0D")}", s"tiles=$tileType")
    val url = "https://d-bl.github.io/GroundForge/index.html"
    s"""
       |  <text style='font-family:Arial;font-size:11pt'>
       |   <tspan x='${offsetX + 15}' y='${offsetY - 20}'>$tileType; $hXw; ${lines.mkString(",")}</tspan>
       |   <tspan x='${offsetX + 15}' y='${offsetY - 0}' style='fill:#008;'>
       |    <a xlink:href='$url?${options.mkString("&amp;")}'>pair/thread diagrams</a>
       |   </tspan>
       |  </text>
       |""".stripMargin
  }

  def createNode(row: Int, col: Int) =
    s"""  <path id='${toNodeId(row, col)}'
       |    d='m ${toX(col) + 2},${toY(row)} a 2,2 0 0 1 -2,2 2,2 0 0 1 -2,-2 2,2 0 0 1 2,-2 2,2 0 0 1 2,2 z'
       |    style='fill:#${toColor(row, col)};stroke:none'
       |  />
       |""".stripMargin

  def patch(rows: Int = 22, cols: Int = 22): String
}

private case class ConnectedPattern(m: String, tileType: String, groupId: String, offsetX: Int, offsetY: Int)
  extends Pattern(m, tileType, groupId, offsetX, offsetY) {
  val triedM = for {
    relative <- toRelSrcNodes(matrix = lines.mkString(""), dimensions = hXw)
    checker = tt.toChecker(relative)
    absolute <- toAbsWithMargins(checker, rows, cols)
  } yield absolute


  def patch(rows: Int = 22, cols: Int = 22): String =
    triedM.map(m =>
       s"""<g>
          |$createTag
          |${createDiagram(m)}
          |</g>
          |""".stripMargin
    ).getOrElse("whoops") // TODO improve error message

  def createDiagram(m: M) = {
    def createTwoIn(row: Int, col: Int): String = {
      val srcNodes = m(row)(col)
      def createPath(start: (Int, Int)): String = {
        val (startRow, startCol) = start
        if ( m(startRow)(startCol).isEmpty) "" else
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

private case class ClonedPattern(m: String, tileType: String, groupId: String, offsetX: Int, offsetY: Int)
  extends Pattern(m, tileType, groupId, offsetX, offsetY) {

  val triedM = for {
    relative <- toRelSrcNodes(matrix = lines.mkString(""), dimensions = hXw)
    absolute <- toAbsWithMargins(relative, this.rows, this.cols)
  } yield absolute

  def patch(rows: Int = 22, cols: Int = 22): String =
    triedM.map(m => s"""
                   |$createTag
                   |<g id ="$groupId">
                   |${createDiagram(m)}
                   |</g>
                   |<g>
                   |$clones
                   |</g>
                   |""".stripMargin
    ).getOrElse("whoops") // TODO improve error message

  def createDiagram(m: M) = {
    def createTwoIn(targetRow: Int, targetCol: Int): String = {
      val srcNodes = m(targetRow)(targetCol)
      def createPath(sourceNode: (Int, Int)): String = {
        val (sourceRow, sourceCol) = sourceNode
        (if (m(sourceRow)(sourceCol).isEmpty) {
          val (r, c) = tt.toOriginal(sourceRow, sourceCol, rows, cols)
          createNode(sourceRow, sourceCol)
        } else "") +
        s"""  <path
            |    style='stroke:#000000;fill:none'
            |    d='M ${toX(sourceCol)},${toY(sourceRow)} ${toX (targetCol)},${toY(targetRow)}'
            |  />
            |""".stripMargin
      }
      s"""${createPath(srcNodes(0))}
         |${createPath(srcNodes(1))}""".stripMargin
    }
    m.indices.flatMap(row => m(row).indices.filter(m(row)(_).nonEmpty).flatMap(col => createTwoIn(row, col))).toArray.mkString("") +
    m.indices.flatMap(row => m(row).indices.filter(m(row)(_).nonEmpty).flatMap(col => createNode(row, col))).toArray.mkString("")
  }

  private def clones: String = {
    val brickOffset = if (tileType == "bricks") cols * 5 else 0
    def cloneRows(row1: Int): String = {
      val row2 = row1 + rows * 10 // TODO refactor into TileType
      List.range(start = -(if (tileType == "bricks")brickOffset else 10*cols), end = 200, step = cols * 10).
        map(w => {
          clone(w, row1) + clone(w - brickOffset, row2)
        }).mkString("")
    }
    List.range(start = -rows * 10, end = 200, step = rows * 20)
      .map(h => cloneRows(h)).mkString("")
  }

  private def clone(i: Int, j: Int): String =
    s"""  <use
       |    transform='translate(${i+100},${j+40})'
       |    xlink:href='#$groupId'
       |    style='stroke:#000;fill:none'
       |  />
       |""".stripMargin
}

