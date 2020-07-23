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
package dibl.fte

import scala.annotation.tailrec

object TileVector {
  def apply(startId: String, deltas: Deltas): Set[(Double, Double)] = {

    /**
      *
      * @param ins  vectors arriving at startId
      *             the keys are the start id as far as traveled
      *             the values are the sum of the traveled steps
      * @param outs vectors leaving from startId
      *             the keys are the id at the end as far as traveled
      *             the values are the sum of the traveled steps
      * @return the sums of vector pairs from both sets with the same id
      */
    @tailrec
    def next(ins: Map[String, Delta],
             outs: Map[String, Delta],
            ): Set[(Double, Double)] = {
      val inIds = ins.keySet
      val outIds = outs.keySet
      //print(s"{$inIds;$outIds}")
      //print(s"${ inIds.size },${ outIds.size }; ")

      /** sum of two followed paths (once they met each other) */
      def sum(id: String) = {
        //println()
        val Delta(dx1, dy1) = ins(id)
        val Delta(dx2, dy2) = outs(id)
        Delta(dx1 + dx2, dy1 + dy2).rounded
      }

      // we found the maximum number of vectors when
      // one set of keys is the subset of another
      if (inIds.subsetOf(outIds)) inIds.map(sum)
      else if (outIds.subsetOf(inIds)) outIds.map(sum)
           else {

             /** @return true if the vector arriving at startId
               *         starts at the endpoint of a vector leaving startId
               */
             def inOuts(location: (String, Delta)) = outIds.toSeq.contains(location._1)

             // extend the vectors arriving at startId
             // as far as their start is not and end of vectors leaving startId
             val newIns = ins.withFilter(!inOuts(_))
               .flatMap { t =>
                 val (id, Delta(dx, dy)) = t
                 nextIn(id).map(follow(dx, dy))
               }

             /** @return true if the vector leaving startId
               *         arrives at the endpoint of a vector leaving startId
               *         either old vectors or extended vectors
               */
             def inIns(location: (String, Delta)) = {
               val id = location._1
               inIds.toSeq.contains(id) || newIns.keys.toSeq.contains(id)
             }

             // extend the vectors leaving from startId
             // as far as their end is not a start of vectors arriving at startId
             // either old vectors or extended vectors
             val newOuts = outs.withFilter(!inIns(_))
               .flatMap { t =>
                 val (id, Delta(dx, dy)) = t
                 nextOut(id).map(follow(dx, dy))
               }

             // terminate infinite recursion loop or next step
             if (ins.map(t => Math.abs(t._2.dx)).max > 3) {
               println(s"escaping from possible infinite loop: in($inIds${ newIns.keys }) out($outIds${ newOuts.keys })")
               inIds.withFilter(outIds.contains).map(sum)
             }
             else next(
               ins.filter(inOuts) ++ (newIns),
               outs.filter(inIns) ++ newOuts,
             )
           }
    }

    /** calculate the new length of the followed path */
    def follow(dx1: Double, dy1: Double)(t2: (String, Delta)) = {
      val (id, Delta(dx2, dy2)) = t2
      (id, Delta(dx1 + dx2, dy1 + dy2))
    }

    /** @param id end point of returned vectors */
    def nextIn(id: String) = deltas.withFilter {
      case (TopoLink(_, `id`, _, _), _) => true
      case _ => false
    }.map { case (TopoLink(id, _, _, _), delta) => (id, delta) }

    /** @param id start point of returned vectors */
    def nextOut(id: String) = deltas.withFilter {
      case (TopoLink(`id`, _, _, _), _) => true
      case _ => false
    }.map { case (TopoLink(_, id, _, _), delta) => (id, delta) }

    // recursion start
    next(nextIn(startId), nextOut(startId))
  }
}
