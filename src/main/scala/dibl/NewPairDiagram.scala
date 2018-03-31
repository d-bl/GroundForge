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

    def isFringe(p: Point): Boolean = {
      p.x < 0 || p.x >= cols || p.y < 0 || p.x >= rows
    }

    def toNodeSeq(row: Int, col: Int): Seq[AnyRef] = {
      val item = itemMatrix(row)(col)
      if(item.vectorCode == '-') return Seq()
      val (left, right) = toRelativeSources(item.vectorCode.toUpper)
      val sourceLeft = Point(x = col + left.x, y = row + left.y)
      val sourceRight = Point(x = col + right.x, y = row + right.y)
      val target = Point(x = col, y = row)
      seqNr += 1
      val node = Node(seqNr, target, sourceLeft, sourceRight, item.stitch, item.id, item.color)
      (isFringe(sourceLeft), isFringe(sourceRight)) match {
        case (false, false) => Seq(node)
        case (true, false) => Seq(node, toSimpleNode(sourceLeft))
        case (false, true) => Seq(node, toSimpleNode(sourceRight))
        case (true, true) => Seq(node, toSimpleNode(sourceRight), toSimpleNode(sourceLeft))
      }
    }

    val nodes: Seq[AnyRef] = Seq(SimpleNode(0, Point(0, 0))) ++
      (0 until cols).flatMap(row =>
        (0 until rows).map(col =>
          toNodeSeq(row, col)
        )
      ).flatten

    val nodeMap = nodes.map {
      case n: Node => n.target -> n.seqNr
      case n: SimpleNode => n.point -> n.seqNr
    }.toMap

    def find(point: Point): Int = nodeMap.getOrElse(point, 0)

    def findColor(nodeNr: Int) = {
      nodes(nodeNr) match {
        case _: SimpleNode => ""
        case Node(_, _, _, _, _, _, c) => c.getOrElse("")
      }
    }

    val links: Seq[LinkProps] = nodes.flatMap {
      case _: SimpleNode => Seq.empty
      case Node(targetNr, _, leftSource, rightSource, _, _, color) =>
        val targetColor = color.getOrElse("")
        val leftNr = find(leftSource)
        val rightNr = find(rightSource)
        Seq( // TODO nr of twists for mid
          LinkProps.pairLink(leftNr, targetNr, start = findColor(leftNr), mid = 0, end = targetColor),
          LinkProps.pairLink(rightNr, targetNr, start = findColor(rightNr), mid = 0, end = targetColor)
        )
    }
    Diagram(nodes.map {
      case SimpleNode(_, Point(x, y)) => NodeProps.node("pair ???", x, y) // TODO pair number
      case Node(_, Point(x, y), _, _, stitch, id, color) => NodeProps.node(s"$stitch - $id", color, x, y)
    }, links)
  }

  private case class SimpleNode(seqNr: Int, point: Point)

  private case class Node(seqNr: Int,
                          target: Point,
                          srcLeft: Point,
                          srcRight: Point,
                          stitch: String,
                          id: String,
                          color: Option[String])

}