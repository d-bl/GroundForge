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

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

case class LinkProps private(elems: Seq[(String, Any)]) extends Props {
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
  def markedAsStart: LinkProps = LinkProps((m - "start" + ("start" -> "thread")).toSeq)

  private def markerRef(key: String): String = m.get(key)
    .filter(value => value != "white")
    .map(value => s"; marker-$key: url('#$key-$value')")
    .getOrElse("")
}
object LinkProps {

  def twistedLinks(newNode: Int,
                   leftNode: Int, rightNode: Int,
                   leftThread: Int, rightThread: Int
                  ): Seq[LinkProps] = {
    val curved = leftNode == rightNode
    val x = LinkProps(Seq(
      "source" -> leftNode,
      "target" -> newNode,
      "end" -> "white",
      "thread" -> leftThread,
      "left" -> curved
    ))
    val y = LinkProps(Seq(
      "source" -> rightNode,
      "target" -> newNode,
      "start" -> "white",
      "thread" -> rightThread,
      "right" -> curved
    ))
    Seq(x, y)
  }

  def crossedLinks(newNode: Int,
                   leftNode: Int, rightNode: Int,
                   leftThread: Int, rightThread: Int
                  ): Seq[LinkProps] = {
    val curved = leftNode == rightNode
    val x = LinkProps(Seq(
      "source" -> leftNode,
      "target" -> newNode,
      "start" -> "white",
      "thread" -> leftThread,
      "left" -> curved
    ))
    val y = LinkProps(Seq(
      "source" -> rightNode,
      "target" -> newNode,
      "end" -> "white",
      "thread" -> rightThread,
      "right" -> curved
    ))
    Seq(x, y)
  }

  def threadLink(source: Int,
                 target: Int,
                 threadNr: Int,
                 whiteStart: Boolean
                ): LinkProps =
    if (whiteStart) LinkProps(Seq(
      "source" -> source,
      "target" -> target,
      "thread" -> threadNr,
      "start" -> "white"
    )) else LinkProps(Seq(
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
              ): LinkProps = LinkProps(Seq(
    "source" -> source,
    "target" -> target,
    "start" -> start,
    "mid" -> mid,
    "end" -> end,
    "weak" -> weak
  ))

  def simpleLink(source: Int, target: Int): LinkProps =
    LinkProps(Seq(
      "source" -> source,
      "target" -> target
    ))

  def transparentLinks(nodes: Seq[Int]
                      ): Seq[LinkProps] =
    if (nodes.length < 2)
      Seq[LinkProps]()
    else nodes
      .zip(nodes.tail)
      .map { case (source, target) => LinkProps(Seq(
        "source" -> source,
        "target" -> target,
        "border" -> true,
        "weak" -> true
      ))}
}
