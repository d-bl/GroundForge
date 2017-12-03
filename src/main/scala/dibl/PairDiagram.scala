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

import dibl.LinkProps.{pairLink, transparentLinks}
import dibl.NodeProps.{errorNode, node}

import scala.annotation.tailrec
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport
object PairDiagram {

  @JSExport
  def create(stitches: String, threadDiagram: Diagram): Diagram = apply(stitches, threadDiagram)

  /** Restyles the nodes of a diagram into nodes for a pair diagram.
    *
    * @param stitches see step 2/3 on https://d-bl.github.io/GroundForge/main.html
    * @param threadDiagram the nodes will be replaced with color codes
    * @return
    */
  def apply(stitches: String, threadDiagram: Diagram): Diagram = {
    val targetsBySource: Map[Int, Seq[Int]] = threadDiagram.links
      .groupBy(_.source)
      .map { case (source, links1) =>
        (source, links1.map(_.target))
      }

    //noinspection ZeroIndexToHead
    def sameTargets(targets: Seq[Int]): Boolean = targets.size > 1 && targets(0) == targets(1)

    val hasDuplicateLinksOut = targetsBySource
      .filter(s2t => sameTargets(s2t._2))
      .keySet

    val stitchList = stitches.split("[^a-zA-Z0-9=]+")
    val defaultStitch = if (stitchList(0).isEmpty || stitchList(0).contains('=')) "ctc" else stitchList(0)
    val stitchMap = stitchList
      .filter(s => s.contains('='))
      .map { s =>
        val xs = s.split("=")
        xs(0) -> xs(1)
      }.toMap

    def translateTitle(n: NodeProps) = {
      if (n.title.startsWith("thread "))
        n.title.replace("thread", "Pair")
      else {
        val s = stitchMap.getOrElse(
          n.id,
          stitchMap.getOrElse(
            n.instructions,
            defaultStitch
          ))
        s"$s - ${ n.id }"
      }
    }

    val pairNodes = threadDiagram.nodes.map(n => node(translateTitle(n), n.x, n.y))
    val links = threadDiagram
      .links
      .filter(link => !link.border)
      .filter(link => !hasDuplicateLinksOut.contains(link.source))
      .map { link =>
        createPairLink(
          link,
          pairNodes(link.source),
          pairNodes(link.target),
          threadDiagram.nodes(link.source).instructions,
          threadDiagram.nodes(link.target).instructions
        )
      }
    Diagram(pairNodes, links)
  }

  private def createPairLink(link: LinkProps,
                             sourcePairNode: NodeProps,
                             targetPairNode: NodeProps,
                             sourceThreadNode: String,
                             targetThreadNode: String
                            ) = {
    val nrOfTwists: Int =
      (sourceThreadNode, targetThreadNode, link.end) match {
        case ("cross", "cross", "white") => sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsRight
        case ("cross", "cross", "") => sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsLeft
        case ("twist", "cross", "white") => sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsRight
        case ("twist", "cross", "") => sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsLeft
        case ("cross", "twist", "white") => sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsLeft
        case ("cross", "twist", "") => sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsRight
        case ("twist", "twist", "white") => sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsLeft
        case ("twist", "twist", "") => sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsRight
        case _ => 0
      }
    pairLink(link.source, link.target,
      start = sourcePairNode.color,
      mid = nrOfTwists - 1,
      end = targetPairNode.color,
      weak = false
    )
  }

  /** Creates a pair diagram. Parameters are explained on the tabs of https://d-bl.github.io/GroundForge/
    *
    * @param compactMatrix see matrix tab
    * @param tiling values 'bricks' or 'checker', see also matrix tab
    * @param absRows see patch size tab
    * @param absCols see patch size tab
    * @param shiftLeft see footsides tab
    * @param shiftUp see footsides tab
    * @param stitches see stitches tab
    * @return In case of an error, the getOrRecover method returns
    *         a diagram with just a node that has an error message as title
    */
  @JSExport
  def get(compactMatrix: String, tiling: String, absRows: Int, absCols: Int, shiftLeft: Int = 0, shiftUp: Int = 0, stitches: String = ""): Diagram =
    create(compactMatrix.trim, tiling, absRows, absCols, shiftLeft, shiftUp, stitches).getOrRecover

  def create(compactMatrix: String, tiling: String, absRows: Int, absCols: Int, shiftLeft: Int = 0, shiftUp: Int = 0, stitches: String = ""): Try[Diagram] =
    Settings(compactMatrix.trim, tiling, absRows, absCols, shiftLeft, shiftUp, stitches)
      .map(PairDiagram(_))

  def apply(triedSettings: Try[Settings]): Diagram = if (triedSettings.isFailure)
    Diagram(Seq(errorNode(triedSettings)), Seq[LinkProps]())
  else apply(triedSettings.get)

