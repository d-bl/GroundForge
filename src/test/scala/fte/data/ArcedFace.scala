package fte.data

import dibl.LinkProps.WhiteStart
import dibl.{Diagram, LinkProps}

case class ArcedFace(leftArc: Seq[LinkProps], rightArc: Seq[LinkProps])
                    (implicit diagram: Diagram) {
  override def toString: String = toS(leftArc.reverse) + ";" + toS(rightArc)

  private def toS(rightArc: Seq[LinkProps]) = {
    rightArc.map{ l =>
      val sourceId = diagram.node(l.source).id
      val targetId = diagram.node(l.target).id
      s"$sourceId->$targetId"
    }.mkString(",")
  }
}

object ArcedFace {
  def facesFrom(linksInTile: Seq[LinkProps])
               (implicit diagram: Diagram
               ): Seq[ArcedFace] = {
    implicit val linksByTarget: Map[String, Seq[LinkProps]] = linksInTile
      .groupBy(l => diagram.node(l.target).id)
    linksByTarget
      .values.toArray
      .map { links =>
        val min = links.minBy(leftFirst(_))
        val max = links.maxBy(leftFirst(_))
        val (left, right) = closeFace(Seq(min), Seq(max))
        ArcedFace(left, right)
      }
  }

  private def leftFirst(link: LinkProps)
                       (implicit diagram: Diagram) = {
    /*        cross   twist
     *        \  /    \   /
     *         \        /
     *        / \     /  \
     */
    val isCross = diagram.node(link.target).instructions == "cross"
    val sourceX = diagram.node(link.source).x // might be the same in thread diagrams
    val whiteStart = link.isInstanceOf[WhiteStart]
    (sourceX, (isCross, whiteStart) match {
      case (true, true) => 0
      case (true, false) => 1
      case (false, false) => 0
      case (false, true) => 1
    })
  }

  @scala.annotation.tailrec
  private def closeFace(left: Seq[LinkProps], right: Seq[LinkProps])
                       (implicit diagram: Diagram, linksByTarget: Map[String, Seq[LinkProps]]
                       ): (Seq[LinkProps], Seq[LinkProps]) = {
    val leftSourceId = diagram.node(left.last.source).id
    val rightSourceId = diagram.node(right.last.source).id
    lazy val toLeftSource: LinkProps = linksByTarget(leftSourceId).minBy(leftFirst(_))
    lazy val toRightSource: LinkProps = linksByTarget(rightSourceId).maxBy(leftFirst(_))
    // TODO check for size is an emergency break on the current eternal loops
    if (leftSourceId == rightSourceId || left.size > 7 || right.size > 7) (left, right)
    else if (diagram.node(toLeftSource.source).id == rightSourceId) (left :+ toLeftSource, right)
    else if (diagram.node(toRightSource.source).id == rightSourceId) (left, right :+ toRightSource)
    else closeFace(left :+ toLeftSource, right :+ toRightSource)
  }
}