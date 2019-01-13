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
   * Logs id,x,y values of the core node, and clockWise arranged sources and targets.
   * The x/y are calculated back to col/row scale for readability.
   */
  def logTileLinks(linkedNodes: Array[(NodeProps, Array[NodeProps])]): Unit = {
    def log(n: NodeProps) = s"(id=${ n.id },x=${ n.x.toInt / 15 - 2 },y=${ n.y.toInt / 15 - 2 })"

    linkedNodes.foreach { case (core, clockWise: Array[NodeProps]) =>
      println(s"core${ log(core) } clockwise(${ clockWise.map(_.id).mkString(",") }) = ${ clockWise.map(log).mkString }")
    }
  }

  /**
   * Finds nodes within the boundaries.
   *
   * A node is a stitch, cross or twist.
   * Tile boundaries (N/E/S/W) are inclusive. The values for these boundaries must be
   * row/column numbers multiplied with the x/y scale for the initial layout of a diagram.
   *
   * @return nodes in random order, each mapped to their sources and targets in a clockwise order.
   */
  def tileLinks(north: Double, east: Double, south: Double, west: Double): Seq[(NodeProps, Array[NodeProps])] = {
    def inTile(node: NodeProps) = {
      !node.pin &&
        node.x >= west && node.x <= east &&
        node.y >= north && node.y <= south
    }

    val sourceLinksForTileNodes = links.filter(link => inTile(nodes(link.target)))
    val targetLinksForTileNodes = links.filter(link => inTile(nodes(link.source)))

    def getSources(core: Int) = {
      sourceLinksForTileNodes
        .filter(_.target == core)
        .sortBy(_.isInstanceOf[WhiteStart])
        .map(_.source)
        .map(nodes(_))
    }

    def getTargets(core: Int) = {
      targetLinksForTileNodes
        .filter(_.source == core)
        .sortBy(_.isInstanceOf[WhiteStart])
        .map(_.target)
        .map(nodes(_))
    }

    // In thread diagrams each link has a short start or tail, as shown in ASCII art:
    //
    //      cross           twist
    //    S      s         s      S     source nodes
    //          /           \
    //     \   /             \  /
    //      \                  /
    //       \                /         core node
    //        \              /
    //      /  \            /   \
    //     /                     \
    //    T     t         t       T     target nodes
    sourceLinksForTileNodes.map(_.target).distinct.map { core =>
      val coreNode = nodes(core)
      val clockWise = coreNode.instructions match {
        // would expect the sources and targets to be ordered for a twist
        // the unit tests show otherwise
        case "twist" => getSources(core) ++ getTargets(core).reverse
        case "cross" => getSources(core).reverse ++ getTargets(core)
        case _ => getSources(core)
          // pairs never go up and are never on top of one another
          // therefore delta-x can determine the order
          .sortBy(n => n.x - coreNode.x) ++ getTargets(core)
          .sortBy(n => coreNode.x - n.x)
      }
      coreNode -> clockWise.toArray // TODO drop toArray when no longer needed in a java context
    }
  }
}
