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

import dibl.proto.TilesConfig
import dibl.{LinkProps, NewPairDiagram, NodeProps, ThreadDiagram}
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
  def fromPairs(urlQuery: String): Option[Graph] = {
    val config = TilesConfig(urlQuery)

    //val diagram = ThreadDiagram(NewPairDiagram.create(config))
    //implicit val scale: Int = 2
    val diagram = NewPairDiagram.create(config)
    implicit val scale: Int = 1

    val cols = config.patchWidth / 3
    val rows = config.patchHeight / 2

    def inCenterBottomTile(link: LinkProps): Boolean = {
      // The top and side tiles of a pair diagram may have irregularities in the margins.
      val target = diagram.nodes(link.target)
      val y = unScale(target.y)
      val x = unScale(target.x)
      y >= rows && x >= cols && x < cols * 2
      // TODO discard invisible links to pins in thread diagrams
    }

    val links = diagram.links.filter(inCenterBottomTile)
    val graph = new Graph(rows, cols)

    // adds each vertex on the torus once
    val vertexMap = links.map(_.target).sortBy(identity).distinct.map { i =>
      val t = diagram.node(i)
      t.id -> graph.createVertex(unScale(t.x) - cols, unScale(t.y) - rows)
    }.toMap

    links.foreach { link =>
      val source = diagram.node(link.source)
      val target = diagram.node(link.target)
      val (dx, dy) = deltas(link, source, target)
      graph.createEdge(vertexMap(source.id), vertexMap(target.id), dx, dy)
    }
    println(vertexMap.toArray.sortBy(_._2.toString).mkString("\n"))
    println("edges " + graph.getEdges.toArray.sortBy(_.toString).mkString("; "))

    if (new OneFormTorus(graph).layout())
      Some(graph)
    else None
  }

  private def deltas(link: LinkProps, source: NodeProps, target: NodeProps)(implicit scale: Int): (Int, Int) = {
    (scale, source.instructions, link.start) match {
      // initial pair diagram
      case (1, _, _) => ((source.x - target.x).toInt, (source.y - target.y).toInt)
      // the left thread leaving a cross has a white start
      case (_, "cross", "white") => (-1, 1)
      case (_, "cross", _) => (1, 1)
      // the left thread leaving a twist has a white start
      case (_, _, "white") => (1, 1)
      case _ => (-1, 1)
    }
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
