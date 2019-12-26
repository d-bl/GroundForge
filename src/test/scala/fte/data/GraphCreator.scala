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

import dibl.Face.facesFrom
import dibl.LinkProps.WhiteStart
import dibl.proto.TilesConfig
import dibl.{ Diagram, LinkProps, NewPairDiagram, NodeProps, ThreadDiagram, TopoLink }
import fte.layout.OneFormTorus

import scala.collection.JavaConverters._

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
  def fromThreadDiagram(urlQuery: String): Option[Graph] = {
    implicit val config: TilesConfig = TilesConfig(urlQuery)
    implicit val diagram: Diagram = ThreadDiagram(NewPairDiagram.create(config))
    implicit val scale: Int = 2
    graphFrom(diagram.links.filter(inCenterBottomTile))
  }

  def fromPairDiagram(urlQuery: String): Option[Graph] = {
    implicit val config: TilesConfig = TilesConfig(urlQuery)
    implicit val diagram: Diagram = NewPairDiagram.create(config)
    implicit val scale: Int = 1
    graphFrom(diagram.links.filter(inCenterBottomTile))
  }

  private def graphFrom(unsortedLinks: Seq[LinkProps])
                       (implicit diagram: Diagram, scale: Int)= {
    val linksInTile = unsortedLinks
      .sortBy(l => diagram.node(l.target).id -> diagram.node(l.source).id)
    val graph = new Graph()
    val topoLinks = TopoLink.simplify(linksInTile)

    // create vertices
    implicit val vertexMap: Map[String, Vertex] = topoLinks
      .map(_.targetId).distinct.zipWithIndex
      .map { case (nodeId, i) =>
        // The initial coordinates don't matter as long as they are unique.
        nodeId -> graph.createVertex(i, 0)
      }.toMap
    println(vertexMap.keys.toArray.sortBy(identity).mkString(","))

    // create edges
    implicit val edges: Array[Edge] = linksInTile.map { link =>
      graph.addNewEdge(createEdge(link))
    }.toArray

    // add the edges to vertices in clock wise order
    topoLinks.groupBy(_.sourceId).foreach { case (id, topoLinks) =>
      val vertex = vertexMap(id)
      topoLinks.filter(_.isRightOfSource).foreach(addTo(vertex, _))
      topoLinks.filter(_.isLeftOfSource).foreach(addTo(vertex, _))
    }
    topoLinks.groupBy(_.targetId).foreach { case (id, topolinks) =>
      val vertex = vertexMap(id)
      topolinks.filter(_.isLeftOfTarget).foreach(addTo(vertex, _))
      topolinks.filter(_.isRightOfTarget).foreach(addTo(vertex, _))
    }

    val faces = facesFrom(topoLinks)
      .map(toJava).asJava

    // TODO faces not yet used in layout method
    if (new OneFormTorus(graph).layout(faces))
      Some(graph)
    else None
  }

  private def addTo(vertex: Vertex,
                    simpleLink: TopoLink)
                   (implicit edges: Array[Edge],
                    vertexMap: Map[String, Vertex]
                   ): Unit = {
    // TODO Vertex.addEdge should add at the end (once TopologicalLink.isLeftOfSource is fixed)
    findEdge(simpleLink).foreach(vertex.addEdge)
  }

  private def toJava(scalaFace: dibl.Face)
                    (implicit edges: Array[Edge],
                     vertexMap: Map[String, Vertex]
                    ) = {
    println(scalaFace)
    val faceEdges = new java.util.LinkedList[Edge]() {
      // TODO what order should the edges get?
      (scalaFace.leftArc ++ scalaFace.rightArc)
        .foreach(findEdge(_).foreach(add))
    }
    new Face() {
      setEdges(faceEdges)
    }
  }

  private def findEdge(link: TopoLink)
                      (implicit edges: Array[Edge],
                       vertexMap: Map[String, Vertex]
                      ): Option[Edge] = {
    edges.find(edge =>
      edge.getStart == vertexMap(link.sourceId) &&
        edge.getEnd == vertexMap(link.targetId)
    )
  }

  private def createEdge(link: LinkProps)
                        (implicit scale: Int, // TODO obsolete when deltas is
                         diagram: Diagram,
                         vertexMap: Map[String, Vertex]) = {
    val source = diagram.node(link.source)
    val target = diagram.node(link.target)
    val whiteStart = link.isInstanceOf[WhiteStart]
    val (dx, dy) = deltas(whiteStart, source, target) // TODO obsolete when booleans of TopologicalLink are fixed
    println(s"(${ source.id },${ target.id }) deltas($dx,$dy) ${ source.isLeftTwist }, ${ source.isRightTwist }, ${ target.isLeftTwist }, ${ target.isRightTwist }, $whiteStart")
    val edge = new Edge(vertexMap(source.id), vertexMap(target.id), dx, dy)
    edge
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
    }
  }

  def inCenterBottomTile(link: LinkProps)
                        (implicit diagram: Diagram, scale: Int, config: TilesConfig): Boolean = {
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
