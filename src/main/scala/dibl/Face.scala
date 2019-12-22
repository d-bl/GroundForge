package dibl

import dibl.LinkProps.WhiteStart

case class Face(leftArc: Seq[LinkProps], rightArc: Seq[LinkProps])
               (implicit diagram: Diagram) {
  override def toString: String = toS(leftArc) + " ; " + toS(rightArc)

  private def toS(rightArc: Seq[LinkProps]) = {
    rightArc.reverse.map(link => s"${Face.sourceIdOf(link)}->${Face.targetIdOf(link)}").mkString(",")
  }
}

object Face {
  def facesFrom(linksInTile: Seq[LinkProps])
               (implicit diagram: Diagram
               ): Seq[Face] = {
    implicit val linksByTarget: Map[String, Seq[LinkProps]] = linksInTile
      .groupBy(targetIdOf(_))
    linksByTarget
      .values.toArray
      .map { links =>
        val min = links.minBy(leftFirst(_))
        val max = links.maxBy(leftFirst(_))
        val (left, right) = closeFace(Seq(min), Seq(max))
        Face(left, right)
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
  private def closeFace(leftArc: Seq[LinkProps], rightArc: Seq[LinkProps])
                       (implicit diagram: Diagram, linksByTarget: Map[String, Seq[LinkProps]]
                       ): (Seq[LinkProps], Seq[LinkProps]) = {
    val sharedSources = sources(leftArc).intersect(sources(rightArc))
    // TODO no clue why check for size still prevents eternal loops
    if (sharedSources.nonEmpty || leftArc.size > 17 || rightArc.size > 17)
      (trim(leftArc, sharedSources), trim(rightArc, sharedSources))
    else {
      val leftSourceId = sourceIdOf(leftArc.last)
      val rightSourceId = sourceIdOf(rightArc.last)
      val toLeftSource: LinkProps = linksByTarget(leftSourceId).minBy(leftFirst(_))
      val toRightSource: LinkProps = linksByTarget(rightSourceId).maxBy(leftFirst(_))
      closeFace(leftArc :+ toLeftSource, rightArc :+ toRightSource)
    }
  }

  private def trim(arc: Seq[LinkProps], sharedSources: Set[String])
                  (implicit diagram: Diagram): Seq[LinkProps] = {
    if(sharedSources.isEmpty) arc
    else arc.slice(0, arc.map(sourceIdOf).indexOf(sharedSources.head))
  }

  private def sources(arc: Seq[LinkProps])
                     (implicit diagram: Diagram) = {
    arc.map(sourceIdOf(_)).toSet
  }

  private def sourceIdOf(l: LinkProps)
                        (implicit diagram: Diagram) = {
    diagram.node(l.source).id
  }

  private def targetIdOf(l: LinkProps)
                        (implicit diagram: Diagram) = {
    diagram.node(l.target).id
  }
}