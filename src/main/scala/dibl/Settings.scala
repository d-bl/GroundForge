/*
 Copyright 2015 Jo Pol
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

import dibl.Matrix._

import scala.util.{Failure, Success, Try}

/** Parameters for the constructor of a [[dibl.PairDiagram]]
  *
  * @param absM A matrix for a patch of lace with repeated tiles. Each cell represents a node in a two-in-two-out directed graph.
  *             A cell contains absolute tuples pointing to other cells for both incoming links and outgoing links for a node.
  * @param stitches A matrix for a single tile with stitch instructions (tcplr) per cell.
  */
abstract class Settings(val absM: M,
                        private val stitches: Seq[Seq[String]],
                        private val colors: Seq[Seq[String]]
                       ) {
  val nrOfPairLinks: Array[Array[Int]] = countLinks(absM)
  protected val relRows: Int = stitches.length
  protected val relCols: Int = stitches.head.length
  protected val margin = 2

  /** Gets the tooltip for a stitch: the ID of a cell (a letter for the column, a digit for a row)
    * in a tile and the symbols specifying the stitch instructions.
    */
  def getTitle(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toOriginalPosition(row,col)
    s"${stitches(cellRow)(cellCol)} - ${Stitches.toID(cellRow, cellCol)}"
  }

  def getStitch(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toOriginalPosition(row,col)
    stitches(cellRow)(cellCol).replace("t", "lr")
  }

  def getColor(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toOriginalPosition(row,col)
    colors(cellRow)(cellCol)
  }

  /** Recalculates the position of a cell from the full patch to the tile */
  protected def toOriginalPosition(row: Int, col: Int): Cell
}

object Settings {

  /** Creates a [[dibl.Settings]] instance.
    *
    * @param str A string with matrix lines. Any character in [[dibl.Matrix.charToRelativeTuples.keySet]]
    *            is converted to a matrix cell. Any sequence of other characters separates matrix lines.
    * @param bricks A key of [[dibl.TileType.stringToType]] for the matrix.
    * @param absRows The desired number of rows for the patch of lace.
    * @param absCols The desired number of columns for the patch of lace.
    * @param shiftLeft The number or columns to the tile to the left foot side.
    * @param shiftUp The number of rows to shift the tile up to the top (read to the false foot side).
    * @param stitches Stitch instructions per tile-cell.
    * @return a [[dibl.Settings]] instance
    */
  def apply(str: String,
            bricks: String,
            absRows: Int,
            absCols: Int,
            shiftLeft: Int = 0,
            shiftUp: Int = 0,
            stitches: String = ""
           ): Try[Settings] = {

    val legalArguments = absCols > 1 && absRows > 1 && shiftLeft >= 0 && shiftUp >= 0
    for {
      _           <- if (legalArguments) Success(Unit) else Failure(new IllegalArgumentException())
      lines       <- toValidMatrixLines(str)
      tileType     = TileType(bricks)
      checker      = tileType.toChecker(lines)
      // shift +2 mimics previous margin of extended matrix to prevent changing link results
      shifted      = shift(checker, shiftUp + 2).map(shiftChars(_, shiftLeft + 2))
      relative     = extend(shifted, absRows, absCols).map(_.map(charToRelativeTuples).toArray)
      absolute     = toAbsolute(relative)
      stitchesObject  = new Stitches(stitches)
      stitchMatrix = stitchesObject.instructions(lines.length, lines(0).length)
      colorMatrix = stitchesObject.instructions(lines.length, lines(0).length)
    } yield tileType.toSettings(absolute, stitchMatrix, colorMatrix)
  }
}
