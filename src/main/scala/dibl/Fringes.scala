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

  private lazy val leftTargetCol: Int = 2
  private lazy val rightTargetCol: Int = absSrcNodes(0).length - leftTargetCol
  private lazy val topTargetRow: Int = 2

  /** @param targetCol   the first column next to a margin for the fringes
    * @param fromOutside figured out with extract parameter as explained on
    *                    https://github.com/DANS-KNAW/course-scala/pull/14#issuecomment-253437387
    * @return the kicking feet of the `|<` or `>|` shaped footside stitches.
    */
  private def reusedPair(targetCol: Int, fromOutside: (Int) => Boolean): Seq[Link] = {

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

  /** The right feet of the `|<` shaped footside stitches. */
  lazy val reusedLeft: Seq[Link] = reusedPair(leftTargetCol, fromOutside = _ < leftTargetCol)

  /** The left feet of the `>|` shaped footside stitches. */
  lazy val reusedRight: Seq[Link] = reusedPair(rightTargetCol, fromOutside = _ > rightTargetCol)

  /** The pairs needed to start a patch of lace along the top and corners,
    * each link is one leg of the `v`'s in the ascii art diagram of the class */
  lazy val newPairs: Seq[Link] = {

    val row: Array[SrcNodes] = absSrcNodes(topTargetRow)
    for {
      targetCol <- leftTargetCol to rightTargetCol
      (sourceRow, sourceCol) <- row(targetCol)
      if sourceRow < topTargetRow || // skip horizontal links
        (targetCol == leftTargetCol && sourceCol < leftTargetCol) || //  unless at a corner
        (targetCol == rightTargetCol && sourceCol > rightTargetCol)
    } yield Link(
      source = Cell(sourceRow, sourceCol),
      target = Cell(topTargetRow, targetCol)
    ) // TODO add the pairs for the corners in case the corner was a not used node
  }
}
