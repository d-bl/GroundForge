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

import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.language.postfixOps

package object dibl {

  // got these type aliases from http://stackoverflow.com/questions/15783837/beginner-scala-type-alias-in-scala-2-10

  /** Tuples pointing to another cell in the matrix. */
  type SrcNodes = Array[(Int,Int)]
  def SrcNodes(xs: (Int,Int)*) = Array(xs: _*)

  /** Row in a matrix of tuples, each tuple points to another cell in the matrix. */
  type R = Array[SrcNodes]
  def R(xs: SrcNodes*) = Array(xs: _*)

  /** Matrix of tuples, each tuple points to another cell in the matrix. */
  type M = Array[R]
  def M(xs: R*) = Array(xs: _*)

  type TargetToSrcs = (Int, (Int, Int))
  def TargetToSrcs (target: Int, sources: (Int, Int)) = (target, sources)

  /** see https://github.com/d-bl/GroundForge/blob/7a94b67/js/sample.js */
  type Props = Map[String,Any]
  def Props(xs: (String, Any)*) = HashMap(xs: _*)

  /** Bridges the JavaScript way of accessing object properties like a HashMap
    * and the scala way allowing code-completion
    */
  implicit class LinkProps(p: Props) {
    /** The id of the source node */
    def source: Int = p.getOrElse("source", 0).asInstanceOf[Int]

    /** The id of the target node */
    def target: Int = p.getOrElse("target", 0).asInstanceOf[Int]
  }

  /** Bridges the JavaScript way of accessing object properties like a HashMap
    * and the scala way allowing code-completion
    */
  implicit class NodeProps(p: Props) {

    /** The title alias tooltip (ID and instructions for a stitch) */
    def title: String = p.getOrElse("title", "").toString

    /** The stitch instructions from the title */
    def instructions: String = p.title.replaceAll(" .*", "").toLowerCase.replaceAll("t", "lr")

    /** If none-zero the node the first one of the thread with that number */
    def startOf: Int = p.getOrElse("startOf", "0").toString.replaceAll("thread", "").toInt

    /** Initial position for the animation */
    def x: Int = p.getOrElse("x", "0").toString.toInt

    /** Initial position for the animation */
    def y: Int = p.getOrElse("y", "0").toString.toInt
  }

  // other tools

  @tailrec
  def transparentLinks (nodes: Seq[Int],
                                links: Seq[Props] = Seq[Props]()
                               ): Seq[Props] =
      if (nodes.length < 2) links
      else transparentLinks(nodes.tail,links :+ Props(
          "source" -> nodes.head,
          "target" -> nodes.tail.head,
          "border" -> true
      ))

  /**
    * Converts an HSL color value to RGB. Conversion formula
    * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
    * Assumes h, s, and l are contained in the set [0, 1] and
    * returns r, g, and b in the set [0, 255].
    *
    * from https://gist.github.com/mjackson/5311256
    *
    * @param h The hue
    * @param s The saturation
    * @param l The lightness
    * @return The Hexadecimal RGB representation
    */
  def hslToRgb(h: Float, s: Float, l: Float): String = {

    val (r, g, b) =
    if (s == 0) (1,1,1) /* achromatic */ else {
      def hue2rgb(p: Float, q: Float, t: Float): Float = {
        var tt = t
        if (tt < 0f) tt += 1f
        if (tt > 1f) tt -= 1f
        if (tt < 1f/6f) return p + (q - p) * 6f * tt
        if (tt < 1f/2f) return q
        if (tt < 2f/3f) return p + (q - p) * (2f/3f - tt) * 6f
        p
      }
      val q = if (l < 0.5f)  l * (1f + s) else l + s - l * s
      val p = 2f * l - q

      ( (255 * hue2rgb(p, q, h + 1f/3f)).toInt
      , (255 * hue2rgb(p, q, h)).toInt
      , (255 * hue2rgb(p, q, h - 1f/3f)).toInt
      )
    }
    f"$r%02X$g%02X$b%02X"
  }
}
