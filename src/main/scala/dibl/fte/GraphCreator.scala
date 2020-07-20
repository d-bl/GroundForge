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

import scala.annotation.tailrec
import scala.util.Try

object GraphCreator {

  def graphFrom(topoLinks: Seq[TopoLink]): Try[String] = {
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
      startId = topoLinks.head.sourceId
      nodes = locations(Map(startId -> (0, 0)), deltas)
      svg = SvgPricking(nodes, deltas, meetHalfway(startId, deltas).toSeq)
    } yield svg
  }

  type Locations = Map[String, (Double, Double)]
  private type Deltas = Map[TopoLink, Delta]

  private def meetHalfway(startId: String, deltas: Deltas): Set[(Double, Double)] = {
    @tailrec
    def next(ins: Map[String, Delta],
             outs: Map[String, Delta],
            ): Set[(Double, Double)] = {
      val inIds = ins.keySet
      val outIds = outs.keySet
      //print(s"{$ins;$outs}")
      //print(s"${ inIds.size },${ outIds.size }; ")

      def inIns(location: (String, Delta)) = inIds.toSeq.contains(location._1)

      def inOuts(location: (String, Delta)) = outIds.toSeq.contains(location._1)

      /** sum of two followed paths (once they met each other) */
      def sum(id: String) = {
        //println()
        val Delta(dx1, dy1) = ins(id)
        val Delta(dx2, dy2) = outs(id)
        Delta(dx1 + dx2, dy1 + dy2).rounded
      }

      if (inIds.subsetOf(outIds)) inIds.map(sum)
      else if (outIds.subsetOf(inIds)) outIds.map(sum)
           else {
             val newIns = ins
               .withFilter(!inOuts(_))
               .flatMap { t =>
                 val (id, Delta(dx, dy)) = t
                 nextIn(id).map(follow(dx, dy))
               }
             val newOuts = outs
               .withFilter(!inIns(_))
               .flatMap { t =>
                 val (id, Delta(dx, dy)) = t
                 nextOut(id).map(follow(dx, dy))
               }
             if (ins.map(t => Math.abs(t._2.dx)).max > 5)
               return Set.empty // TODO emergency break for a never ending loop
             next(ins.filter(inOuts) ++ newIns, outs.filter(inIns) ++ newOuts)
           }
    }

    /** calculate the new length of the followed path */
    def follow(dx1: Double, dy1: Double)(t2: (String, Delta)) = {
      val (id, Delta(dx2, dy2)) = t2
      (id, Delta(dx1 + dx2, dy1 + dy2))
    }

    def nextIn(id: String) = deltas.withFilter {
      case (TopoLink(_, `id`, _, _), _) => true
      case _ => false
    }.map { case (TopoLink(id, _, _, _), delta) => (id, delta) }

    def nextOut(id: String) = deltas.withFilter {
      case (TopoLink(`id`, _, _, _), _) => true
      case _ => false
    }.map { case (TopoLink(_, id, _, _), delta) => (id, delta) }

    next(nextIn(startId), nextOut(startId))
  }

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
}
