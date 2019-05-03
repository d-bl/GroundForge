package dibl

import scala.reflect.io.File

object SheetDemos {
  def main(args: Array[String]): Unit = {
    new java.io.File("target/test/sheets").mkdirs()
    val str = Matrices.values.flatMap {str =>
      println(str)
      val Array(m,t) = str.split(";")
      val patterns = new SheetSVG
      patterns.add(m, t)
      patterns.toSvgDoc()
        .split("\n")
        .filter(_.contains("tiles.html"))
        .map(_.replace("xlink:",""))
    }
    File("target/test/sheetToTiles.html").writeAll(str.mkString("<br>\n"))
  }
}
