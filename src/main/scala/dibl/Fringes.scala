/*
 Copyright 2016 Jo Pol
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

import scala.collection.immutable.IndexedSeq
import scala.collection.immutable.Range.Inclusive
import scala.collection.mutable

/**
  * Below a schematic ascii visualisation of a definition of a
  * two-in-two out directed graph representing a bobbin lace pair diagram.
  * {{{
  * ::v v::
  * > o o <
  * > o o <
  * ::^ ^::
  * }}}
  * Each 'o' character represents a possible node in turn representing a stitch,
  * each link represents a pair of threads wound on bobbins.
  * The `v` symbols represent zero to two incoming links/pairs,
  * the `^` symbols zero to two outgoing pairs.
  * The `<` and `>` zero to three incoming and/or outgoing pairs.
  *
  * The right and left fringes can be laced together with additional edge pairs
  * to make `|<` respective `>|` shaped stitches along the edges called footsides.
  * Think these footside stitches as stick figures with arms and feet.
  * The sideways reaching arm and standing foot is a pair left aside by a previous row of stitches,
  * the upwards reaching arm and kicking foot is a pair coming into play again for a new row of stitches.
  *
  * @param absSrcNodes Defines the o's in the ascii art diagram by two incoming pairs.
  *                    The row and col of a cell define the target of incoming links.
  *                    The tuples in a cell define the row/col of either zero or two sources of a link.
  *                    The outgoing pairs follow implicitly from neighbouring nodes beside and/or below.
  *                    A source is one or two horizontal/vertical cells or one diagonal cell above/aside a target.
  *                    The outer two rows and columns are placeholders for sources and targets of the fringes.
  */
class Fringes(absSrcNodes: Array[Array[SrcNodes]]) {

  private val drawingScale = 10
  private val topTargetRow = 2
  private val leftTargetCol = 2
  private val rightTargetCol = absSrcNodes(0).length - leftTargetCol - 1
  private val bottomTargetRow = absSrcNodes.length - topTargetRow - 1

  private def fromOutside(targetCol: Int, sourceRow: Int, sourceCol: Int): Boolean =
    sourceRow < topTargetRow || // vertical/diagonal links
      sourceCol < leftTargetCol || // from outer left
      sourceCol > rightTargetCol // from outer right

  private def intoSide(targetCol: Int): Seq[Link] = {

    val col: Array[SrcNodes] = absSrcNodes.map(_ (targetCol))
    for {
      targetRow <- topTargetRow + 1 until col.length - 2
      (sourceRow, sourceCol) <- col(targetRow)
      if fromOutside(targetCol, sourceRow, sourceCol)
    } yield Link(
      source = Cell(sourceRow, sourceCol),
      target = Cell(targetRow, targetCol)
    )
  }

  val coreLinks: Seq[Link] = for {
    targetRow <- absSrcNodes.indices
    targetCol <- absSrcNodes(targetRow).indices
    (sourceRow, sourceCol) <- absSrcNodes(targetRow)(targetCol)
    if !fromOutside(targetCol, sourceRow, sourceCol)
  } yield Link(Cell(sourceRow, sourceCol), Cell(targetRow, targetCol))

  private val regularNewPairs: Seq[Link] = {
    val row = absSrcNodes(topTargetRow)
    for {
      targetCol <- leftTargetCol to rightTargetCol
      (sourceRow, sourceCol) <- row(targetCol)
      if fromOutside(targetCol, sourceRow, sourceCol)
    } yield Link(
      source = Cell(sourceRow, sourceCol),
      target = Cell(topTargetRow, targetCol)
    )
  }

  private val count = Matrix.countLinks(absSrcNodes)

  /** Nodes that need a link going into the footside.
    * Nodes that needs two out going links are returned twice.
    * Nodes in the outer column come before the previous column.
    */
  private def needsOutLink(outerCol: Int, innerCol: Int): Seq[Cell] = {
    for {
      targetRow <- topTargetRow until bottomTargetRow
      targetCol <- outerCol to(innerCol, innerCol - outerCol)
      _ <- 1 to (4 - count(targetRow)(targetCol)) % 4
    } yield Cell(targetRow, targetCol)
  }

