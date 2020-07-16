package dibl

import java.io.{BufferedReader, FileInputStream, InputStreamReader}

import dibl.sheet.SheetSVG

import scala.collection.JavaConverters._

object Patterns {

  private def getTesselaceNr(s: String) = {
    s.replaceAll(". pattern.*", "")
      .replaceAll(".*\"", "")
  }
  private def toSheetSvg(str: String) = {
    val Array(m, t) = str.split(";")
    val patterns = new SheetSVG
    patterns.add(m, t)
    patterns.toSvgDoc()
  }

  private lazy val tesselaceLines: Seq[String] = readLines("docs/help/TesseLace-Index.md", "pattern=")

  /** (nr, sheetArgs, svg, tileArgs)  */
  lazy val tesselaceSheets: Seq[(String, String, String, String)] = tesselaceLines.flatMap { line => line
    .replaceAll(""".*"patch=""", "")
    .replaceAll("""" *%}""", "")
    .split("&patch=")
    .zipWithIndex.toSeq
    .map { case (sheetArgs: String, i: Int) =>
      val svg = toSheetSvg(sheetArgs)
      val tileArgs = svg.split("\n")
        .filter(_.contains("tiles.html"))
        .map(_.replaceAll(".*[?]","").replaceAll("'.*",""))
        .head
      (s"${getTesselaceNr(line)}-$i", sheetArgs, svg, tileArgs)
    }
  }
  lazy val tesselace: Seq[(String, String)] = tesselaceLines.map { s =>
    (getTesselaceNr(s)
      , s.replaceAll(".*pattern=.", "tile=")
      .replaceAll("\".*", "")
    )
  }
  lazy val whiting: Seq[(String, String)] = readLines("docs/help/Whiting-Index.md", "tiles?whiting").map { s =>
    (s.replaceAll(".*whiting=", "")
      .replaceAll("&.*", "")
      , s.replaceAll(".*tiles[?]", "")
    )
  }
  lazy val all: Seq[(String, String)] = tesselace.toList ::: whiting.toList //::: tesselaceSheets.map(_._4)

  private def readLines(file: String, content: String): Seq[String] = {
    new BufferedReader(new InputStreamReader(new FileInputStream(file)))
      .lines().iterator().asScala
      .withFilter(_.contains(content))
      .toSeq.distinct
  }
}
