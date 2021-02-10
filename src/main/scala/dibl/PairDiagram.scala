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

import dibl.LinkProps.pairLink
import dibl.NodeProps.node

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("PairDiagram") object PairDiagram {

  @JSExport
  def legend(urlQuery: String): String = {
    val stitchIdTuples = urlQuery
      .split("&")
      .filter(_.toLowerCase.matches(".*=[ctrlp]+"))
      .map(_.split("=", 2))
      .map { case Array(id, stitch) =>
        stitch -> id
      }
    val stitchToIdsMap = stitchIdTuples
      .groupBy(_._1)
      .mapValues(_.map(_._2))
      .toSeq
    stitchToIdsMap
      .map { case (stitch, ids) =>
        Stitches.defaultColorName(stitch) -> ids.mkString(s"$stitch (", ", ", ")")
      }.groupBy(_._1)
      .mapValues(_.map(_._2))
      .map { case (color, stitches) =>
        stitches.mkString(f"$color%-10s ", "\n           ", "")
      }
      .mkString("\n")
  }

  @JSExport
  def create(stitches: String, threadDiagram: Diagram): Diagram = apply(stitches, threadDiagram)

  /** Restyles the nodes of a diagram into nodes for a pair diagram.
   *
   * @param stitches      see step 2/3 on https://d-bl.github.io/GroundForge/index.html
   * @param threadDiagram the nodes will be replaced with color codes
   * @return
   */
  def apply(stitches: String, threadDiagram: Diagram): Diagram = {

    val stitchMap = new Stitches(stitches)

    def translateTitle(n: NodeProps) = {
      if (n.title.startsWith("thread "))
        n.title.replace("thread", "Pair")
      else {
        s"${ stitchMap.stitch(n.id, n.title.replaceAll(" .*", "")) } - ${ n.id }"
      }
    }

    val pairNodes = threadDiagram.nodes.map(n => node(translateTitle(n), n.x, n.y))
    val links = threadDiagram
      .filterLinks
      .map { case (source, target) =>
        createPairLink(
          source, target,
          pairNodes(source),
          pairNodes(target),
          threadDiagram.nodes(source).instructions,
          threadDiagram.nodes(target).instructions
        )
      }
    Diagram(pairNodes, links)
  }

  private def createPairLink(source: Int, target: Int,
                             sourcePairNode: NodeProps,
                             targetPairNode: NodeProps,
                             sourceThreadNode: String,
                             targetThreadNode: String
                            ) = {
    val nrOfTwists: Int =
      (sourceThreadNode, targetThreadNode) match {
        case ("cross", "cross") => sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsLeft
        case ("twist", "cross") => sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsLeft
        case ("cross", "twist") => sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsRight
        case ("twist", "twist") => sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsRight
        case _ => 0
      }
    pairLink(source, target,
      start = sourcePairNode.color,
      mid = nrOfTwists - 1,
      end = targetPairNode.color
    )
  }
}
