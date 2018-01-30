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
import dibl.LinkProps.Path

import scala.scalajs.js

sealed abstract class LinkProps extends Props {

  @deprecated
  val props: Map[String, Any]

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

  val markers: Map[String, Any] = props.filter{case (k,_) => Seq("start", "end", "mid").contains(k)}
  val start: String = props.getOrElse("start", "").asInstanceOf[String]
  val end: String = props.getOrElse("end", "").asInstanceOf[String]
  val nrOfTwists: Int = props.getOrElse("mid", 0).asInstanceOf[Int]

  /** Creates a copy of the link with a marker for the start of a thread. */
  def markedAsStart: LinkProps = this // assignment for non-thread subclasses

  val weak: Boolean = false
  val border: Boolean = false
  val cssClass: String = props.get("thread").map(nr => s"link thread$nr").getOrElse("link")

  /**
   * @param p The path for the link calculated by the simulation of D3.js
   * @return The path to render.
   *         Thread segments are rendered shorter on one end to emulate an over/under effect.
   *         When thread segments are on top of one another, they need a curve
   *         to make both visible, see issue #70 for images.
   */
  def renderedPath(p: Path): Path = p // assignment for non-thread subclasses
}

object LinkProps {

  sealed case class Path(source: Point, target: Point, curveTo: Option[Point] = None) {
    private val twistWidth = 6
    private val straightDistance = twistWidth / 5
    private val curvedDistance = straightDistance * 2
    private lazy val dX: Double = target.x - source.x
    private lazy val dY: Double = target.y - source.y
    private lazy val linkLength: Double = sqrt(dX*dX + dY*dY)
    lazy val dXCurveTo: Double = dX * (twistWidth / linkLength)
    lazy val dYCurveTo: Double = dY * (twistWidth / linkLength)
    lazy val dXGap: Double = dXCurveTo / curvedDistance
    lazy val dYGap: Double = dYCurveTo / curvedDistance
    lazy val straightDX: Double = dXCurveTo / straightDistance
    lazy val straightDY: Double = dYCurveTo / straightDistance
  }

  private val threadStartMarker = "start" -> "thread"

  private case class PlainLink(override val props: Map[String, Any],
                               override val weak: Boolean = false,
                               override val border: Boolean = false
                              ) extends LinkProps

  private case class WhiteEnd(override val props: Map[String, Any]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteEnd(props + threadStartMarker)

    override def renderedPath(p: Path): Path = {
      Path(p.source, Point(p.target.x - p.straightDX, p.target.y - p.straightDY))
    }
  }

  private case class WhiteStart(override val props: Map[String, Any]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteStart(props + ("start" -> "thread"))

    override def renderedPath(p: Path): Path = {
      Path(Point(p.source.x + p.straightDX, p.source.y + p.straightDY), p.target)
    }
  }

  private case class WhiteEndRight(override val props: Map[String, Any]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteEndRight(props + threadStartMarker)

    override def renderedPath(p: Path): Path = {
      Path(
        p.source,
        Point(// move target a fixed distance back and rotate it clockwise by 45 degrees
          p.target.x + p.dYGap - p.dXGap,
          p.target.y - p.dXGap - p.dYGap),
        Some(Point(// curve to: point at fixed distance to source rotated counter clockwise around source by 45 degrees
          p.source.x + p.dYCurveTo + p.dXCurveTo,
          p.source.y - p.dXCurveTo + p.dYCurveTo))
      )
    }
  }

  private case class WhiteEndLeft(override val props: Map[String, Any]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteEndLeft(props + threadStartMarker)

    override def renderedPath(p: Path): Path = {
      Path(
        p.source,
        Point(// move target a fixed distance back and rotate it counter clockwise by 45 degrees
          p.target.x - p.dYGap - p.dXGap,
          p.target.y + p.dXGap - p.dYGap),
        Some(Point(// curve to: point at fixed distance to source rotated clockwise around source by 45 degrees
          p.source.x - p.dYCurveTo + p.dXCurveTo,
          p.source.y + p.dXCurveTo + p.dYCurveTo))
      )
    }
  }

  private case class WhiteStartRight(override val props: Map[String, Any]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteStartRight(props + threadStartMarker)

    override def renderedPath(p: Path): Path = {
      Path(
        Point(// move source a fixed distance and rotate it counter clockwise by 45 degrees
          p.source.x + p.dYGap + p.dXGap,
          p.source.y - p.dXGap + p.dYGap),
        p.target,
        Some(Point(// move source a fixed distance and rotate it counter clockwise by 45 degrees
          p.target.x + p.dYCurveTo - p.dXCurveTo,
          p.target.y - p.dXCurveTo - p.dYCurveTo))
      )
    }
  }

  private case class WhiteStartLeft(override val props: Map[String, Any]) extends LinkProps {

    override def markedAsStart: LinkProps = WhiteStartLeft(props + threadStartMarker)

    override def renderedPath(p: Path): Path = {
      Path(
        Point(// move source a fixed distance and rotate it clockwise by 45 degrees
          p.source.x - p.dYGap + p.dXGap,
          p.source.y + p.dXGap + p.dYGap),
        p.target,
        Some(Point(// curve to: point at fixed distance to target rotated counter clockwise around target by 45 degrees
          p.target.x - p.dYCurveTo - p.dXCurveTo,
          p.target.y + p.dXCurveTo - p.dYCurveTo))
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

  private def threadProps(source: Int, target: Int, threadNr: Int) = Map(
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
              ): LinkProps = PlainLink(
    Map(
      "source" -> source,
      "target" -> target,
      "start" -> start,
      "mid" -> mid,
      "end" -> end
    ), weak = weak
  )

  def simpleLink(source: Int, target: Int): LinkProps =
    PlainLink(Map(
      "source" -> source,
      "target" -> target
    ))

  def transparentLinks(nodes: Seq[Int]): Seq[LinkProps] =
    if (2 > nodes.length) Seq.empty
    else nodes
      .zip(nodes.tail)
      .map { case (source, target) => PlainLink(
        Map(
          "source" -> source,
          "target" -> target
        ), weak = true, border = true
      )}
}
