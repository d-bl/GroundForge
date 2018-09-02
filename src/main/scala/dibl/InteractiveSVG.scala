package dibl

import scala.scalajs.js.annotation.JSExport

@JSExport
object InteractiveSVG {

  /** Completes a document supposed to have groups of SVG elements as in
   * docs/help/images/matrix-template.png
   *
   * The groups are positioned outside the visible area of the document
   * with their circles on the same position.
   * The id of a group is the character in the circle prefixed with a "g".
   * A element (with id "oops") on the same pile indicates a stitch that has
   * another number of outgoing pairs than 2. The transparency when referencing
   * this element indicates the number of outgoing pairs
   * Very bright: just one; darker: more than two.
   *
   * @param config values of form fields on tiles.html plus values collected by setStitch calls
   * @return SVG elements at some grid position referencing something in the pile.
   *         Some elements reference in an opaque way and call setStitch on click events.
   *         Other elements reference semi transparent and repeat the opaque elements.
   */
  @JSExport
  def create(config: Config): String = {
    val itemMatrix = config.itemMatrix

    val pairsOut: Array[Array[Int]] = {
      val rows: Int = itemMatrix.length
      val cols: Int = itemMatrix.head.length
      val pairsOut = Array.fill[Array[Int]](rows)(
        Array.fill[Int](cols)(0)
      )
      for {r <- 0 until rows
           c <- 0 until cols
      } {
        Matrix.toRelativeSources(itemMatrix(r)(c).vectorCode.toUpper)
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

    (for {
      r <- itemMatrix.indices
      c <- itemMatrix.head.indices
    } yield {
      val item = itemMatrix(r)(c)
      val stitch = item.stitch
      val vectorCode = item.vectorCode.toString.toUpperCase
      val translate = s"transform='translate(${ c * 10 + 38 },${ r * 10 + 1 })'"
      val nrOfPairsOut = pairsOut(r)(c)
      val opacity = getOpacity(vectorCode, item.isOpaque)
      val isActiveNode = opacity == "1" && vectorCode != "-"
      s"""${ warning(vectorCode, translate, nrOfPairsOut) }
         |<use ${ events(isActiveNode, item.id) }
         |  xlink:href='#g$vectorCode'
         |  id='svg-r${ r + 1 }-c${ c + 1 }'
         |  $translate
         |  style='stroke:${ item.color.getOrElse("#000") };opacity:$opacity;'
         |><title>$stitch</title>
         |</use>
         |${ textInput(isActiveNode, r, c, config) }""".stripMargin
    }).mkString("\n")
  }

  private def events(isActive: Boolean, id: String) = {
    if (isActive)
      s"data-formid='${ id }' onclick='setStitch(this)'"
    else ""
  }

  private def textInput(isActive: Boolean, r: Int, c: Int, config: Config) = {
    val item = config.itemMatrix(r)(c)
    if (isActive)
      s"""<foreignObject x='${ 19 + c * 10 }' y='${ 970 + r * 10 }' width='4em' height='8'>
         |  <input name='${ item.id }'
         |    id='${ item.id }'
         |    name='${ item.id }'
         |    type='text'
         |    value='${ item.stitch }'
         |    onchange='showProto();showDiagrams()'
         |  ></input>
         |</foreignObject>
         |""".stripMargin
    else ""
  }

  private def getOpacity(vectorCode: String, isOpaque: Boolean): String = {
    vectorCode match {
      case "-" => "0.05"
      case _ if isOpaque => "1"
      case _ => "0.3"
    }
  }

  private def warning(vectorCode: String, translate: String, nrOfPairsOut: Int) = {
    (nrOfPairsOut, vectorCode) match {
      case (2, _) | (_, "-") => "" // a two-in/two-out stitch or no stitch
      case _ => s"""<use xlink:href='#oops' $translate style='opacity:0.${ 1 + nrOfPairsOut };'></use>"""
    }
  }
}
