package dibl.proto

import java.lang.Math.{ abs, floorMod }

case class PairParams(centerMatrix: Seq[String] = Seq("5-", "-5"),
                      leftMatrix: Seq[String] = Seq.empty,
                      rightMatrix: Seq[String] = Seq.empty,
                      shiftRowsSE: Int = 2,
                      shiftRowsSW: Int = 2,
                      shiftColsSE: Int = 2,
                      shiftColsSW: Int = 0,
                      swatchWidth: Int = 12,
                      swatchHeight: Int = 12,
                     ) {
  val leftMatrixCols: Int = leftMatrix.headOption.map(_.length).getOrElse(0)
  val centerMatrixCols: Int = centerMatrix.headOption.map(_.length).getOrElse(0)
  val rightMatrixCols: Int = rightMatrix.headOption.map(_.length).getOrElse(0)
  val centerMatrixRows: Int = centerMatrix.length
  val rightMatrixRows: Int = rightMatrix.length
  val leftMatrixRows: Int = leftMatrix.length

  def toSimple3x2: PairParams = {
    val isHorBrick = centerMatrixRows > 0 &&
      floorMod(abs(shiftRowsSE), centerMatrixRows) == 0 &&
      floorMod(abs(shiftRowsSW), centerMatrixRows) == 0
    val isVerBrick = centerMatrixCols > 0 &&
      floorMod(abs(shiftColsSE), centerMatrixCols) == 0 &&
      floorMod(abs(shiftColsSW), centerMatrixCols) == 0
    (isHorBrick, isVerBrick) match {
      case (true, true) => this
      case (true, false) =>
        val m = centerMatrix ++ centerMatrix.map(flip)
        copy(
          centerMatrix = m,
          swatchWidth = Math.max(swatchWidth, centerMatrixCols * 6),
          swatchHeight = Math.max(swatchHeight, centerMatrixRows * 2),
        )
      case (false, true) =>
        val n = centerMatrixRows / 2
        val mTop = centerMatrix.slice(0, n)
        val mBottom = centerMatrix.slice(n, centerMatrix.size)
        copy(
          centerMatrix = centerMatrix.zip(mBottom ++ mTop).map { case (a, b) => a + b },
          swatchWidth = Math.max(swatchWidth, centerMatrixCols * 3),
          swatchHeight = Math.max(swatchHeight, centerMatrixRows * 4),
        )
      case _ => ???
    }
  }

  private def flip(s: String) = {
    val n = centerMatrixCols / 2
    s.substring(n) + s.substring(0, n)
  }

  override def toString: String = s"tile=${ centerMatrix.mkString(",") }&footside=${ leftMatrix.mkString(",") }&headside=${ rightMatrix.mkString(",") }&swatchWidth=$swatchWidth&&swatchHeight=$swatchHeight&"
}

object PairParams {

  def apply(urlQuery: String): PairParams = {

    val queryFields: Map[String, String] = urlQuery
      .split("&")
      .filter(_.matches(".+=.+"))
      .map { kv: String => (kv.replaceAll("=.*", ""), kv.replaceAll(".*=", "")) }
      .toMap

    def swatchSize(dim: String) = {
      val str = queryFields.getOrElse(
        s"patch$dim",
        queryFields.getOrElse(s"swatch$dim", "12")
      )
      if (str.matches("[0-9]+")) str.toInt
      else 12
    }

    def shift(direction: String, default: Int) = {
      val str = queryFields.getOrElse(s"shift$direction", default.toString)
      if (str.matches("-?[0-9]+")) str.toInt
      else default
    }

    val regExp = "[^0-9A-Za-z-]+"
    val centerMatrix = queryFields.matrixLines("tile")
    new PairParams(
      centerMatrix = centerMatrix,
      leftMatrix = queryFields.matrixLines("footside"),
      rightMatrix = queryFields.matrixLines("headside"),
      shiftColsSE = shift("ColsSE", centerMatrix.head.length),
      shiftColsSW = shift("ColsSW", 0),
      shiftRowsSE = shift("RowsSE", centerMatrix.size),
      shiftRowsSW = shift("RowsSW", centerMatrix.size),
      swatchHeight = swatchSize("Height"),
      swatchWidth = swatchSize("Width"),
    )
  }
}
