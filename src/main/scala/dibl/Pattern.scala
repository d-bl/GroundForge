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

import dibl.Matrix.{countLinks, toAbsWithMargins, toRelSrcNodes}

object Pattern {
  def apply(tileMatrix: String,
            tileType: String,
            groupId: String,
            offsetX: Int = 80,
            offsetY: Int = 120): String = {
    require(offsetX > 0 && offsetY > 0, "invalid patch dimensions")

    val tt = TileType(tileType)
    val lines = Matrix.toMatrixLines(tileMatrix).get
    val tileRows = lines.length
    val tileCols = lines(0).length
    val hXw = s"${tileRows}x$tileCols"
    val options = Array(s"matrix=${lines.mkString("%0D")}", s"tiles=$tileType")
    val url = "https://d-bl.github.io/GroundForge/index.html"

    def createPatch(m: M) =  s"""
      |  <text style='font-family:Arial;font-size:11pt'>
      |   <tspan x='${offsetX + 15}' y='${offsetY - 20}'>$tileType; $hXw; ${lines.mkString(",")}</tspan>
      |   <tspan x='${offsetX + 15}' y='${offsetY - 0}' style='fill:#008;'>
      |    <a xlink:href='$url?${options.mkString("&amp;")}'>pair/thread diagrams</a>
      |   </tspan>
      |  </text>
      |  <g id ="$groupId">
      |${createDiagram(m)}
      |  </g>
      |  <g>
      |$clones
      |  </g>
      |""".stripMargin

    def createDiagram(m: M) = {
      def createTwoIn(targetRow: Int, targetCol: Int): String =
        m(targetRow)(targetCol).map { sourceNode =>
          val (sourceRow, sourceCol) = sourceNode
          s"""    <path
             |      style='stroke:#000;fill:none'
             |      d='M ${toX(sourceCol)},${toY(sourceRow)} ${toX (targetCol)},${toY(targetRow)}'
             |    />
             |""".stripMargin + (
            if (m(sourceRow)(sourceCol).nonEmpty) ""
            else createNode(sourceRow, sourceCol))
        }.mkString("")
      m.indices.flatMap(row => m(row).indices.filter(m(row)(_).nonEmpty).flatMap(col => createTwoIn(row, col))).toArray.mkString("") +
      m.indices.flatMap(row => m(row).indices.filter(m(row)(_).nonEmpty).flatMap(col => createNode(row, col))).toArray.mkString("")
    }

    def toX(col: Int): Int = col * 10 + offsetX
    def toY(row: Int): Int = row * 10 + offsetY
    def toColor(row: Int, col: Int): String = {
      val (r,c) = tt.toTileIndices(row, col, tileRows, tileCols)
      val n = tileRows * tileCols + 0f
      val i = ((r * tileCols) + c) % n
      val hue = i / n
      val brightness = 0.2f + 0.15f * (i %3)
      hslToRgb(hue, 1f, brightness)  }

    def createNode(row: Int, col: Int) =
       s"""    <path
          |      d='m ${toX(col) + 2},${toY(row)} a 2,2 0 0 1 -2,2 2,2 0 0 1 -2,-2 2,2 0 0 1 2,-2 2,2 0 0 1 2,2 z'
          |      style='fill:#${toColor(row, col)};stroke:none'
          |    />
          |""".stripMargin

    def clones: String = {
      val brickOffset = if (tileType == "bricks") tileCols * 5 else 0
      def cloneRows(row1: Int): String = {
        val row2 = row1 + tileRows * 10 // TODO refactor into TileType
        List.range(start = -(if (tileType == "bricks")brickOffset else 10*tileCols), end = 200, step = tileCols * 10).
          map(w => {
            clone(w, row1) + clone(w - brickOffset, row2)
          }).mkString("")
      }
      List.range(start = -tileRows * 10, end = 200, step = tileRows * 20)
        .map(h => cloneRows(h)).mkString("")
    }

    def clone(i: Int, j: Int): String =
      s"""    <use
         |      transform='translate(${i+100},${j+40})'
         |      xlink:href='#$groupId'
         |      style='stroke:#000;fill:none'
         |    />
         |""".stripMargin

    def stripMargins(m: M) = countLinks(m).slice(2, 2 + tileRows).map(_.slice(2, 2 + tileCols))
    def toNeedColor(c: Int): String = if (c % 4 == 0) "#000" else "#888"

    val triedSVG = for {
      relative <- toRelSrcNodes(matrix = lines.mkString(""), dimensions = hXw)
      m <- toAbsWithMargins(relative, tileRows, tileCols)
      c = stripMargins(m)
      //_ = println(c.toList.toArray.deep.mkString("\n") + "\n")
      colors = c.indices.map(row => c(row).indices.map(col => toNeedColor(c(row)(col))))
      //_ = println(x.toList.toArray.deep.mkString("\n") + "\n-------------")
      svg = createPatch(m)
    } yield svg

    triedSVG.getOrElse("<text><tspan>whoops</tspan></text>") // TODO improve error message
  }
}


