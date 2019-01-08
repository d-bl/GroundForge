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

import dibl.LinkProps.WhiteStart

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("Diagram") case class Diagram(nodes: Seq[NodeProps],
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

  @JSExport
  def withLocationsOf(jsNodes: js.Array[js.Dictionary[Any]]): Diagram = {
    val nudgedNodes = nodes.indices.map{ i =>
      val newX = jsNodes(i)("x").toString.toInt
      val newY = jsNodes(i)("y").toString.toInt
      nodes(i).withLocation(newX, newY)
    }
    Diagram(nudgedNodes,links)
  }

  def withLocations(javaNodes: Array[Array[Double]]): Diagram = {
    val nudgedNodes = nodes.indices.map{ i =>
      val newX = javaNodes(i)(0)
      val newY = javaNodes(i)(1)
      nodes(i).withLocation(newX, newY)
    }
    Diagram(nudgedNodes,links)
  }

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
   * Finds nodes within the boundaries.
   *
   * Tile boundaries (N/E/S/W) are inclusive. The values for these boundaries must be
   * row/column numbers multiplied with the x/y scale for the initial layout of a diagram.
   *
   * @return nodes with sources and targets, all in random order.
   */
  def tileLinks(north: Double, east: Double, south: Double, west: Double): Seq[LinkedNode] = {
    nodes.zipWithIndex.filter { case (node, _) =>
      node.x >= east && node.x <= west &&
        node.y >= north && node.y <= south &&
        !node.pin
    }.map{case (node, nr)=>
      val sources = links
        .filter(_.target == nr)
        .map(l => nodes(l.source) -> links(l.target).isInstanceOf[WhiteStart])
        .toMap
        //.sortBy(_.id.reverse) // TODO sort by angle if different x and/or y

      val targets = links
        .filter(_.source == nr)
        .map(l => nodes(l.target) -> links(l.target).isInstanceOf[WhiteStart])
        .toMap
        //.sortBy(_.id.reverse) // TODO sort by angle if different x and/or y

      LinkedNode(node, sources, targets)
    }
  }

  /**
   * Logs id,x,y values of the core, sources and targets.
   * The x/y are calculated back to col/row scale for readability.
   * For example: core=(id=a1,x=2,y=2) sources=(id=a1,x=2,y=2; id=a1,x=2,y=2) targets=(id=a1,x=2,y=2; id=a1,x=2,y=2)
   */
  def logTileLinks(links: Array[LinkedNode]): Unit = {
    def log1(n: NodeProps) = s"(id=${n.id},x=${n.x.toInt/15-2},y=${n.y.toInt/15-2})"
    def log(nodes: Map[NodeProps, Boolean]) = s"${nodes.map{case (k,v) => s"${log1(k)} -> $v)"}.mkString("; ")}"
    links.foreach(l =>
      println(s"core${log1(l.core)} sources(${log(l.sources)}) targets(${log(l.targets)}) clockwise(${l.clockwise.map(_.id).mkString(",")})")
    )
  }
}
