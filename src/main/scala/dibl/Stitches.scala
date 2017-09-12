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

import java.lang.Math.max

import dibl.Stitches._

import scala.annotation.tailrec

class Stitches(src: String) {
  private val (assignments, defaults) = src
    .toLowerCase
    .split("[^a-z0-9=]+")
    .partition(_.contains("="))

  private val default =
    if (defaults.isEmpty) "ctc"
    else makeValid(defaults.head, "ctc")

  private val map = assignments.flatMap {
    splitAssignment(_, default)
  }.toMap

  /**
    * @param id one or two letters followed by digits
    * @return the default stitch in case of an invalid id
    */
  def apply(id: String): String = {
    map.getOrElse(id.toLowerCase(), default)
  }

  def apply(row: Int, col: Int): String = {
    map.getOrElse(s"${toAlpha(col)}${row + 1}", default)
  }

  /**
    * @param nrOfRows negative is interpreted as zero
    * @param nrOfCols negative is interpreted as zero
    * @return a matrix with stitch instruction of at least 1x1
    */
  def toMatrix(nrOfRows: Int, nrOfCols: Int): Seq[Seq[String]] = {
    (0 until max(0, nrOfRows)).map { row =>
      (0 until max(0, nrOfCols)).map { col =>
        apply(row, col)
      }
    }
  }
}

object Stitches {

  private val letters = 'a' to 'z'

  @tailrec
  private def toAlpha(col: Int, r: String = ""): String = {
    val s = s"${letters(col % 26)}$r"
    if (col < 26) s else toAlpha(col / 26 - 1, s)
  }

  /**
    * @param assignment example: A1=B3=ctct, partition in the class takes care of at least one '='
    * @param default    example: ctc
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
    instructions.replaceAll("[^ctrlp]", "") match {
      // skip invalid characters
      case s if s.replaceAll("[^p]", "").length > 1 => default // at most one pin
      case s if s.replaceAll("[^c]", "").length < 1 => default // at least one cross
      case s => s
    }
  }
}
