package dibl

import java.net.URLDecoder

import dibl.Config._

import scala.collection.immutable

case class Config(urlQuery: String) {

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

  val stitches: String = keyValueStrings
    .filter(_.toLowerCase.matches(popupIdFilter))
    .mkString(" ")

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
      "----------"
      ).toUpperCase.toCharArray)

  def isOpaque(row: Int, col: Int): Boolean = {
    true//TODO
  }

  def popupId(row: Int, col: Int): String = {
    "notYetImplemented"//TODO
  }

  def vectorCode(row: Int, col: Int): Char = {
    totalMatrix(row)(col) //TODO prevent index out of bound
  }
}

object Config {
  private val types: String = Config.MatrixType.values.map(_.toString.toCharArray.head).mkString

  // must match keyValueStrings where the keys are returned popupId's and values are stitch definitions
  private val popupIdFilter = s"[$types]-r[0-9]+-c[0-9]+=[ctlrp]+"

  object MatrixType extends Enumeration {
    type MatrixPrefix = Value
    val HEAD_SIDE, TILE, FOOT_SIDE = Value
  }
}
