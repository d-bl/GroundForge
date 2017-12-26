/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import java.lang.Math.sqrt

import dibl.Force.Point

import scala.scalajs.js

trait LinkProps extends Props {
  val elems: Seq[(String, Any)]

  private val m = elems.toMap

  /** The id of the source node. */
  val source: Int = m.getOrElse("source", 0).asInstanceOf[Int]

  /** The id of the target node. */
  val target: Int = m.getOrElse("target", 0).asInstanceOf[Int]

  val weak: Boolean = m.getOrElse("left", false).asInstanceOf[Boolean]

  override def toJS(): js.Dictionary[Any] = {
    val jsItem = js.Object().asInstanceOf[js.Dictionary[Any]]
    jsItem("source") = source
    jsItem("target") = target
    jsItem("weak") = weak
    jsItem("markerRefs") = markerRefs
    // the next 3 can be dropped when show-grap.js/simEnded knows how to use markerRefs
    jsItem("start") = start
    jsItem("end") = end
    jsItem("mid") = nrOfTwists
    jsItem
  }

  val start: String = m.getOrElse("start", "").asInstanceOf[String]
  val end: String = m.getOrElse("end", "").asInstanceOf[String]
  val nrOfTwists: Int = m.getOrElse("mid", 0).asInstanceOf[Int]
  val left: Boolean = m.getOrElse("left", false).asInstanceOf[Boolean]
  val right: Boolean = m.getOrElse("right", false).asInstanceOf[Boolean]
  val border: Boolean = m.getOrElse("border", false).asInstanceOf[Boolean]
  val toPin: Boolean = m.getOrElse("toPin", false).asInstanceOf[Boolean]
  val cssClass: String = m.get("thread").map(nr => s"link thread$nr").getOrElse("link")
  val markerRefs: String =
    markerRef("start") +
      markerRef("end") +
      m.get("mid").map(_ => s"; marker-mid: url('#twist-1')").getOrElse("")

  /** Creates a copy of the link which is marked as the start of a thread. */
  def markedAsStart: LinkProps = PlainLink((m - "start" + ("start" -> "thread")).toSeq)

  private def markerRef(key: String): String = m.get(key)
    .filter(value => value != "white")
    .map(value => s"; marker-$key: url('#$key-$value')")
    .getOrElse("")

  // see issue #70 for images
  def renderedPath(p: Path): Path
}

case class PlainLink(override val elems: Seq[(String, Any)]) extends LinkProps {
  override def markedAsStart: LinkProps = PlainLink((elems.toMap - "start" + ("start" -> "thread")).toSeq)
  def renderedPath(p: Path): Path = p
}

case class WhiteEnd(override val elems: Seq[(String, Any)]) extends LinkProps {

  override def markedAsStart: LinkProps = WhiteEnd((elems.toMap - "start" + ("start" -> "thread")).toSeq)

  def renderedPath(p: Path): Path = {
    Path(p.source, Point(p.target.x - p.straightDX, p.target.y - p.straightDY))
  }
}

case class WhiteStart(override val elems: Seq[(String, Any)]) extends LinkProps {

  override def markedAsStart: LinkProps = WhiteStart((elems.toMap - "start" + ("start" -> "thread")).toSeq)

  def renderedPath(p: Path): Path = {
    Path(Point(p.source.x + p.straightDX, p.source.y + p.straightDY), p.target)
  }
}

case class WhiteEndRight(override val elems: Seq[(String, Any)]) extends LinkProps {

  override def markedAsStart: LinkProps = WhiteEndRight((elems.toMap - "start" + ("start" -> "thread")).toSeq)

  def renderedPath(p: Path): Path = {
    Path(
      p.source,
      Point(// move target a fixed distance back and rotate it clockwise by 45 degrees
        p.target.x + p.dY4 - p.dX4,
        p.target.y - p.dX4 - p.dY4),
      Some(Point(// curve to: point at fixed distance to source rotated counter clockwise around source by 45 degrees
        p.source.x + p.dY1 + p.dX1,
        p.source.y - p.dX1 + p.dY1))
    )
  }
}

case class WhiteEndLeft(override val elems: Seq[(String, Any)]) extends LinkProps {

  override def markedAsStart: LinkProps = WhiteEndLeft((elems.toMap - "start" + ("start" -> "thread")).toSeq)

  def renderedPath(p: Path): Path = {
    Path(
      p.source,
      Point(// move target a fixed distance back and rotate it counter clockwise by 45 degrees
        p.target.x - p.dY4 - p.dX4,
        p.target.y + p.dX4 - p.dY4),
      Some(Point(// curve to: point at fixed distance to source rotated clockwise around source by 45 degrees
        p.source.x - p.dY1 + p.dX1,
        p.source.y + p.dX1 + p.dY1))
    )
  }
}

