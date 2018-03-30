package dibl

import dibl.Force.Point

import scala.scalajs.js.annotation.JSExport

@JSExport
object NewPairDiagram {

  @JSExport
  def create(config: Config): Diagram = {

    val itemMatrix = config.itemMatrix
    val rows: Int = itemMatrix.length
    val cols: Int = itemMatrix.head.length
    var seqNr = -1

    def pointToTuple(point: Point): (Point, NrPoint) = {
      seqNr += 1
      point -> NrPoint(seqNr, point)
    }

    def isFringe(p: Point): Boolean = {
      p.x < 0 || p.x >= cols || p.y < 0 || p.x >= rows
    }

    def toNodeTuple(row: Int, col: Int) = Seq(pointToTuple(Point(0, 0))) ++ {
      val item = itemMatrix(row)(col)
      val (left, right) = Matrix.charToRelativePoints(item.vectorCode.toUpper)
      val sourceLeft = Point(x = col + left.x, y = row + left.y)
      val sourceRight = Point(x = col + right.x, y = row + right.y)
      val target = Point(x = col, y = row)
      seqNr += 1
      val targetTuple = target -> Node(seqNr, target, sourceLeft, sourceRight, item.stitch, item.isOpaque, item.color)
      (isFringe(sourceLeft), isFringe(sourceRight)) match {
        case (false, false) => Map(targetTuple)
        case (true, false) => Map(targetTuple, pointToTuple(sourceLeft))
        case (false, true) => Map(targetTuple, pointToTuple(sourceRight))
        case (true, true) => Map(targetTuple, pointToTuple(sourceRight), pointToTuple(sourceLeft))
      }
    }

    val nodeMap: Map[Point, AnyRef] =
      (0 until cols).flatMap(row =>
        (0 until rows).map(col =>
          toNodeTuple(row, col)
        )
      ).flatten.toMap

    def find(point: Point): Int = nodeMap.get(point).map {
      case NrPoint(n, _) => n
      case Node(n, _, _, _, _, _, _) => n
    }.getOrElse(0)

    val nodeValues = nodeMap.values.toSeq
    val links: Seq[LinkProps] = nodeValues
      .withFilter { case (_, n) => n.isInstanceOf[Node] }
      .flatMap { case (_, Node(targetNr, _, leftSource, rightSource, _, _, _)) => Seq(
        LinkProps.simpleLink(find(leftSource), targetNr),
        LinkProps.simpleLink(find(rightSource), targetNr)
      )
      }
    Diagram(nodeValues.map {
      case NrPoint(_, Point(x, y)) => NodeProps.node("ctct", x, y)
      case Node(_, Point(x, y), _, _, stitch, _, color) => NodeProps.node(s"$stitch - ${}", color, x, y)
    }, links)
  }

  private case class NrPoint(seqNr: Int, point: Point)

  private case class Node(seqNr: Int,
                          target: Point,
                          srcLeft: Point,
                          srcRight: Point,
                          stitch: String,
                          isOpaque: Boolean,
                          color: Option[String])

}