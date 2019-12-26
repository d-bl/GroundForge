package dibl

import dibl.LinkProps.WhiteStart

case class TopoLink(sourceId: String, targetId: String, isLeftOfTarget: Boolean, isLeftOfSource: Boolean) {
  val isRightOfTarget: Boolean = !isLeftOfTarget
  val isRightOfSource: Boolean = !isLeftOfTarget

  override def toString: String = s"$sourceId,$targetId,$isLeftOfTarget,$isLeftOfSource"
    .replaceAll("(rue|alse)", "")
}

object TopoLink {

  /** reduces diagram info of a tile to topological info embedded on a flat torus */
  def simplify(linksInOneTile: Seq[LinkProps])
              (implicit diagram: Diagram): Seq[TopoLink] = {
    implicit val complexLinks: Seq[LinkProps] = linksInOneTile
    linksInOneTile.map(link => {
      isLeftOfSource(link)
      // TODO so far fixing isLeftOfSource causes exceptions,
      //  it should simplify Vertex.addEdge and GraphCreator.deltas
      TopoLink(sourceOf(link).id, targetOf(link).id, isLeftOfTarget(link), isLeftOfSource = isLeftOfTarget(link))
    })
  }

  private def isLeftOfTarget(link: LinkProps)
                            (implicit diagram: Diagram,
                             linksInTile: Seq[LinkProps]
                            ): Boolean = {
    def isSiblingAtTarget(other: LinkProps) = {
      sourceOf(other).id != sourceOf(link).id &&
        targetOf(other).id == targetOf(link).id
    }

    def isLeftPairOfTarget = {
      val otherX: Double = linksInTile
        .find(isSiblingAtTarget) // TODO relatively expensive lookup for large matrices
        .map(other => sourceOf(other).x)
        .getOrElse(0)
      sourceOf(link).x < otherX
    }

    isLeft(
      targetOf(link).instructions,
      link.isInstanceOf[WhiteStart],
      isLeftPairOfTarget _
    )
  }

  private def isLeftOfSource(link: LinkProps)
                            (implicit diagram: Diagram,
                             linksInTile: Seq[LinkProps]
                            ): Boolean = {
    def isSiblingAtSource(other: LinkProps) = {
      targetOf(other).id != targetOf(link).id &&
        sourceOf(other).id == sourceOf(link).id
    }

    def isLeftPairAtSource = {
      val otherX: Double = linksInTile
        .find(isSiblingAtSource) // TODO relatively expensive lookup for large matrices
        .map(other => targetOf(other).x)
        .getOrElse(0)
      targetOf(link).x < otherX
    }

    isLeft(
      sourceOf(link).instructions,
      link.isInstanceOf[WhiteStart],
      isLeftPairAtSource _
    )
  }

  private def isLeft(sourceInstructions: String,
                     isWhiteStart: Boolean,
                     isLeftPair: () => Boolean
                    ) = {
    (sourceInstructions, isWhiteStart) match {
      /*    cross   twist
       *    \  /    \   /
       *     \        /
       *    / \     /  \
       */
      case ("cross", true) => true
      case ("cross", false) => false
      case ("twist", false) => true
      case ("twist", true) => false
      case _ => isLeftPair()
    }
  }

  private def sourceOf(l: LinkProps)
                      (implicit diagram: Diagram) = {
    diagram.node(l.source)
  }

  private def targetOf(l: LinkProps)
                      (implicit diagram: Diagram) = {
    diagram.node(l.target)
  }
}
