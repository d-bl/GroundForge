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

    for (specs <- Matrices.values) {
      val fringes = new Fringes(Matrices.toAbsolute(specs))

      // preparation of visual verification
      val spaceLess = specs.replace(" ", "_").replace(";", "_")
      File(s"target/test/fringes/$spaceLess.svg").writeAll(fringes.svgDoc)

      // log which ones might have interesting properties to examine
      val groupedByTarget = fringes.footSides.groupBy { case (source, target) => target }
      val duplicateTargets = groupedByTarget.count { case (target, links) => links.size > 1 }
      val nodesOnInnerCols = groupedByTarget.keys.count { case (_, targetCol) => targetCol == 3 || targetCol == 22 }
      val accumulatedLinks = fringes.footSides ++ fringes.newPairs ++ fringes.coreLinks
      val parallelLinks = accumulatedLinks.size - accumulatedLinks.toSet.size

      if (nodesOnInnerCols > 0 && duplicateTargets > 0)
        println(s"$duplicateTargets $parallelLinks    $spaceLess")
    }
  }
}
