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

import dibl.Stitches._

class Stitches(src: String) {
  private val (assignments, defaults) = src
    .toLowerCase
    .split("[^a-z0-9=]+")
    .partition(_.contains("="))
  private val default =
    if (defaults.isEmpty) "ctc"
    else makeValid(defaults.head, "ctc")
  private val map = assignments.flatMap { splitAssignment(_, default) }.toMap

  /**
   * @param id one or two letters followed by digits
   * @return the default stitch in case of an invalid id
   */
  def apply(id: String): String = {
    map.getOrElse(id.toLowerCase(), default)
  }

  /**
   * @param nrOfRows maximum Int.MaxValue
   * @param nrOfCols maximised to 702 (one or two letters: 27*26)
   *                 even just 26 could overwhelm browsers and users
   * @return
   */
  def toMatrix(nrOfRows: Int, nrOfCols: Int): Seq[Seq[String]] = {
    (1 to nrOfRows).map { row =>
      columnIndices
        .take(nrOfCols)
        .map { col =>
          apply(s"$col$row")
        }
    }
  }
}

object Stitches {

  private val letters = 'a' to 'z'
  private val columnIndices = letters ++
    letters.flatMap(i =>
      letters.map(j => s"$i$j")
    )

  /**
   * @param assignment example: A1=B3=ctct, partition in the class takes care of at least one '='
   * @param default example: ctc
   * @return
   */
  private def splitAssignment(assignment: String, default: String) = {
    val items = assignment.split("=")
    val instructions = makeValid(items.last, default)
    items.init // now we've got the IDs
      // should be "cross", "twist" or a valid grid id (letters followed by digits)
      .filter(_.matches("([a-z]+[0-9]+|cross|twist)"))
      .map(_ -> instructions)
  }

  private def makeValid(instructions: String, default: String) = {
    instructions.replaceAll("[^ctrlp]", "") match { // skip invalid characters
      case s if s.replaceAll("[^p]", "").length > 1 => default // at most one pin
      case s if s.replaceAll("[^c]", "").length < 1 => default // at least one cross
      case s => s
    }
  }
}
