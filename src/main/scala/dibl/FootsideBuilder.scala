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

/** Builder of the fringes around the 'o'-s in a following matrix:
  * <pre>
  * ::v v::
  * > o o <
  * > o o <
  * ::^ ^::
  * </pre>
  * In above ascii art each "o" represent either an unused node or a stitch.
  * The 'v' symbols represent zero to two incoming pairs, the '^' symbols zero to two outgoing pairs.
  * The '<' and '>' zero to three incoming and/or outgoing pairs.
  *
  * @param abs The coordinates of a cell represent the target of a link,
  *            each cell contains the coordinates of the sources of links.
  */
class FootsideBuilder (abs: M) {

  val absRows = abs.length - 4
  val absCols = abs(0).length - 4

  // don't want to be bothered by links coming with different directions out of the margin
  for {row <- 2 until absRows + 2} {
    dropLinksInMargin(row, 2, 1)
    dropLinksInMargin(row, absCols + 1, absCols + 2)
  }

  val nrOfLinks = countLinks(abs)
  // so far the preparations

  def dropLinksInMargin(targetRow: Int, targetCol: Int, sourceCol: Int): Unit = {
    val sources = abs(targetRow)(targetCol)
    if (sources.length > 1)
      abs(targetRow)(targetCol) = (sources(0)._2, sources(1)._2) match {
        case (`sourceCol`, `sourceCol`) => Array[(Int, Int)]()
        case (`sourceCol`, _) => Array(sources(1))
        case (_, `sourceCol`) => Array(sources(0))
        case _ => sources
      }
  }

  def linkRatio(row: Int, col: Int) = // (total,incoming) links
    (nrOfLinks(row)(col), abs(row)(col).length)

  def needFootside(row: Int, col: Int)= linkRatio(row, col) match {
    case (0, _) => false
    case (4, _) => false
    case (_) => true
  }


  def addFootside(targetCol: Int, sourceCol: Int) = {

    def swap(sources: SrcNodes) =
      if(targetCol > sourceCol || sources.length < 2) sources
      else Array(sources(1),sources(0))

    @tailrec
    def addFootsideStitches(needFootside: Seq[Int], sourceRow: Int = 2): Unit =
      if (needFootside.nonEmpty) {
        val targetRow = needFootside.head
        linkRatio(targetRow, targetCol) match {
          // case (1, 0) => // TODO
          // case (1, 1) =>
          // case (2, 0) =>
          // case (2, 2) =>
          case (2, 1) => // one in, one out: create |>
            abs(targetRow)(sourceCol) = swap(Array((sourceRow, sourceCol), (targetRow, targetCol)))
            abs(targetRow)(targetCol) = swap(Array((sourceRow, sourceCol)) ++ abs(targetRow)(targetCol))
            addFootsideStitches(needFootside.tail, targetRow)
          case (3, 1) => // one in, two out: create -
            abs(targetRow)(targetCol) = swap(Array((sourceRow, sourceCol)) ++ abs(targetRow)(targetCol))
            addFootsideStitches(needFootside.tail, sourceRow)
          case (3, 2) => // two in, one out: create |_
            abs(targetRow)(sourceCol) = swap(Array((sourceRow, sourceCol), (targetRow, targetCol)))
            addFootsideStitches(needFootside.tail, targetRow)
          case (total,in) => println(s"Missed footside case: total=$total, in=$in, cell=($targetRow, $targetCol)")
            addFootsideStitches(needFootside.tail, targetRow)
        }
      }
    addFootsideStitches(abs.indices.filter(needFootside(_, targetCol)))
  }

  def build(): Unit = {
    // footside: assign the '>'-s and '<'-s of the ascii art representation
    addFootside(2, 1)
    addFootside(absCols + 1, absCols + 2)

    // The 'v'-s in the ascii art representation may shake hands
    // move the the legs into one column but separate the ends in different rows
    for {col <- 0 until absCols + 2} if (abs(2)(col).length == 2) {
      val sources = abs(2)(col)
      val (srcRowLeft,_) = sources(0)
      val (srcRowRigth,_) = sources(1)
      abs(2)(col) = (srcRowLeft, srcRowRigth) match {
        // horizontal connection if source and target on the same row (2 in this case)
        case (2, 2) => sources // keep both original sources
        case (2, _) => Array(sources(0), (0, col)) // keep left source
        case (_, 2) => Array((0, col), sources(1)) // keep right source
        case _ => Array((0, col), (1, col)) // put both sources in a single column
      }
    }

    // add bobbins
    for {col <- 0 until absCols + 4} if (abs(absRows + 1)(col).length == 2) {
      nrOfLinks(absRows + 1)(col) match {
        case (2) => ()
          abs(absRows + 2)(col) = Array((absRows + 1, col))
          abs(absRows + 3)(col) = Array((absRows + 1, col))
        case (3) =>
          abs(absRows + 2)(col) = Array((absRows + 1, col))
        case _ => ()
      }
    }
  }
}
