package dibl.proto

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
    val itemMatrix = config.getItemMatrix

    val clones = (for {
      r <- itemMatrix.indices
      c <- itemMatrix(r).indices.reverse
    } yield {
      val item = itemMatrix(r)(c)
      val stitch = item.stitch
      val vectorCode = item.vectorCode.toString.toUpperCase
      val translate = s"transform='translate(${ c * 10 + 38 },${ r * 10 + 1 })'"
      val nrOfPairsOut = config.nrOfPairsOut(r)(c)
      val opacity = (vectorCode, item.isOpaque )match {
        case ("-", _) => "0.05"
        case (_, true) => "1"
        case _ => "0.3"
      }
      val ringColor = (item.stitch, item.color) match {
        case ("-", _) => "#CCC"
        case (_, Some(color)) => color
        case _ => "#000"
      }
      val isActiveNode = item.isOpaque && !"-VWXYZ".contains(vectorCode)
      s"""${ warning(vectorCode, translate, nrOfPairsOut, item.noStitch) }
         |<use ${ events(isActiveNode, item.id) }
         |  xlink:href='#vc$vectorCode'
         |  id='svg-r${ r + 1 }-c${ c + 1 }'
         |  $translate
         |  style='stroke:$ringColor;opacity:$opacity;'
         |><title>$stitch</title>
         |</use>
         |${ textInput(isActiveNode, r, c, config) }""".stripMargin
    }).mkString("\n")
    embed(clones)
  }

  private def events(isActive: Boolean, id: String) = {
    if (isActive)
      s"data-formid='$id' onclick='setStitch(this)'"
    else ""
  }

  private def textInput(isActive: Boolean, r: Int, c: Int, config: TilesConfig) = {
    val item = config.getItemMatrix(r)(c)
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

  private def warning(vectorCode: String, translate: String, nrOfPairsOut: Int, noStitch: Boolean) = {
    (nrOfPairsOut, vectorCode, noStitch) match {
      case (_, _, true) | (2, _, _) | (_, "-", _) => "" // a two-in/two-out stitch or no stitch
      case _ => s"""<use xlink:href='#oops' $translate style='opacity:0.${ 1 + nrOfPairsOut };'></use>"""
    }
  }

  private lazy val symbols = {

    val arrowStyle = """fill:none;stroke-width:1.1;stroke:#000;marker-end:url(#Arrow1Mend)"""

    def arrow(path: String) = s"""<path d="m $path" style="$arrowStyle"></path>"""

    val shortE = arrow("-12,978 -6,0")
    val shortNE = arrow("-12,969 -7,7")
    val shortN = arrow("-20,970 0,6")
    val shortNW = arrow("-29,969 7,7")
    val shortW = arrow("-28,978 6,0")
    val doubleW = arrow("-28,978 c 3,-2 4,-1 6,0") + arrow("-28,978 c 2,2 4,1 6,0")
    val doubleNW = arrow("-29,969 c 1,4 4,6 7,7") + arrow("-29,969 c 4,1 6,4 7,7")
    val doubleN = arrow("-20,970 c 2,3 1,4 0,6") + arrow("-20,970 c -2,3 -1,4 0,6")
    val doubleNE = arrow("-12,969 c -4,1 -6,4 -7,7") + arrow("-12,969 c -1,4 -4,6 -7,7")
    val doubleE = arrow("-12,978 c -3,-2 -4,-1 -6,0") + arrow("-12,977 c -3,2 -4,1 -6,0")
    val doubleS = arrow("-20,986 c -2,-3 -1,-4 0,-6") + arrow("m -20,986 c 2,-3 1,-4 0,-6")
    val doubleSE = arrow("-11,986 c -4,-1 -6,-4 -7,-7") + arrow("-11,986 c -1,-4 -4,-6 -7,-7")
    val doubleSW = arrow("-28,986 c 1,-4 4,-6 7,-7") + arrow("m -28,986 c 4,-1 6,-4 7,-7")
    val shortSW = arrow("-12,986 -7,-7")
    val shortS = arrow("-20,986 0,-6")
    val shortSE = arrow("-28,986 7,-7")
    val longN = arrow("-20,960 0,16")
    val longE = arrow("-2,978 -16,0")
    val longW = arrow("-38,978 16,0")

    val textProps = """y="979.27722" x="-21.02791" style="font-size:3.3px;font-family:Arial;fill:#000000;stroke:none""""
    val circlePath = "m -18.064645,978.05982 c 0,0.55229 -0.223858,1.05229 -0.585787,1.41422 -0.361929,0.36192 -0.861929,0.58578 -1.414213,0.58578 -0.552284,0 -1.052284,-0.22386 -1.414213,-0.58578 -0.361929,-0.36193 -0.585787,-0.86193 -0.585787,-1.41422 0,-0.55228 0.223858,-1.05228 0.585787,-1.41421 0.361929,-0.36193 0.861929,-0.58579 1.414213,-0.58579 0.552284,0 1.052284,0.22386 1.414213,0.58579 0.361929,0.36193 0.585787,0.86193 0.585787,1.41421 z"
    val circle = s"""<path style="fill:none;stroke-width:1" d="$circlePath"></path>"""

    def symbol(tag: Char, arrows: String) = {
      s"""<g id="vc$tag"><text $textProps>$tag</text>$circle$arrows</g>"""
    }
    Seq(
      symbol('0', shortE + shortNE) ,
      symbol('1', shortE + shortN) ,
      symbol('2', shortE + shortNW) ,
      symbol('3', shortW + shortE) ,
      symbol('4', shortNE + shortN) ,
      symbol('5', shortNE + shortNW) ,
      symbol('6', shortNE + shortW) ,
      symbol('7', shortN + shortNW) ,
      symbol('8', shortN + shortW) ,
      symbol('9', shortNW + shortW) ,
      symbol('A', shortE + longN) ,
      symbol('B', shortNE + longN) ,
      symbol('C', longN + shortNW) ,
      symbol('D', longN + shortW) ,
      symbol('E', longE + shortNE) ,
      symbol('F', longE + shortN) ,
      symbol('G', longE + longN) ,
      symbol('H', longE + shortNW) ,
      symbol('I', longE + shortW) ,
      symbol('J', longW + shortE) ,
      symbol('K', longE + longW) ,
      symbol('L', shortNE + longW) ,
      symbol('M', shortN + longW) ,
      symbol('N', longN + longW) ,
      symbol('O', shortNW + longW) ,
      symbol('P', doubleW) ,
      symbol('Q', doubleNW) ,
      symbol('R', doubleN) ,
      symbol('S', doubleNE) ,
      symbol('T', doubleE) ,
      symbol('V', shortW) ,
      symbol('W', shortNW) ,
      symbol('X', shortN) ,
      symbol('Y', shortNE) ,
      symbol('Z', shortE) ,
      symbol('-', ""),
    ).mkString("\n      ")
  }

  def embed(clones: String): String = {

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
       |      ></path>
       |      $symbols
       |    </g>
       |    $clones
       |  </g>
       |</svg>
       |""".stripMargin
  }
}
