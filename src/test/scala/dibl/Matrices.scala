/*
 Copyright 2015 Jo Pol
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

import java.io.{BufferedReader, FileReader}

import dibl.Matrix.{charToRelativeTuples, extend}
import scala.collection.JavaConverters._

object Matrices {
  val values: Seq[String] = new BufferedReader(new FileReader("docs/gallery.html"))
    .lines()
    .iterator()
    .asScala
    .filter(_.contains("?m="))
    .toSeq
    .flatMap(_
      .replaceAll(".*m=", "")
      .replace("&tiles=", ";")
      .replace("&#\">stitches</a>,<br>* <a href=\"sheet.html?", "&")
      .replaceAll("\".*", "")
      .split("&patch=")
      .toSet
      .toStream
      .distinct
    )

  def toAbsolute(args: String, rows: Int =22, cols:Int = 22, shiftUp: Int =0, shiftLeft: Int = 0): M = {
    val specs = args.split(";")
    val matrixLines = Matrix.toValidMatrixLines(specs.head).get
    val tileSpec = if (specs.length > 1) specs(1) else "checker"
    val tileType = TileType(tileSpec)
    val checker = tileType.toChecker(matrixLines)
    val shifted = Matrix.shift(checker, shiftUp).map(Matrix.shiftChars(_, shiftLeft))
    val extended = extend(shifted, rows, cols)
    val relative = extended.map(_.map(charToRelativeTuples).toArray)
    Matrix.toAbsolute(relative)
  }
}
