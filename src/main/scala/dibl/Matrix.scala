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

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

object Matrix {

  def apply(key: String, index: Int, absRows: Int, absCols: Int, left: Int = 0, up: Int = 0): M =
  toAbsWithMargins(toCheckerboard(shift(getRelSources(key, index),left,up)), absRows, absCols)

  @tailrec
  def shiftT(m: Array[_], shift: Int): Array[_] = {
    if (shift==0) m else shiftT(m.tail :+ m.head, shift-1)
  }

  //@tailrec TODO fix compiler errors
  //def shiftX[T](m: Array[T], shift: Int): Array[T] = {
  //  if (shift==0) m else shiftX(m.tail :+ m.head, shift-1)
  //}
    def shift(m: M, left: Int, up: Int): M = {
    // TODO fix runtime error: "[Ljava.lang.Object; cannot be cast to [[Lscala.Tuple2;"
    m //shiftT(for (r <- m) yield shiftT(r,left).asInstanceOf[R],up).asInstanceOf[M]
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
  def toCheckerboard(m: M): M = {
    val rows = m.length
    val cols = m(0).length
    val result: M = Array.ofDim[SrcNodes](2*rows, cols)
    for {
      row <- m.indices
      col <- m(0).indices
    } {
      result(row)(col) = m(row)(col)
      result(row+rows)((col+cols/2)%cols) = m(row)(col)
    }
    result
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
    * The other symbols match two nodes for (potential) new pairs or for pairs of bobbins.
    * See also println's in unit tests.
    */
  def toAbsWithMargins(rel: M, absRows: Int, absCols: Int): M = {
    val abs = Array.fill(absRows + 3,absCols + 4)(SrcNodes())
    val relRows = rel.length
    val relCols = rel(0).length
    for {
      absRow <- 2 until absRows + 2
      absCol <- 2 until absCols + 2
    } {
      abs(absRow)(absCol) = for ((relRow, relCol) <- rel(absRow % relRows)(absCol % relCols))
        yield (absRow + relRow, absCol + relCol)
    }
    for (col <- 2 until absCols + 2) {
      // top margin
      for (i <- abs(2)(col).indices) {
        val (srcRow, _) = abs(2)(col)(i)
        if (srcRow == 1) abs(2)(col)(i) = (i % 2, col)
      }
      // bottom margin
      if (abs(absRows + 1)(col).length > 1) {
        abs(absRows + 2)(col) = Array(abs(absRows + 1)(col)(1))
        abs(absRows + 1)(col) = Array(abs(absRows + 1)(col)(0))
      }
    }
    // foot sides: join each pair-out (nrOfNodes==3) with the next pair-in (col in margin)
    val nrOfLinks = countLinks(abs)
    def nextPairIn(row: Int, targetCol: Int, srcCol: Int, src: Int): Option[(Int, Int)] = {
      for(r <- row+1 until absRows+2) {
        if(abs(r)(targetCol).length>src && abs(r)(targetCol)(src)._2==srcCol)
          return Some(abs(r)(targetCol)(src))
      }
      None
    }
    val targetCol = absCols + 1
    for (row <- 2 until absRows + 2) {
      if(nrOfLinks(row)(2)==3) {
        nextPairIn(row,2,1,0) match {
          case Some((r,c)) => abs(r)(c) = SrcNodes((row,2))
          case None => abs(row)(0) = Array((row,2))
        }
      }
      if(nrOfLinks(row)(targetCol)==3) {
        nextPairIn(row,targetCol,targetCol+1,1) match {
          case Some((r,c)) => abs(r)(c) = SrcNodes((row,targetCol))
          case None => abs(row)(targetCol+2) = Array((row,targetCol))
        }
      }
    }
    val nrOfLinks2 = countLinks(abs)
    val leftFootsides = ListBuffer[(Int,Int)]()
    val rightFootsides = ListBuffer[(Int,Int)]()
    for (row <- 2 until absRows + 2) {
      if(nrOfLinks2(row)(1)>0) leftFootsides += ((row,1))
      if(nrOfLinks2(row)(targetCol+1)>0) rightFootsides += ((row,targetCol+1))
    }
    def connectFootsides (sources: Seq[(Int,Int)]): Unit = {
      for (i <- 1 until sources.length) {
        val (row,col) = sources(i)
        abs(row)(col) = abs(row)(col) :+ sources(i-1)
      }
    }
    leftFootsides += ((absRows+2,1))
    rightFootsides += ((absRows+2,targetCol+1))
    connectFootsides(leftFootsides)
    connectFootsides(rightFootsides)
    abs
  }

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
  /** Each string represents a matrix generated by Veronika Irvine:
    * http://web.uvic.ca/~vmi/papers/jmm2014.html
    * http://web.uvic.ca/~vmi/papers/bridges2012.html
    * Each cell (alias character) represents a target node in a two-in-two-out directed graph, see relSourcesMap.
    */
  val matrixMap: HashMap[String,Array[String]] = HashMap (
    "2x2" -> Array[String]("4368","535-","6666","684-","8811","6622","4477"),
    "2x4" -> Array[String]("46636668","48322483","83154-7-","5831-4-7","4830--77","43436868","43535863","43116888","43215883","48405887","46-16868", "48635663","53535353","43735-53","43126-78","43225-73","48415377","46-26-58","48-25-53","46419177","66666666","466-6686","46836-48","566-66-5","6868-4-4","486--486","586--4-5","688814-1","88881111","48847481","588-14-2","46647817","437-4-73","48835-43","48154-77","48487171","46487127","66662222","46462727","4804-777","5-5--5-5","44447777","46316688","563166-7","46833468","46323488","4831-488"),
    "4x2" -> Array[String]("66666666","43680099","43683486","4368-468","43684-86","43681188","43536866","43535368","43535-86","43532188","435-8666","6666-468","435-1099","435-3586","435--568","435-1288","43980088","43981166","43216688","4321-498","43214-89","43218866","66661188","43217368","43217-86","4321-768","66666622","66668811","66667-12","66669900","66662222","66009922","66-46822","66-40199","66-49811","66886611","6688-421","66888800","66881122","66887-10","66882211","66118822","66119911","667-8611","66-45-86","667--521","667-8900","66990022","6699-401","66991111","66992200","66226622","43686622","43688811","43687321","66-42188","43687-12","4368-721","43689900","43682222","43019922","43536822","43539811","435-8622","435-8911","43986611","6611-498","43983412","4398-421","43984-12","43988800","43987301","43987-10","4398-701","88881111","88118811","887--501","66112288","88991100","44447777","44774477","68-4684-","68-4217-","684--55-","6888114-","6811884-","6811-75-","687-107-","43686666","687-124-","5353535-","535-864-","535--55-")
  )

  /** Gets the dimensions from a matrixMap key
    *
    * @param key for matrixMap
    * @return rows, cols and any other numbers found
    */
  def dim(key: String): Array[Int] =
    key.split("[^0-9]+").map(_.toInt)

  /** Translates a character in a matrix string into relative links with source nodes.
    * The source nodes are defined with relative (row,column) numbers.
    * A node can be connected in eight directions, but source nodes are not found downwards.
    * A source node on the right means the same row and the next column hence (0,1).
    * A source node at north west means a previous row and column hence (-1,-1).
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

  private def unpack(s: String): R = s.toCharArray.map {
    relSourcesMap.get(_).get
  }

  /** Gets the relative source nodes.
   * @param set key for a set of predefined matrices
   * @param nr sequence number of the matrix in the set
   */
  private def getRelSources(set: String, nr: Int): M = {
    val s = matrixMap.get(set).get(nr) // TODO: both set and nr could be invalid
    val cols = dim(set)(1) // won't fail if previous is OK
    unpack(s).grouped(cols).toArray
  }
}