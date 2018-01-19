package dibl

import dibl.Stitches.defaultColorValue

import scala.scalajs.js.annotation.JSExport

@JSExport
object InteractiveSVG {

  /**
   *
   * @param config form values plus values collected by setStitch calls
   * @return SVG elements using (cloning in InkScape terminology) SVG elements
   *         as in docs/help/images/matrix-template.png but stacked at a single position.
   *         With valid input, the cloned elements are supposed to build a directed
   *         two-in-two-out graph without cycles. Interactive elements are opaque
   *         and call setStitch on click events.
   *         The more transparent elements repeat the interactive elements.
   */
  @JSExport
  def create(config: Config): String = {
    val itemMatrix = config.itemMatrix
    (for {
      r <- itemMatrix.indices
      c <- itemMatrix.head.indices
    } yield {
      val stitch = itemMatrix(r)(c).stitch
      val color = defaultColorValue(stitch)
      val vectorCode = itemMatrix(r)(c).vectorCode.toString.toUpperCase
      val opacity = if (vectorCode == "-") "0.05" else if (itemMatrix(r)(c).isOpaque) "1" else "0.3"
      s"""<use xlink:href='#g${ vectorCode }'
         |  id='svg-r${r+1}-c${c+1}'
         |  transform='translate(${c*10+38},${r*10+1})'
         |  style='stroke:${if (color.nonEmpty) color else "#000"};opacity:$opacity;"'
         |  onclick='${if (opacity=="1") "setStitch(this)" else ""}'
         |><title>$stitch</title>
         |</use>""".stripMargin
    }).mkString("\n")
  }
}
