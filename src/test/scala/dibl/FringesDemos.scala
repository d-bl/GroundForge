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

import java.io.FileOutputStream

import scala.reflect.io.File

object FringesDemos extends  {

  File("target/test/fringes/").createDirectory()

  def main(args: Array[String]): Unit = {

    for (specs <- Matrices.values) {
      val fringes = new Fringes(Matrices.toAbsolute(specs))

      // preparation of visual verification
      val spaceLess = specs.replace(" ","_").replace(";","_")
      File(s"target/test/fringes/$spaceLess.svg").writeAll(fringes.svgDoc)

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
}
