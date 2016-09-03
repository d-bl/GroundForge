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
            groupId: String = "GF0",
            offsetX: Int = 80,
            offsetY: Int = 120
           ): String = {

    val tt = TileType(tileType)
    val lines = Matrix.toMatrixLines(tileMatrix).get // TODO fix exception
    val tileRows = lines.length
    val tileCols = lines(0).length
    val hXw = s"${tileRows}x$tileCols"
    val options = Array(s"matrix=${lines.mkString("%0D")}", s"tiles=$tileType")
    val url = "https://d-bl.github.io/GroundForge/index.html"

    def createPatch(m: M) =
      s"""
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

      val needColor: Seq[(Int, Int)] = {
        val c = stripMargins(m)
        //println(c.toList.toArray.deep.mkString("\n") + "\n")
        c.indices.flatMap(row => c(row).indices.map(col => (row, col, c(row)(col) % 4 > 0)))
          .filter(t => t._3).map(t => (t._1, t._2))
      }
      //println(needColor.mkString(" ") + "\n------------- " + needColor.indexOf((2,3)))

      def toX(col: Int): Int = col * 10 + offsetX
      def toY(row: Int): Int = row * 10 + offsetY
      def toColor(row: Int, col: Int): String = {
        val cell@(r,c) = tt.toTileIndices(row, col, tileRows, tileCols)
        val i = needColor.indexOf(cell) + 0f
        if (i < 0) "999999" else {
          val hue = i / needColor.size
          //println(s"hue=$hue i=$i row=$row,$r col=$col,$c i=$i n=${ needColor.size}")
          val brightness = 0.2f + 0.15f * (i % 3)
          hslToRgb(hue, 1f, brightness)
        }
      }

      def createNode(row: Int, col: Int) =
        s"""    <path
           |      d='m ${toX(col) + 2},${toY(row)} a 2,2 0 0 1 -2,2 2,2 0 0 1 -2,-2 2,2 0 0 1 2,-2 2,2 0 0 1 2,2 z'
           |      style='fill:#${toColor(row, col)};fill-opacity:0.85;stroke:none'
           |    />
           |""".stripMargin

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

    val triedSVG = for {
      relative <- toRelSrcNodes(matrix = lines.mkString(""), dimensions = hXw)
      m <- toAbsWithMargins(relative, tileRows, tileCols)
      svg = createPatch(m)
    } yield svg

    triedSVG.getOrElse(s"<text><tspan>${triedSVG.failed.get.getMessage}</tspan></text>")
  }
}


