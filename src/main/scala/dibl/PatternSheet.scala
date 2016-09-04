package dibl

import scala.collection.mutable.ListBuffer
import scala.scalajs.js.annotation.JSExport

/** @param patchRows an A4 can contain 2 columns and 3 rows
  * @param pageSize default A4, in fact attributes for the SVG root element
  */
@JSExport
case class PatternSheet(patchRows: Int = 2, pageSize: String = "height='210mm' width='297mm'") {
  private val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg' xmlns:inkscape='http://www.inkscape.org/namespaces/inkscape'"
  private val patterns = new ListBuffer[String]

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
       |$toSvgGroups
       |</svg>
       |""".stripMargin
  def toList: List[String] = patterns.toList
  def toSvgGroups: String = patterns.mkString("")
}
