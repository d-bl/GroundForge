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
package dibl.sheet

import dibl.Matrix.toValidMatrixLines
import dibl.{ M, Matrix, hslToRgb }

import scala.util.Try

object Pattern {

/** Builds an SVG drawing
  *
  * @param tileMatrix See https://github.com/d-bl/GroundForge/blob/62f22ec/docs/images/legend.png
  *                   thick arrows indicate vertices traveling two cells
  * @param tileType how the tile is stacked to build a pattern: like a brick wall or a checker board
  * @param groupId the id of the to-be-cloned group of objects
  */
  def apply(tileMatrix: String,
            tileType: String,
            groupId: String = "GF0"
           ): Try[String] = for {
            lines <- toValidMatrixLines(tileMatrix)
           } yield new Pattern(
             tileMatrix,
             tileType,
             groupId,
             lines,
             lines.map(_.map(Matrix.toRelativeSources).toArray)
           ).createPatch
}

/** Builder of an SVG drawing
  *
  * @param tileMatrix see apply method
  * @param tileType see apply method
  * @param groupId see apply method
  * @param lines the tileMatrix split into lines
  * @param relative a converted version of the tileMatrix, e.g.
  *                 '5' which has incoming vertices from the north east and north west
  *                 becomes SrcNodes((-1,-1),(-1, 1))
  */
private class Pattern (tileMatrix: String,
                       tileType: String,
                       groupId: String = "GF0",
                       lines: Array[String],
                       relative: M
                      ){

  private val tt = TileType(tileType)
  private val tileRows = lines.length
  private val tileCols = lines(0).length
  private def toX(col: Int): Int = col * 10
  private def toY(row: Int): Int = row * 10

  /**
    * Cells of the matrix for which the dot should get a color.
    * The position of the cell in the sequence becomes the position
    * of the color (hue) in the rainbow.
    *
    * Each cell should have two vertices coming in, and two out.
    * Think the matrix wrapped around a donut, thus cells at one
    * side connect with cells on the other side of the matrix.
    * To indicate which cells connect in that way, they get the same color.
    * Cells in the middle (having 4 vertices without wrapping) become grey.
    */
  private val needColor: Seq[(Int, Int)] = {

    val linkCount = Array.fill(tileRows, tileCols)(0)
    for {
      row <- relative.indices
      col <- relative(row).indices
      (relSrcRow, relSrcCol) <- relative(row)(col) // zero or two elements
    }{
      val absSrcRow = row + relSrcRow
      val absSrcCol = col + relSrcCol
      val srcRowInMatric = absSrcRow >= 0 && absSrcRow < tileRows
      val srcColInMatrix = absSrcCol >= 0 && absSrcCol < tileCols
      val inMatrix = srcRowInMatric && srcColInMatrix
      if (inMatrix) linkCount(absSrcRow)(absSrcCol) += 1
      linkCount(row)(col) += 1
    }
    for {
      row <- relative.indices
      col <- relative(row).indices
      if linkCount(row)(col) % 4 > 0 // zero nor four
    } yield (row, col)
  }

  private def createNode(row: Int, col: Int): String = {
    val i = needColor.indexOf(tt.toAbsTileIndices(row, col, tileRows, tileCols))
    val color = if (i < 0) "333333" else {
      val hue = (i + 0f) / needColor.size
      val brightness = 0.2f + 0.15f * (i % 3)
      hslToRgb(hue, 1f, brightness)
    }
    s"""    <path
        |      d='m ${toX(col) + 2},${toY(row)} a 2,2 0 0 1 -2,2 2,2 0 0 1 -2,-2 2,2 0 0 1 2,-2 2,2 0 0 1 2,2 z'
        |      style='fill:#$color;stroke:none'
        |    ></path>
        |""".stripMargin
  }

  private def createTwoIn(targetRow: Int, targetCol: Int): String =
    (for{
      (relativeSourceRow, relativeSourceCol) <- relative(targetRow)(targetCol)
      sourceRow = relativeSourceRow + targetRow
      sourceCol = relativeSourceCol + targetCol
      needSourceNode = sourceRow < 0 || sourceCol < 0 || sourceRow >= tileRows || sourceCol >= tileCols
    } yield s"""    <path
                |      style='stroke:#000;fill:none'
                |      d='M ${toX(sourceCol)},${toY(sourceRow)} ${toX(targetCol)},${toY(targetRow)}'
                |    ></path>
                |""".stripMargin +
      (if (needSourceNode) createNode(sourceRow, sourceCol) else "")
    ).mkString

  private def forAllCells(func: (Int, Int) => String): String =
    (for {
      row <- relative.indices
      col <- relative(row).indices
      if relative(row)(col).nonEmpty
      result <- func(row, col)
    } yield result).mkString

  private def clones: String = {
    (for { // TODO somehow refactor computations into TileType
      dY <- List.range(start = 0, end = 270 - tileRows * 10, step = tileRows * 10)
      startX = if (tileType != "bricks" || 0 == dY % (tileRows * 20)) 0 else 5 * tileCols
      dX <- List.range(start = startX, end = 330 - tileCols * 10, step = tileCols * 10)
    } yield s"""    <use
                |      transform='translate($dX,$dY)'
                |      xlink:href='#$groupId'
                |      style='fill-opacity:0.1'
                |    ></use>
                |""".stripMargin).tail.mkString
  }

  private val options = Array(s"matrix=${lines.mkString("%0D")}", s"tiles=$tileType")
  private val dims = s"patchHeight=${3*tileRows}&patchWidth=${3*tileCols}"
  private val shifts = if (tileType == "checker")
                         s"shiftColsSW=0&shiftRowsSW=$tileRows&shiftColsSE=$tileCols&shiftRowsSE=$tileRows"
                       else
                         s"shiftColsSW=-${tileCols/2}&shiftRowsSW=$tileRows&shiftColsSE=${tileCols/2}&shiftRowsSE=$tileRows"
  private val url = "https://d-bl.github.io/GroundForge/tiles.html"
  private def createPatch: String =
    s"""
       |  <text style='font-family:Arial;font-size:11pt'>
       |   <tspan x='15' y='-20'>$tileType; ${tileRows}x$tileCols; ${lines.mkString(",")}</tspan>
       |   <tspan x='15' y='-40' style='fill:#008;'>
       |    <a xlink:href='$url?tile=${lines.mkString(",")}&$dims&$shifts'>pair/thread diagrams</a>
       |   </tspan>
       |  </text>
       |  <g id ='$groupId'>
       |${forAllCells(createTwoIn)}
       |${forAllCells(createNode)}
       |  </g>
       |  <g transform='translate(15,5)'>
       |$clones
       |  </g>
       |""".stripMargin
}
