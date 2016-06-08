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

import java.lang.Math.max

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

object Matrix {

  @tailrec
  private def shiftX[T: ClassTag](m: Array[T], shift: Int): Array[T] =
    if (shift <= 0) m else shiftX(m.tail :+ m.head, shift - 1)

  def shift[T: ClassTag](m: Array[Array[T]], left: Int, up: Int): Array[Array[T]] =
    shiftX(for (r <- m) yield shiftX(r, left), up)

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
  def brickWallToCheckerboard(m: M): M = {
    m ++ m.map{r =>
      val (left,right) = r.splitAt(r.length/2)
      right ++ left
    }
  }

  /** Converts relative source nodes in one matrix into
    * absolute source nodes in a matrix with different dimensions
    * and a margin for loose ends.
    */
  def toAbsWithMargins(rel: M, absRows: Int, absCols: Int): Try[M] =
    if (absRows < 1 || absCols < 1)
      Failure(new IllegalArgumentException(""))
    else Success{
      val abs = Array.fill(absRows + 4, absCols + 4)(SrcNodes())
      val relRows = rel.length
      val relCols = rel(0).length
      for {
        targetRow <- 2 until absRows + 2
        targetCol <- 2 until absCols + 2
      } {
        abs(targetRow)(targetCol) = for ((relRow, relCol) <- rel(targetRow % relRows)(targetCol % relCols))
          yield (max(1,targetRow + relRow), max(1,targetCol + relCol))
      }
      abs
    }

  def maybeSwap(src: Array[(Int, Int)]): Array[(Int, Int)] =
    if (src(0)._2 >= src(1)._2) Array(src(1), src(0))
    else if (src(0)._1 >= src(1)._1) Array(src(1), src(0))
    else src

  def countLinks(m: M): Array[Array[Int]] = {
    val links = Array.fill(m.length,m(0).length)(0)
    for {row <- m.indices
         col <- m(row).indices
        } {
      links(row)(col) += m(row)(col).length
      for {(srcRow,srcCol) <- m(row)(col)} links(srcRow)(srcCol) += 1
    }
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

  /** Translates each character into a relative source node.
    *
    * @param matrix the characters in the string are keys in relSourcesMap
    * @param dimensions a string with at least two sequences of digits,
    *                   s1*s2 should equal the length of the matrix string
    * @return
    */
  def toRelSrcNodes(matrix: String, dimensions: String): Try[M] = {
    dims(dimensions).flatMap { case (rows,cols) =>
      val matrixSize = rows * cols
      if (matrixSize != matrix.length)
        Failure(new IllegalArgumentException(
          s"length of '$matrix' is ${matrix.length} while '$dimensions' asks for $matrixSize"
        ))
      else if (!matrix.matches("[-0-9A-O]+"))
        Failure(new IllegalArgumentException(
          s"'$matrix' is not a valid matrix string"
        ))
      else Success( // we can't run into trouble any more
        matrix.toCharArray.map {
          relSourcesMap.get(_).get
        }.grouped(cols).toArray
      )
    }
  }

  def toRelSrcNodes(str: String): Try[M] = {
    for {
      lines <- toMatrixLines(str)
      dims = s"${lines.length}x${lines(0).length}"
      relM <- toRelSrcNodes(matrix = lines.mkString(""), dimensions = dims)
    } yield relM
  }

  def toMatrixLines(str: String): Try[Array[String]] = {
    val lines = str.split("[^-0-9A-O]+")
    if (lines.map(_.length).sortBy(n => n).distinct.length == 1) Success(lines)
    else Failure(new scala.Exception(s"lines of matrix have varying lengths $str ==> ${lines.mkString(", ")}"))
  }

  /** @param s for example "4x2..."
    * @return (rows,cols) in case of the example: (4,2)
    */
  def dims(s: String): Try[(Int,Int)] =
    if (!s.matches("[0-9]+[^0-9]+[0-9]+.*"))
      Failure(new IllegalArgumentException(
        s"'$s' should contain at least two sequences of digits"
      ))
    else Success {
      val ints = s.split("[^0-9]+").map(_.toInt)
      (ints(0), ints(1))
    }
}