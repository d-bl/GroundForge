/*
 hexColor2016 Jo Pol
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

import java.io.{File, FileOutputStream}

import dibl.Matrix.{charToRelativeTuples, extend, toAbsolute, toValidMatrixLines}
import org.scalatest.{FlatSpec, Matchers}

class FringesDemos extends FlatSpec with Matchers {

  new File("target/test/fringes").mkdirs()

  "pattern sheet" should "succeed" in {

    for (specs <- Matrices.values.map(_.split(";"))) {
      val matrixSpec = specs(0)
      val tileSpec = if (specs.length > 1) specs(1) else "checker"
      val tileType = TileType(tileSpec)
      val matrixLines = toValidMatrixLines(matrixSpec).get
      val checker = tileType.toChecker(matrixLines)
      val extended = extend(checker, 22, 22)
      val relative = extended.map(_.map(charToRelativeTuples).toArray)
      val svgDoc = new Fringes(toAbsolute(relative)).svgDoc
      write(new File(s"target/test/fringes/${matrixSpec}_$tileSpec.svg"), svgDoc)
    }
  }

  def write(file: File, content: String) = {
    val fos = new FileOutputStream(file)
    try {
      fos.write(content.getBytes("UTF8"))
    } finally {
      fos.close()
    }
  }
}
