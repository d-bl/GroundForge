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
    val pairsOut: Array[Array[Int]] = computePairsOut(config)

    pairsOut.head
    (for {
      r <- itemMatrix.indices
      c <- itemMatrix.head.indices
    } yield {
      val stitch = itemMatrix(r)(c).stitch
      val color = defaultColorValue(stitch)
      val vectorCode = itemMatrix(r)(c).vectorCode.toString.toUpperCase
      val opacity = if (vectorCode == "-") "0.05" else if (itemMatrix(r)(c).isOpaque) "1" else "0.3"
      val translate = s"transform='translate(${c * 10 + 38},${r * 10 + 1})'"
      val nrOfPairsOut = pairsOut(r)(c)
      (if (nrOfPairsOut == 0 || nrOfPairsOut == 2) ""
      else s"""<use xlink:href='#oops' $translate style='opacity:0.${1 + nrOfPairsOut};'/>"""
        ) +
        s"""<use xlink:href='#g$vectorCode'
           |  id='svg-r${r + 1}-c${c + 1}'
           |  $translate
           |  style='stroke:${if (color.nonEmpty) color else "#000"};opacity:$opacity;'
           |  onclick='${if (opacity == "1") "setStitch(this)" else ""}'
           |><title>$stitch</title>
           |</use>""".stripMargin
    }).mkString("\n")
  }

  private def computePairsOut(config: Config) = {
    val itemMatrix = config.itemMatrix
    val rows: Int = itemMatrix.length
    val cols: Int = itemMatrix.head.length
    val pairsOut = Array.fill[Array[Int]](rows)(
      Array.fill[Int](cols)(0)
    )
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      Matrix.charToRelativeTuples(itemMatrix(r)(c).vectorCode.toUpper)
        .foreach { case (relativeSourceRow, relativeSourceCol) =>
          val row: Int = r + relativeSourceRow
          val col: Int = c + relativeSourceCol
          if (row >= 0 && col >= 0 && col < cols && row < rows) {
            pairsOut(row)(col) += 1
          }
        }
    }
    pairsOut
  }
}
