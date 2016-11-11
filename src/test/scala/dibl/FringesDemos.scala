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

  "links" should "add up" in {

    for (specs <- Matrices.values.map(_.split(";"))) {
      val matrixLines = toValidMatrixLines(specs.head).get
      val tileSpec = if (specs.length > 1) specs(1) else "checker"
      val tileType = TileType(tileSpec)
      val checker = tileType.toChecker(matrixLines)
      val extended = extend(checker, 22, 22)
      val relative = extended.map(_.map(charToRelativeTuples).toArray)
      val absolute = toAbsolute(relative)
      val fringes = new Fringes(absolute)

      // preparation of visual verification
      val spaceLess = matrixLines.mkString("_")
      write(new File(s"target/test/fringes/${spaceLess}_$tileSpec.svg"), fringes.svgDoc)

      // automated verification
      val accumulatedLinks = fringes.reusedLeft ++ fringes.reusedRight ++ fringes.newPairs ++ fringes.coreLinks
      accumulatedLinks.size shouldBe fringes.allLinks.size
      accumulatedLinks.toSet shouldBe fringes.allLinks.toSet

      // log which ones might have interesting properties to examine
      val absDiff = Math.abs(fringes.reusedLeft.size - fringes.needsOutLeft.size)
      val duplicateOuts = fringes.needsOutLeft.size - fringes.needsOutLeft.toSet.size +
        fringes.needsOutRight.size - fringes.needsOutRight.toSet.size
      val nodesOnInnerCols = fringes.needsOutLeft.count { case (_, sourceCol) => sourceCol == 3 } +
        fringes.needsOutLeft.count { case (_, sourceCol) => sourceCol == 23 }
      if (nodesOnInnerCols > 0 && duplicateOuts > 1)
        println(s"$absDiff $duplicateOuts    $spaceLess")
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
