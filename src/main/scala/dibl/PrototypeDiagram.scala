package dibl

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("PrototypeDiagram") object PrototypeDiagram {

  /** Completes a document supposed to have groups of SVG elements as in
   * docs/help/images/matrix-template.png
   *
   * The groups are positioned outside the visible area of the document
   * with their circles on the same position.
   * The id of a group is the character in the circle prefixed with a "vc".
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
  def create(config: TilesConfig): String = {
    val itemMatrix = config.itemMatrix

    val clones = (for {
      r <- itemMatrix.indices
      c <- itemMatrix(r).indices.reverse
    } yield {
      val item = itemMatrix(r)(c)
      val stitch = item.stitch
      val vectorCode = item.vectorCode.toString.toUpperCase
      val translate = s"transform='translate(${ c * 10 + 38 },${ r * 10 + 1 })'"
      val nrOfPairsOut = config.pairsOut(r)(c)
      val opacity = getOpacity(vectorCode, item.isOpaque)
      val isActiveNode = opacity == "1" && vectorCode != "-"
      s"""${ warning(vectorCode, translate, nrOfPairsOut) }
         |<use ${ events(isActiveNode, item.id) }
         |  xlink:href='#vc$vectorCode'
         |  id='svg-r${ r + 1 }-c${ c + 1 }'
         |  $translate
         |  style='stroke:${ item.color.getOrElse("#000") };opacity:$opacity;'
         |><title>$stitch</title>
         |</use>
         |${ textInput(isActiveNode, r, c, config) }""".stripMargin
    }).mkString("\n")
    embed(clones)
  }

  private def events(isActive: Boolean, id: String) = {
    if (isActive)
      s"data-formid='${ id }' onclick='setStitch(this)'"
    else ""
  }

  private def textInput(isActive: Boolean, r: Int, c: Int, config: TilesConfig) = {
    val item = config.itemMatrix(r)(c)
    if (isActive)
      s"""<foreignObject x='${ 19 + c * 10 }' y='${ 970 + r * 10 }' width='4em' height='8'>
         |  <input name='${ item.id }'
         |    id='${ item.id }'
         |    type='text'
         |    value='${ item.stitch }'
         |    onchange='showProto()'
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

  private def embed(clones: String): String = {
    def symbol(value: String) = {
      s"""<text y="979.27722" x="-21.02791" style="font-size:3.3px;font-family:Arial;fill:#000000;stroke:none">$value</text>
         |        <path d="m -18.06,978.06 c 0,0.55 -0.22,1.05 -0.59,1.41 -0.36,0.36 -0.86,0.59 -1.41,0.59 -0.55,0 -1.05,-0.22 -1.41,-0.56 -0.36,-0.36 -0.58,-0.86 -0.59,-1.41 0,-0.55 0.22,-1.05 0.59,-1.41 0.36,-0.36 0.86,-0.59 1.41,-0.59 0.55,0 1.05,0.22 1.41,0.58 0.36,0.36 0.59,0.86 0.58,1.41 z"
         |              style="fill:none;stroke-width:1"></path>
       """.stripMargin
    }

    val style = """style="fill:none;stroke-width:1.1;stroke:#000;marker-end:url(#Arrow1Mend)""""
    s"""<svg
       |    xmlns="http://www.w3.org/2000/svg"
       |    xmlns:xlink='http://www.w3.org/1999/xlink'
       |    width="297mm"
       |    height="210mm"
       |    id="svg2"
       |    version="1.1">
       |  <defs id="defs4">
       |    <marker orient="auto" refY="0" refX="0" id="Arrow1Mend" style="overflow:visible">
       |      <path d="M 0,0 2,-5 -12.5,0 5,5 0,0 Z" style="fill-rule:evenodd;stroke-width:1pt" transform="matrix(-0.22,0,0,-0.22,-2,0)"></path>
       |    </marker>
       |  </defs>
       |  <g id="layer1" transform="matrix(2.7,0,0,2.7,-10,-2600)">
       |    <g>
       |      <path id="oops"
       |          d="m -15.737308,978.07528 a 4.4367617,4.4367617 0 0 1 -2.222417,3.84823 4.4428755,4.4428755 0 0 1 -4.443852,-0.002 4.4428755,4.4428755 0 0 1 -2.219481,-3.84991 l 4.442876,0.002"
       |          style="fill:#000000;stroke:none;"
       |          ></path>
       |      <g id="vc2">${symbol("2")}
       |        <path $style d="m -12.051706,978.09704 -6.012939,-0.0372"></path>
       |        <path $style d="m -28.650432,969.47404 7.171574,7.17157"></path>
       |      </g>
       |      <g id="vc0">${symbol("0") }
       |        <path d="m -12.051698,978.09705 -6.012939,-0.0372" $style ></path>
       |        <path d="m -11.47885,969.47404 -7.171574,7.17157" $style ></path>
       |      </g>
       |      <g id="vc1">${symbol("1") }
       |        <path $style d="m -12.051706,978.09702 -6.012939,-0.0372"></path>
       |        <path $style d="m -20.064645,970.0598 0,6"></path>
       |      </g>
       |      <g id="vcI">${symbol("I") }
       |        <path d="m -2.064637,978.0598 -16,0" $style ></path>
       |        <path d="m -28.051698,978.09702 5.987061,-0.0372" $style ></path>
       |      </g>
       |      <g id="vc4">${symbol("4") }
       |        <path d="m -11.478848,969.47403 -7.171574,7.17158" $style ></path>
       |        <path d="m -20.064635,970.05982 0,6" $style ></path>
       |      </g>
       |      <g id="vc5">${symbol("5") }
       |        <path d="m -11.491798,969.43678 -7.171574,7.17158" $style ></path>
       |        <path d="m -28.663372,969.43678 7.171574,7.17158" $style ></path>
       |      </g>
       |      <g id="vc7">${symbol("7") }
       |        <path d="m -20.064645,970.05982 0,6" $style ></path>
       |        <path d="m -28.650432,969.47403 7.171574,7.17158" $style ></path>
       |      </g>
       |      <g id="vc8">${symbol("8") }
       |        <path d="m -20.064645,970.0598 0,6" $style ></path>
       |        <path d="m -28.064645,978.0598 6,0" $style ></path>
       |      </g>
       |      <g id="vc9">${symbol("9") }
       |        <path d="m -28.663372,969.4368 7.171574,7.17157" $style ></path>
       |        <path d="m -28.077585,978.02258 6,0" $style ></path>
       |      </g>
       |      <g id="vcA">${symbol("A") }
       |        <path d="m -12.051706,978.09704 -6.012939,-0.0372" $style ></path>
       |        <path d="m -20.064645,960.05982 0,16" $style ></path>
       |      </g>
       |      <g id="vcB">${symbol("B") }
       |        <path $style d="m -11.47885,969.47404 -7.171574,7.17157"></path>
       |        <path $style d="m -20.064637,960.05982 0,16"></path>
       |      </g>
       |      <g id="vcC">${symbol("C") }
       |        <path d="m -20.064645,960.05982 0,16" $style ></path>
       |        <path d="m -28.650432,969.47404 7.171574,7.17157" $style ></path>
       |      </g>
       |      <g id="vcD">${symbol("D") }
       |        <path $style d="m -20.064645,960.05982 0,16"></path>
       |        <path d="m -28.051706,978.09702 5.987061,-0.0372" $style ></path>
       |      </g>
       |      <g id="vcE">${symbol("E") }
       |        <path $style d="m -2.090516,977.98535 -15.987061,0.0372"></path>
       |        <path $style d="m -11.49179,969.43678 -7.171574,7.17158"></path>
       |      </g>
       |      <g id="vcF">${symbol("F") }
       |        <path d="m -2.077585,978.0226 -16,0" $style ></path>
       |        <path d="m -20.077585,970.0226 0,6" $style ></path>
       |      </g>
       |      <g id="vcG">${symbol("G") }
       |        <path $style d="m -2.077585,978.0226 -16,0"></path>
       |        <path $style d="m -20.077585,960.0226 0,16"></path>
       |      </g>
       |      <g id="vcH">${symbol("H") }
       |        <path d="m -2.077585,978.0226 -15.98706,0.0372" $style ></path>
       |        <path $style d="m -28.650425,969.47403 7.17157,7.17158"></path>
       |      </g>
       |      <g id="vcK">${symbol("K") }
       |        <path $style d="m -2.064645,978.0598 -16,0"></path>
       |        <path $style d="m -38.064645,978.0598 16,0"></path>
       |      </g>
       |      <g id="vcJ">${symbol("J") }
       |        <path d="m -38.077585,978.02257 16,0" $style ></path>
       |        <path d="m -12.090524,978.05979 -5.987061,-0.0372" $style ></path>
       |      </g>
       |      <g id="vcL">${symbol("L") }
       |        <path $style d="m -11.49179,969.43678 -7.171574,7.17158"></path>
       |        <path $style d="m -38.077577,978.02257 16,0"></path>
       |      </g>
       |      <g id="vc6">${symbol("6") }
       |        <path d="m -11.49179,969.43678 -7.171574,7.17158" $style ></path>
       |        <path d="m -28.077577,978.02257 6,0" $style ></path>
       |      </g>
       |      <g id="vcN">${symbol("N") }
       |        <path $style d="m -20.077585,960.02257 0,16"></path>
       |        <path $style d="m -38.077585,978.02257 16,0"></path>
       |      </g>
       |      <g id="vcM">${symbol("M") }
       |        <path d="m -20.077585,970.02257 0,6" $style ></path>
       |        <path d="m -38.077585,978.02257 16,0" $style ></path>
       |      </g>
       |      <g id="vcO">${symbol("O") }
       |        <path $style d="m -28.663372,969.43678 7.171574,7.17158"></path>
       |        <path $style d="m -38.064646,978.05979 15.987061,-0.0372"></path>
       |      </g>
       |      <g id="vc3">${symbol("3") }
       |        <path $style d="m -28.064646,978.05979 5.987061,-0.0372"></path>
       |        <path $style d="m -12.090524,978.05979 -5.987061,-0.0372"></path>
       |      </g>
       |      <g id="vc-">${symbol("-") }
       |      </g>
       |    </g>
       |$clones
       |  </g>
       |</svg>
       |""".stripMargin
  }
}
