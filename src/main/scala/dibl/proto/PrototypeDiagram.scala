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
      s"data-formid='${ id }' onclick='setStitch(this)'"
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

  def embed(clones: String): String = {
    val textProps = """y="979.27722" x="-21.02791" style="font-size:3.3px;font-family:Arial;fill:#000000;stroke:none""""
    val circleProps = """style="fill:none;stroke-width:1" d="m -18.064645,978.05982 c 0,0.55229 -0.223858,1.05229 -0.585787,1.41422 -0.361929,0.36192 -0.861929,0.58578 -1.414213,0.58578 -0.552284,0 -1.052284,-0.22386 -1.414213,-0.58578 -0.361929,-0.36193 -0.585787,-0.86193 -0.585787,-1.41422 0,-0.55228 0.223858,-1.05228 0.585787,-1.41421 0.361929,-0.36193 0.861929,-0.58579 1.414213,-0.58579 0.552284,0 1.052284,0.22386 1.414213,0.58579 0.361929,0.36193 0.585787,0.86193 0.585787,1.41421 z""""
    val arrowStyle = """style="fill:none;stroke-width:1.1;stroke:#000;marker-end:url(#Arrow1Mend)""""
    val part1 =
      s"""      <g id="vc0">
         |        <text $textProps>0</text><path $circleProps></path>
         |        <path d="m -12.051698,978.09705 -6.012939,-0.0372" $arrowStyle></path>
         |        <path d="m -11.47885,969.47404 -7.171574,7.17157" $arrowStyle></path>
         |      </g>
         |      <g id="vc1">
         |        <text $textProps>1</text><path $circleProps></path>
         |        <path d="m -12.051706,978.09702 -6.012939,-0.0372" $arrowStyle></path>
         |        <path d="m -20.064645,970.0598 0,6" $arrowStyle></path>
         |      </g>
         |      <g id="vc2">
         |        <text $textProps>2</text><path $circleProps></path>
         |        <path d="m -12.051706,978.09704 -6.012939,-0.0372" $arrowStyle></path>
         |        <path d="m -28.650432,969.47404 7.171574,7.17157" $arrowStyle></path>
         |      </g>
         |      <g id="vc3">
         |        <text $textProps>3</text><path $circleProps></path>
         |        <path d="m -28.064646,978.05979 5.987061,-0.0372" $arrowStyle></path>
         |        <path d="m -12.090524,978.05979 -5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |      <g id="vc4">
         |        <text $textProps>4</text><path $circleProps></path>
         |        <path d="m -11.478848,969.47403 -7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -20.064635,970.05982 0,6" $arrowStyle></path>
         |      </g>
         |      <g id="vc5">
         |        <text $textProps>5</text><path $circleProps></path>
         |        <path d="m -11.491798,969.43678 -7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -28.663372,969.43678 7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vc6">
         |        <text $textProps>6</text><path $circleProps></path>
         |        <path d="m -11.49179,969.43678 -7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -28.077577,978.02257 6,0" $arrowStyle></path>
         |      </g>
         |      <g id="vc7">
         |        <text $textProps>7</text><path $circleProps></path>
         |        <path d="m -20.064645,970.05982 0,6" $arrowStyle></path>
         |        <path d="m -28.650432,969.47403 7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vc8">
         |        <text $textProps>8</text><path $circleProps></path>
         |        <path d="m -20.064645,970.0598 0,6" $arrowStyle></path>
         |        <path d="m -28.064645,978.0598 6,0" $arrowStyle></path>
         |      </g>
         |      <g id="vc9">
         |        <text $textProps>9</text><path $circleProps></path>
         |        <path d="m -28.663372,969.4368 7.171574,7.17157" $arrowStyle></path>
         |        <path d="m -28.077585,978.02258 6,0" $arrowStyle></path>
         |      </g>
         |"""

    val part2 =
      s"""      <g id="vcA">
         |        <text $textProps>A</text><path $circleProps></path>
         |        <path d="m -12.051706,978.09704 -6.012939,-0.0372" $arrowStyle></path>
         |        <path d="m -20.064645,960.05982 0,16" $arrowStyle></path>
         |      </g>
         |      <g id="vcB">
         |        <text $textProps>B</text><path $circleProps></path>
         |        <path d="m -11.47885,969.47404 -7.171574,7.17157" $arrowStyle></path>
         |        <path d="m -20.064637,960.05982 0,16" $arrowStyle></path>
         |      </g>
         |      <g id="vcC">
         |        <text $textProps>C</text><path $circleProps></path>
         |        <path d="m -20.064645,960.05982 0,16" $arrowStyle></path>
         |        <path d="m -28.650432,969.47404 7.171574,7.17157" $arrowStyle></path>
         |      </g>
         |      <g id="vcD">
         |        <text $textProps>D</text><path $circleProps></path>
         |        <path d="m -20.064645,960.05982 0,16" $arrowStyle></path>
         |        <path d="m -28.051706,978.09702 5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |      <g id="vcE">
         |        <text $textProps>E</text><path $circleProps></path>
         |        <path d="m -2.090516,977.98535 -15.987061,0.0372" $arrowStyle></path>
         |        <path d="m -11.49179,969.43678 -7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vcF">
         |        <text $textProps>F</text><path $circleProps></path>
         |        <path d="m -2.077585,978.0226 -16,0" $arrowStyle></path>
         |        <path d="m -20.077585,970.0226 0,6" $arrowStyle></path>
         |      </g>
         |      <g id="vcG">
         |        <text $textProps>G</text><path $circleProps></path>
         |        <path d="m -2.077585,978.0226 -16,0" $arrowStyle></path>
         |        <path d="m -20.077585,960.0226 0,16" $arrowStyle></path>
         |      </g>
         |      <g id="vcH">
         |        <text $textProps>H</text><path $circleProps></path>
         |        <path d="m -2.077585,978.0226 -15.98706,0.0372" $arrowStyle></path>
         |        <path d="m -28.650425,969.47403 7.17157,7.17158" $arrowStyle></path>
         |      </g>
         |"""

    val part3 =
      s"""      <g id="vcI">
         |        <text $textProps>I</text><path $circleProps></path>
         |        <path d="m -2.064637,978.0598 -16,0" $arrowStyle></path>
         |        <path d="m -28.051698,978.09702 5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |      <g id="vcJ">
         |        <text $textProps>J</text><path $circleProps></path>
         |        <path d="m -38.077585,978.02257 16,0" $arrowStyle></path>
         |        <path d="m -12.090524,978.05979 -5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |      <g id="vcK">
         |        <text $textProps>K</text><path $circleProps></path>
         |        <path d="m -2.064645,978.0598 -16,0" $arrowStyle></path>
         |        <path d="m -38.064645,978.0598 16,0" $arrowStyle></path>
         |      </g>
         |      <g id="vcL">
         |        <text $textProps>L</text><path $circleProps></path>
         |        <path d="m -11.49179,969.43678 -7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -38.077577,978.02257 16,0" $arrowStyle></path>
         |      </g>
         |      <g id="vcM">
         |        <text $textProps>M</text><path $circleProps></path>
         |        <path d="m -20.077585,970.02257 0,6" $arrowStyle></path>
         |        <path d="m -38.077585,978.02257 16,0" $arrowStyle></path>
         |      </g>
         |      <g id="vcN">
         |        <text $textProps>N</text><path $circleProps></path>
         |        <path d="m -20.077585,960.02257 0,16" $arrowStyle></path>
         |        <path d="m -38.077585,978.02257 16,0" $arrowStyle></path>
         |      </g>
         |      <g id="vcO">
         |        <text $textProps>O</text><path $circleProps></path>
         |        <path d="m -28.663372,969.43678 7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -38.064646,978.05979 15.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |"""

    val part4 =
      s"""      <g id="vcP">
         |        <path $circleProps></path><text $textProps>P</text>
         |        <path d="m -28.064646,978.05979 c 2.695104,-1.72224 4.352555,-1.11629 5.987061,-0.0372" $arrowStyle></path>
         |        <path d="m -28.064646,978.05979 c 2.225492,1.55137 4.178556,1.24684 5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |      <g id="vcQ">
         |        <path $circleProps></path><text $textProps>Q</text>
         |        <path d="m -28.650432,969.47403 c 1.186417,3.54854 3.731819,5.793 7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -28.650432,969.47403 c 4.170483,0.95665 5.985806,3.81058 7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vcR">
         |        <path $circleProps></path><text $textProps>R</text>
         |        <path d="m -20.064635,970.05982 c 2.229953,2.58242 0.789017,4.2147 0,6" $arrowStyle></path>
         |        <path d="m -20.064635,970.05982 c -2.263016,2.52293 -1.06713,4.19704 0,6" $arrowStyle></path>
         |      </g>
         |      <g id="vcS">
         |        <path $circleProps></path><text $textProps>S</text>
         |        <path d="m -11.478848,969.47403 c -4.271302,0.77178 -5.861627,3.84953 -7.171574,7.17158" $arrowStyle></path>
         |        <path d="m -11.478848,969.47403 c -0.540678,3.86673 -3.75869,5.83827 -7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vcT">
         |        <path $circleProps></path><text $textProps>T</text>
         |        <path d="m -12.090524,978.05979 c -2.596115,-1.9428 -4.382871,-1.03176 -5.987061,-0.0372" $arrowStyle></path>
         |        <path d="m -11.992394,977.40391 c -2.596115,1.9428 -4.382871,1.03176 -5.987061,0.0372" $arrowStyle></path>
         |      </g>
         |
         |      <g id="vcV">
         |        <path $circleProps></path><text $textProps>V</text>
         |        <path d="m -28.064646,978.05979 5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |      <g id="vcW">
         |        <path $circleProps></path><text $textProps>W</text>
         |        <path d="m -28.650432,969.47403 7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vcX">
         |        <path $circleProps></path><text $textProps>X</text>
         |        <path d="m -20.064635,970.05982 0,6" $arrowStyle></path>
         |      </g>
         |      <g id="vcY">
         |        <path $circleProps></path><text $textProps>Y</text>
         |        <path d="m -11.478848,969.47403 -7.171574,7.17158" $arrowStyle></path>
         |      </g>
         |      <g id="vcZ">
         |        <path $circleProps></path><text $textProps>Z</text>
         |        <path d="m -12.090524,978.05979 -5.987061,-0.0372" $arrowStyle></path>
         |      </g>
         |"""

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
       |$part1$part2$part3$part4
       |      <g id="vc-">
       |        <text $textProps>-</text><path $circleProps></path>
       |      </g>
       |    </g>
       |$clones
       |  </g>
       |</svg>
       |""".stripMargin
  }
}
