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

import scala.reflect.io.File

object FringesDemos extends {

  File("target/test/fringes/").createDirectory()

  def main(args: Array[String]): Unit = {

    val special = new Fringes(Matrices.toAbsolute("5831 -4-7", 11, 12))
    File(s"target/test/fringesMixedUp.svg").writeAll(special.svgDoc)

    // note that the PairDiagram class implicitly applies as shiftLeft=2
    val issue69 = new Fringes(Matrices.toAbsolute("5-K-5-K- -L-O-L-O 5-L---H- -E-H-E-H;bricks", 22, 23, shiftLeft = 6))
    File(s"target/test/issue69.svg").writeAll(issue69.svgDoc)

    val longJumps1 = new Fringes(Matrices.toAbsolute("5-O-O-5- -E-E-5-5 5-5---5- -5-O-E-5;bricks", 25, 12))
    File(s"target/test/longJumps1.svg").writeAll(longJumps1.svgDoc)

    val longJumps2 = new Fringes(Matrices.toAbsolute("5-L-L-K- -L---5-O L-O---5- -E-H-E-E;bricks", 25, 12, shiftLeft = 1))
    File(s"target/test/longJumps2.svg").writeAll(longJumps2.svgDoc)

    val longJumps3 = new Fringes(Matrices.toAbsolute("5-L-L-K- -L---5-O 5-O---H- -E-H-E-H;bricks", 25, 12, shiftLeft = 1))
    File(s"target/test/longJumps3.svg").writeAll(longJumps3.svgDoc)

    for (specs <- Matrices.values) {
      for (i <- 0 to 8) {
        val absolute = Matrices.toAbsolute(specs, 25, 12, shiftLeft = i)
        val fringes = new Fringes(absolute)

        // preparation of visual verification
        val spaceLess = specs.replace(" ", "_").replace(";", "_")
        //File(s"target/test/fringes/$spaceLess-$i.svg").writeAll(fringes.svgDoc)

        val footsides = fringes.leftFootSides ++ fringes.rightFootSides
        // log which ones might have interesting properties to examine
        val groupedByTarget = footsides.groupBy { case (source, target) => target }
        val duplicateTargets = groupedByTarget.count { case (target, links) => links.size > 1 }
        val nodesOnInnerCols = groupedByTarget.keys.count { case (_, targetCol) => targetCol == 3 || targetCol == 22 }
        val accumulatedLinks = footsides ++ fringes.newPairs ++ fringes.coreLinks
        val parallelLinks = accumulatedLinks.size - accumulatedLinks.toSet.size

        println(s"$specs $i")
      }
    }
  }
}
