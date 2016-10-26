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

import java.lang.Math._

import scala.collection.immutable.HashMap
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object Matrix {

  /** @param lines for example: <pre>
    *              ab
    *              cd
    *              </pre>
    * @return <pre>
    *         --------
    *         --------
    *         --abab--
    *         --cdcd--
    *         --abab--
    *         --cdcd--
    *         --------
    *         --------
    *         </pre>
    */
  def extend(lines: Array[String], absRows: Int, absCols: Int): Array[String] = {
    def repeatRows(rows: Array[String]): Array[String] =
      Array.fill((absRows + rows.length) / rows.length)(rows).flatten.take(absRows)
    def repeatCols(row: String): String =
      (row * ((absCols + row.length) / row.length)).take(absCols)
    val marginRows = Array.fill(2)("-" * (absCols + 4))
    marginRows ++ repeatRows(lines.map("--" + repeatCols(_) + "--")) ++ marginRows
  }

  def shift[T: ClassTag](xs: Array[T], n: Int): Array[T] = {
    val modN = n % xs.length
    xs.takeRight(xs.length - modN) ++ xs.take(modN)
  }

  def shiftChars(xs: String, n: Int): String = {
    val modN = n % xs.length
    xs.takeRight(xs.length - modN) ++ xs.take(modN)
  }

  /** Converts a matrix with multiple tuples per cell pointing to source cells
    *
    * @param m tuples have relative positions
    * @return tuples have absolute position
    */
  def toAbsolute(m: M): M = {
    Array.tabulate(m.length)(targetRow =>
      Array.tabulate(m(0).length)(targetCol =>
        for ((relSrcRow, relSrcCol) <- m(targetRow)(targetCol))
          // not allowing zero helps creating footsides, should be done there
          yield (max(1, targetRow + relSrcRow), max(1, targetCol + relSrcCol))
      )
    )
  }

  /** Counts the numbers of links arriving at or leaving from a cell
    *
    * @param m tuples have absolute positions
    * @return a matrix with the same dimensions as m
    */
  def countLinks(m: M): Array[Array[Int]] = {
    val links = Array.fill(m.length,m(0).length)(0)
    for {
      row <- m.indices
      col <- m(row).indices
      _  = links(row)(col) += m(row)(col).length
      (srcRow,srcCol) <- m(row)(col)
      _ = links(srcRow)(srcCol) += 1
    } {}
    links
  }

  /** Translates a character in a matrix string into relative links with two source nodes.
    * The source nodes are defined with relative (row,column) numbers.
    * A node can be connected in eight directions, but source nodes are not found downwards.
    */
  val relSourcesMap: HashMap[Char,SrcNodes] = HashMap (
                                      // ascii art of incoming links for a node
    '0' -> SrcNodes((-1, 1),( 0, 1)), // .../_
    '1' -> SrcNodes((-1, 0),( 0, 1)), // ..|._
    '2' -> SrcNodes((-1,-1),( 0, 1)), // .\.._
    '3' -> SrcNodes(( 0,-1),( 0, 1)), // _..._
    '4' -> SrcNodes((-1, 0),(-1, 1)), // ..|/.
    '5' -> SrcNodes((-1,-1),(-1, 1)), // .\./.
    '6' -> SrcNodes(( 0,-1),(-1, 1)), // _../.
    '7' -> SrcNodes((-1,-1),(-1, 0)), // .\|..
    '8' -> SrcNodes(( 0,-1),(-1, 0)), // _.|..
    '9' -> SrcNodes(( 0,-1),(-1,-1)), // _\...
    // double length for vertical link only
    'A' -> SrcNodes((-2, 0),( 0, 1)), // ..|._
    'B' -> SrcNodes((-2, 0),(-1, 1)), // ..|/.
    'C' -> SrcNodes((-1,-1),(-2, 0)), // .\|..
    'D' -> SrcNodes(( 0,-1),(-2, 0)), // _.|..
    // double length for horizontal links too
    'E' -> SrcNodes((-1, 1),( 0, 2)), // .../_
    'F' -> SrcNodes((-1, 0),( 0, 2)), // ..|._
    'G' -> SrcNodes((-2, 0),( 0, 2)), // ..|._
    'H' -> SrcNodes((-1,-1),( 0, 2)), // .\.._
    'I' -> SrcNodes(( 0,-1),( 0, 2)), // _..._
    'J' -> SrcNodes(( 0,-2),( 0, 1)), // _..._
    'K' -> SrcNodes(( 0,-2),( 0, 2)), // _..._
    'L' -> SrcNodes(( 0,-2),(-1, 1)), // _../.
    'M' -> SrcNodes(( 0,-2),(-1, 0)), // _.|..
    'N' -> SrcNodes(( 0,-2),(-2, 0)), // _.|..
    'O' -> SrcNodes(( 0,-2),(-1,-1)), // _\...
    '-' -> SrcNodes()                 // not used node
  )

  /** Matches any sequence of characters that are not a key of [[relSourcesMap]] */
  val separator: String = "[^-0-9A-O]+"

  /** Split on sequences of characters that are not a key of [[relSourcesMap]].
    *
    * @param str compact matrix specifying a 2-in-2out-directed graph
    * @return Failure if resulting lines do not have equal length,
    *         or are longer than 26 as stitch ID's tag columns with a single capital letter.
    */
  def toValidMatrixLines(str: String): Try[Array[String]] = {
    val lines = str.split(separator)
    if (lines.map(_.length).sortBy(n => n).distinct.length != 1)
      Failure(new scala.Exception(s"Matrix lines have varying lengths: $str ==> ${lines.mkString(", ")}"))
    else if (lines(0).length > 26)
      Failure(new scala.Exception(s"Matrix lines exceeds maximum length of 26: $str ==> ${lines.mkString(", ")}"))
    else Success(lines)
  }
}