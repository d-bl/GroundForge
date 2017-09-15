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
import scala.util.matching.Regex

class Stitches(src: String) {

  private val (assignments, defaults) = src
    .toLowerCase
    .split("[^a-z0-9=]+")
    .partition(_.contains("="))

  private val tuples = assignments.flatMap(splitAssignment)

  private val defaultStitch = defaults.mkString match {
    case "" => "ctc"
    case s => makeValid(s, "ctc")
  }

  private val stitches: Map[StitchId, String] = tuples.map {
    case (id, instructions, _) => (id, makeValid(instructions, defaultStitch))
  }.toMap

  private val colors: Map[StitchId, String] = tuples
    .filter(tuple => availableColors.contains(tuple._3))
    .map {
      case (id, _, color) => (id, color)
    }.toMap

  /**
   * @param id one or two letters followed by digits
   * @return the default stitch in case of an invalid id
   */
  def stitch(id: StitchId): String = {
    stitches.getOrElse(id.toLowerCase(), defaultStitch)
  }

  /**
   * @return the default stitch if a row/col is out of bounds
   */
  def stitch(row: Int, col: Int): String = {
    stitch(toID(row, col))
  }

  /** @return a matrix of at least 1x1 with strings containing a sequence of ctlrp */
  def instructions(nrOfRows: Int, nrOfCols: Int): Seq[Seq[String]] = {
    (0 until max(0, nrOfRows)).map { row =>
      (0 until max(0, nrOfCols)).map { col =>
        val id = toID(row, col)
        stitches.getOrElse(id, defaultStitch)
      }
    }
  }

  /** @return a matrix of at least 1x1 containing empty strings colors
   *          defined for end/start-markers in the SVG class
   */
  def colors(nrOfRows: Int, nrOfCols: Int): Seq[Seq[String]] = {
    (0 until max(0, nrOfRows)).map { row =>
      (0 until max(0, nrOfCols)).map { col =>
        val id = toID(row, col)
        colors.getOrElse(id, defaultColor(stitches.getOrElse(id, defaultStitch)))
      }
    }
  }
}

object Stitches {

  type StitchId = String

  private val letters = 'a' to 'z'
  private val availableColors = Set("red", "green", "purple", "blue", "brown")

  @tailrec
  private def toAlpha(col: Int, r: String = ""): StitchId = {
    val s = s"${ letters(col % 26) }$r"
    if (col < 26) s
    else toAlpha(col / 26 - 1, s)
  }

  private def toID(row: Int, col: Int) = {
    s"${ toAlpha(col) }${ row + 1 }"
  }

  /**
   * @param assignment example: A1=B3=ctct, the caller takes care of at least one '='
   * @return
   */
  private def splitAssignment(assignment: String): Array[(StitchId, String, String)] = {
    val (ids, values) = assignment.split("=").partition(_.matches("([a-z]+[0-9]+|cross|twist)"))
    val (colors, instructions) = values.partition(availableColors.contains)
    ids.map(id => (id, instructions.mkString, colors.mkString))
  }

  private def makeValid(String: String, default: String): String = {
    String.replaceAll("[^ctrlp]", "") match {
      // skip invalid characters
      case s if s.replaceAll("[^p]", "").length > 1 => default // at most one pin
      case s if s.replaceAll("[^c]", "").length < 1 => default // at least one cross
      case s => s
    }
  }

  /** Just single t's between c's **/
  private def singleTwists(stitch: String) = {
    stitch.split("c").distinct.mkString == "t"
  }

  /** At least one pair twisted twice */
  private def repeatedTwists(stitch: String) = {
    stitch.replace("", "lr") match {
      case s if s.count('r' == _) > 1 => true
      case s if s.count('l' == _) > 1 => true
      case _ => false
    }
  }

  val r: Regex = "([lrt]*)(.*c)(.*)".r()

  private def defaultColor(s: String) = {
    val crossed = s.count('c' == _)
    val pins = s.count('p' == _)
    if (crossed == 0 || pins != 0) ""
    else {
      val r(openTwists, stitch, closeTwists) = s
      val twisted =
        openTwists.count('t' == _) > 0 ||
          closeTwists.count('t' == _) > 0
      if (stitch == "c" && twisted) "green"
      else if (stitch == "ctc")
             if (twisted) "red"
             else "purple"
      else if (crossed > 2 && singleTwists(stitch)) "blue"
      else if (crossed == 2 && repeatedTwists(stitch)) "brown"
      else ""
    }
  }
}
