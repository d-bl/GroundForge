package dibl

import dibl.sheet.SheetSVG

import scala.reflect.io.File

object SheetDemos extends DemoFixture {
  def main(args: Array[String]): Unit = {
    File(s"$testDir/sheetToTiles.html")
      .writeAll(Patterns.tesselaceSheets.flatMap(_._3
        .split("\n")
        .filter(_.contains("tiles.html"))
        .map(_.replace("xlink:", ""))
      ).mkString("<br>\n"))
    Patterns.tesselaceSheets.foreach(x => println(x._4))
    Patterns.tesselaceSheets.foreach { case (nr: String, _, svg: String, _) =>
      File(s"$testDir/$nr.svg").writeAll(svg)
    }
  }
}
