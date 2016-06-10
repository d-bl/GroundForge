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

abstract class Settings private(val absM: M,
                                val stitches: Array[Array[String]]
                           ) {
  val nrOfPairLinks = countLinks(absM)
  protected val relRows = stitches.length
  protected val relCols = stitches(0).length
  protected val margin = 2

  /** Gets the tooltip for a stitch: its ID and the symbols specifying the thread movements */
  def getTitle(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toStitchId(row,col)
    s"${stitches(cellRow)(cellCol)} - ${"ABCDEFGHIJKLMNOPQRSTUVWXYZ"(cellCol)}${cellRow+1}"
  }

  /** Recalculates a cell ID from the large matrix back to the original matrix */
  def toStitchId(row: Int, col: Int): (Int, Int)
}

object Settings {

  def apply(str: String,
            bricks: Boolean,
            absRows: Int,
            absCols: Int,
            shiftLeft: Int = 0,
            shiftUp: Int = 0,
            stitches: String = ""
           ): Try[Settings] = {
    val (toSettings, toCheckerBoard) = tileTypes(bricks)
    for {
      relative <- toRelSrcNodes(str)
      checker = toCheckerBoard(relative)
      shifted = shift(checker, shiftLeft, shiftUp)
      absolute <- toAbsWithMargins(shifted, absRows, absCols)
      _ = createFootsides(absolute)
      stitchMatrix = convert(stitches, relative.length, relative(0).length)
    } yield toSettings(absolute, stitchMatrix)
  }

  val toCheckerSettings: (M, Array[Array[String]]) => Settings = (absolute, stitchMatrix) =>
    new Settings(absolute, stitchMatrix) {
      def toStitchId(row: Int, col: Int) =
        ((row - margin + relRows) % relRows, col % relCols)
    }
  val toBrickSettings: (M, Array[Array[String]]) => Settings = (absolute, stitchMatrix) =>
    new Settings(absolute, stitchMatrix) {
      def toStitchId(row: Int, col: Int) = {
        val brickOffset = ((row + margin + relRows) / relRows % 2) * (relCols / 2) + margin
        ((row - margin + relRows) % relRows, (brickOffset + col) % relCols)
      }
    }
  val brickToChecker: (M) => M = (m) => brickWallToCheckerboard(m)
  val checkerToChecker: (M) => M = (m) => identity(m)
  val tileTypes = Map(
    true -> (toBrickSettings, brickToChecker),
    false -> (toCheckerSettings, checkerToChecker)
  )

  private def convert(str: String,
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
