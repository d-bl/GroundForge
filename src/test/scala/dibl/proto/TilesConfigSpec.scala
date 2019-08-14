package dibl.proto

import org.scalatest.{ FlatSpec, Matchers }

class TilesConfigSpec extends FlatSpec with Matchers {

  private val patterns: Seq[String] = (for {
    cols <- 1 until 3
    rows <- 1 until 3
    height <- 3 until 7
    width <- 3 until 7
  } yield Seq(
    (s"patchWidth=$width&patchHeight=$height&${ checker(rows, cols) }&checker"),
    (s"patchWidth=$width&patchHeight=$height&${ horBricks(rows, cols) }&horBricks"),
    (s"patchWidth=$width&patchHeight=$height&${ verBricks(rows, cols) }&verBricks"),
  )).flatten

  "getItemMatrix" should "fill the prototype completely" in {
    println(patterns.length)
    val errors = patterns.map(q =>
      (TilesConfig(q).getItemMatrix.flatten.map(_.vectorCode).mkString(""), q)
    ).filter(_._1.contains("-"))
    errors should have length (0)
  }

  private def checker(rows: Int, cols: Int): String = {
    s"tile=${ matrix(rows, cols) }&shiftColsSW=0&shiftRowsSW=$rows&shiftColsSE=$cols&shiftRowsSE=$rows"
  }

  private def horBricks(rows: Int, cols: Int): String = {
    s"tile=${ matrix(rows, cols) }&shiftColsSW=${ - Math.round(cols / 2) }&shiftRowsSW=$rows&shiftColsSE=${ cols - Math.round(cols / 2) }&shiftRowsSE=$rows"
  }

  private def verBricks(rows: Int, cols: Int): String = {
    s"tile=${ matrix(rows, cols) }&shiftColsSW=0&shiftRowsSW=$rows&shiftColsSE=$cols&shiftRowsSE=${ Math.round(rows / 2) }"
  }

  private def matrix(rows: Int, cols: Int): String = {
    Array.fill(rows)("1" * cols).mkString(",")
  }
}
