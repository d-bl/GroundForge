package dibl

import dibl.Stitches.defaultColorValue

import scala.scalajs.js.annotation.JSExport

@JSExport
object Config {

  @JSExport
  def create(urlQuery: String): Config = new Config(urlQuery)
}

@JSExport
class Config(urlQuery: String) {
  println(urlQuery)

  private val keyValueStrings: Seq[String] = urlQuery
    .split("&")
    .filter(_.matches(".+=.*"))

  private val fields: Map[String, String] = keyValueStrings
    .map { kv: String => (kv.replaceAll("=.*", ""), kv.replaceAll(".*=", "")) }
    .toMap

  private def getMatrix(key: String): Array[String] = {
    fields.getOrElse(key, "").toLowerCase.split("[^-a-z0-9]+").map(_.trim)
  }

  // TODO defend against unequal rows lengths
  val leftMatrix: Array[String] = getMatrix("footside")
  val rightMatrix: Array[String] = getMatrix("headside")
  private val centerMatrix: Array[String] = getMatrix("tile")

  private val leftMatrixStitch: String = fields.getOrElse("footsideStitch", "ctctt")
  private val rightMatrixStitch: String = fields.getOrElse("headsideStitch", "ctctt")
  private val centerMatrixStitch: String = fields.getOrElse("tileStitch", "ctc")

  @JSExport
  val leftMatrixCols: Int = Option(leftMatrix.head).map(_.length).getOrElse(2)
  @JSExport
  val centerMatrixCols: Int = Option(centerMatrix.head).map(_.length).getOrElse(5)
  @JSExport
  val rightMatrixCols: Int = Option(rightMatrix.head).map(_.length).getOrElse(2)

  private val centerMatrixRows: Int = centerMatrix.length

  @JSExport
  val maxTileRows: Int = Math.max(centerMatrixRows, Math.max(leftMatrix.length, rightMatrix.length))

  // TODO defaults based on the dimensions of the above matrices
  @JSExport
  val totalRows: Int = fields.getOrElse("patchHeight", "12").replaceAll("[^0-9-]", "").toInt
  private val centerCols: Int = fields.getOrElse("patchWidth", "12").replaceAll("[^0-9-]", "").toInt
  val shiftRowsSE: Int = fields.getOrElse("shiftRowsSE", "12").replaceAll("[^0-9-]", "").toInt
  val shiftRowsSW: Int = fields.getOrElse("shiftRowsSW", "12").replaceAll("[^0-9-]", "").toInt
  val shiftColsSE: Int = fields.getOrElse("shiftColsSE", "12").replaceAll("[^0-9-]", "").toInt
  val shiftColsSW: Int = fields.getOrElse("shiftColsSW", "12").replaceAll("[^0-9-]", "").toInt

  private val leftMarginWidth = leftMatrix.head.trim.length
  private val offsetRightMargin = leftMarginWidth + centerCols

  @JSExport
  val totalCols: Int = centerCols +
    leftMarginWidth +
    (if (offsetRightMargin == 0) 0
     else rightMatrix.head.length)

  case class Item(id: String,
                  vectorCode: Char = '-',
                  stitch: String = "",
                  isOpaque: Boolean = false) {
    val color: Option[String] = Option(defaultColorValue(stitch))
      .filter(_.nonEmpty)
  }

  val itemMatrix: Array[Array[Item]] = Array.fill[Array[Item]](totalRows)(
    Array.fill[Item](totalCols)(Item(""))
  )

