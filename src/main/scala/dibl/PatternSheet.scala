package dibl

import scala.collection.mutable.ListBuffer
import scala.scalajs.js.annotation.JSExport

/** @param patchRows a landscape A4 can contain 3 columns and 2 rows
  * @param pageSize attributes for the SVG root element, default landscape A4
  */
@JSExport
case class PatternSheet(patchRows: Int = 2, pageSize: String = "height='210mm' width='297mm'") {
  private val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg' xmlns:inkscape='http://www.inkscape.org/namespaces/inkscape'"
  private val patterns = new ListBuffer[String]

  /**
    *
    * @param m See https://github.com/d-bl/GroundForge/blob/gh-pages/images/legend.png
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
    patterns.append(Pattern(m, tileType, s"GFP$n", x, y))
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
