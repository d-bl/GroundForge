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
import scala.util.Try

case class NodeProps private(elems: Seq[(String, Any)]) extends Props {
  private val m = elems.toMap

  val x: Double = m.getOrElse("x", 0.0).asInstanceOf[Double]
  val y: Double = m.getOrElse("y", 0.0).asInstanceOf[Double]

  override def toJS(): js.Dictionary[Any] = {
    val jsItem = js.Object().asInstanceOf[js.Dictionary[Any]]
    jsItem("x") = x
    jsItem("y") = y
    jsItem("bobbin") = bobbin
    jsItem("startOf") = startOf
    jsItem
  }

  /** The title alias tooltip (ID and instructions for a stitch) */
  val title: String = m.getOrElse("title", "").asInstanceOf[String]

  /** The stitch instructions from the title */
  val instructions: String = title.replaceAll(" .*", "").toLowerCase.replaceAll("t", "lr")

  /** If none-zero the node is the first one of the thread with that number */
  val startOf: Int = m.getOrElse("startOf", "thread0").toString.replaceAll("thread", "").toInt

  val pin: Boolean = m.getOrElse("pin", false).toString.toBoolean

  val bobbin: Boolean = m.getOrElse("bobbin", false).toString.toBoolean

  val stitch: Boolean = m.getOrElse("stitch", false).asInstanceOf[Boolean]

  val cssClasses: String = (m.get("startOf"), m.get("thread")) match {
    case (Some(_), _) =>
      s"node threadStart"
    case (None, Some(t)) =>
      s"node thread$t"
    case _ =>
      s"node"
  }

  /** Sets the location of a cross, twist or pin of one stitch to a single location
    * which is a good enough start of the animation.
    */
  def withLocationOf(pairNode: NodeProps): NodeProps = {
    val map = m - "x" - "y" + ("x"-> pairNode.x) + ("y"-> pairNode.y)
    NodeProps(map.toSeq)
  }

  def withLocation(newX: Double, newY: Double): NodeProps = {
    val map = m - "x" - "y" + ("x"-> newX) + ("y"-> newY)
    NodeProps(map.toSeq)
  }
}

object NodeProps {
  def pinNode = NodeProps(Seq("title" -> "pin", "pin" -> "true"))

  def crossNode = NodeProps(Seq("title" -> "cross"))

  def twistNode = NodeProps(Seq("title" -> "twist"))

  def errorNode(tried: Try[_]) = NodeProps(Seq("title" -> tried.failed.get.getMessage, "bobbin" -> true))

  def errorNode(message: String) = NodeProps(Seq("title" -> message, "bobbin" -> true))

  def bobbinNode(thread: Int, x: Double, y: Double) = NodeProps(Seq("bobbin" -> "true", "x" -> x, "y" -> y, "thread" -> thread))

  def threadStartNode(n: Int) = NodeProps(Seq("title" -> s"thread $n", "startOf" -> s"thread$n"))

  def node(title: String, x: Double, y: Double) = NodeProps(Seq("title" -> title, "x" -> x, "y" -> y))

  def node(x: Double, y: Double) = NodeProps(Seq("x" -> x, "y" -> y))
}