  private def apply(settings: Settings) = {

    val fringes = new Fringes(settings.absM)
    val sources: Seq[Cell] = fringes.newPairs.map { case (source, _) => source }
    val sourcesIndices = sources.indices
    val plainLinks: Seq[Link] =
      sourcesIndices.filter(i => fringes.isLeftPair(i)).map(i => fringes.newPairs(i)) ++
        fringes.leftFootSides ++
        fringes.coreLinks ++
        sourcesIndices.filter(i => !fringes.isLeftPair(i)).map(i => fringes.newPairs(i)) ++
        fringes.rightFootSides
    val linksByTarget: Map[Cell, Seq[Link]] = replaceYsWithVs(plainLinks.groupBy { case (_, target) => target })
    val targets: Seq[Cell] = linksByTarget.keys.toSeq

    def footsideTwists(row: Int, col: Int) = {
      val lengths = linksByTarget(Cell(row, col))
        .map { case ((sRow, _), (tRow, _)) =>
          if (sRow < 2) 0 else tRow - sRow - 2
        }
      //noinspection ZeroIndexToHead
      val twists =
        "l" * Math.max(0, lengths(0)) +
          "r" * Math.max(0, lengths(1))
      twists
    }

    val nodeMap: Map[Cell, Int] = {
      val nodes = sources ++ targets
      nodes.indices.map(n => (nodes(n), n))
    }.toMap

    val nodes = sourcesIndices.map(col =>
      node(s"Pair ${1 + nodeMap((0, col))}", x = 15.0 * col, y = 0.0)) ++
      targets.map { case (row, col) =>
        val title = footsideTwists(row, col) + settings.getTitle(row, col)
        node(title, settings.getColor(row, col),  x = 15.0 * col, y = 15.0 * row)
      }

    val cols = Set(2, settings.absM(0).length - 2)
    val links =
      linksByTarget.values.flatten.map { case ((sourceRow, sourceCol), (targetRow, targetCol)) =>
        val sourceNode = nodeMap((sourceRow, sourceCol))
        val targetNode = nodeMap((targetRow, targetCol))
        val sourceStitch = nodes(sourceNode).instructions
        val targetStitch = nodes(targetNode).instructions
        val sourceCells = settings.absM(targetRow)(targetCol)
        val countedTargetChar = if (sourceCells(0) == (sourceRow, sourceCol)) 'l' else 'r'
        val countedSourceChar = 't' // TODO figure out which leg of the source stitch
        val nrOfTwists = sourceStitch.replaceAll(".*c","").count(_ == countedSourceChar) +
          targetStitch.replaceAll("t", "lr").replaceAll("c.*","").count(_ == countedTargetChar)
        pairLink(sourceNode, targetNode,
          start = if (sourceRow < 2) "pair" else Stitches.defaultColor(sourceStitch),
          mid = if (sourceRow < 2) 0 else nrOfTwists - 1,
          end = Stitches.defaultColor(targetStitch),
          weak = cols.contains(sourceCol) || targetRow - sourceRow > 1
        )
      }.toSeq ++ transparentLinks(sourcesIndices.toArray)
    Diagram(nodes, links)
  }

  /** Y and V are ascii art representations of sections in the two-in-two out graph
    * with the leg of the Y representing parallel links alias plaits
    *
    * @param linksByTarget a map of nodes to their two links coming into the node
    * @return idem, with the parallel legs of the Y's replaced by a single node
    */
  //noinspection ZeroIndexToHead
  def replaceYsWithVs(linksByTarget: Map[Cell, Seq[Link]]): Map[Cell, Seq[Link]] = {

    val plaitSources: Set[Cell] = linksByTarget.values
      .filter (twoIn => twoIn(0) == twoIn(1) )
      .map (twoIn => twoIn(0)._1 )
      .toSeq
      .toSet

    @tailrec
    def sourceOfParallelLinks (node: Cell):Cell = {
      val leftSource = linksByTarget(node)(1)._1
      if (!plaitSources.contains(leftSource))
        node
      else sourceOfParallelLinks(leftSource)
    }

    linksByTarget
      .filter { case (target, _) => !plaitSources.contains(target)}
      .map { case (target, twoIn) =>
        if (twoIn(0) != twoIn(1))
          (target, twoIn)
        else {
          val leftSource: Cell = twoIn(1)._1
          val replacedTwoIn = linksByTarget(sourceOfParallelLinks(leftSource))
          val newTwoIn = replacedTwoIn.map { case (source, _) => (source, target)}
          (target, newTwoIn)
        }
      }
  }

  /** Property of a link, a cross mark indicates additional twist(s).
    *
    * @param sourceStitch stitch instructions in terms of ctrlp
    * @param targetStitch idem
    * @param toLeftOfTarget which pair of the two-in
    * @return the number of additional twists, assuming the first twist is part of the Belgian color code
    */
  def midMarker(sourceStitch: String, targetStitch: String, toLeftOfTarget: Boolean): Int = {
    val twists = (targetStitch.replaceAll("c.*", "") + sourceStitch.replaceAll(".*c", ""))
      .replaceAll("p", "")
    val c = if (toLeftOfTarget) 'l' else 'r'
    Math.max(0, twists.count(_ == c) - 1)
  }
}
