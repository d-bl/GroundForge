package dibl.proto

import dibl._

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("TilesConfig") case class TilesConfig(urlQuery: String) {
  println(urlQuery)

  private val keyValueStrings: Seq[String] = urlQuery
    .split("&")
    .filter(_.matches(".+=.*"))

  private val queryFields: Map[String, String] = keyValueStrings
    .map { kv: String => (kv.replaceAll("=.*", ""), kv.replaceAll(".*=", "")) }
    .toMap

  // TODO defend against unequal rows lengths
  private val leftMatrix: Seq[String] = queryFields.matrixLines("footside")
  private val rightMatrix: Seq[String] = queryFields.matrixLines("headside")
  private val centerMatrix: Seq[String] = queryFields.matrixLines("tile")

  private val leftMatrixStitch: String = queryFields.getOrElse("footsideStitch", "ctctt")
  private val rightMatrixStitch: String = queryFields.getOrElse("headsideStitch", "ctctt")
  private val centerMatrixStitch: String = queryFields.getOrElse("tileStitch", "ctc")

  @JSExport
  val leftMatrixCols: Int = Option(leftMatrix.head).map(_.length).getOrElse(2)
  @JSExport
  val centerMatrixCols: Int = Option(centerMatrix.head).map(_.length).getOrElse(5)
  @JSExport
  val rightMatrixCols: Int = Option(rightMatrix.head).map(_.length).getOrElse(2)

  @JSExport
  val centerMatrixRows: Int = centerMatrix.length
  @JSExport
  val rightMatrixRows: Int = rightMatrix.length
  @JSExport
  val leftMatrixRows: Int = leftMatrix.length

  @JSExport
  val maxTileRows: Int = Math.max(centerMatrixRows, Math.max(leftMatrix.length, rightMatrix.length))

  // patch size is at least tile-size; default 2x2 tiles
  val patchHeight: Int = Math.max(queryFields.getOrElse("patchHeight", (4*centerMatrixRows).toString).safeToInt, centerMatrixRows)
  val patchWidth: Int = Math.max(queryFields.getOrElse("patchWidth", (6*centerMatrixCols).toString).safeToInt, centerMatrixCols)
  // defaults for a checker matrix
  val shiftRowsSE: Int = queryFields.getOrElse("shiftRowsSE", centerMatrixRows.toString).safeToInt
  val shiftRowsSW: Int = queryFields.getOrElse("shiftRowsSW", centerMatrixRows.toString).safeToInt
  val shiftColsSE: Int = queryFields.getOrElse("shiftColsSE", centerMatrixCols.toString).safeToInt
  val shiftColsSW: Int = queryFields.getOrElse("shiftColsSW", "0").safeToInt

  private val leftMarginWidth = leftMatrix.head.trim.length
  private val offsetRightMargin = leftMarginWidth + patchWidth

  @JSExport
  val totalRows: Int = patchHeight

  @JSExport
  val totalCols: Int = patchWidth +
    leftMarginWidth +
    (if (offsetRightMargin == 0) 0
     else rightMatrix.head.length)

  private val targetMatrix: Array[Array[Item]] = Array.fill[Array[Item]](patchHeight)(
    Array.fill[Item](totalCols)(Item("", relativeSources = Array.empty))
  )

  @JSExport
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

  /**
    * Sets the opaque items in the targetMatrix.
    * See /GroungForge-help/docs/Replace.md#arrange-the-repeats
    *
    * @param inputMatrix       tiles.html#footside, #tile or #headside
    *                          valid characters visualized on GroundForge-help/docs/images/matrix-template.png
    * @param offsetOfFirstTile horizontal position of the original tile within the targetMatrix
    * @param defaultStitch     tiles.html#footsideStitch, #tileStitch or #headsideStitch
    */
  private def setFirstTile(inputMatrix: Seq[String], offsetOfFirstTile: Int, defaultStitch: String): Unit = {
    for {
      row <- inputMatrix.indices
      col <- inputMatrix(row).indices
    } {
      val targetCol = col + offsetOfFirstTile
      val id = Stitches.toID(row, targetCol)
      val vectorCode = inputMatrix(row)(col)
      val stitch = if ("-VWXYZ".contains(vectorCode.toUpper)) "-"
                   else queryFields.getOrElse(id, defaultStitch)
      if (row < patchHeight && targetCol < totalCols)
        targetMatrix(row)(targetCol) = Item(
          id,
          vectorCode,
          stitch,
          row < inputMatrix.length,
          relativeSources = Matrix.toRelativeSources(vectorCode)
        )
    }
  }

  private def copyStitch(targetRow: Int, targetCol: Int, sourceCol: Int, sourceRow: Int): Unit = {
    val item = targetMatrix(targetRow)(targetCol)
    if (!item.isOpaque || item.vectorCode == '-') // the condition keeps enough overlapping corners opaque
      targetMatrix(targetRow)(targetCol) = targetMatrix(sourceRow)(sourceCol).copy(isOpaque = false)
  }

  private def repeatSide(offsetOfFirstTile: Int, rows: Int, cols: Int): Unit = {
    for {col <- offsetOfFirstTile until offsetOfFirstTile + cols} {
      for {row <- rows until totalRows} {
        copyStitch(row, col, col, row % rows)
      }
    }
  }

  /**
    * @param startRow Top position for the new tile within the targetMatrix.
    * @param startCol Left position for the new tile within the patch.
    *                 The patch is a subsection of the targetMatrix:
    *                 from leftMatrixCols until offsetRightMargin.
    */
  private def copyCenterTile(startRow: Int, startCol: Int): Unit = {
    for {
      sourceRow <- 0 until centerMatrixRows // row within the specified tile as well as the copy in the target matrix
      targetRow = startRow + sourceRow
      col <- 0 until centerMatrixCols // col within the specified tile
    } if (0 <= targetRow && targetRow < totalRows) { // row is inside patch
      val sourceCol = col + leftMatrixCols // col of the tile copied previously into the target mat rix
      val targetCol = startCol + col + leftMatrixCols // col of the tile to be copied into the target matrix
      if (leftMarginWidth <= targetCol && targetCol < offsetRightMargin) { // col is inside patch
        copyStitch(targetRow, targetCol, sourceCol, sourceRow)
      }
    }
  }

  /**
    * Called outside copyCenterTile to prevent the loops inside
    * which have to check stitchIsInsidePatch for each potential stitch ojn the tile.
    *
    * @return false if the tile is completely outside the patch area
    */
  private def tileIsInsidePatch(startRow: Int, startCol: Int): Boolean = {
    -centerMatrixCols < startCol && startCol < offsetRightMargin &&
      -centerMatrixRows < startRow && startRow < totalRows
  }

  setFirstTile(centerMatrix, leftMarginWidth, centerMatrixStitch)
  if (centerMatrixRows > 0 && centerMatrixCols > 0 && patchWidth > 0 && patchHeight > 0) {
    val squaredPatchSize = Math.max(patchWidth, patchHeight)
    for {
      i <- 0 until squaredPatchSize
      j <- squaredPatchSize until -squaredPatchSize by -1
    } if (!(i == 0 & j == 0)) {
      val startRow = j * shiftRowsSW + i * shiftRowsSE
      val startCol = j * shiftColsSW + i * shiftColsSE
      if (tileIsInsidePatch(startRow, startCol))
        copyCenterTile(startRow, startCol)
    }
  }

  // the foot/head-sides now can overwrite the center tiles as far as they exceeded their area
  setFirstTile(leftMatrix, 0, leftMatrixStitch)
  repeatSide(0, leftMatrix.length, leftMatrixCols)
  setFirstTile(rightMatrix, offsetRightMargin, rightMatrixStitch)
  repeatSide(offsetRightMargin, rightMatrix.length, rightMatrixCols)

  // rejoin links to ignored stitches
  Item.cleanupIgnoredStitches(targetMatrix)

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
    else if (patchHeight < minHeightForBricks) invalidMin("height", minHeightForBricks)
    else if (isHBrick || isVBrick) (
      // bounding box starts at the third row/col, thus we have four links on all nodes
      3.5,// north
      3.5 + leftMarginWidth + centerMatrixCols, // east
      3.5 + centerMatrixRows,// south
      3.5 + leftMarginWidth,// west
    )
    else if (shiftColsSE < 2 && shiftRowsSE < 2) invalid("type of tiling is not supported")
    else if (minWidth > totalCols) invalidMin("patch width", minWidth)
    else if (minHeight > patchHeight) invalidMin("height", minHeight)
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
