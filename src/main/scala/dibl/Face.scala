package dibl

case class Face(leftArc: Seq[SimpleLink], rightArc: Seq[SimpleLink]) {
  override def toString: String = toS(leftArc) + " ; " + toS(rightArc)

  private def toS(rightArc: Seq[SimpleLink]) = {
    rightArc.map(link => s"${ link.sourceId }->${ link.targetId }").mkString(",")
  }
}

object Face {
  def facesFrom(linksInTile: Seq[SimpleLink]): Seq[Face] = {
    implicit val linksByTarget: Map[String, Seq[SimpleLink]] = linksInTile.groupBy(_.targetId)
    linksByTarget
      .values.toArray
      .map { links =>
        val (left, right) = closeFace(
          links.filter(_.isLeftOfTarget),
          links.filterNot(_.isLeftOfTarget))
        Face(left, right)
      }
  }

  @scala.annotation.tailrec
  private def closeFace(leftArc: Seq[SimpleLink], rightArc: Seq[SimpleLink])
                       (implicit linksByTarget: Map[String, Seq[SimpleLink]]
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
    else {
      val reversed = arc.reverse
      val i = reversed.map(_.sourceId).indexOf(sharedSources.head)
      reversed.slice(i, arc.size)
    }
  }

  private def sources(arc: Seq[SimpleLink]) = {
    arc.map(_.sourceId).toSet
  }
}