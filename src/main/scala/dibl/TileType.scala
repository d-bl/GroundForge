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
  def toSettings(m: M, stitchMatrix: Array[Array[String]], footside: String): Settings
  // TODO add method(s) for Pattern class

  def toChecker (lines: Array[String]): Array[String]

  /** @param row relative row number in the generated patch
    * @param col relative col number in the generated patch
    * @param tileRows height of the tile matrix that defines the pattern
    * @param tileCols width of the tile matrix that defines the pattern
    * @return reduced values for (row,col)
    */
  def toAbsTileIndices(row: Int, col: Int, tileRows: Int, tileCols: Int): (Int, Int)
}

object TileType {
  def apply(key: String) = Map(
    "bricks" -> Brick
  ).getOrElse(key,Checker)
}

object Checker extends TileType {
  def toSettings(m: M, stitchMatrix: Array[Array[String]], footside: String): Settings =
    new Settings(m, stitchMatrix, footside) {
      def toOriginalPosition(row: Int, col: Int) =
        ((row - margin + relRows) % relRows, col % relCols)
    }

  def toChecker(lines: Array[String]): Array[String] = lines

  def toAbsTileIndices(row: Int, col: Int, tileRows: Int, tileCols: Int): (Int, Int) = {
    ((row + tileRows) % tileRows, (col + tileCols) % tileCols)
  }

}

object Brick extends TileType {
  def toSettings(m: M, stitchMatrix: Array[Array[String]], footside: String): Settings =
    new Settings(m, stitchMatrix, footside) {
      def toOriginalPosition(row: Int, col: Int) = {
        val brickOffset = ((row + margin + relRows) / relRows % 2) * (relCols / 2) + margin
        ((row - margin + relRows) % relRows, (brickOffset + col) % relCols)
      }
    }

  def toAbsTileIndices(row: Int, col: Int, tileRows: Int, tileCols: Int): (Int, Int) = {
    val cell@(r,c) = Checker.toAbsTileIndices(row, col, tileRows, tileCols)
    if (row >= 0) cell else (r, (c + tileCols / 2) % tileCols )
  }

  /** Creates a checkerboard-matrix from a brick-matrix by
    * adding two half bricks to the bottom of the brick-matrix.
    *
    * @param lines having all the same length.
    *              <pre>
    *              +------+
    *              | a  b |
    *              | c  d |
    *              +------+
    *              </pre>
    * @return <pre>
    *         +-------+
    *         | a   b |
    *         | c   d |
    *         +---+---+
    *           b | a
    *           d | c
    *         +---+---+
    *         </pre>
    */
  def toChecker (lines: Array[String]): Array[String] = {
    val n = lines(0).length / 2
    lines ++ lines.map(line => line.drop(n) ++ line.take(n))
  }
}

