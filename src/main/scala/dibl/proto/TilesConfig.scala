package dibl.proto

import dibl._

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("TilesConfig") case class TilesConfig(urlQuery: String) {
  println(urlQuery)

  private val keyValueStrings: Seq[String] = urlQuery
    .split("&")
    .filter(_.matches(".+=.*"))

  private val fields: Map[String, String] = keyValueStrings
    .map { kv: String => (kv.replaceAll("=.*", ""), kv.replaceAll(".*=", "")) }
    .toMap

  private def getMatrix(key: String): Seq[String] = {
    fields.getOrElse(key, "").toLowerCase.split("[^-a-z0-9]+").map(_.trim)
  }

  // TODO defend against unequal rows lengths
  val leftMatrix: Seq[String] = getMatrix("footside")
  val rightMatrix: Seq[String] = getMatrix("headside")
  private val centerMatrix: Seq[String] = getMatrix("tile")

  private val leftMatrixStitch: String = fields.getOrElse("footsideStitch", "ctctt")
  private val rightMatrixStitch: String = fields.getOrElse("headsideStitch", "ctctt")
  private val centerMatrixStitch: String = fields.getOrElse("tileStitch", "ctc")

  @JSExport
  val leftMatrixCols: Int = Option(leftMatrix.head).map(_.length).getOrElse(2)
  @JSExport
  val centerMatrixCols: Int = Option(centerMatrix.head).map(_.length).getOrElse(5)
  @JSExport
  val rightMatrixCols: Int = Option(rightMatrix.head).map(_.length).getOrElse(2)

  val centerMatrixRows: Int = centerMatrix.length

  @JSExport
  val maxTileRows: Int = Math.max(centerMatrixRows, Math.max(leftMatrix.length, rightMatrix.length))

  // TODO defaults based on the dimensions of the above matrices
  @JSExport
  val totalRows: Int = fields.getOrElse("patchHeight", "12").safeToInt
  private val centerCols: Int = fields.getOrElse("patchWidth", "12").safeToInt
  val shiftRowsSE: Int = fields.getOrElse("shiftRowsSE", "12").safeToInt
  val shiftRowsSW: Int = fields.getOrElse("shiftRowsSW", "12").safeToInt
  val shiftColsSE: Int = fields.getOrElse("shiftColsSE", "12").safeToInt
  val shiftColsSW: Int = fields.getOrElse("shiftColsSW", "12").safeToInt

  private val leftMarginWidth = leftMatrix.head.trim.length
  private val offsetRightMargin = leftMarginWidth + centerCols

  @JSExport
  val totalCols: Int = centerCols +
    leftMarginWidth +
    (if (offsetRightMargin == 0) 0
     else rightMatrix.head.length)

  private val targetMatrix: Array[Array[Item]] = Array.fill[Array[Item]](totalRows)(
    Array.fill[Item](totalCols)(Item("", relativeSources = Array.empty))
  )

  def getItemMatrix: Seq[Seq[Item]] = {
    // items represent the callers vision: elements for the prototype/pair-diagram
    // target has internal meaning: the matrix to fill with the left/center/right-matrix
    targetMatrix.map(_.toSeq)
  }

  lazy val nrOfPairsOut: Seq[Seq[Int]] = {
    val rows: Int = targetMatrix.length
    val cols: Int = targetMatrix.head.length
    val pairsOut = Array.fill[Array[Int]](rows)(Array.fill[Int](cols)(0))
    for {
      r <- targetMatrix.indices
      c <- targetMatrix(r).indices
    } {
      targetMatrix(r)(c).relativeSources
        .foreach { case (relativeSourceRow, relativeSourceCol) =>
          val row: Int = r + relativeSourceRow
          val col: Int = c + relativeSourceCol
          if (row >= 0 && col >= 0 && col < cols && row < rows) {
            pairsOut(row)(col) += 1
          }
        }
    }
    pairsOut.toSeq.map(_.toSeq)
  }

  // repeat foot-side / head-side

  if (leftMarginWidth > 0) replaceItems(leftMatrix, 0, leftMatrixStitch)
  if (offsetRightMargin > 0) replaceItems(rightMatrix, offsetRightMargin, rightMatrixStitch)

  private def replaceItems(inputMatrix: Seq[String], offset: Int, defaultStitch: String): Unit = {
    for {
      row <- targetMatrix.indices
      rSource = row % inputMatrix.length
      col <- inputMatrix(rSource).indices
    } {
      val id = Stitches.toID(rSource, col + offset)
      val vectorCode = inputMatrix(rSource)(col)
      val stitch = if ("-VWXYZ".contains(vectorCode.toUpper)) "-"
                   else fields.getOrElse(id, defaultStitch)
      targetMatrix(row)(col + offset) = Item(
        id,
        vectorCode,
        stitch,
        row < inputMatrix.length,
        relativeSources = Matrix.toRelativeSources(vectorCode)
      )
    }
  }

  // repeat tiles, see: docs/help/Tiles.md#arrange-the-repeats
  private val targetSquareSize = Math.max(totalRows, centerCols)
  for {
    i <- 0 until targetSquareSize
    j <- -targetSquareSize until targetSquareSize
    translateRow = (i * shiftRowsSE) + (j * shiftRowsSW)
    translateCol = (i * shiftColsSE) + (j * shiftColsSW)
    r <- centerMatrix.indices
    c <- centerMatrix(r).indices
  } {
    // t in rt/ct stands for target cell, r and c for row and col
    val rt = r + translateRow
    val ct = c + translateCol
    if (rt >= 0 && ct >= 0 && rt < totalRows && ct < centerCols) {
      val id = Stitches.toID(r, c + leftMarginWidth)
      val vectorCode = centerMatrix(r)(c) // symbol on cheat sheet
      val stitch = if ("-VWXYZ".contains(vectorCode.toUpper)) "-"
                   else fields.getOrElse(id, centerMatrixStitch)
      targetMatrix(rt)(ct + leftMarginWidth) = Item(id, vectorCode, stitch, r == rt && c == ct,
        relativeSources = Matrix.toRelativeSources(vectorCode))
    }
  }

  // rejoin links to ignored stitches
  Item.cleanupIgnoredStitches(targetMatrix)

  /**
   * Get links for one tile.
   *
   * @param diagram A diagram created from this object.
   *                Use diagrams with the original nodes for transformation from pairs to threads to pairs etc.
   *                The result is not defined when using nodes with changed values for the x/y properties
   *                for any of the transformation steps. Plaits with more than 12 half stitches (ct)
   *                might cause a problem with duplicate ids in transformed diagrams.
   * @param scale   Use value one for the initial pair diagram,
   *                multiply by 2 for each transition from pair to thread diagram.
   *
   * Requirements:
   * - The values for totalRows alias patchHeight respective totalCols alias patchWidth
   *   must add at least 4 rows/cols to the dimensions of the centerMatrix alias tile.
   * - No gaps between tiles.
   * - As for now: the leftMatrix and rightMatrix must be empty.
   *
   * @return An empty array on some types of invalid arguments, the type of error is logged to standard-out.
   *
   *         Changes to the diagram won't affect previously returned results, nor the other way around.
   *
   *         Node objects inside the tile are different from those outside the tile.
   *         Nodes outside the tile will have an id property shared by a node inside the tile on the
   *         opposite side. Where along the opposite side is defined by the four shift properties.
   *
   *         Each transformation from pairs to threads puts more nodes at the same x/y positions.
   *         The start of their id-s will be identical, the tail of their id-s will be different.
   */
  @JSExport
  def linksOfCenterTile(diagram: Diagram, scale: Int): Array[(NodeProps, Array[NodeProps])] = {
    val links: Seq[(NodeProps, Array[NodeProps])] = boundsForTileLinks match {
      case (0,0,0,0) => Seq.empty
      case (n, e, s, w) => diagram.tileLinks(n * 15 * scale, e * 15 * scale, s * 15 * scale, w * 15 * scale)
    }
    if (links.exists{link =>
      // safeguard against invalid results
      val (core, clockWise) = link
      core.id.isEmpty || clockWise.length != 4
    }) Seq.empty
    else links
  }.toArray

  /**
    * @param scale Use value 15 for the initial pair diagram,
    *              multiply by 2 for each transition from pair to thread diagram.
    * @return SVG element <rect> bounding box for nodes of linksOfCenterTile
    */
  def svgBoundsOfCenterTile (scale: Int): String = {
    val style = "fill:none;stroke:#ddd;stroke-width:2;stroke-linecap:round;stroke-linejoin:miter;stroke-opacity:1"
    val (n, e, s, w) = boundsForTileLinks
    s"<rect ry='0.25' y='${n * scale}' x='${w * scale}' height='${(s - n) * scale}' width='${(e - w) * scale}' style='$style'></rect>"
  }

  /**
    *
    * @return A bounding box in terms of rows/cols for one tile.
    *         At least two rows and cols of the same tile surround the returned tile.
    *         (0,0,0,0) if the prototype diagram is too small to find the required tile
    *         or for not implemented types of tiling.
    */
  def boundsForTileLinks: (Double, Double, Double, Double) = {
    lazy val minWidthForBricks = centerMatrixCols + 4 + leftMarginWidth
    lazy val minHeightForBricks = centerMatrixRows + 4
    lazy val minWidth = shiftColsSE + centerMatrixCols + 2 + leftMarginWidth
    lazy val minHeight = shiftRowsSE + centerMatrixRows + 2
    lazy val isHBrick =
      shiftRowsSE == shiftRowsSW &&
        shiftRowsSE == centerMatrixRows &&
        shiftColsSE - shiftColsSW == centerMatrixCols
    lazy val isVBrick =
      shiftColsSE == shiftColsSW &&
        shiftColsSE == centerMatrixCols &&
        shiftRowsSE - shiftRowsSW == centerMatrixRows

    def invalidMin(dimension: String, value: Int): (Double, Double, Double, Double) = {
      invalid(s"patch $dimension should be at least $value")
    }
    def invalid(msg: String): (Double, Double, Double, Double) = {
      println(msg)
      (0,0,0,0)
    }
    // Offsets and distances between the nodes on the initial square grid:
    // https://github.com/d-bl/GroundForge/blob/94342eb/src/main/scala/dibl/NewPairDiagram.scala#L20
    // https://github.com/d-bl/GroundForge/blob/268b2e2/src/main/scala/dibl/ThreadDiagram.scala#L105-L107
    // In other words: 15 between rows/cols, 2 rows/cols allowance for the fringe.
    if (totalCols < minWidthForBricks) invalidMin("width", minWidthForBricks)
    else if (totalRows < minHeightForBricks) invalidMin("height", minHeightForBricks)
    else if (isHBrick || isVBrick) (
      // bounding box starts at the third row/col, thus we have four links on all nodes
      3.5,// north
      3.5 + leftMarginWidth + centerMatrixCols, // east
      3.5 + centerMatrixRows,// south
      3.5 + leftMarginWidth,// west
    )
    else if (shiftColsSE < 2 && shiftRowsSE < 2) invalid("type of tiling is not supported")
    else if (minWidth > totalCols) invalidMin("patch width", minWidth)
    else if (minHeight > totalRows) invalidMin("height", minHeight)
    else {
      // TODO extend dimensions of overlapping tiles to avoid gaps,
      //  then the tile type no longer matters
      val offsetCols = 1.5 + shiftColsSE + leftMarginWidth
      val offsetRows = 1.5 + shiftRowsSE
      (
        offsetRows,// north
        offsetCols + centerMatrixCols, // east
        offsetRows + centerMatrixRows, // south
        offsetCols,// west
      )
    }
  }
}
