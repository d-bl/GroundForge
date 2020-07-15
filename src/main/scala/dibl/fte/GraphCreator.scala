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
package dibl.fte

import dibl.fte.data.{ Edge, Graph, Vertex }
import dibl.fte.layout.{ LocationsDFS, OneFormTorus }

import scala.annotation.tailrec
import scala.collection.JavaConverters._
import scala.util.Try

object GraphCreator {

  def graphFrom(topoLinks: Seq[TopoLink]): Try[(Graph, String)] = {
    val clockWise: Map[String, Seq[TopoLink]] = {
      val linksBySource = topoLinks.groupBy(_.sourceId)
      topoLinks.groupBy(_.targetId)
        .map { case (id, linksIn) =>
          val linksOut = linksBySource(id)
          (id, linksOut.filter(_.isRightOfSource)
            ++ linksOut.filter(_.isLeftOfSource)
            ++ linksIn.filter(_.isLeftOfTarget)
            ++ linksIn.filter(_.isRightOfTarget)
          )
        }
    }
    for {
      data <- Try(Data(Face(topoLinks), clockWise, topoLinks))
      deltas <- Delta(data, topoLinks)
      recursionStartId = topoLinks.head.sourceId
      nodes = locations(Map(recursionStartId -> (0, 0)), deltas)
      graph = initGraph(deltas, clockWise)
      //_ = println(s" ===== ${graph.getVertices.size()} =?= ${nodes.size}")
      tileVectors = new LocationsDFS(graph.getVertices, graph.getEdges).getVectors
      svg = SvgPricking(nodes, deltas, getTileVector(recursionStartId, deltas, nodes))
      //_ = println(graph.getVertices.asScala.map(v => v.getX -> v.getY ))
      //_ = println(nodes.values.toList)
      _ <- Try(Option(new OneFormTorus(graph).layout(tileVectors))
        .getOrElse(throw new Exception("no translation vectors found")))
      _ = println(s"tileVectors === ${ tileVectors.asScala.mkString("; ") } === ${ getTileVector(recursionStartId, deltas, nodes) }")
    } yield (graph, svg)
  }

  type Locations = Map[String, (Double, Double)]
  private type Deltas = Map[TopoLink, Delta]

  def getTileVector(recursionStartId: String, deltas: Map[TopoLink, Delta], nodes: Locations): Seq[(Double, Double)] = {
    deltas.keys
      .filter { // find links arriving at the start
        case TopoLink(_, `recursionStartId`, _, _) => true
        case _ => false
      }.tail
      .map { link =>
        val (x, y) = nodes(recursionStartId)
        val Delta(dX, dY) = deltas(link)
        // link start before the tile:
        val (x1, y1) = (x - dX, y - dY)
        // link start beyond the tile:
        val (x2, y2) = nodes(link.sourceId)
        (x2 - x1, y2 - y1)
      }
  }.toSeq

  @tailrec
  private def locations(nodes: Locations, deltas: Deltas): Locations = {
    // 45 calls for 663 nodes of whiting=F14_P193&tileStitch=crcrctclclcr
    // print(s" ${nodes.size}-${deltas.size} ")

    val nodeIds = nodes.keys.toSeq
    val candidates = deltas.filter {
      case (TopoLink(source, target, _, _), _) =>
        nodeIds.contains(source) && !nodeIds.contains(target)
    }
    if (candidates.isEmpty) nodes
    else {
      val newNodes = candidates.map {
        case (TopoLink(source, target, _, _), Delta(dx, dy)) =>
          val (x, y) = nodes(source)
          target -> (x + dx, y + dy)
      } ++ nodes
      val newNodeIds = newNodes.keys.toSeq
      locations(
        newNodes,
        deltas.filterNot(d => newNodeIds.contains(d._1.sourceId) && newNodeIds.contains(d._1.targetId))
        // less complex to calculate but depleting deltas less
        // (doesn't influence number of calls but reduces the filter to find candidates):
        // deltas.filterNot(delta => candidates.contains(delta._1))
      )
    }
  }

  private def initGraph(deltas: Deltas, clockWise: Map[String, Seq[TopoLink]]) = {
    val topoLinks: Seq[TopoLink] = deltas.keys.toSeq
    val graph = new Graph()

    // create vertices
    implicit val vertexMap: Map[String, Vertex] = topoLinks
      .map(_.targetId).distinct.zipWithIndex
      .map { case (nodeId, i) =>
        // The initial coordinates don't matter as long as they are unique.
        nodeId -> graph.createVertex(i, 0)
      }.toMap

    // create edges
    implicit val edges: Array[Edge] = deltas.map { case (link, Delta(dx, dy)) =>
      graph.addNewEdge(new Edge(
        vertexMap(link.sourceId),
        vertexMap(link.targetId)
      )).setDeltaX(dx).setDeltaY(dy)
    }.toArray

    // add edges to vertices in clockwise order
    clockWise.foreach { case (id, topoLinks) =>
      topoLinks.foreach(findEdge(_).foreach(vertexMap(id).addEdge))
    }
    graph
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
}
