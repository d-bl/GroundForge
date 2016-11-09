/*
 Copyright 2016 Jo Pol
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

import scala.collection.mutable.ListBuffer
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

/** @param patchRows a landscape A4 can contain 3 columns and 2 rows
  * @param pageSize attributes for the SVG root element, default landscape A4
  */
@JSExport
case class PatternSheet(patchRows: Int = 2, pageSize: String = "height='210mm' width='297mm'") {
  private val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg' xmlns:inkscape='http://www.inkscape.org/namespaces/inkscape'"
  private val patterns = new ListBuffer[String]

  private def failureMessage(tried: Try[_]): String =
    s"<text><tspan x='2' y='14' style='fill:#FF0000'>${tried.failed.get.getMessage}</tspan></text>"

  /**
    *
    * @param m See https://github.com/d-bl/GroundForge/blob/master/docs/images/legend.png
    *          On the right an example, on the left the meaning of the characters,
    *          thick arrows indicate vertices traveling two cells.
    * @param tileType how the matrix is stacked to build a pattern: like a brick wall or a checker board
    * @return this, to allow chaining
    */
  @JSExport
  def add(m: String, tileType: String): PatternSheet = {
    val n = patterns.size
    val x = 25 + (n / patchRows) * 360
    val y = 95 + (n % patchRows) * 335
    val triedSVG = Pattern(m, tileType, s"GFP$n", x, y)
    patterns.append(triedSVG.getOrElse(failureMessage(triedSVG)))
    this
  }

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport // parentheses are required for JavaScript
  def toSvgDoc():String = s"""
       |<svg version='1.1' id='svg2' $pageSize $nameSpaces>
       |${patterns.mkString}
       |</svg>
       |""".stripMargin
}
