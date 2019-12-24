package dibl

import dibl.LinkProps.WhiteStart

case class SimpleLink(sourceId: String, targetId: String, isLeftOfTarget: Boolean) {
  val isRightOfTarget: Boolean = !isLeftOfTarget
  val isRightOfSource: Boolean = !isLeftOfTarget
  val isLeftOfSource: Boolean = isLeftOfTarget
}

object SimpleLink {
  def simplify(linksInOneTile: Seq[LinkProps])
              (implicit  diagram: Diagram): Seq[SimpleLink] = {
    implicit val complexLinks: Seq[LinkProps] = linksInOneTile
    linksInOneTile.map(link => SimpleLink(sourceIdOf(link), targetIdOf(link), isLeft(link)))
  }

  private def isLeft(link: LinkProps)
                    (implicit diagram: Diagram,
                     linksInTile: Seq[LinkProps]
                    ): Boolean = {
    (
      diagram.node(link.target).instructions,
      link.isInstanceOf[WhiteStart]
    ) match {
      /*    cross   twist
       *    \  /    \   /
       *     \        /
       *    / \     /  \
       */
      case ("cross", true) => true
      case ("cross", false) => false
      case ("twist", false) => true
      case ("twist", true) => false
      case _ => // TODO relatively expensive lookup for large matrices
        val otherX: Double = linksInTile.find(other =>
          sourceIdOf(other) != sourceIdOf(link) &&
            targetIdOf(other) == targetIdOf(link)
        ).map(other => diagram.node(other.source).x).getOrElse(0)
        diagram.node(link.source).x < otherX
    }
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
