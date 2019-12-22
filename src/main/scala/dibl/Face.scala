package dibl

import dibl.LinkProps.WhiteStart

case class Face(leftArc: Seq[SimpleLink], rightArc: Seq[SimpleLink])
               (implicit diagram: Diagram) {
  override def toString: String = toS(leftArc) + " ; " + toS(rightArc)

  private def toS(rightArc: Seq[SimpleLink]) = {
    rightArc.reverse.map(link => s"${link.sourceId}->${link.targetId}").mkString(",")
  }
}

case class SimpleLink(sourceId: String, targetId: String, isLeftOfTarget: Boolean) {
  val isRightOfTarget: Boolean = !isLeftOfTarget
  val isRightOfSource: Boolean = !isLeftOfTarget
  val isLeftOfSource: Boolean = isLeftOfTarget
}

object Face {
  def facesFrom(linksInTile: Seq[LinkProps])
               (implicit diagram: Diagram): Seq[Face] = {
    implicit val linksByTarget: Map[String, Seq[SimpleLink]] = linksInTile
      .map(link => SimpleLink(sourceIdOf(link), targetIdOf(link), isLeft(link)))
      .groupBy(_.targetId)
    linksByTarget
      .values.toArray
      .map { links =>
        val (left, right) = closeFace(
          links.filter(_.isLeftOfTarget),
          links.filterNot(_.isLeftOfTarget)
        )
        Face(left, right)
      }
  }
  private def isLeft(linkProps: LinkProps)(implicit diagram: Diagram): Boolean = {
    (
      diagram.node(linkProps.target).instructions,
      linkProps.isInstanceOf[WhiteStart]
    ) match {
      /*        cross   twist
       *        \  /    \   /
       *         \        /
       *        / \     /  \
       */
      case ("cross", true) => true
      case ("cross", false) => false
      case ("twist", false) => true
      case ("twist", true) => false
      case _ => ??? // TODO need other link for pair diagram,
    }
  }

  @scala.annotation.tailrec
  private def closeFace(leftArc: Seq[SimpleLink], rightArc: Seq[SimpleLink])
                       (implicit diagram: Diagram, linksByTarget: Map[String, Seq[SimpleLink]]
                       ): (Seq[SimpleLink], Seq[SimpleLink]) = {
    val sharedSources = sources(leftArc).intersect(sources(rightArc))
    if (sharedSources.nonEmpty)
      (trim(leftArc, sharedSources), trim(rightArc, sharedSources))
    else {
      val leftSourceId = leftArc.last.sourceId
      val rightSourceId = rightArc.last.sourceId
      val toLeftSource = linksByTarget(leftSourceId).find(_.isRightOfSource).head
      val toRightSource = linksByTarget(rightSourceId).find(_.isLeftOfSource).head
      closeFace(leftArc :+ toLeftSource, rightArc :+ toRightSource)
    }
  }

  private def trim(arc: Seq[SimpleLink], sharedSources: Set[String]): Seq[SimpleLink] = {
    if (sharedSources.isEmpty) arc
    else arc.slice(0, arc.map(_.sourceId).indexOf(sharedSources.head))
  }

  private def sources(arc: Seq[SimpleLink]) = {
    arc.map(_.sourceId).toSet
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