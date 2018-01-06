package dibl

import java.net.URLDecoder

import scala.scalajs.js.annotation.JSExport

@JSExport
class Config(urlQuery: String) {

  private val keyValueStrings: Seq[String] = urlQuery
    .split("&")
    .filter(_.matches(".+=.*"))
  val fields: Map[String, String] = keyValueStrings
    .map { kv: String => (kv.replaceAll("=.*", ""), kv.replaceAll(".*=", "")) }
    .toMap

  // TODO how to save a new version of the page?
  val wayBack: Option[String] = keyValueStrings.map {
    case s if URLDecoder.decode(s, "UTF-8").matches("m=.*;(bricks|checker)[;0-9]*")
    => Some(s"https://web.archive.org/web/20170908061030/https://d-bl.github.io/GroundForge/?$urlQuery")
    case _ => None
  }.find(_.isDefined).flatten

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
  private val repCenter = Array.fill[Array[Item]](maxRows)(Array.fill[Item](maxCols)(Item()))
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
      val id = Stitches.toID(r + 2 + leftMatrix.head.length, c).toUpperCase
      val stitch = fields.getOrElse(id, "ctc")
      val vectorCode = centerMatrix(r)(c)
      repCenter(rt)(ct) = Item(vectorCode, stitch, r == rt && c == ct)
    }
  }
  println(urlQuery)
  println(s"repeatRows=$maxRows repeatCols=$maxCols shiftColsSE=$shiftColsSE shiftColsSW=$shiftColsSW shiftRowsSE=$shiftRowsSE shiftRowsSW=$shiftRowsSW")

  // TODO change into Seq[Seq[(VectorCode:Char,StitchId:String:[ctlrp]*,IsOpaque:Boolean)]]
  val totalMatrix: Seq[String] = (0 until maxRows)
    .map(row => ("--" +
      leftMatrix(row % leftMatrix.length) +
      repCenter(row).map(_.vectorCode).mkString +
      rightMatrix(row % rightMatrix.length) +
      "--"
      ).toUpperCase)

  @JSExport
  val encodedMatrix: String = totalMatrix.map(_.mkString).mkString(",")

  def vectorCode(row: Int, col: Int): Char = {
    totalMatrix(row)(col) //TODO prevent index out of bound
  }

  def stitch(row: Int, col: Int): String = {
    fields.getOrElse(Stitches.toID(row, col).toUpperCase, "ctc")
  }

  def isOpaque(row: Int, col: Int): Boolean = {
    val margin = 2
    val leftLen = margin + leftMatrix(0).length
    val centerLen = leftLen + centerMatrix(0).length
    val rightStart = leftLen + centerMatrix(0).length * this.maxCols
    val rightLen = rightMatrix(0).length
    val inLeft = margin <= col && col < leftLen && row < leftMatrix.length
    val inCenter = leftLen <= col && col < centerLen && row < centerMatrix.length
    val inRight = rightStart <= col && col < rightStart + rightLen && row < rightMatrix.length
    inLeft || inCenter || inRight
  }
}

@JSExport
object Config {

  @JSExport
  def create(urlQuery: String): Config = new Config(urlQuery)
}
