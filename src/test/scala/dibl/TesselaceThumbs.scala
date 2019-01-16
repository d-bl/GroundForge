/*
 Copyright 2016 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import java.io.{BufferedReader, FileInputStream, InputStreamReader}

import dibl.Force.Point

import scala.collection.JavaConverters._
import scala.reflect.io.File

object TesselaceThumbs {

  def main(args: Array[String]): Unit = {
    val w2 = Seq(13,14,15,16,17,18) ++ (501 to 684) ++ (687 to 722)
    val h2 = Seq(376,449,453,455)
    new BufferedReader(new InputStreamReader(new FileInputStream(
      "docs/help/TesseLace-Index.md"
    )))
      .lines()
      .iterator()
      .asScala
      .withFilter(_.contains("pattern="))
      .toSeq
      .distinct
      .foreach { s =>
        val nr = s
          .replaceAll(". pattern.*", "")
          .replaceAll(".*\"", "")
        // some patterns scale badly with too large dimension, or not understood circumstances
        // use the if to retry these exceptions
        if (true) {
          val q = s
            .replaceAll(".*pattern=.", "tile=")
            .replaceAll("\".*", "")
          val w = if (w2.contains(nr.toInt)) 40
                  else if (nr == "128") 16
                  else if (nr == "129") 15
                  else if (nr == "451") 15
                  else 20
          val h = if (h2.contains(nr.toInt)) 40
                  else if (nr == "128") 14
                  else if (nr == "129") 15
                  else if (nr == "451") 23
                  else 20
          val config = TilesConfig(q + s"&patchWidth=$w&patchHeight=$h&tileStitch=c")
          val nudgedDiagram = Force
            .nudgeNodes(NewPairDiagram.create(config), Point(100, 100))
            .getOrElse(throw new Exception("whoops"))
          val svg = D3jsSVG.render(nudgedDiagram, width = 200, height = 200)
          File(s"target/test/pattern/$nr.svg").createFile().writeAll(svg)
        }
        // '/C/Program Files/Inkscape/inkscape.exe' 001.svg --export-png=001.png -w78 -h78
        // the next attempt failed
        // for i in *.svg; do '/C/Program Files/Inkscape/inkscape.exe' $i --export-png=`echo $i | sed -e 's/svg$/png/'` -w78 -h78; done
      }
  }
}
