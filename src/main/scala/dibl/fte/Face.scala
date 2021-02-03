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

case class Face(leftArc: Seq[TopoLink], rightArc: Seq[TopoLink]) {
  val counterClockWise: Seq[TopoLink] = leftArc ++ rightArc.reverse

  override def toString: String = toS(leftArc) + " ; " + toS(rightArc)

  private def toS(links: Seq[TopoLink]) = {
    links.map(link => s"${ link.sourceId }->${ link.targetId }").mkString(",")
  }
}

object Face {

  def apply(links: Seq[TopoLink]): Seq[Face] = {
    implicit val linksByTarget: Map[String, Seq[TopoLink]] = links.groupBy(_.targetId)
    linksByTarget.values
      .map { links =>
        val (left, right) = closeFace(
          links.filter(_.isLeftOfTarget),
          links.filterNot(_.isLeftOfTarget))
        Face(left, right)
      }
  }.toSeq

  @scala.annotation.tailrec
  private def closeFace(leftArc: Seq[TopoLink], rightArc: Seq[TopoLink])
                       (implicit linksByTarget: Map[String, Seq[TopoLink]]
                       ): (Seq[TopoLink], Seq[TopoLink]) = {
    val sharedSources = sources(leftArc).intersect(sources(rightArc))
    if (sharedSources.nonEmpty)
      (trim(leftArc, sharedSources), trim(rightArc, sharedSources))
    else {
      val leftSourceId = leftArc.last.sourceId
      val rightSourceId = rightArc.last.sourceId
      val toLeftSource = linksByTarget(leftSourceId).filter(_.isRightOfTarget)
      val toRightSource = linksByTarget(rightSourceId).filter(_.isLeftOfTarget)
      closeFace(leftArc ++ toLeftSource, rightArc ++ toRightSource)
    }
  }

  private def trim(arc: Seq[TopoLink], sharedSources: Set[String]): Seq[TopoLink] = {
    if (sharedSources.isEmpty) arc
    else {
      val reversed = arc.reverse
      val i = reversed.map(_.sourceId).indexOf(sharedSources.head)
      reversed.slice(i, arc.size)
    }
  }

  private def sources(arc: Seq[TopoLink]) = {
    arc.map(_.sourceId).toSet
  }
}