case class WhiteStartRight(override val elems: Seq[(String, Any)]) extends LinkProps {

  override def markedAsStart: LinkProps = WhiteStartRight((elems.toMap - "start" + ("start" -> "thread")).toSeq)

  def renderedPath(p: Path): Path = {
    Path(
      Point(// move source a fixed distance and rotate it counter clockwise by 45 degrees
        p.source.x + p.dY4 + p.dX4,
        p.source.y - p.dX4 + p.dY4),
      p.target,
      Some(Point(// move source a fixed distance and rotate it counter clockwise by 45 degrees
        p.target.x + p.dY1 - p.dX1,
        p.target.y - p.dX1 - p.dY1))
    )
  }
}

case class WhiteStartLeft(override val elems: Seq[(String, Any)]) extends LinkProps {

  override def markedAsStart: LinkProps = WhiteStartLeft((elems.toMap - "start" + ("start" -> "thread")).toSeq)

  def renderedPath(p: Path): Path = {
    Path(
      Point(// move source a fixed distance and rotate it clockwise by 45 degrees
        p.source.x - p.dY4 + p.dX4,
        p.source.y + p.dX4 + p.dY4),
      p.target,
      Some(Point(// curve to: point at fixed distance to target rotated counter clockwise around target by 45 degrees
        p.target.x - p.dY1 - p.dX1,
        p.target.y + p.dX1 - p.dY1))
    )
  }
}

case class Path(source: Point, target: Point, curveTo: Option[Point] = None) {
  private val twistWidth = 6
  private val straightDistance = twistWidth / 5
  private val curvedDistance = straightDistance * 2
  lazy val dX = target.x - source.x
  lazy val dY = target.y - source.y
  lazy val linkLength = sqrt(dX*dX + dY*dY)
  lazy val dX1 = dX * (twistWidth / linkLength)
  lazy val dY1 = dY * (twistWidth / linkLength)
  lazy val dX4 = dX1 / curvedDistance
  lazy val dY4 = dY1 / curvedDistance
  lazy val straightDX = dX1 / straightDistance
  lazy val straightDY = dY1 / straightDistance
}

object LinkProps {

  def twistedLinks(newNode: Int,
                   leftNode: Int, rightNode: Int,
                   leftThread: Int, rightThread: Int
                  ): Seq[LinkProps] = {
    val curved = leftNode == rightNode
    val x = if (curved) WhiteEndLeft(Seq(
      "source" -> leftNode,
      "target" -> newNode,
      "end" -> "white",
      "thread" -> leftThread,
      "left" -> true
    ))else WhiteEnd(Seq(
      "source" -> leftNode,
      "target" -> newNode,
      "end" -> "white",
      "thread" -> leftThread,
      "left" -> false
    ))
    val y = if (curved) WhiteStartRight(Seq(
      "source" -> rightNode,
      "target" -> newNode,
      "start" -> "white",
      "thread" -> rightThread,
      "right" -> true
    ))else WhiteStart(Seq(
      "source" -> rightNode,
      "target" -> newNode,
      "start" -> "white",
      "thread" -> rightThread,
      "right" -> false
    ))
    Seq(x, y)
  }

  def crossedLinks(newNode: Int,
                   leftNode: Int, rightNode: Int,
                   leftThread: Int, rightThread: Int
                  ): Seq[LinkProps] = {
    val curved = leftNode == rightNode
    val x = if (curved) WhiteStartLeft(Seq(
      "source" -> leftNode,
      "target" -> newNode,
      "start" -> "white",
      "thread" -> leftThread,
      "left" -> true
    ))else WhiteStart(Seq(
      "source" -> leftNode,
      "target" -> newNode,
      "start" -> "white",
      "thread" -> leftThread,
      "left" -> false
    ))
    val y = if(curved) WhiteEndRight(Seq(
      "source" -> rightNode,
      "target" -> newNode,
      "end" -> "white",
      "thread" -> rightThread,
      "right" -> true
    ))else WhiteEnd(Seq(
      "source" -> rightNode,
      "target" -> newNode,
      "end" -> "white",
      "thread" -> rightThread,
      "right" -> false
    ))
    Seq(x, y)
  }

  def threadLink(source: Int,
                 target: Int,
                 threadNr: Int,
                 whiteStart: Boolean
                ): LinkProps =
    if (whiteStart) WhiteStart(Seq(
      "source" -> source,
      "target" -> target,
      "thread" -> threadNr,
      "start" -> "white"
    )) else PlainLink(Seq(
      "source" -> source,
      "target" -> target,
      "thread" -> threadNr
    ))

  def pairLink(source: Int,
               target: Int,
               start: String,
               mid: Int,
               end: String,
               weak: Boolean
              ): LinkProps = PlainLink(Seq(
    "source" -> source,
    "target" -> target,
    "start" -> start,
    "mid" -> mid,
    "end" -> end,
    "weak" -> weak
  ))

  def simpleLink(source: Int, target: Int): LinkProps =
    PlainLink(Seq(
      "source" -> source,
      "target" -> target
    ))

  def transparentLinks(nodes: Seq[Int]
                      ): Seq[LinkProps] =
    if (nodes.length < 2)
      Seq[LinkProps]()
    else nodes
      .zip(nodes.tail)
      .map { case (source, target) => PlainLink(Seq(
        "source" -> source,
        "target" -> target,
        "border" -> true,
        "weak" -> true
      ))}
}
