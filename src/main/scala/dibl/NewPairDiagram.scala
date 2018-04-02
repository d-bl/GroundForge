package dibl

import dibl.Force.Point
import dibl.Matrix.toRelativeSources

import scala.scalajs.js.annotation.JSExport

@JSExport
object NewPairDiagram {

  @JSExport
  def create(config: Config): Diagram = {

    val itemMatrix = config.itemMatrix
    val rows: Int = itemMatrix.length
    val cols: Int = itemMatrix.head.length
    var seqNr = 0

    def toSimpleNode(point: Point) = {
      seqNr += 1
      SimpleNode(seqNr, point)
    }

    val northEastNode = toPoint(0, 0)
    val southWestNode = toPoint(rows - 1, cols - 1)

    def isFringe(startPoint: Point): Boolean = {
      startPoint.x < northEastNode.x || startPoint.x > southWestNode.x || startPoint.y < northEastNode.y
    }

    def toNodeSeq(row: Int, col: Int): Seq[ConnectedNode] = {
      val item = itemMatrix(row)(col)
      val relativeSources = toRelativeSources(item.vectorCode)
      if (relativeSources.isEmpty) return Seq.empty // vectorCode == '-' or invalid
      val Array((leftRow, leftCol), (rightRow, rightCol)) = relativeSources

      val sourceLeft = toPoint(row + leftRow, col + leftCol)
      val sourceRight = toPoint(row + rightRow, col + rightCol)
      val target = toPoint(row, col)
      val colorName = Stitches.defaultColorName(item.stitch)
      seqNr += 1
      val node = Node(seqNr, target, sourceLeft, sourceRight, item.stitch, item.id, colorName)
      (isFringe(sourceLeft), isFringe(sourceRight)) match {
        case (false, false) => Seq(node)
        case (true, false) => Seq(node, toSimpleNode(sourceLeft))
        case (false, true) => Seq(node, toSimpleNode(sourceRight))
        case (true, true) => Seq(node, toSimpleNode(sourceRight), toSimpleNode(sourceLeft))
      }
    }

    val nodes: Seq[ConnectedNode] = Seq(SimpleNode(0, Point(0, 0))) ++
      (0 until cols).flatMap(row =>
        (0 until rows).map(col =>
          toNodeSeq(row, col)
        )
      ).flatten

    val nodeMap: Map[Point, Int] = nodes.map(n => n.target -> n.seqNr).toMap

    def findNode(point: Point): ConnectedNode = nodes(nodeMap.getOrElse(point, 0))

    def toPairLinks(target: Node) = {
      val leftNode = findNode(target.srcLeft)
      val rightNode = findNode(target.srcRight)
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

    Diagram(
      nodes.map {
        case SimpleNode(_, Point(x, y)) => NodeProps.node("Pair 0", x, y) // TODO pair numbers, close sides
        case Node(_, Point(x, y), _, _, stitch, id, color) => NodeProps.node(s"$stitch - $id", color, x, y)
      },
      nodes.withFilter(_.isInstanceOf[Node])
        .flatMap { case target: Node => toPairLinks(target) }
    )
  }

  private def toPoint(row: Double, col: Double) = Point(x = 30 + 15 * col, y = 30 + 15 * row)

  private trait ConnectedNode {
    val seqNr: Int
    val target: Point
    val color = ""

    def twistsToLeftOf(target: Node) = 0

    def twistsToRightOf(target: Node) = 0
  }

  private case class SimpleNode(override val seqNr: Int, override val target: Point) extends ConnectedNode

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