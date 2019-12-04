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
import dibl.{ LinkProps, NewPairDiagram, ThreadDiagram }
import fte.layout.OneFormTorus

object GraphCreator {

  /**
   * @param urlQuery parameters for: https://d-bl.github.io/GroundForge/tiles?
   *                 The patch size must span 3 columns and 2 rows of checker tiles.
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
    val cols = config.patchWidth / 3
    val rows = config.patchHeight / 2
    val graph = new Graph(rows, cols)
    val diagram = NewPairDiagram.create(config)

    def inCenterBottomTile(link: LinkProps): Boolean = {
      // The top and side margins of the pair diagram may have irregularities.
      val target = diagram.nodes(link.target)
      val y = unScale(target.y)
      val x = unScale(target.x)
      y >= rows && x >= cols && x < cols * 2
    }

    diagram.links.filter(inCenterBottomTile)
      .foreach { link =>
        val src = diagram.node(link.source)
        val dest = diagram.node(link.target)
        graph.addEdge(
          unScale(dest.x) - cols, unScale(dest.y) - rows,
          unScale(src.x) - cols, unScale(src.y) - rows,
        )
      }
    if (new OneFormTorus(graph).layout())
      Some(graph)
    else None
  }

  /** Revert [[NewPairDiagram]].toPoint
    * TODO see also [[ThreadDiagram]].scale
    */
  private def unScale(i: Double): Int = {
    i / 15 - 2
  }.toInt
}
