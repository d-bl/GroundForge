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
  def toCheckerboard(m: M): M = {
    m ++ m.map{r =>
      val (left,right) = r.splitAt(r.length/2)
      right ++ left
    }
  }

  /** Converts relative source nodes in one matrix into
    * absolute source nodes in a matrix wih different dimensions
    * and a margin for loose ends.
    * <pre>
    * ::v v::
    * > o o <
    * > o o <
    * ::^ ^::
    * </pre>
    * In above ascii art each "o" represent a node for a stitch, matching an element in the matrix.
    * The 'v' symbols represent zero to two incoming pairs, the '^' symbols zero to two outgoing pairs,
    * The '<' and '>' zero to three incoming and/or outgoing pairs.
    */
  def toAbsWithMargins(rel: M, absRows: Int, absCols: Int): Try[M] =
    if (absRows < 1 || absCols < 1)
      Failure(new IllegalArgumentException(""))
    else Success({
      val abs = Array.fill(absRows + 4, absCols + 4)(SrcNodes())
      val relRows = rel.length
      val relCols = rel(0).length

      def dropLinksInMargin(row: Int, targetCol: Int, sourceCol: Int): Unit = {
        val right = abs(row)(targetCol)
        if (right.length > 1)
          abs(row)(targetCol) = (right(0)._2, right(1)._2) match {
            case (`sourceCol`, `sourceCol`) => Array[(Int, Int)]()
            case (`sourceCol`, _) => Array(right(1))
            case (_, `sourceCol`) => Array(right(0))
            case _ => right
          }
      }
      def addBobbins(): Unit = {
        for {col <- 2 until absCols + 2} if (abs(absRows + 1)(col).length == 2) {
          val toLeft = {
            val left = abs(absRows + 1)(col - 1)
            if (left.length != 2) false
            else {
              val (r, c) = left(1)
              r == absRows + 1 && c == col
            }
          }
          val toRight = {
            val right = abs(absRows + 1)(col + 1)
            if (right.length != 2) false
            else {
              val (r, c) = right(0)
              r == absRows + 1 && c == col
            }
          }
          (toRight, toLeft) match {
            case (false, false) => ()
              abs(absRows + 2)(col) = Array((absRows + 1, col))
              abs(absRows + 3)(col) = Array((absRows + 1, col))
            case (true, _) =>
              abs(absRows + 2)(col) = Array((absRows + 1, col))
            case (_, true) =>
              abs(absRows + 2)(col) = Array((absRows + 1, col))
            case _ => ()
          }
        }
      }

      // assign incoming links for the 'o'-s in the ascii art representation
      for {
        absRow <- 2 until absRows + 2
        absCol <- 2 until absCols + 2
      } {
        abs(absRow)(absCol) = for ((relRow, relCol) <- rel(absRow % relRows)(absCol % relCols))
          yield (absRow + relRow, absCol + relCol)
      }

      // footside: assign the '>'-s and '<'-s of the ascii art representation (simple case only yet)
      for {row <- 2 until absRows + 2} {
        // don't want to be bothered by links coming with different directions out of the margin
        dropLinksInMargin(row, 2, 1)
        dropLinksInMargin(row, absCols + 1, absCols + 2)
      }
      val nrOfLinks = countLinks(abs)
      def addFootside(targetCol: Int, sourceCol: Int) = {
        for {row <- 2 until absRows + 2} {
          (nrOfLinks(row)(targetCol), abs(row)(targetCol).length) match {
            case (2, 1) =>
              abs(row)(sourceCol) = Array((row - 1, sourceCol))
              abs(row)(targetCol) = abs(row)(targetCol) ++ Array((row, sourceCol), (row - 1, sourceCol))
            case _ => () // TODO the more complex cases
          }
        }
      }
      addFootside(2, 1)
      addFootside(absCols + 1, absCols + 2)

      // The 'v'-s and '^'-s in the ascii art representation may shake hands
      // move the the legs into one column but separate the ends in different rows
      for {col <- 0 until absCols + 2} if (abs(2)(col).length == 2) {
        val (leftSrcRow, leftSrcCol) = abs(2)(col)(0)
        val (rightSrcRow, rightSrcCol) = abs(2)(col)(1)
        (leftSrcRow,rightSrcRow) match {
          case (2,2) => ()
          case (2,_) => () //abs(2)(col) = Array((leftSrcRow, leftSrcCol), (1, col))
          case (_,2) => () //abs(2)(col) = Array((0, col), (rightSrcRow, rightSrcCol))
          case _ => abs(2)(col) = Array((0, col), (1, col))
        }
      }
      addBobbins()
      abs
    })

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
  /** Each string represents a matrix from: http://web.uvic.ca/~vmi/papers/interleavedpatterns.html
    * Each cell (alias character) represents a target node in a two-in-two-out directed graph, see relSourcesMap.
    */
  val matrixMap: HashMap[String,Array[String]] = HashMap (
    "2x4 torchon" -> Array[String]("53535353","566-66-5","5-5--5-5","44447777","66666666"),
    "2x4 bias" -> Array[String]("-4866-48","48154-77","4804-777"),
    "2x4 paris/kat" -> Array[String]("6868-4-4"),
    "2x4 cloth" -> Array[String]("88881111","66662222"),
    "2x4 vrouwkens" -> Array[String]("43126-78"), // mirrored version: mannekens
    "2x4 small snowflakes" -> Array[String]("43735-53","563234-7"),
    "2x4 rose ground" -> Array[String]("5831-4-7","14838-48","588-14-2","4830--77","-43734-7"),
    "2x2" -> Array[String]("4368","535-","6666","684-","8811","6622","4477"),
    "2x4" -> Array[String](
      "46636668","48322483","563234-7","5831-4-7","4830--77","43436868","43535863","43116888","43215883","48405887",
      "46-16868","48635663","53535353","43735-53","43126-78","43225-73","48415377","46-26-58","48-25-53","46419177",
      "66666666","466-6686","46836-48","566-66-5","6868-4-4","-4866-48","586--4-5","688814-1","88881111","44881748",
      "588-14-2","44667781","-43734-7","48835-43","48154-77","48487171","84647712","66662222","64647272","4804-777",
      "5-5--5-5","44447777","46316688","563166-7","46833468","46323488","14838-48"),
    "4x2" -> Array[String](
      "66666666","43680099","43683486","4368-468","43684-86","43681188","43536866","43535368","43535-86","43532188",
      "435-8666","6666-468","435-1099","435-3586","435--568","435-1288","43980088","43981166","43216688","4321-498",
      "43214-89","43218866","66661188","43217368","43217-86","4321-768","66666622","66668811","66667-12","66669900",
      "66662222","66009922","66-46822","66-40199","66-49811","66886611","6688-421","66888800","66881122","66887-10",
      "66882211","66118822","66119911","667-8611","66-45-86","667--521","667-8900","66990022","6699-401","66991111",
      "66992200","66226622","43686622","43688811","43687321","66-42188","43687-12","4368-721","43689900","43682222",
      "43019922","43536822","43539811","435-8622","435-8911","43986611","6611-498","43983412","4398-421","43984-12",
      "43988800","43987301","43987-10","4398-701","88881111","88118811","887--501","66112288","88991100","44447777",
      "44774477","68-4684-","68-4217-","684--55-","6888114-","6811884-","6811-75-","687-107-","43686666","687-124-",
      "5353535-","535-864-","535--55-")
  )

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
    '-' -> SrcNodes()                 // not used node
  )

  /** Translates each character into a relative source node.
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
      else if (!matrix.matches("[-0-9]+"))
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