package dibl.proto

import org.scalatest.{ FlatSpec, Matchers }

class TilesConfigSpec extends FlatSpec with Matchers {

  private val patterns: Seq[String] = (for {
    cols <- 1 until 3
    rows <- 1 until 3
    height <- 3 until 7
    width <- 3 until 7
    headHeight <- 0 until 3
    headWidth <- 0 until 3
    footHeight <- 0 until 3
    footWidth <- 0 until 3
  } yield Seq(
    (s"patchWidth=$width&patchHeight=$height&${ checker(rows, cols) }&headside=${ matrix(headHeight, headWidth) }&footside=${ matrix(footHeight, footWidth) }&checker"),
    (s"patchWidth=$width&patchHeight=$height&${ horBricks(rows, cols) }&headside=${ matrix(headHeight, headWidth) }&footside=${ matrix(footHeight, footWidth) }&horBricks"),
    (s"patchWidth=$width&patchHeight=$height&${ verBricks(rows, cols) }&headside=${ matrix(headHeight, headWidth) }&footside=${ matrix(footHeight, footWidth) }&verBricks"),
  )).flatten

  "getItemMatrix" should "fill the prototype completely" in {
    val errors = patterns.map(q =>
      (TilesConfig(q).getItemMatrix.flatten.map(_.vectorCode).mkString(""), q)
    ).filter(_._1.contains("-"))
    println(patterns.length)
    errors should have length (0)
  }

  "getItemMatrix" should "stretch the patch to contain a full tile" in {
    val tile = "5-----5-----,-cvv-b-c-zzb,-x--5-5-5--x,-x-y-5-5-w-x,--5-y-5-w-5-,-e-5-y-zz5-w,01e-y-b-zz21,-x-h-y-y-e-4,--5-w-b-y-5-,-y-w-5-5-y-w,----5-5-5---,-g-y-5-5-w-n,5-----5-----,-cvv-b-c-zzb,-x--5-5-5--x,-x-y-5-5-w-x,--5-y-5-w-5-,-e-5-y-zz5-w,01e-y-b-zz21,-x-h-y-y-e-4,--5-w-b-y-5-,-y-w-5-5-y-w,----5-5-5---,-g-y-5-5-w-n"
    val q = s"whiting=A14_P87&patchWidth=5&patchHeight=5&tile=$tile&shiftColsSW=0&shiftRowsSW=24&shiftColsSE=12&shiftRowsSE=12"
    val config = TilesConfig(q)
    config.getItemMatrix should have size 24
    config.getItemMatrix.head should have size 12
    config.getItemMatrix.flatten.map(_.vectorCode).mkString("") shouldBe tile.replaceAll(",","")
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
    if (rows==0 || cols == 0) return ""
    Array.fill(rows)("1" * cols).mkString(",")
  }
}
