package dibl

import dibl.LinkProps.WhiteStart

case class TopoLink(sourceId: String, targetId: String, isLeftOfTarget: Boolean, isLeftOfSource: Boolean) {
  val isRightOfTarget: Boolean = !isLeftOfTarget
  val isRightOfSource: Boolean = !isLeftOfTarget

  override def toString: String = s"$sourceId,$targetId,$isRightOfTarget,$isLeftOfSource"
    .replaceAll("(rue|alse)", "")
}

object TopoLink {

  /** reduces diagram info of a tile to topological info embedded on a flat torus */
  def simplify(linksInOneTile: Seq[LinkProps])
              (implicit diagram: Diagram): Seq[TopoLink] = {
    implicit val complexLinks: Seq[LinkProps] = linksInOneTile
    linksInOneTile.map(link => {
      isLeftOfSource(link)
      val bool = isLeftOfTarget(link)
      // TODO so far attempts to fix isLeftOfSource caused eternal loops,
      //  it should simplify Vertex.addEdge and GraphCreator.deltas
      TopoLink(sourceIdOf(link), targetIdOf(link), bool, isLeftOfSource = bool)
    })
  }

  private def isLeftOfTarget(link: LinkProps)
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

  private def isLeftOfSource(link: LinkProps)
                            (implicit diagram: Diagram,
                             linksInTile: Seq[LinkProps]
                            ): Boolean = {
    (
      diagram.node(link.source).instructions,
      link.isInstanceOf[WhiteStart]
    ) match {
      /*    cross   twist
       *    \  /    \   /
       *     \        /
       *    / \     /  \
       */
      case ("cross", true) => false
      case ("cross", false) => true
      case ("twist", false) => false
      case ("twist", true) => true
      case _ => // TODO relatively expensive lookup for large matrices
        val otherX: Double = linksInTile.find(other =>
          sourceIdOf(other) == sourceIdOf(link) &&
            targetIdOf(other) != targetIdOf(link)
        ).map(other => diagram.node(other.target).x).getOrElse(0)
        diagram.node(link.target).x < otherX
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
