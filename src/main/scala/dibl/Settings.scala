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

import dibl.Footsides.createFootsides
import dibl.Matrix._

import scala.util.Try

/** Parameters for the constructor of a [[dibl.PairDiagram]]
  *
  * @param absM A matrix for a patch of lace with repeated tiles. Each cell represents a node in a two-in-two-out directed graph.
  *             A cell contains tuples pointing to other cells for both incoming links and outgoing links for a node.
  * @param stitches A matrix for a single tile with stitch instructions (tcplr) per cell.
  */
abstract class Settings(val absM: M,
                        val stitches: Array[Array[String]],
                        val footside: String = "ttctc"
                       ) {
  val nrOfPairLinks: Array[Array[Int]] = countLinks(absM)
  protected val relRows = stitches.length
  protected val relCols = stitches(0).length
  protected val margin = 2

  /** Gets the tooltip for a stitch: the ID of a cell (a letter for the column, a digit for a row)
    * in a tile and the symbols specifying the stitch instructions.
    */
  def getTitle(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toOriginalPosition(row,col)
    s"${stitches(cellRow)(cellCol)} - ${"ABCDEFGHIJKLMNOPQRSTUVWXYZ"(cellCol)}${cellRow+1}"
  }

  /** Recalculates the position of a cell from the full patch to the tile */
  protected def toOriginalPosition(row: Int, col: Int): (Int, Int)
}

object Settings {

  /** Creates a [[dibl.Settings]] instance.
    *
    * @param str A string with matrix lines. Any character in [[dibl.Matrix.relSourcesMap.keySet]]
    *            is converted to a matrix cell. Any sequence of other characters separates matrix lines.
    * @param bricks A key selecting the [[dibl.TileType]] of the matrix.
    * @param absRows The desired number of rows for the patch of lace.
    * @param absCols The desired number of columns for the patch of lace.
    * @param shiftLeft The number or columns to the tile to the left foot side.
    * @param shiftUp The number of rows to shift the tile up to the top (read to the false foot side).
    * @param stitches Stitch instructions per tile-cell.
    * @param footside Stitch for the footsides
    * @return a [[dibl.Settings]] instance
    */
  def apply(str: String,
            bricks: String,
            absRows: Int,
            absCols: Int,
            shiftLeft: Int = 0,
            shiftUp: Int = 0,
            stitches: String = "",
            footside: String = "ttctc"
           ): Try[Settings] = {
    val tileType = TileType(bricks)
    for {
      lines       <- toValidMatrixLines(str)
      relative     = tileType.toChecker(lines).map(_.map(relSourcesMap).toArray)
      shifted      = shift(relative, shiftLeft, shiftUp)
      absolute    <- toAbsWithMargins(shifted, absRows, absCols)
      _            = createFootsides(absolute)
      stitchMatrix = toStitchMatrix(stitches, lines.length, lines(0).length)
    } yield tileType.toSettings(absolute, stitchMatrix, footside)
  }

  /** Converts a string with stitch instructions into a matrix.
    *
    * @param str Key-value pairs, a key is an ID of a cell in a matrix, separated with an '=' from the value.
    *            A value is a sequence of stitch instructions defaulting to 'ctc' for not mentioned cells.
    * @param rows The number of rows in a tile.
    * @param cols The number of columns in a tile.
    * @return A matrix with stitch instructions.
    */
  private def toStitchMatrix(str: String,
                             rows: Int,
                             cols: Int
                            ): Array[Array[String]] = {
    val result = Array.fill(rows, cols)("ctc")
    str.toLowerCase()
      .split("[^a-z0-9=]+") // split on sequences of delimiting characters (white space, punctuation and anything unknown)
      .map(_.split("=")) // split each component into key value pairs
      .filter(_.length == 2) // omit key=value=something arrays
      .filter(_ (0).matches("[a-z]+[0-9]+")) // the key should be a valid grid id
      .filter(_ (1).matches("[lrctp]+")) // the value should contain valid stitch symbols
      .filter(_ (1).matches(".*c.*")) // a stitch should have at least a cross (2nd thread over 3rd)
      .filter(_ (1).replaceAll("[^p]","").length < 2) // a stitch should have more than a pin
      .foreach { kv =>
        val key = kv(0)
        val col = key(0).toInt - 'a'.toInt
        val row = key(1).toInt - '1'.toInt
        if (row < rows && col < cols)
          result(row)(col) = kv(1)
      }
    result
  }
}
