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

case class Settings private(absM: M,
                            stitches: Array[Array[String]],
                            bricks: Boolean
                           ) {
  val nrOfPairLinks = countLinks(absM)
  private val relRows = stitches.length
  private val relCols = stitches(0).length
  private val margin = 2

  def getTitle(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toCell(row,col)
    s"${stitches(cellRow)(cellCol)} - ${"ABCDEFGHIJKLMNOPQRSTUVWXYZ"(cellCol)}${cellRow+1}"
  }

  private def toCell(row: Int, col: Int): (Int, Int) = {
    val brickOffset = if (!bricks) 0 else ((row + margin + relRows) / relRows % 2) * (relCols / 2) + margin
    ((row - margin + relRows) % relRows, (brickOffset + col) % relCols)
  }
}

object Settings {

  def apply(key: String,
            nr: Int,
            absRows: Int,
            absCols: Int,
            shiftLeft: Int = 0,
            shiftUp: Int = 0,
            stitches: String = ""
           ): Try[Settings] =
    for {
      str <- getMatrix(key, nr)
      relM <- toRelSrcNodes(matrix = str, dimensions = key)
      absM <- toAbs(relM, absRows, absCols, shiftLeft, shiftUp)
    } yield create(relM, absM, stitches, bricks = true)

  def create(str: String,
            bricks: Boolean,
            absRows: Int,
            absCols: Int,
            shiftLeft: Int = 0,
            shiftUp: Int = 0,
            stitches: String = ""
           ): Try[Settings] = {
    for {
        relM <- toRelSrcNodes(str)
        absM <- if (bricks) toAbs(relM, absRows, absCols, shiftLeft, shiftUp)
                else toAbsWithMargins(shift(relM, shiftLeft, shiftUp), absRows, absCols)
      } yield create(relM, absM, stitches, bricks)
    }

  private def create(relM: M, absM: M, stitches: String, bricks: Boolean): Settings =
    new Settings(
      absM,
      stitches = convert(stitches, relM.length, relM(0).length),
      bricks
    )

  private def convert(str: String,
                      rows: Int,
                      cols: Int
                     ): Array[Array[String]] = {
    val result = Array.fill(rows, cols)("ctc")
    str.toLowerCase()
      .split("[^a-z0-9=]+")
      .map(_.split("="))
      .filter(_.length == 2)
      .filter(_ (0).matches("[a-z][0-9]"))
      .filter(_ (1).matches("[lrctp]+"))
      .filter(_ (1).matches(".*c.*"))
      .filter(_ (1).replaceAll("[^p]","").length < 2)
      .foreach { kv =>
        val col = kv(0)(0).toInt - 'a'.toInt
        val row = kv(0)(1).toInt - '1'.toInt
        if (row < rows && col < cols)
          result(row)(col) = kv(1)
      }
    result
  }

  private def toAbs(m: M,
                    absRows: Int,
                    absCols: Int,
                    shiftLeft: Int,
                    shiftUp: Int
                   ): Try[M] =
    toAbsWithMargins(shift(toCheckerboard(m), shiftLeft, shiftUp), absRows, absCols)

  def getMatrix(key: String,
                        nr: Int
                       ): Try[String] = {
    val matrices = matrixMap.get(key)
    if (matrices.isEmpty) Failure(new Exception(s"$key not found"))
    else if (nr < 0 || nr >= matrices.get.length) Failure(new Exception(s"$nr is invalid for $key"))
    else Success(matrices.get(nr))
  }
}
