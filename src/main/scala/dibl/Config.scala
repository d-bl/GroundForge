package dibl

import scala.scalajs.js.annotation.JSExport

@JSExport
class Config(urlQuery: String) {
  println(urlQuery)

  private val keyValueStrings: Seq[String] = urlQuery
    .split("&")
    .filter(_.matches(".+=.*"))
  val fields: Map[String, String] = keyValueStrings
    .map { kv: String => (kv.replaceAll("=.*", ""), kv.replaceAll(".*=", "")) }
    .toMap

  private def getMatrix(key: String): Array[String] = {
    fields.getOrElse(key, "").toLowerCase.split("[^-a-z0-9]+")
  }

  val leftMatrix: Array[String] = getMatrix("footside")
  val centerMatrix: Array[String] = getMatrix("tile")
  val rightMatrix: Array[String] = getMatrix("headside")
  // TODO defaults based on the dimensions of the above matrices
  val maxRows: Int = fields.getOrElse("repeatHeight", "12").replaceAll("[^0-9-]", "").toInt
  val maxCols: Int = fields.getOrElse("repeatWidth", "12").replaceAll("[^0-9-]", "").toInt
  val shiftRowsSE: Int = fields.getOrElse("shiftRowsSE", "12").replaceAll("[^0-9-]", "").toInt
  val shiftRowsSW: Int = fields.getOrElse("shiftRowsSW", "12").replaceAll("[^0-9-]", "").toInt
  val shiftColsSE: Int = fields.getOrElse("shiftColsSE", "12").replaceAll("[^0-9-]", "").toInt
  val shiftColsSW: Int = fields.getOrElse("shiftColsSW", "12").replaceAll("[^0-9-]", "").toInt

  case class Item(vectorCode: Char = '-', stitch: String = "ctc", isOpaque: Boolean = false)

  // See https://github.com/d-bl/GroundForge/blob/7b1effb/docs/help/images/shift-directions.png
  // JS-filters on https://github.com/jo-pol/GroundForge/blob/d442e96/docs/js/tiles.js#L24-L33
  val itemMatrix: Array[Array[Item]] = Array.fill[Array[Item]](maxRows)(
    Array.fill[Item](maxCols + 4 + leftMatrix.head.length + rightMatrix.head.length)(Item())
  )

  private val leftMarginWidth = 2 + leftMatrix.head.length
  private val offsetRightMargin = leftMarginWidth + maxCols
  for {r <- 0 until maxRows} {
    for {c <- 0 until leftMatrix.head.length} {
      val rSource = r % leftMatrix.length
      val id = Stitches.toID(rSource, c + 2)
      val stitch = fields.getOrElse(id, "ctc")
      itemMatrix(r)(c + 2) = Item(leftMatrix(rSource)(c), stitch, r < leftMatrix.length)
    }
    for {c <- 0 until rightMatrix.head.length} {
      val rSource = r % rightMatrix.length
      val id = Stitches.toID(rSource, c + offsetRightMargin)
      val stitch = fields.getOrElse(id, "ctc")
      itemMatrix(r)(c + offsetRightMargin) = Item(rightMatrix(rSource)(c), stitch, r < rightMatrix.length)
    }

  }
  for {
    i <- 0 until maxRows
    j <- -maxCols until maxCols
    translateRow = (i * shiftRowsSE) + (j * shiftRowsSW)
    translateCol = (i * shiftColsSE) + (j * shiftColsSW)
    r <- 0 until centerMatrix.length
    c <- 0 until centerMatrix.head.length
  } {
    // t in rt/ct stands for target cell, r and c for row and col
    val rt = r + translateRow
    val ct = c + translateCol
    if (rt >= 0 && ct >= 0 && rt < maxRows && ct < maxCols) {
      val id = Stitches.toID(r, c+leftMarginWidth)
      val stitch = fields.getOrElse(id, "ctc")
      val vectorCode = centerMatrix(r)(c)
      itemMatrix(rt)(ct + leftMarginWidth) = Item(vectorCode, stitch, r == rt && c == ct)
    }
  }

  @JSExport
  val encodedMatrix: String = itemMatrix
    .map(_.map(_.vectorCode).mkString)
    .mkString(",")
    .toUpperCase

  @JSExport
  val totalRows: Int = itemMatrix.length

  @JSExport
  val totalCols: Int = itemMatrix.head.length
}

@JSExport
object Config {

  @JSExport
  def create(urlQuery: String): Config = new Config(urlQuery)
}
