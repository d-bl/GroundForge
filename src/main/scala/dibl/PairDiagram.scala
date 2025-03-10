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

  /**
   *
   * @param input value of query/form field with id/name droste1 or droste2
   * @return multiline legend for the color code
   */
  @JSExport
  def drosteLegend(input: String): String = {
    val stitches = new Stitches(input)
    val default = Array[String]("default", stitches.defaultStitch)
    val keyValuePairs: Seq[Array[String]] = stitches.tuples
      .map{ case (id, stitch, _) =>
        Array(id, stitch)
      }.toSeq
    (keyValuePairs :+ default)
      .map { case Array(id, stitch) =>
        stitch -> id
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))
      .toSeq
      .map { case (stitch, ids) =>
        Stitches.defaultColorName(stitch) -> ids.mkString(s"$stitch (", ", ", ")")
      }.groupBy(_._1)
      .mapValues(_.map(_._2))
      .map { case (color, stitches) =>
        val col = if (color.trim.isEmpty) "black" else color
        stitches.sortBy(identity).mkString(f"$col%-10s", "\n          ", "")
      }
      .mkString("\n")
  }

  @JSExport
  def create(stitches: String, threadDiagram: Diagram): Diagram = apply(stitches, threadDiagram)

  /** Restyles the nodes of a diagram into nodes for a pair diagram.
    *
    * @param stitches see step 2/3 on https://d-bl.github.io/GroundForge/index.html
    * @param threadDiagram the nodes will be replaced with color codes
    * @return
    */
  def apply(stitches: String, threadDiagram: Diagram): Diagram = {
    println(s"pair diagram from thread diagram")
    val stitchMap = new Stitches(stitches)

    def translateTitle(n: NodeProps) = {
      if (n.title.startsWith("thread "))
        n.title.replace("thread", "Pair")
      else {
        s"${ stitchMap.stitch(n.id, n.title.replaceAll(" .*","")) } - ${ n.id }"
      }
    }

    val pairNodes = threadDiagram.nodes.map(n => node(translateTitle(n), n.x, n.y))
    def nrOfTwists(source: Int, target: Int) = {
      val sourcePairNode = pairNodes(source)
      val targetPairNode = pairNodes(target)

      def twistsToRight = {
        sourcePairNode.closingTwistsRight + targetPairNode.openingTwistsLeft
      }

      def twistsToLeft = {
        sourcePairNode.closingTwistsLeft + targetPairNode.openingTwistsRight
      }
      threadDiagram.links.collectFirst { case l if
        l.source == source && l.target == target =>
        (l.getClass.getSimpleName, threadDiagram.node(l.source).instructions) match {
          case ("WhiteStart", "cross") => twistsToLeft
          case ("WhiteStart", "twist") => twistsToRight
          case ("WhiteEnd", "cross") => twistsToRight
          case ("WhiteEnd", "twist") => twistsToLeft
          case _ => 0
        }
      }.getOrElse(0)
    }

    val diagramLinks = threadDiagram.filterLinks
    val leftValues = diagramLinks.map(_._1).toSet
    val rightValues = diagramLinks.map(_._2).toSet

    val filteredLinks = diagramLinks.filter { case (left, right) =>
      // remove links that are not connected to any other link
      leftValues.contains(right) || rightValues.contains(left)
    }
    val usedNodeIndices = filteredLinks.flatMap{case (source, target) => Seq(source, target)}.distinct
    val indexMap = usedNodeIndices.zipWithIndex.toMap

    val links = filteredLinks.map { case (source, target) =>
      pairLink(indexMap(source), indexMap(target), mid = nrOfTwists(source, target))
    }
    Diagram(usedNodeIndices.map(pairNodes), links)
  }
}
