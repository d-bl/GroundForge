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
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.matching.Regex

/**
  * @param src Example: "tc A1=B3=ctc=purple".
  *
  *            Anything but latin-letters, digits or equal signs
  *            is considered punctuation separating assignments.
  *            The colors in the examples are defaults and
  *            can be omitted or overridden with another value.
  *            An alphanumeric sequence of characters identifies a matrix cell.
  *            "cross"and "twist" are id's to create a pair diagram from a thread diagram.
  *            Anything but an id, supported(!) color name or punctuation is
  *            considered a stitch.  Illegal characters in stitches
  *            (might originate from not supported colors) are ignored.
  *
  *            Garbage in means defaults out.
  */
class Stitches(src: String) {

  private val (assignments, defaults) = src
    .toLowerCase
    .split("[^a-z0-9=]+")
    .partition(_.contains("="))

  private val tuples = assignments.flatMap(splitAssignment)

  private val defaultStitch = defaults.headOption.getOrElse("") match {
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
  def stitch(id: StitchId, default: StitchId = defaultStitch): String = {
    val d = default.toLowerCase match {
      case "cross" => stitches.getOrElse("cross", defaultStitch)
      case "twist" => stitches.getOrElse("twist", defaultStitch)
      case _ => defaultStitch
    }
    stitches.getOrElse(id.toLowerCase(), d)
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
        colors.getOrElse(id, defaultColorName(stitches.getOrElse(id, defaultStitch)))
      }
    }
  }
}

@JSExportTopLevel("Stitches") object Stitches {

  type StitchId = String

  private val letters = 'a' to 'z'
  private val availableColors = Set("red", "green", "purple", "blue", "brown", "turquoise")

  @tailrec
  private def toAlpha(col: Int, r: String = ""): StitchId = {
    val s = s"${ letters(col % 26) }$r"
    if (col < 26) s
    else toAlpha(col / 26 - 1, s)
  }

  @JSExport
  def toID(row: Int, col: Int): String = {
    s"${ toAlpha(col) }${ row + 1 }"
  }

  /**
   * @param assignment example: A1=B3=ctct, the caller takes care of at least one '='
   * @return
   */
  private def splitAssignment(assignment: String): Array[(StitchId, String, String)] = {
    val (ids, values) = assignment.split("=").partition(_.matches("([a-z]+[0-9]+[0-9a-z]*|cross|twist)"))
    val (instructions, colors) = values.partition(_.matches("[ctlrp]+"))
    ids.map(id => (
      id,
      instructions.headOption.getOrElse("ctc"),
      colors.find(availableColors.contains).getOrElse("")
    ))
  }

  @JSExport
  def makeValid(s: String, default: String): String = {
    val strippedPins = s.replaceAll("p", "")
    if (strippedPins.length == 0) default // need more than a pin
    else if (s.length - strippedPins.length > 1) default // at most one pin
    else s
  }

  val colors: Map[String, String] = Map(
    "" -> "#000",
    "red" -> "#f00",
    "blue" -> "#18C",
    "green" -> "#080",
    "brown" -> "#c90",
    "purple" -> "#c3f",
    "yellow" -> "#ee0",
    "turquoise" -> "#0f9")

  @JSExport
  def defaultColorValue(stitch: String): String = {
    val validStitch = makeValid(stitch, stitch + stitch)
    if (stitch != validStitch) ""
    else Stitches.colors.getOrElse(defaultColorName(stitch), "#000")
  }

  /**
    * @param s a sequence of': (either l's or r's) and/or t's
    *  @return true if both pairs are twisted at least once
    */
  private def bothTwisted(s: String) = s.count('t' == _) > 0

  /** Split a string (with any sequence of ctlr) into
    * - everything before the first c
    * - the first c up to and including the last c
    * - everything after the last c
    */
  private val regex: Regex = "([lrt]*)(.*c)?(.*)".r()

  /**
    * @param stitch Any sequence of "ctrlp" of at least length one.
    *               Though rl=lr=t the t should be used wherever possible,
    *               meaning (ignoring pins): between, before and after c's only
    *               either [l's and/or t's] or [r's and/or t's]
    * @return An empty string (rendered as black) or the color name for a stitch.
    */
  def defaultColorName(stitch: String): String = {
    // keep this method at the bottom of this class or adjust /GroundForge-helpcolor-code.md
    val hasPins = stitch.count('p' == _) > 0
    val crossCount = stitch.count('c' == _)
    val regex(openTwists, coreStitch, closeTwists) = stitch
    val twisted = bothTwisted(openTwists) || bothTwisted(closeTwists)
    (hasPins, crossCount, coreStitch, twisted) match {
      case (false, 1, _, true) => "green"
      case (false, 2, "cttc", _) => "turquoise"
      case (false, 2, "ctc", true) => "red"
      case (false, 2, "ctc", false) => "purple"
      case (false, 2, core, _) if !core.matches("c(|.|tt)c") => "brown"
      case (false, n, core, _) if n > 2 && core.matches("c(tc)+") => "blue"
      case (false, n, core, _) if n > 3 && core.matches("(crr)?(cllcrr)+(cll)?c") => "yellow"
      case _ => ""
    }
  }
}
