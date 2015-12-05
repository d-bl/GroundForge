package dibl

import dibl.Matrix._

import scala.util.{Failure, Success, Try}

case class Settings private(absM: M,
                            stitches: Array[Array[String]]
                           ) {
  val nrOfPairLinks = countLinks(absM)
  private val relRows = stitches.length
  private val relCols = stitches(0).length
  private val margin = 2

  def getStitch(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toCell(row,col)
    stitches(cellRow)(cellCol)
  }

  def getTitle(row: Int, col: Int): String = {
    val (cellRow, cellCol) = toCell(row,col)
    s"${stitches(cellRow)(cellCol)} - ${"ABCDEFGHIJKLMNOPQRSTUVWXYZ"(cellCol)}${cellRow+1}"
  }

  private def toCell(row: Int, col: Int): (Int, Int) = {
    val brickOffset = ((row + margin) / relRows % 2) * (relCols / 2) + margin
    (row % relRows, (brickOffset + col) % relCols)
  }
}

object Settings {

  def apply(key: String,
            nr: Int,
            absRows: Int,
            absCols: Int,
            shiftLeft: Int = 0,
            shiftUp: Int = 0,
            stitches: String = ""
           ): Try[Settings] =
    for {
      str <- getMatrix(key, nr)
      relM <- toRelSrcNodes(matrix = str, dimensions = key)
      absM <- toAbs(relM, absRows, absCols, shiftLeft, shiftUp)
    } yield create(relM, absM, stitches)

  def apply(): Try[Settings] =
    for {
      relM <- toRelSrcNodes(matrix = "5831-4-7", dimensions = "2x4")
      absM <- toAbs(relM, absRows = 8, absCols = 5, shitfLeft = 0, shiftUp = 0)
    } yield create(relM, absM, stitches = "A1=tc, B1=tctc, C1=tc, D1=tctc, A2=tc, C2=tc")

  private def create(relM: M,
                     absM: M,
                     stitches: String
                    ): Settings =
    new Settings(
      absM,
      stitches = convert(stitches, relM.length, relM(0).length)
    )

  private def convert(str: String,
                      rows: Int,
                      cols: Int
                     ): Array[Array[String]] = {
    val result = Array.fill(rows, cols)("ctc")
    str.toLowerCase()
      .split("[^a-z0-9=]+")
      .map(_.split("="))
      .filter(_.length == 2)
      .filter(_ (0).matches("[a-z][0-9]"))
      .filter(_ (1).matches("[lrctp]+"))
      .filter(_ (1).matches(".*c.*"))
      .filter(_ (1).replaceAll("[^p]","").length < 2)
      .foreach { kv =>
        val col = kv(0)(0).toInt - 'a'.toInt
        val row = kv(0)(1).toInt - '1'.toInt
        if (row < rows && col < cols)
          result(row)(col) = kv(1)
      }
    result
  }

  private def toAbs(m: M,
                    absRows: Int,
                    absCols: Int,
                    shitfLeft: Int,
                    shiftUp: Int
                   ): Try[M] =
    toAbsWithMargins(shift(toCheckerboard(m), shitfLeft, shiftUp), absRows, absCols)

  private def getMatrix(key: String,
                        nr: Int
                       ): Try[String] = {
    val matrices = matrixMap.get(key)
    if (matrices.isEmpty) Failure(new Exception(s"$key not found"))
    else if (nr < 0 || nr >= matrices.get.length) Failure(new Exception(s"$nr is invalid for $key"))
    else Success(matrices.get(nr))
  }
}