  lazy val pairsOut: Array[Array[Int]] = {
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

  // repeat foot-side / head-side

  if (leftMarginWidth > 0) replaceItems(leftMatrix, 0, leftMatrixStitch)
  if (offsetRightMargin > 0) replaceItems(rightMatrix, offsetRightMargin, rightMatrixStitch)

  private def replaceItems(inputMatrix: Array[String], offset: Int, defaultStitch: String): Unit = {
    for {r <- 0 until totalRows} {
      for {c <- 0 until inputMatrix.head.length} {
        val rSource = r % inputMatrix.length
        val id = Stitches.toID(rSource, c + offset)
        val vectorCode = inputMatrix(rSource)(c)
        val stitch = if (vectorCode == '-') ""
                     else fields.getOrElse(id, defaultStitch)
        itemMatrix(r)(c + offset) = Item(id, vectorCode, stitch, r < inputMatrix.length)
      }
    }
  }

  // repeat tiles, see: docs/help/images/shift-directions.png

  //noinspection RangeToIndices
  for { // TODO reduce ranges to avoid if
    i <- 0 until totalRows
    j <- -centerCols until centerCols
    translateRow = (i * shiftRowsSE) + (j * shiftRowsSW)
    translateCol = (i * shiftColsSE) + (j * shiftColsSW)
    r <- 0 until centerMatrixRows
    c <- 0 until centerMatrix.head.length
  } {
    // t in rt/ct stands for target cell, r and c for row and col
    val rt = r + translateRow
    val ct = c + translateCol
    if (rt >= 0 && ct >= 0 && rt < totalRows && ct < centerCols) {
      val id = Stitches.toID(r, c + leftMarginWidth)
      val vectorCode = centerMatrix(r)(c)
      val stitch = if (vectorCode == '-') ""
                   else fields.getOrElse(id, centerMatrixStitch)
      itemMatrix(rt)(ct + leftMarginWidth) = Item(id, vectorCode, stitch, r == rt && c == ct)
    }
  }

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
   *         Otherwise tuples with (source,target) for all links within a tile and to adjacent tiles.
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
  def centerTile(diagram: Diagram, scale: Int): Seq[LinkedNodes] = {

    lazy val minWidthForBricks = centerMatrixCols + 4
    lazy val minHeightForBricks = centerMatrixRows + 4
    lazy val minWidth = shiftColsSE + centerMatrixCols + 2
    lazy val minHeight = shiftRowsSE + centerMatrixRows + 2
    lazy val isHBrick =
    shiftRowsSE == shiftRowsSW &&
      shiftRowsSE == centerMatrixRows &&
      shiftColsSE - shiftColsSW == centerMatrixCols
    lazy val isVBrick =
    shiftColsSE == shiftColsSW &&
      shiftColsSE == centerMatrixCols &&
      shiftRowsSE - shiftRowsSW == centerMatrixRows

    def invalidMin(dimension: String, value: Int): Seq[LinkedNodes] = {
      invalid(s"patch $dimension should be at least $value")
    }
    def invalid(msg: String): Seq[LinkedNodes] = {
      println(msg)
      Seq.empty
    }
    // Offsets and distances between the nodes on the initial square grid:
    // https://github.com/d-bl/GroundForge/blob/94342eb/src/main/scala/dibl/NewPairDiagram.scala#L20
    // https://github.com/d-bl/GroundForge/blob/268b2e2/src/main/scala/dibl/ThreadDiagram.scala#L105-L107
    // In other words: 15 between rows/cols, 2 rows/cols allowance for the fringe.
    //                 Another 2 rows/cols allowance to have four links on all nodes.
    if (!leftMatrix.mkString.isEmpty || !rightMatrix.mkString.isEmpty)
      invalid("foot sides not supported")
    else if (totalCols < minWidthForBricks) invalidMin("width", minWidthForBricks)
    else if (totalRows < minHeightForBricks) invalidMin("height", minHeightForBricks)
    else if (isHBrick || isVBrick) diagram.tileLinks(
      scale * 52.5,
      scale * 52.5,
      scale * (52.5 + 15 * centerMatrixCols),
      scale * (52.5 + 15 * centerMatrixRows)
    ) // TODO find the first tile closest to NE but at least 2 rows/cols to the SW
    else if (shiftColsSE < 2 && shiftRowsSE < 2) invalid("type of tiling is not suported")
    else if (minWidth > totalCols) invalidMin("patch width", minWidth)
    else if (minHeight > totalRows) invalidMin("height", minHeight)
    else {
      val offsetCols = (1.5 + shiftColsSE) * 15
      val offsetRows = (1.5 + shiftRowsSE) * 15
      diagram.tileLinks(
        scale * offsetCols,
        scale * offsetRows,
        scale * (offsetCols + 15 * centerMatrixCols),
        scale * (offsetRows + 15 * centerMatrixRows)
      )
    }
  }
}
