package dibl

/**
  * Below a schematic ascii visualisation of a definition of a
  * two-in-two out directed graph representing a bobbin lace pair diagram.
  * {{{
  * ::v v::
  * > o o <
  * > o o <
  * ::^ ^::
  * }}}
  * Each 'o' character represents a possible node or stitch,
  * each link represents a pair of threads wound on bobbins.
  * The `v` symbols represent zero to two incoming links/pairs,
  * the `^` symbols zero to two outgoing pairs.
  * The `<` and `>` zero to three incoming and/or outgoing pairs.
  *
  * The right and left fringes are laced together with additional edge pairs
  * to make `|<` respective `>|` shaped stitches along the edges called footsides.
  * Think these footside stitches as stick figures with arms and feet.
  * The sideways reaching arm and standing foot is a pair left aside by a previous row of stitches,
  * the upwards reaching arm and kicking foot is a pair coming into play again for a new row of stitches.
  *
  * @param absSrcNodes Defines the o's in the ascii art diagram by two incoming pairs.
  *                    The row and col of a cell define the target of incoming links.
  *                    The tuples in a cell define the row/col of zero or two sources of a link.
  *                    The outgoing pairs follow implicitly from neighbouring nodes beside and/or below.
  *                    A source is one or two horizontal/vertical cells or one diagonal cell above/aside a target.
  *                    The outer two rows and columns are placeholders for sources and targets of the fringes.
  */
class Fringes(absSrcNodes: Array[Array[SrcNodes]]) {

  type Cell = (Int, Int)

  def Cell(row: Int, col: Int) = (row, col)

  type Link = (Cell, Cell)

  def Link(source: Cell, target: Cell) = (source, target)

  private val drawingScale = 10
  private val topTargetRow: Int = 2
  private val leftTargetCol: Int = 2
  private lazy val rightTargetCol: Int = absSrcNodes(0).length - leftTargetCol - 1

  /** @param targetCol   the first column next to a margin for the fringes
    * @param fromOutside filter for the links to return
    * @return the kicking feet of the `|<` or `>|` shaped footside stitches.
    */
  private def intoSide(targetCol: Int, fromOutside: (Int) => Boolean): Seq[Link] = {

    val col: Array[SrcNodes] = absSrcNodes.map(_ (targetCol)) // either zero or two incoming pairs per element
    val firstUsedNode = col.map(_.length).indexOf(2) // find the first with two incoming pairs
    for {
      targetRow <- firstUsedNode + 1 until col.length
      (sourceRow, sourceCol) <- col(targetRow)
      if fromOutside(sourceCol)
    } yield Link(
      source = Cell(sourceRow, sourceCol),
      target = Cell(targetRow, targetCol)
    )
  }

  private def intoTop(targetRow: Int, fromOutside: (Int, Int, Int) => Boolean): Seq[Link] = {
    val row = absSrcNodes(targetRow)
    for {
      targetCol <- leftTargetCol to rightTargetCol
      (sourceRow, sourceCol) <- row(targetCol)
      if fromOutside(targetCol, sourceRow, sourceCol)
    } yield Link(
      source = Cell(sourceRow, sourceCol),
      target = Cell(targetRow, targetCol)
    )
  }

  lazy val allLinks: Seq[Link] = for {
    targetRow <- absSrcNodes.indices
    targetCol <- absSrcNodes(targetRow).indices
    source <- absSrcNodes(targetRow)(targetCol)
  } yield Link(source, Cell(targetRow, targetCol))

  /** The right feet of the `|<` shaped footside stitches. */
  lazy val reusedLeft: Seq[Link] = {
    intoSide(leftTargetCol, fromOutside = _ < leftTargetCol) ++
      intoSide(leftTargetCol + 1, fromOutside = _ < leftTargetCol)
  }

  /** The left feet of the `>|` shaped footside stitches. */
  lazy val reusedRight: Seq[Link] = {
    intoSide(rightTargetCol, fromOutside = (sourceCol) => sourceCol > rightTargetCol) ++
      intoSide(rightTargetCol - 1, fromOutside = (sourceCol) => sourceCol  > rightTargetCol)
  }

  /** The red links in the [[svgDoc]],
    * the pairs needed to start a patch of lace along the top and corners,
    * each link is one leg of the `v`'s in the ascii art diagram of the class */
  lazy val newPairs: Seq[Link] = {
    intoTop(topTargetRow, fromOutside = (targetCol, sourceRow, sourceCol) =>
      sourceRow < topTargetRow || // vertical/diagonal links
        (targetCol == leftTargetCol && sourceCol < leftTargetCol) || // from outer left
        (targetCol == rightTargetCol && sourceCol > rightTargetCol) // from outer right
    ) ++
      intoTop(topTargetRow + 1, (targetCol, sourceRow, sourceCol) =>
        sourceRow < topTargetRow // double length vertical links
      )
    // TODO add the pairs for the corners in case the corner was a not used node
  }

  /** TODO additional links so all nodes have two incoming links except for the sources of [[newPairs]] */
  lazy val footSides: Seq[Link] = ???

  /** an SVG document with all links of the two-in-two-out directed graph in black,
    * incoming links along the top drawn in read on top of the black,
    * incoming links along the side in green on top of the black.
    * TODO add the not yet implemented [[footSides]] in blue
    */
  lazy val svgDoc =
  s"""<svg
      |  version='1.1'
      |  id='svg2' height='90mm'
      |  width='90mm' xmlns:xlink='http://www.w3.org/1999/xlink'
      |  xmlns='http://www.w3.org/2000/svg'
      |  xmlns:inkscape='http://www.inkscape.org/namespaces/inkscape'
      |>
      |<g transform='translate($drawingScale,$drawingScale)'>
      |${draw(allLinks, "#000")}
      |${draw(newPairs, "#F00")}
      |${draw(reusedLeft, "#0F0")}
      |${draw(reusedRight, "#0F0")}
      |</g>
      |</svg>""".stripMargin

  private def draw(links: Seq[Link], color: String): String = (for {
    ((sourceRow, sourceCol), (targetRow, targetCol)) <- links
  } yield {
    s"""<path style='stroke:$color;fill:none'
        |  d='M ${sourceCol * drawingScale},${sourceRow * drawingScale} ${targetCol * drawingScale},${targetRow * drawingScale}'
        |/>
        |""".stripMargin
  }).mkString
}
