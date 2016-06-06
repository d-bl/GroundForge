package dibl

import scala.collection.mutable.ListBuffer
import scala.scalajs.js.annotation.JSExport

/** @param patchRows an A4 can contain 2 columns and 3 rows
  * @param pageSize default A4, in fact attributes for the SVG root element
  */
@JSExport
case class PatternSheet(patchRows: Int = 3, pageSize: String = "height='297mm' width='210mm'") {
  private val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg'"
  private val patterns = new ListBuffer[Pattern]

  @JSExport
  def add(m: String, isBrick: Boolean): PatternSheet = {
    val n = patterns.size
    val x = 70 + (n / patchRows) * 360
    val y = 120 + (n % patchRows) * 335
    val lines = Matrix.toMatrixLines(m).get
    patterns.append(new Pattern(lines.mkString(""), isBrick, lines.length, lines(0).length, s"GFP$n", x, y))
    this
  }

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport // parentheses are required for JavaScript
  def toSvgDoc():String = s"${s"<svg version='1.1' id='svg2' $pageSize $nameSpaces>"}\n$toSvgGroups\n</svg>"
  def toList: List[Pattern] = patterns.toList
  def toSvgGroups: String = patterns.map(_.patch).mkString("")
}