  private val targets = mutable.Stack[Cell]()

  private def popLinks(sourceRow: Int, sourceCol: Int): Seq[Link] = for {
    _ <- 1 to (4 - count(sourceRow)(sourceCol)) % 4 // nr of needed links out
    if targets.nonEmpty
  } yield Link(Cell(sourceRow, sourceCol), targets.pop())

  private def pushLinks(targetRow: Int, targetCol: Int): Unit = absSrcNodes(targetRow)(targetCol).foreach {
    case (sourceRow, sourceCol) =>
      if (fromOutside(targetCol, sourceRow, sourceCol))
        targets.push(Cell(targetRow, targetCol))
  }

  private def createLinks(cols: Inclusive): IndexedSeq[Link] = {
    for {
      row <- bottomTargetRow to(topTargetRow, -1)
      col <- cols
      links = popLinks(row, col)
      _ = pushLinks(row, col)
    } yield links
  }.flatten

  private def leftOvers(source: Cell) = targets.toArray
    .filter { case (sourceRow, _) => sourceRow > topTargetRow }
    .map(target => Link(source, target))

  val leftFootSides = createLinks(leftTargetCol to leftTargetCol + 1)
  private val leftNewPairs = leftOvers(Cell(0, 0))
  targets.clear()
  val rightFootSides = createLinks(rightTargetCol to(rightTargetCol - 1, -1))

  private val requiredPairs = leftNewPairs ++ regularNewPairs ++ leftOvers(Cell(0, rightTargetCol + 2))

  /** The pairs needed to start a patch of lace along the top and for the footsides,
    * each link is one leg of the `v`'s in the ascii art diagram of the class.
    * Each link has a unique source node.
    * The red links in the [[svgDoc]].
    */
  val newPairs = for {i <- requiredPairs.indices} yield Link((0, i), requiredPairs(i)._2)

  /** An SVG document with all links of the two-in-two-out directed graph,
    * The core links are black, incoming links along the top drawn are red,
    * incoming links along the side are green,
    * dots for nodes that need outgoing links, darker dots require two links.
    * Semi transparency makes the color lighter unless multiple links are stacked.
    *
    * Changing the content after creation renders inconsistent results.
    */
  lazy val svgDoc =
  s"""<svg
      |  version='1.1'
      |  id='svg2'
      |  height='80mm'
      |  width='80mm' xmlns:xlink='http://www.w3.org/1999/xlink'
      |  xmlns='http://www.w3.org/2000/svg'
      |>
      |<g transform='translate($drawingScale,$drawingScale)'>
      |${draw(needsOutLink(leftTargetCol, leftTargetCol + 1))}
      |${draw(needsOutLink(rightTargetCol, rightTargetCol - 1))}
      |${draw(coreLinks, "#000")}
      |${draw(newPairs, "#F00")}
      |${draw(intoSide(leftTargetCol) ++ intoSide(leftTargetCol + 1), "#080")}
      |${draw(intoSide(rightTargetCol) ++ intoSide(rightTargetCol - 1), "#080")}
      |${draw(leftFootSides ++ rightFootSides, "#808")}
      |</g>
      |</svg>""".stripMargin

  private def draw(nodes: Seq[Cell]): String = nodes.map {
    case (sourceRow, sourceCol) =>
      s"""<circle style='fill:#888;fill-opacity:0.4;stroke:none'
          |  cx='${sourceCol * drawingScale}'
          |  cy='${sourceRow * drawingScale}'
          |  r='3'
          |/>
          |""".stripMargin
  }.mkString

  private def draw(links: Seq[Link], color: String): String = links.map {
    case ((sourceRow, sourceCol), (targetRow, targetCol)) =>
      val sourceX = sourceCol * drawingScale
      val sourceY = sourceRow * drawingScale
      val targetX = targetCol * drawingScale
      val targetY = targetRow * drawingScale
      s"""<path style='stroke:$color;stroke-opacity:0.4;fill:none' d='M $sourceX,$sourceY $targetX,$targetY'/>"""
  }.mkString
}
