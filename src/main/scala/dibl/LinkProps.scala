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

  @deprecated
  val elems: Seq[(String, Any)]

  @deprecated
  private val props: Map[String, Any] = elems.toMap

  /** The id of the source node. */
  val source: Int = props.getOrElse("source", 0).asInstanceOf[Int]

  /** The id of the target node. */
  val target: Int = props.getOrElse("target", 0).asInstanceOf[Int]

  override def toJS(): js.Dictionary[Any] = {
    val jsItem = js.Object().asInstanceOf[js.Dictionary[Any]]
    jsItem("source") = source
    jsItem("target") = target
    jsItem("weak") = weak
    jsItem("start") = start
    jsItem("end") = end
    jsItem("mid") = nrOfTwists
    jsItem
  }

  val start: String = props.getOrElse("start", "").asInstanceOf[String]
  val end: String = props.getOrElse("end", "").asInstanceOf[String]
  val nrOfTwists: Int = props.getOrElse("mid", 0).asInstanceOf[Int]
  val right: Boolean = false
  val left: Boolean = false
  val weak: Boolean = props.getOrElse("weak", false).asInstanceOf[Boolean]
  val border: Boolean = props.getOrElse("border", false).asInstanceOf[Boolean]
  val toPin: Boolean = props.getOrElse("toPin", false).asInstanceOf[Boolean]
  val cssClass: String = props.get("thread").map(nr => s"link thread$nr").getOrElse("link")
  val markers: Map[String, Any] = props.filter{case (k,_) => Seq("start", "end", "mid").contains(k)}

  /** Creates a copy of the link which is marked as the start of a thread. */
  def markedAsStart: LinkProps
  def elemsWithThreadStart: Seq[(String, Any)] = (props + ("start" -> "thread")).toSeq

  // see issue #70 for images of curved threads
  def renderedPath(p: Path): Path
}

case class Path(source: Point, target: Point, curveTo: Option[Point] = None) {
  private val twistWidth = 6
  private val straightDistance = twistWidth / 5
  private val curvedDistance = straightDistance * 2
  private lazy val dX: Double = target.x - source.x
  private lazy val dY: Double = target.y - source.y
  private lazy val linkLength: Double = sqrt(dX*dX + dY*dY)
  lazy val dX1: Double = dX * (twistWidth / linkLength)
  lazy val dY1: Double = dY * (twistWidth / linkLength)
  lazy val dX4: Double = dX1 / curvedDistance
  lazy val dY4: Double = dY1 / curvedDistance
  lazy val straightDX: Double = dX1 / straightDistance
  lazy val straightDY: Double = dY1 / straightDistance
}

object LinkProps {

  case class PlainLink(override val elems: Seq[(String, Any)]) extends LinkProps {
    override def markedAsStart: LinkProps = PlainLink(elems)
    def renderedPath(p: Path): Path = p
  }

  case class WhiteEnd(override val elems: Seq[(String, Any)]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteEnd(elemsWithThreadStart)

    def renderedPath(p: Path): Path = {
      Path(p.source, Point(p.target.x - p.straightDX, p.target.y - p.straightDY))
    }
  }

  case class WhiteStart(override val elems: Seq[(String, Any)]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteStart(elemsWithThreadStart)

    def renderedPath(p: Path): Path = {
      Path(Point(p.source.x + p.straightDX, p.source.y + p.straightDY), p.target)
    }
  }

  case class WhiteEndRight(override val elems: Seq[(String, Any)]) extends LinkProps {

    override val right = true
    override def markedAsStart: LinkProps = WhiteEndRight(elemsWithThreadStart)

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

    override val left = true
    override def markedAsStart: LinkProps = WhiteEndLeft(elemsWithThreadStart)

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

    override val right = true
    override def markedAsStart: LinkProps = WhiteStartRight(elemsWithThreadStart)

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

    override val left = true
    override def markedAsStart: LinkProps = WhiteStartLeft(elemsWithThreadStart)

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

  def twistedLinks(target: Int,
                   leftSource: Int, rightSource: Int,
                   leftThreadNr: Int, rightThreadNr: Int
                  ): Seq[LinkProps] = {
    if (leftSource == rightSource) Seq( // curved as otherwise on top of one another
      WhiteEndLeft(threadProps(leftSource, target, leftThreadNr)),
      WhiteStartRight(threadProps(rightSource, target, rightThreadNr))
    )else  Seq(
      WhiteEnd(threadProps(leftSource, target, leftThreadNr)),
      WhiteStart(threadProps(rightSource, target, rightThreadNr))
    )
  }

  def crossedLinks(target: Int,
                   leftSource: Int, rightSource: Int,
                   leftThreadNr: Int, rightThreadNr: Int
                  ): Seq[LinkProps] = {
    if (leftSource == rightSource) Seq( // curved as otherwise on top of one another
      WhiteStartLeft(threadProps(leftSource, target, leftThreadNr)),
      WhiteEndRight(threadProps(rightSource, target, rightThreadNr))
    )else  Seq(
      WhiteStart(threadProps(leftSource, target, leftThreadNr)),
      WhiteEnd(threadProps(rightSource, target, rightThreadNr))
    )
  }

  def threadLink(source: Int,
                 target: Int,
                 threadNr: Int,
                 whiteStart: Boolean
                ): LinkProps =
    if (whiteStart)
      WhiteStart(threadProps(source, target, threadNr))
    else PlainLink(threadProps(source, target, threadNr))//connected to the leash of a bobbin

  private def threadProps(source: Int, target: Int, threadNr: Int) = Seq(
    "source" -> source,
    "target" -> target,
    "thread" -> threadNr
  )

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

  def transparentLinks(nodes: Seq[Int]): Seq[LinkProps] =
    if (2 > nodes.length)
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
