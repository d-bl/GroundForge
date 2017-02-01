/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html
*/
package dibl

import dibl.Force.{Point, simulate}

import scala.reflect.io.File

object SVGDemo {
  def main(args: Array[String]): Unit = {
    val pairDiagram = PairDiagram("5-,-5", "checker", stitches = "A1=ct B2=ctct", absRows = 6, absCols = 6).get

    File("target/demo.svg").writeAll(
      """<?xml version="1.0" encoding="UTF-8"?>""" +
        SVG.render(pairDiagram)
    )
  }
}
