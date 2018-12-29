package dibl

import dibl.Force.Point

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("NewPairDiagram") object NewPairDiagram {

  @JSExport
  def create(config: TilesConfig): Diagram = {

    val itemMatrix = config.itemMatrix
    val rows: Int = itemMatrix.length
    val cols: Int = itemMatrix.head.length
    var seqNr = 0

    def toPoint(row: Double, col: Double) = {
      Point(x = 30 + 15 * col, y = 30 + 15 * row)
    }

    val northEastNode = toPoint(0, 0)
    val southWestNode = toPoint(rows - 1, cols - 1)

    def isFringe(startPoint: Point): Boolean = {
      startPoint.x < northEastNode.x || startPoint.x > southWestNode.x || startPoint.y < northEastNode.y
    }

    /** @param point  one of the tips of a V, the legs may lie flat but never collapse
      * @param target the bottom of the V, makes the point unique when we have to search for it
      * @return
      */
    def toSimpleNode(point: Point, target: Node) = {
      seqNr += 1
      SimpleNode(seqNr, point, target.target)
    }

    def toNodeSeq(row: Int, col: Int): Seq[ConnectedNode] = {
      val item = itemMatrix(row)(col)
      if(item.relativeSources.isEmpty) return Seq.empty
      val Array((leftRow, leftCol), (rightRow, rightCol)) = item.relativeSources
          val sourceLeft = toPoint(row + leftRow, col + leftCol)
          val sourceRight = toPoint(row + rightRow, col + rightCol)
          val target = toPoint(row, col)

          val colorName = Stitches.defaultColorName(item.stitch)
          seqNr += 1
          val node = Node(seqNr, target, sourceLeft, sourceRight, item.stitch, item.id, colorName)
          (isFringe(sourceLeft), isFringe(sourceRight)) match {
            case (false, false) => Seq(node)
            case (true, false) => Seq(node, toSimpleNode(sourceLeft, node))
            case (false, true) => Seq(node, toSimpleNode(sourceRight, node))
            case (true, true) => Seq(node, toSimpleNode(sourceLeft, node), toSimpleNode(sourceRight, node))
          }
    }

    val nodes: Seq[ConnectedNode] = Seq(SimpleNode(0, Point(0, 0), Point(0, 0))) ++
      (0 until rows).flatMap(row =>
        (0 until cols).map(col =>
          toNodeSeq(row, col)
        )
      ).flatten

    // lookup table
    val nodeMap: Map[(Point, Point), Int] = nodes.map {
      case n: Node => (n.target, n.target) -> n.seqNr
      case n: SimpleNode => (n.source, n.target) -> n.seqNr
    }.toMap

    def findNode(source: Point, target: Point): ConnectedNode = {
      // try to find a SimpleNode, if not found, try a Node, fall back to first dummy node
      nodes(nodeMap.getOrElse((source, target), nodeMap.getOrElse((source, source), 0)))
    }

    def toPairLinks(target: Node) = {
      val leftNode = findNode(target.srcLeft, target.target)
      val rightNode = findNode(target.srcRight, target.target)
      Seq(
        LinkProps.pairLink(
          source = leftNode.seqNr,
          target = target.seqNr,
          start = leftNode.color,
          mid = leftNode.twistsToLeftOf(target) - 1,
          end = target.color),
        LinkProps.pairLink(
          source = rightNode.seqNr,
          target = target.seqNr,
          start = rightNode.color,
          mid = rightNode.twistsToRightOf(target) - 1,
          end = target.color)
      )
    }

    var pairNr = 0
    Diagram(
      nodes.map {
        case SimpleNode(_, Point(x, y), _) =>
          pairNr += 1 // TODO why does it start with 3?
          NodeProps.node(s"Pair $pairNr", x, y)
        case Node(_, Point(x, y), _, _, stitch, id, color) => NodeProps.node(s"$stitch - $id", color, x, y)
      },
      nodes.withFilter(_.isInstanceOf[Node])
        .flatMap { case target: Node => toPairLinks(target) }
    )
  }

  private trait ConnectedNode {
    val seqNr: Int
    val target: Point
    val color: String

    def twistsToLeftOf(target: Node) = 0

    def twistsToRightOf(target: Node) = 0
  }

  private case class SimpleNode(override val seqNr: Int,
                                source: Point,
                                override val target: Point
                               ) extends ConnectedNode {
    override val color = "pair" // the "color" is a named marker alias customized "arrow head"
  }

  private case class Node(override val seqNr: Int,
                          override val target: Point,
                          srcLeft: Point,
                          srcRight: Point,
                          stitch: String,
                          id: String,
                          override val color: String) extends ConnectedNode {
    private val openingTwists: String = stitch.replaceAll("c.*", "").replaceAll("t", "lr")
    private val closingTwists = stitch.replaceAll(".*c", "").replaceAll("t", "lr")
    private val openingTwistsLeft: Int = openingTwists.count(_ == 'l')
    private val openingTwistsRight: Int = openingTwists.count(_ == 'r')
    private val closingTwistsLeft: Int = closingTwists.count(_ == 'l')
    private val closingTwistsRight: Int = closingTwists.count(_ == 'r')

    // stitches (X's) arranged in ascii art:
    // X-X source
    // -X- target
    // the caller knows if this (the source) is the left or right stitch

    override def twistsToLeftOf(target: Node): Int = closingTwistsRight + target.openingTwistsLeft

    override def twistsToRightOf(target: Node): Int = closingTwistsLeft + target.openingTwistsRight
  }

}
