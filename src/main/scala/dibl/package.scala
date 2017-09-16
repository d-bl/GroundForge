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

import scala.language.postfixOps
import scala.scalajs.js.annotation.JSExport
import scala.util.{Success, Try}

package object dibl {

  // got these type aliases from http://stackoverflow.com/questions/15783837/beginner-scala-type-alias-in-scala-2-10

  type Cell = (Int, Int)
  def Cell(row: Int, col: Int): Cell = (row, col)

  type Link = (Cell, Cell)
  def Link(source: Cell, target: Cell): Link = (source, target)
  implicit class RichLink(left: Link) {
    def target: Cell = left._2
  }

  /** Tuples pointing to another cell in the matrix. */
  type SrcNodes = Array[Cell]
  def SrcNodes(leftIn: Cell, rightIn: Cell): SrcNodes = Array(leftIn, rightIn)
  def SrcNodes(): SrcNodes = Array[Cell]()

  /** Row in a matrix of tuples, each tuple points to another cell in the matrix. */
  type R = Array[SrcNodes]
  def R(xs: SrcNodes*) = Array(xs: _*)

  /** Matrix of tuples, each tuple points to another cell in the matrix. */
  type M = Array[R]
  def M(xs: R*) = Array(xs: _*)
  implicit class RichMatrix(left: M) {
    def toS: String = left.deep.mkString(",").replace("Array","").replace("(((","\n(((").replace("(()","\n(()")
  }

  type TargetToSrcs = (Int, Cell)
  def TargetToSrcs (target: Int, sources: Cell): TargetToSrcs = (target, sources)

  implicit class TriedDiagram(left: Try[Diagram]) {
    @JSExport
    def getOrRecover: Diagram = left
      .recoverWith { case e => Success(Diagram(
        Seq(NodeProps.errorNode(e.getMessage)),
        Seq[LinkProps]()
      ))
      }.get
  }

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
