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

import java.io.{ BufferedReader, FileInputStream, InputStreamReader }

import scala.collection.JavaConverters._
import scala.reflect.io.File

object NewPairDemos {

  def main(args: Array[String]): Unit = {

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
        val q = s
          .replaceAll(".*pattern=.", "tile=")
          .replaceAll("\".*", "")
        val nr = s
          .replaceAll(". pattern.*", "")
          .replaceAll(".*\"", "")
        val config = Config.create(q)
        val leftCol = config.itemMatrix.map(_.head)
//        (1 until leftCol.length)
//          .withFilter(config.pairsOut(_)(0) < 2)
//          .withFilter(i => Matrix.relativeSourceMap(leftCol(i).vectorCode).head._1 < 0)
//          .foreach { i =>
//            // TODO replace item, though an undesired side effect
//          }
        val svg = D3jsSVG.render(NewPairDiagram.create(config))
        File(s"target/test/pattern/$nr.svg").createFile().writeAll(svg)
      }
  }
}
