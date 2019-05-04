/*
 Copyright 2016 Jo Pol
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

import dibl.sheet.SheetSVG

import scala.reflect.io.File

object PatternDemos {

  def main(args: Array[String]): Unit = {

    File("target/test/pattern/").createDirectory()

    {
      val patterns = new SheetSVG
      patterns.add("5831 -4-7", "bricks")
      patterns.add("-437 34-7", "bricks")
      patterns.add("5831 -4-7 3158 -7-4", "checker")
      patterns.add("4830 --77", "bricks")
      File("target/test/pattern/rose.svg").writeAll(patterns.toSvgDoc())
    }

    {
      val patterns = new SheetSVG
      patterns.add("B-5-C- -5-5-- B---C-", "bricks") // double length vertical rows
      patterns.add("-5---5-5 5-O-E-5-", "bricks") // double length horizontal lines

      patterns.add("586- -4-5 5-21 -5-7", "bricks")
      patterns.add("4831 -117 5-7- 86-5", "checker")

      patterns.add("588- -4-5 6-58 -214", "checker")
      patterns.add("5831 -4-7", "bricks")

      patterns.add("4832 2483", "bricks")
      patterns.add("5---5-5- -O-E-5-5", "bricks") // double length horizontal lines

      patterns.add("586- -4-5 5-21 -5-777", "checker") // reports an error
      File("target/test/pattern/pattern-sheet.svg").writeAll(patterns.toSvgDoc())
    }

    {
      val patterns = SheetSVG(1, "width='340' height='330'")
      patterns.add("586- -4-5 5-21 -5-7", "bricks")
      File("target/test/pattern/minimal.svg").writeAll(patterns.toSvgDoc())
    }
  }
}
