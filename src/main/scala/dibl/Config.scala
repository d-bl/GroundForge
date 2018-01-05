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

  val repeatRows: Int = fields.getOrElse("repeatHeight", "12").replaceAll("[^0-9]", "*").toInt
  val repeatCols: Int = fields.getOrElse("repeatWidth", "12").replaceAll("[^0-9]", "*").toInt
  val leftMatrix: Array[String] = getMatrix("footside")
  val centerMatrix: Array[String] = getMatrix("tile")
  val rightMatrix: Array[String] = getMatrix("headside")

  private def getMatrix(key: String): Array[String] = {
    fields.getOrElse(key, "").toLowerCase.split("[^-a-z0-9]+")
  }

  // TODO javascript to scala (current implementation only for checker board repeating)
  //  for (var i=0 ; i<maxRows ; i++) // tiles in a diagonal
  //    for (var j=-maxCols ; j<maxCols ; j++) // parallel diagonals
  //      for (var r=0; r+(i*shiftRowsSE)+(j*shiftRowsSW) < maxRows && r<rows; r++)
  //        for (var c=0 ; c+(i*shiftColsSE)+(j*shiftColsSW) < maxCols && c<cols; c++) {
  //          // t in rt/ct stands for target cell
  //          var rt = r+(i*shiftRowsSE)+(j*shiftRowsSW)
  //          var ct = c+(i*shiftColsSE)+(j*shiftColsSW)
  //          if (rt >= 0 && ct >=0)
  //            repeatedCenter[rt][ct] = centerMatrix[r][c]
  //        }
  val repeatedCenter: Array[String] = centerMatrix.map(_ * repeatCols).map(_.take(repeatCols))

  // TODO change into Seq[Seq[(VectorCode:Char,StitchId:String:[ctlrp]*,IsOpaque:Boolean)]]
  val totalMatrix: Seq[Array[Char]] = (0 until repeatRows)
    .map(row => ("--" +
      leftMatrix(row % leftMatrix.length) +
      repeatedCenter(row % centerMatrix.length) +
      rightMatrix(row % rightMatrix.length) +
      "--"
      ).toUpperCase.toCharArray)

  @JSExport
  val encodedMatrix: String = totalMatrix.map(_.mkString).mkString(",")

  def isOpaque(row: Int, col: Int): Boolean = {
    val margin = 2
    val leftLen = margin + leftMatrix(0).length
    val centerLen = leftLen + centerMatrix(0).length
    val rightStart = leftLen + centerMatrix(0).length * this.repeatCols
    val rightLen = rightMatrix(0).length
    val inLeft = margin <= col && col < leftLen && row < leftMatrix.length
    val inCenter = leftLen <= col && col < centerLen && row < centerMatrix.length
    val inRight = rightStart <= col && col < rightStart + rightLen && row < rightMatrix.length
    inLeft || inCenter || inRight
  }

  def vectorCode(row: Int, col: Int): Char = {
    totalMatrix(row)(col) //TODO prevent index out of bound
  }
}

@JSExport
object Config {

  @JSExport
  def create(urlQuery: String): Config = new Config(urlQuery)
}
