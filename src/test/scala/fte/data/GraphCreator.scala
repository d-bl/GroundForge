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

import dibl.NewPairDiagram
import dibl.proto.TilesConfig
import fte.layout.OneFormTorus

object GraphCreator {

  /**
   * @param urlQuery see links to https://d-bl.github.io/GroundForge/tiles
   *                 patch size must match one checker matrix
   * @return
   */
  def from(urlQuery: String): Option[Graph] = {
    val config = TilesConfig(urlQuery)
    val matrix = config.getItemMatrix.map(_.map(_.relativeSources))
    val cols = config.centerMatrixCols // also offset to get generated graph within the viewport
    val graph = new Graph(config.centerMatrixRows, cols)

    def addNode(destRow: Int, destCol: Int): Unit = {
      val srcNodes = matrix(destRow)(destCol)
      if (srcNodes.isEmpty) return
      val (srcRow1, srcCol1) = srcNodes(0)
      val (srcRow2, srcCol2) = srcNodes(1)
      graph.addPairsIn(
        cols + destCol, destRow,
        cols + destCol + srcCol1, cols + destRow + srcRow1,
        cols + destCol + srcCol2, cols + destRow + srcRow2,
      )
    }

    for {
      row <- matrix.indices
      col <- matrix(row).indices
    } yield addNode(row, col)
    println("edges" + graph.getEdges)
    println("vertices" + graph.getVertices)
    if (new OneFormTorus(graph).layout())
      Some(graph)
    else None
  }

  def from2(urlQuery: String): Option[Graph] = {
    val config = TilesConfig(urlQuery)
    val graph = new Graph(config.patchHeight, config.patchWidth)
    val diagram = NewPairDiagram.create(config)
    diagram.links.foreach { link =>
      val src = diagram.node(link.source)
      val dest = diagram.node(link.target)
      graph.addEdge(src.y.toInt/15, src.x.toInt/15, dest.y.toInt/15, dest.x.toInt/15)
    }
    println("edges" + graph.getEdges)
    println("vertices" + graph.getVertices)
    if (new OneFormTorus(graph).layout())
      Some(graph)
    else None
  }
}
