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

import java.io._

import dibl.Matrix.{ extend, toRelativeSources }
import dibl.sheet.TileType

import scala.collection.JavaConverters._

object Matrices {
  val values: Seq[String] = new BufferedReader(new InputStreamReader(
    new FileInputStream("docs/help/TesseLace-Index.md")
  )).lines()
    .iterator()
    .asScala
    .filter(_.contains("""patches="patch"""))
    .toSeq
    .flatMap(_
      .replaceAll(""".*"patch=""","")
      .replaceAll("""" *%}""","")
      .split("&patch=")
    ).distinct

  def toAbsolute(args: String, rows: Int =22, cols:Int = 22, shiftUp: Int =0, shiftLeft: Int = 0): M = {
    val specs = args.split(";")
    val matrixLines = Matrix.toValidMatrixLines(specs.head).get
    val tileSpec = if (specs.length > 1) specs(1) else "checker"
    val tileType = TileType(tileSpec)
    val checker = tileType.toChecker(matrixLines)
    val shifted = Matrix.shift(checker, shiftUp).map(Matrix.shiftChars(_, shiftLeft))
    val extended = extend(shifted, rows, cols)
    val relative = extended.map(_.map(toRelativeSources).toArray)
    Matrix.toAbsolute(relative)
  }
}
