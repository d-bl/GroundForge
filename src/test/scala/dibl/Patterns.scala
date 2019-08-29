package dibl

import java.io.{BufferedReader, FileInputStream, InputStreamReader}

import scala.collection.JavaConverters._

object Patterns {

  lazy val tesselace: Seq[(String, String)] = readLines("docs/help/TesseLace-Index.md", "pattern=").map { s =>
    (s.replaceAll(". pattern.*", "")
      .replaceAll(".*\"", "")
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
  lazy val all: Seq[(String, String)] = tesselace.toList ::: whiting.toList

  private def readLines(file: String, content: String): Seq[String] = {
    new BufferedReader(new InputStreamReader(new FileInputStream(file)))
      .lines().iterator().asScala
      .withFilter(_.contains(content))
      .toSeq.distinct
  }
}
