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

import dibl.Matrix.{toMatrixLines, toRelSrcNodes}

import scala.util.Try

object Pattern {

  def failureMessage(tried: Try[_]): String =
    s"<text><tspan x='2' y='14'>${tried.failed.get.getMessage}</tspan></text>"

  def apply(tileMatrix: String,
            tileType: String,
            groupId: String = "GF0",
            offsetX: Int = 80,
            offsetY: Int = 120
           ): String = {

    val triedSVG = for {
      lines <- toMatrixLines(tileMatrix)
      relative <- toRelSrcNodes(tileMatrix)
    } yield new Pattern(
      tileMatrix,
      tileType,
      groupId,
      offsetX,
      offsetY,
      lines,
      relative
    ).createPatch

    triedSVG.getOrElse(failureMessage(triedSVG))
  }
}

private class Pattern (tileMatrix: String,
                       tileType: String,
                       groupId: String = "GF0",
                       offsetX: Int = 80,
                       offsetY: Int = 120,
                       lines: Array[String],
                       relative: M
                      ){

  val tt = TileType(tileType)
  val tileRows = lines.length
  val tileCols = lines(0).length
  def toX(col: Int): Int = col * 10 + offsetX
  def toY(row: Int): Int = row * 10 + offsetY

  val needColor: Seq[(Int, Int)] = {

    val linkCount = Array.fill(tileRows, tileCols)(0)
    relative.indices.foreach(row =>
      relative(row).indices.foreach(col =>
        relative(row)(col).foreach { srcNode =>
          val (srcRow, srcCol) = srcNode
          val r = row + srcRow
          val c = col + srcCol
          if (r >= 0 && r < tileRows && c >= 0 && c < tileCols)
            linkCount(r)(c) += 1
          linkCount(row)(col) += 1
        }
      )
    )
    linkCount.indices.flatMap(row => linkCount(row).indices.map(col => (row, col))).
      filter(t => {
        val (r,c) = t
        linkCount(r)(c) % 4 > 0
      })
  }

  def toColor(row: Int, col: Int): String = {
    val cell = tt.toAbsTileIndices(row, col, tileRows, tileCols)
    val i = needColor.indexOf(cell)
    if (i < 0) "999999" else {
      val hue = (i + 0f) / needColor.size
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
    relative(targetRow)(targetCol).map { case (relativeSourceRow, relativeSourceCol) =>
      val sourceRow = relativeSourceRow + targetRow
      val sourceCol = relativeSourceCol + targetCol
      val needSourceNode = sourceRow < 0 || sourceCol < 0 || sourceRow >= tileRows || sourceCol >= tileCols
      s"""    <path
          |      style='stroke:#000;fill:none'
          |      d='M ${toX(sourceCol)},${toY(sourceRow)} ${toX(targetCol)},${toY(targetRow)}'
          |    />
          |""".stripMargin +
        (if (needSourceNode) createNode(sourceRow, sourceCol) else "")
    }.mkString

  def forAllCells(func: (Int, Int) => String): String =
    (for {
      row <- relative.indices
      col <- relative(row).indices
      if relative(row)(col).nonEmpty
      result <- func(row, col)
    } yield result).mkString

  def clones: String = {
    val brickOffset = if (tileType == "bricks") tileCols * 5 else 0 // TODO refactor into TileType
    val startX = if (brickOffset > 0) brickOffset else 10 * tileCols
    (for {
      dY <- List.range(start = -tileRows * 10, end = 200, step = tileRows * 20)
      dX <- List.range(start = -startX, end = 200, step = tileCols * 10)
      result <- clone(dX, dY) + clone(dX - brickOffset, dY + tileRows * 10)
    } yield result).mkString
  }

  def clone(dX: Int, dY: Int): String =
    s"""    <use
        |      transform='translate(${dX+95},${dY+45})'
        |      xlink:href='#$groupId'
        |      style='stroke:#000;fill:none'
        |    />
        |""".stripMargin

  val options = Array(s"matrix=${lines.mkString("%0D")}", s"tiles=$tileType")
  val url = "https://d-bl.github.io/GroundForge/index.html"
  def createPatch =
    s"""
       |  <text style='font-family:Arial;font-size:11pt'>
       |   <tspan x='${offsetX - 15}' y='${offsetY - 50}'>$tileType; ${tileRows}x$tileCols; ${lines.mkString(",")}</tspan>
       |   <tspan x='${offsetX - 15}' y='${offsetY - 30}' style='fill:#008;'>
       |    <a xlink:href='$url?${options.mkString("&amp;")}'>pair/thread diagrams</a>
       |   </tspan>
       |  </text>
       |  <g id ="$groupId">
       |${forAllCells(createTwoIn)}
       |${forAllCells(createNode)}
       |  </g>
       |  <g>
       |$clones
       |  </g>
       |""".stripMargin
}
