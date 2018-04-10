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
    fields.getOrElse(key, "").toLowerCase.split("[^-a-z0-9]+")
  }

  // TODO defend against unequal rows lengths
  val leftMatrix: Array[String] = getMatrix("footside")
  val rightMatrix: Array[String] = getMatrix("headside")
  private val centerMatrix: Array[String] = getMatrix("tile")

  @JSExport
  val leftMatrixCols: Int = Option(leftMatrix.head).map(_.length).getOrElse(2)
  @JSExport
  val centerMatrixCols: Int = Option(centerMatrix.head).map(_.length).getOrElse(5)
  @JSExport
  val rightMatrixCols: Int = Option(rightMatrix.head).map(_.length).getOrElse(2)

  @JSExport
  val maxTileRows: Int = Math.max(centerMatrix.length, Math.max(leftMatrix.length, rightMatrix.length))

  // TODO defaults based on the dimensions of the above matrices
  @JSExport
  val totalRows: Int = fields.getOrElse("repeatHeight", "12").replaceAll("[^0-9-]", "").toInt
  private val centerCols: Int = fields.getOrElse("repeatWidth", "12").replaceAll("[^0-9-]", "").toInt
  private val shiftRowsSE: Int = fields.getOrElse("shiftRowsSE", "12").replaceAll("[^0-9-]", "").toInt
  private val shiftRowsSW: Int = fields.getOrElse("shiftRowsSW", "12").replaceAll("[^0-9-]", "").toInt
  private val shiftColsSE: Int = fields.getOrElse("shiftColsSE", "12").replaceAll("[^0-9-]", "").toInt
  private val shiftColsSW: Int = fields.getOrElse("shiftColsSW", "12").replaceAll("[^0-9-]", "").toInt

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

  // repeat foot-side / head-side

  if (leftMarginWidth > 0) replaceItems(leftMatrix, 0)
  if (offsetRightMargin > 0) replaceItems(rightMatrix, offsetRightMargin)

  private def replaceItems(inputMatrix: Array[String], offset: Int): Unit = {
    for {r <- 0 until totalRows} {
      for {c <- 0 until inputMatrix.head.length} {
        val rSource = r % inputMatrix.length
        val id = Stitches.toID(rSource, c + offset)
        val vectorCode = inputMatrix(rSource)(c)
        val stitch = if (vectorCode == '-') ""
                     else fields.getOrElse(id, "")
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
    r <- 0 until centerMatrix.length
    c <- 0 until centerMatrix.head.length
  } {
    // t in rt/ct stands for target cell, r and c for row and col
    val rt = r + translateRow
    val ct = c + translateCol
    if (rt >= 0 && ct >= 0 && rt < totalRows && ct < centerCols) {
      val id = Stitches.toID(r, c + leftMarginWidth)
      val vectorCode = centerMatrix(r)(c)
      val stitch = if (vectorCode == '-') ""
                   else fields.getOrElse(id, "ctc")
      itemMatrix(rt)(ct + leftMarginWidth) = Item(id, vectorCode, stitch, r == rt && c == ct)
    }
  }
}
