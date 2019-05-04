package dibl.sheet

import scala.collection.mutable.ListBuffer
import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

/** SVG document builder.
  *
  * @param patchRows The number of generated rows.
  *                  A landscape A4 can contain 3 columns and 2 rows.
  * @param pageSize Attributes for the SVG root element.
  *                 Defaults to landscape A4.
  */
@JSExportTopLevel("SheetSVG") case class SheetSVG(patchRows: Int = 2,
                                                  pageSize: String = "height='210mm' width='297mm'",
                                                  idBase: String = "GFP"
                                                 ) {
  private val patterns = new ListBuffer[String]

  /**
    *
    * @param m See https://github.com/d-bl/GroundForge/blob/62f22ec/docs/images/legend.png
    *          On the right an example, on the left the meaning of the characters,
    *          thick arrows indicate vertices traveling two cells.
    * @param tileType how the matrix is stacked to build a pattern: like a brick wall or a checker board
    * @return this, to allow chaining
    */
  @JSExport
  def add(m: String, tileType: String): SheetSVG = {
    val n = patterns.size
    val x = 25 + (n / patchRows) * 360
    val y = 95 + (n % patchRows) * 335
    val triedSVG = Pattern(m, tileType, s"$idBase$n")
    if (triedSVG.isSuccess) patterns.append(s"""
       |<g transform='translate($x,$y)'>
       |${triedSVG.get}
       |</g>
       |""".stripMargin)
    else patterns.append(s"""
       |<text>
       |<tspan style='fill:#FF0000'
       |  x='2' y='14'
       |>${triedSVG.failed.get.getMessage}</tspan>
       |</text>
       |""".stripMargin)
    this
  }

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport // parentheses are required for JavaScript
  def toSvgDoc():String = s"""
       |<svg version='1.1'
       |  id='svg2'
       |  $pageSize
       |  xmlns='http://www.w3.org/2000/svg'
       |  xmlns:xlink='http://www.w3.org/1999/xlink'
       |>
       |${patterns.mkString}
       |</svg>
       |""".stripMargin
}
