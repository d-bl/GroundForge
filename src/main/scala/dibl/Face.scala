package dibl

case class Face(leftArc: Seq[TopoLink], rightArc: Seq[TopoLink]) {
  override def toString: String = toS(leftArc) + " ; " + toS(rightArc)

  private def toS(rightArc: Seq[TopoLink]) = {
    rightArc.map(link => s"${ link.sourceId }->${ link.targetId }").mkString(",")
  }
}

object Face {
  def facesFrom(linksInTile: Seq[TopoLink]): Seq[Face] = {
    implicit val linksByTarget: Map[String, Seq[TopoLink]] = linksInTile.groupBy(_.targetId)
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
  private def closeFace(leftArc: Seq[TopoLink], rightArc: Seq[TopoLink])
                       (implicit linksByTarget: Map[String, Seq[TopoLink]]
                       ): (Seq[TopoLink], Seq[TopoLink]) = {
    val sharedSources = sources(leftArc).intersect(sources(rightArc))
    if (sharedSources.nonEmpty)
      (trim(leftArc, sharedSources), trim(rightArc, sharedSources))
    else {
      val leftSourceId = leftArc.last.sourceId // TODO no such element exception for torchon pair diagram
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