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
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
case class Diagram(nodes: Seq[NodeProps],
                   links: Seq[LinkProps]
                  ) {

  @JSExport
  def node(i: Int): NodeProps = nodes(i)

  @JSExport
  def link(i: Int): LinkProps = links(i)

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport
  def jsNodes(): js.Array[js.Dictionary[Any]] = toJS(nodes)

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport
  def jsLinks(): js.Array[js.Dictionary[Any]] = toJS(links)

  private def toJS(items: Seq[Props]): js.Array[js.Dictionary[Any]] = {

    val jsItems = new js.Array[js.Any](items.length).asInstanceOf[js.Array[js.Dictionary[Any]]]
    for {i <- items.indices} {
      jsItems(i) = items(i).toJS()
      jsItems(i)("index") = i
    }
    jsItems
  }

  /**
   * @return Visible links as source/target-tuples of indexes in nodes, stripped of duplicates.
   *         An example: the 8 links in ascii-art ">==<" graph should be reduced to 4 links as "><".
   *         This means recursively replace the sources of "<" links with the sources of "=" links.
   */
  def filterLinks: Seq[(Int, Int)] = {
    val visibleLinks = links
      .withFilter(!_.border)
      .map(l => (l.source, l.target))
    val targetsOfDuplicates = visibleLinks
      .groupBy { case (s: Int, t: Int) => (s, t) }
      .filter { case (_, duplicates) => 1 < duplicates.size }
      .keySet
      .map(_._2)
    val linksByTarget = visibleLinks.groupBy { case (_, t: Int) => t }

    @tailrec
    def FindSource(s: Int): Int = {
      if (targetsOfDuplicates.contains(s))
        FindSource(linksByTarget(s).head._1)
      else s
    }

    visibleLinks
      .withFilter { case (_, t) => !targetsOfDuplicates.contains(t) }
      .map { case (s, t) => (FindSource(s), t) }
  }

  /**
   * Get links for one tile.
   *
   * Tile boundaries (N/E/S/W) are inclusive. The values for these boundaries must be
   * row/column numbers multiplied with the scale for the initial layout of a diagram.
   *
   * @return tuples with (source,target) for all links witin a tile and to adjecent tiles.
   *         Node objects inside the tile are different from those outside the tile.
   *         Nodes outside the tile may have an id property shared
   *         by a node inside the tile on the opposite side (unless the oposite is a foot side)
   *         provided that the boundaries match all nodes in one tile.
   */
  def tileLinks(north: Double, east: Double, south: Double, west: Double): Seq[LinkedNodes] = {
    val nodeNrs = nodes.zipWithIndex.filter { case (node, _) =>
      node.x >= east && node.x <= west &&
        node.y >= north && node.y <= south &&
        !node.pin
    }.map(_._2)
    val filteredLinks = links
      .filter(link => nodeNrs.contains(link.source) || nodeNrs.contains(link.target))
    val uniqueNodes = filteredLinks.flatMap(l => Array(l.source, l.target)).distinct
    println(
      s"""nr of links = ${filteredLinks.size};
         |nr of nodes = ${uniqueNodes.size};
         |nr of ids = ${filteredLinks.flatMap(l => Array(nodes(l.source).id, nodes(l.target).id)).distinct.size}
         |""".stripMargin
    )
    uniqueNodes.sortBy(nodes(_).id).foreach{nr =>
      val n = nodes(nr)
      println(s"${n.id} x=${n.x.toInt/15-2} y=${n.y.toInt/15-2}")
    }
    filteredLinks
      .map{link =>
        //println(s"ids(${nodes(link.source).id} -> ${nodes(link.target).id}); objects(${nodes(link.source).##} -> ${nodes(link.target).##}); ")
        LinkedNodes(nodes(link.source), nodes(link.target))
      }
  }
}
case class LinkedNodes(source: NodeProps, target: NodeProps)
