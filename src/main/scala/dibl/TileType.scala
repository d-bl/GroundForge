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

abstract class TileType {
  def toSettings(m: M, stitchMatrix: Array[Array[String]]): Settings
  // TODO add method(s) for Pattern class

  def toChecker(m: M): M
}

object TileType {
  def apply(key: String) = Map(
    "bricks" -> Brick
  ).getOrElse(key,Checker)
}

object Checker extends TileType {
  def toSettings(m: M, stitchMatrix: Array[Array[String]]): Settings =
    new Settings(m, stitchMatrix) {
      def toOriginalPosition(row: Int, col: Int) =
        ((row - margin + relRows) % relRows, col % relCols)
    }

  def toChecker(m: M): M = m
}

object Brick extends TileType {
  def toSettings(m: M, stitchMatrix: Array[Array[String]]): Settings =
    new Settings(m, stitchMatrix) {
      def toOriginalPosition(row: Int, col: Int) = {
        val brickOffset = ((row + margin + relRows) / relRows % 2) * (relCols / 2) + margin
        ((row - margin + relRows) % relRows, (brickOffset + col) % relCols)
      }
    }

  /** Creates a checkerboard-matrix from a brick-matrix by
    * adding two half bricks to the bottom of the brick-matrix.
    * In ascii-art:
    * <pre>
    * +-------+
    * | a   b |
    * | c   d |
    * +---+---+
    *   b | a
    *   d | c
    * +---+---+
    * </pre>
    */
  def toChecker(m: M): M = {
    m ++ m.map { r =>
      val (left, right) = r.splitAt(r.length / 2)
      right ++ left
    }
  }
}

