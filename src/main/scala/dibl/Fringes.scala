package dibl

/**
  * Below an ascii visualisation of a definition of a two-in-two out directed graph representing a bobbin lace pair diagram.
  * Each character represents a node or stitch, each link represents a pair of threads wound on bobbins.
  * {{{
  * ::v v::
  * > o o <
  * > o o <
  * ::^ ^::
  * }}}
  * Each `o` represent either an unused node or a stitch with two incoming pairs and two outgoing pairs.
  * The `v` symbols represent zero to two incoming pairs for the graph, the `^` symbols zero to two outgoing pairs.
  * The `<` and `>` zero to three incoming and/or outgoing pairs.
  * The row and col of each cell tagged as `o` defines the target of zero or two incoming links,
  * and implicitly the source of an equal number of outgoing links.
  *
  * @param absSrcNodes Defines the o's in above ascii art by two incoming pairs.
  *                    The tuples in a cell define the row/col of zero or two sources of a link.
  *                    A source is one or two horizontal/vertical cells or one diagonal cell above/aside a target.
  *                    The third row from the top and the third row from each side are used to figure out incoming pairs.
  */
class Fringes(absSrcNodes: Array[Array[SrcNodes]]) {

  type Cell = (Int, Int)
  def Cell (row: Int, col: Int) = (row, col)

  type Link = (Cell, Cell)
  def Link (source: Cell, target: Cell) = (source, target)

  private lazy val leftTargetCol: Int = 2
  private lazy val rightTargetCol: Int = absSrcNodes(0).length - leftTargetCol
  private lazy val topTargetRow: Int = 2

  private def reusedPair(targetCol: Int, notVertical: (Int) => Boolean): Seq[Link] = {

    val col: Array[SrcNodes] = absSrcNodes.map(_ (targetCol))
    for {
      targetRow <- topTargetRow + 1 until col.length
      (sourceRow, sourceCol) <- col(targetRow)
      if notVertical(sourceCol) // skip vertical links
    } yield Link(
      source = (sourceRow, sourceCol),
      target = (targetRow, targetCol)
    )
  }

  lazy val reusedLeft = reusedPair(leftTargetCol, notVertical = _ < leftTargetCol)

  lazy val reusedRight = reusedPair(rightTargetCol, notVertical = _ > rightTargetCol)

  lazy val newPairs: Seq[Link] = {

    val row: Array[SrcNodes] = absSrcNodes(topTargetRow)
    for {
      targetCol <- leftTargetCol to rightTargetCol
      (sourceRow, sourceCol) <- row(targetCol)
      if sourceRow < topTargetRow && // skip horizontal links
        targetCol != leftTargetCol && //  unless at a corner
        targetCol != rightTargetCol
    } yield Link(
      source = (sourceRow, sourceCol),
      target = (topTargetRow, targetCol)
    )
  }
}
