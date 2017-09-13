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

  private val default = defaults.mkString match {
    case "" => "ctc"
    case s => makeValid(s, "ctc")
  }

  private val tuples = assignments.flatMap(splitAssignment)

  private val stitches: Map[StitchId, Instructions] = tuples.map {
    case (id, instructions, _) => (id, makeValid(instructions, default))
  }.toMap

  private val colors: Map[StitchId, Instructions] = tuples.map {
    case (id, stitch, color) =>
      (id,
        if (color != "") color
        else stitch match {
          case s if s.contains("p") => ""
          case s if s.count(_ == 'c') > 2 => "" // TODO "blue", need a single t between c's
          case s if s.contains("ctct") || s.contains("tctc") => "red"
          case s if s.contains("ctc") => "purple"
          case _ => ""
          // TODO see https://github.com/d-bl/GroundForge/issues/49#issuecomment-328344801
        }
      )
  }.toMap

  /**
    * @param id one or two letters followed by digits
    * @return the default stitch in case of an invalid id
    */
  def stitch(id: StitchId): Instructions = {
    stitches.getOrElse(id.toLowerCase(), default)
  }

  /**
    * @return the default stitch if a row/col is out of bounds
    */
  def stitch(row: Int, col: Int): Instructions = {
    stitch(s"${toAlpha(col)}${row + 1}")
  }

  /**
    * @param nrOfRows negative is interpreted as zero
    * @param nrOfCols negative is interpreted as zero
    * @return a matrix with stitch instruction of at least 1x1
    */
  def instructions(nrOfRows: Int, nrOfCols: Int): Seq[Seq[Instructions]] = {
    (0 until max(0, nrOfRows)).map { row =>
      (0 until max(0, nrOfCols)).map { col =>
        stitch(row, col)
      }
    }
  }

  def colors(nrOfRows: Int, nrOfCols: Int): Seq[Seq[ColorName]] = {
    (0 until max(0, nrOfRows)).map { row =>
      (0 until max(0, nrOfCols)).map { col =>
        ???
      }
    }
  }
}

object Stitches {

  type StitchId = String
  type ColorName = String
  type Instructions = String

  private val letters = 'a' to 'z'
  private val availableColors = Set("red", "green", "puple")

  @tailrec
  private def toAlpha(col: Int, r: String = ""): StitchId = {
    val s = s"${letters(col % 26)}$r"
    if (col < 26) s else toAlpha(col / 26 - 1, s)
  }

  /**
    * @param assignment example: A1=B3=ctct, partition in the class takes care of at least one '='
    * @return
    */
  private def splitAssignment(assignment: String): Array[(StitchId, Instructions, ColorName)] = {
    val (ids, values) = assignment.split("=").partition(_.matches("([a-z]+[0-9]+|cross|twist)"))
    val (colors, instructions) = values.partition(availableColors.contains)
    ids.map(id => (id, instructions.mkString, colors.mkString))
  }

  private def makeValid(instructions: String, default: Instructions): Instructions = {
    instructions.replaceAll("[^ctrlp]", "") match {
      // skip invalid characters
      case s if s.replaceAll("[^p]", "").length > 1 => default // at most one pin
      case s if s.replaceAll("[^c]", "").length < 1 => default // at least one cross
      case s => s
    }
  }
}
