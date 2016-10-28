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

import dibl.Matrix.toValidMatrixLines

import scala.util.Try

object Pattern {

/** Builds an SVG drawing
  *
  * @param tileMatrix See https://github.com/d-bl/GroundForge/blob/master/docs/images/legend.png
  *                   thick arrows indicate vertices traveling two cells
  * @param tileType how the tile is stacked to build a pattern: like a brick wall or a checker board
  * @param groupId the id of the to-be-cloned group of objects
  * @param offsetX relative horizontal position on a sheet
  * @param offsetY relative vertical position on a sheet
  */
  def apply(tileMatrix: String,
            tileType: String,
            groupId: String = "GF0",
            offsetX: Int = 80,
            offsetY: Int = 120
           ): Try[String] = for {
            lines <- toValidMatrixLines(tileMatrix)
           } yield new Pattern(
             tileMatrix,
             tileType,
             groupId,
             offsetX,
             offsetY,
             lines,
             lines.map(_.map(Matrix.charToRelativeTuples).toArray)
           ).createPatch
}

/** Builder of an SVG drawing
  *
  * @param tileMatrix see apply method
  * @param tileType see apply method
  * @param groupId see apply method
  * @param offsetX see apply method
  * @param offsetY see apply method
  * @param lines the tileMatrix split into lines
  * @param relative a converted version of the tileMatrix, e.g.
  *                 '5' which has incoming vertices from the north east and north west
  *                 becomes SrcNodes((-1,-1),(-1, 1))
  */
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
  val needColor: Seq[(Int, Int)] = {

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

  def createNode(row: Int, col: Int): String = {
    val i = needColor.indexOf(tt.toAbsTileIndices(row, col, tileRows, tileCols))
    val color = if (i < 0) "333333" else {
      val hue = (i + 0f) / needColor.size
      val brightness = 0.2f + 0.15f * (i % 3)
      hslToRgb(hue, 1f, brightness)
    }
    s"""    <path
        |      d='m ${toX(col) + 2},${toY(row)} a 2,2 0 0 1 -2,2 2,2 0 0 1 -2,-2 2,2 0 0 1 2,-2 2,2 0 0 1 2,2 z'
        |      style='fill:#$color;stroke:none'
        |    />
        |""".stripMargin
  }

  def createTwoIn(targetRow: Int, targetCol: Int): String =
    (for{
      (relativeSourceRow, relativeSourceCol) <- relative(targetRow)(targetCol)
      sourceRow = relativeSourceRow + targetRow
      sourceCol = relativeSourceCol + targetCol
      needSourceNode = sourceRow < 0 || sourceCol < 0 || sourceRow >= tileRows || sourceCol >= tileCols
    } yield s"""    <path
                |      style='stroke:#000;fill:none'
                |      d='M ${toX(sourceCol)},${toY(sourceRow)} ${toX(targetCol)},${toY(targetRow)}'
                |    />
                |""".stripMargin +
      (if (needSourceNode) createNode(sourceRow, sourceCol) else "")
    ).mkString

  def forAllCells(func: (Int, Int) => String): String =
    (for {
      row <- relative.indices
      col <- relative(row).indices
      if relative(row)(col).nonEmpty
      result <- func(row, col)
    } yield result).mkString

  def clones: String = {
    (for { // TODO somehow refactor computations into TileType
      dY <- List.range(start = 0, end = 270 - tileRows * 10, step = tileRows * 10)
      startX = if (tileType != "bricks" || 0 == dY % (tileRows * 20)) 0 else 5 * tileCols
      dX <- List.range(start = startX, end = 330 - tileCols * 10, step = tileCols * 10)
    } yield s"""    <use
                |      transform='translate($dX,$dY)'
                |      xlink:href='#$groupId'
                |      style='fill-opacity:0.1'
                |    />
                |""".stripMargin).tail.mkString
  }

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
       |  <g transform='translate(15,5)'>
       |$clones
       |  </g>
       |""".stripMargin
}
