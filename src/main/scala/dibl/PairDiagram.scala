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

object PairDiagram {

  /** Restyles the nodes of a diagram into nodes for a pair diagram.
    *
    * @param stitches see step 2/3 on https://d-bl.github.io/GroundForge/recursive.html
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

    def translateTitle(title: String) = {
      val threadTitle: String = title
      if (threadTitle.startsWith("thread "))
        threadTitle.replace("thread", "Pair")
      else stitchMap.getOrElse(threadTitle, defaultStitch)
    }

    val links = threadDiagram
      .links
      .filter(link => !link.border)
      .filter(link => !hasDuplicateLinksOut.contains(link.source))
      .map(link => LinkProps.simpleLink(link.source, link.target))
    val nodes = threadDiagram.nodes.map(p => node(translateTitle(p.title), p.x, p.y))
    Diagram(nodes, links)
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
  def create(compactMatrix: String, tiling: String, absRows: Int, absCols: Int, shiftLeft: Int = 0, shiftUp: Int = 0, stitches: String = ""): Try[Diagram] =
    Settings(compactMatrix, tiling, absRows, absCols, shiftLeft, shiftUp, stitches)
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
        node(title, x = 15.0 * col, y = 15.0 * row)
      }

    val cols = Set(2, settings.absM(0).length - 2)
    val links =
      linksByTarget.values.flatten.map { case ((sourceRow, sourceCol), (targetRow, targetCol)) =>
        val sourceNode = nodeMap((sourceRow, sourceCol))
        val targetNode = nodeMap((targetRow, targetCol))
        val sourceStitch = nodes(sourceNode).title.replaceAll(" .*", "").replaceAll("t", "lr")
        val targetStitch = nodes(targetNode).title.replaceAll(" .*", "").replaceAll("t", "lr")
        val toLeftOfTarget = settings.absM(targetRow)(targetCol)(0) == (sourceRow, sourceCol)
        pairLink(sourceNode, targetNode,
          start = if (sourceRow < 2) "pair" else marker(sourceStitch),
          mid = if (sourceRow < 2) 0 else midMarker(sourceStitch, targetStitch, toLeftOfTarget),
          end = marker(targetStitch),
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

  /** Property of a link, the Belgian color code of a node tells a lace maker which stitch to make.
    * The end marker and start marker of a link take the color of the node.
    *
    * @param stitch lower case instructions, t(wist), already expanded to l(eft)r(ight).
    *               A stitch is made with two pair alias four threads.
    *               A twist means even threads/bobbins (2nd and/or 4th) to the left by one position.
    *               A c(ross) means the second thread to the right by one position.
    *               A p(in) is put between the two pairs.
    * @return the color of a node
    */
  def marker(stitch: String): String = {
    if (stitch.endsWith("clrclrc") || stitch.contains("p")) ""
    else if (stitch.endsWith("lrclrc")) "red"
    else if (stitch.endsWith("clrc")) "purple"
    else if (stitch.endsWith("lrc")) "green"
    else if (stitch.startsWith("clrclrc")) ""
    else if (stitch.startsWith("clrclr")) "red"
    else if (stitch.startsWith("clrc")) "purple"
    else if (stitch.startsWith("clr")) "green"
    else ""
  }

  /** Property of a link, a cross mark indicates additional twist(s).
    *
    * @param sourceStitch as for [[marker]]
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
