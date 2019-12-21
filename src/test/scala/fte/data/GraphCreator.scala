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
package fte.data

import dibl.LinkProps.WhiteStart
import dibl.proto.TilesConfig
import dibl.{Diagram, LinkProps, NewPairDiagram, NodeProps, ThreadDiagram}
import fte.data.ArcedFace.facesFrom
import fte.layout.OneFormTorus

object GraphCreator {

  /**
    * @param urlQuery parameters for: https://d-bl.github.io/GroundForge/tiles?
    *                 For now the tile must be a checker tile and
    *                 the patch size must span 3 columns and 2 rows of checker tiles.
    *                 A simplified ascii-art view of a pattern definition:
    *                 +----+----+----+
    *                 |....|....|....|
    *                 |....|....|....|
    *                 +----+----+----+
    *                 |....|XXXX|....|
    *                 |....|XXXX|....|
    *                 +----+----+----+
    * @return The X's in the pattern definition are added to the returned graph.
    */
  def fromDiagram(urlQuery: String): Option[Graph] = {
    implicit val config: TilesConfig = TilesConfig(urlQuery)

    implicit val diagram: Diagram = ThreadDiagram(NewPairDiagram.create(config))
    implicit val scale: Int = 2
    //    implicit val diagram: Diagram = NewPairDiagram.create(config)
    //    implicit val scale: Int = 1

    val links = diagram.links.filter(inCenterBottomTile)
      .sortBy(l => s"${ diagram.node(l.target).id }${ diagram.node(l.source).id }")
    val graph = new Graph()

    // create each vertex on the torus once
    val vertexMap = links.map(_.target).distinct.zipWithIndex
      .map { case (nodeNr, i) =>
        // The initial coordinates don't matter as long as they are unique.
        diagram.node(nodeNr).id -> graph.createVertex(i, 0)
      }.toMap
    println(vertexMap.keys.toArray.sortBy(identity).mkString(","))

    // create edges of one tile
    links.foreach { link =>
      val source = diagram.node(link.source)
      val target = diagram.node(link.target)
      val whiteStart = link.isInstanceOf[WhiteStart]
      val (dx, dy) = deltas(whiteStart, source, target)
      println(s"(${ source.id },${ target.id }) deltas($dx,$dy) ${ source.isLeftTwist }, ${ source.isRightTwist }, ${ target.isLeftTwist }, ${ target.isRightTwist }, $whiteStart")
      graph.createEdge(vertexMap(source.id), vertexMap(target.id), dx, dy)
    }
    println(facesFrom(links).mkString("\n"))

    if (new OneFormTorus(graph).layout())
      Some(graph)
    else None
  }

  private def deltas(whiteStart: Boolean, source: NodeProps, target: NodeProps)
                    (implicit scale: Int): (Int, Int) = {
    /* NodeProps        deltas     cross   twist
     *  o--- x/cols     y          \  /    \   /
     *  |               |           \        /
     *  y/rows          o--- x     / \     /  \
     */
    (scale, source.isLeftTwist, source.isRightTwist, target.isLeftTwist, target.isRightTwist, whiteStart) match {
      // initial pair diagram
      case (1, _, _, _, _, _) => ((source.x - target.x).toInt, (source.y - target.y).toInt)
      // the left thread leaving a cross has a white start
      case (_, false, false, _, _, true) => (-1, -1)
      case (_, false, false, _, _, false) => (1, -1)
      // the right thread arriving at a cross has a white start
      case (_, _, _, false, false, true) => (1, -1)
      case (_, _, _, false, false, false) => (-1, -1)
      // threads arriving at twists not treated above
      case (_, _, _, _, true, true) => (0, -1)
      case (_, _, _, _, true, false) => (1, -1)
      case (_, _, _, true, _, false) => (0, -1)
      case (_, _, _, true, _, true) => (1, -1)
      // threads leaving twists not treated above
      case (_, _, true, _, _, true) => (0, -1)
      case (_, _, true, _, _, false) => (1, -1)
      case (_, true, _, _, _, false) => (0, -1)
      case (_, true, _, _, _, true) => (1, -1)
    }
  }

  private def inCenterBottomTile(link: LinkProps)
                                (implicit diagram: Diagram, scale: Int, config: TilesConfig) = {
    // The top and side tiles of a diagram may have irregularities along the outer edges.
    // So select links arriving in the center bottom checker tile.
    val cols = config.patchWidth / 3
    val rows = config.patchHeight / 2

    val target = diagram.node(link.target)
    val source = diagram.node(link.source)
    val y = unScale(target.y)
    val x = unScale(target.x)
    y >= rows && x >= cols && x < cols * 2 && !link.withPin && target.id != "" && source.id != ""
  }

  /** Revert [[NewPairDiagram]].toPoint
    *
    * @param i     value for x or y
    * @param scale value 1 or factor to also undo [[ThreadDiagram]].scale
    * @return x/y on canvas reduced to row/col number in the input
    */
  private def unScale(i: Double)(implicit scale: Int): Int = {
    i / scale / 15 - 2
    }.toInt
}
