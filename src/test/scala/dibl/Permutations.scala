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
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import dibl.D3jsSVG.{prolog, render}
import dibl.Force.{Point, nudgeNodes}

import scala.reflect.io.File

object Permutations {
  def main(args: Array[String]): Unit = {
    new java.io.File("target/test/permutations").mkdirs()
    val stitches = Seq("ct", "ctct", "crclct", "clcrclc", "ctctc", "ctclctc")
    for {
      d2 <- stitches
      b2 <- stitches.filter(_!=d2)
      a1 <- stitches
      c1 <- stitches.filter(_!=a1)
    } {
      val stitches = s"D2=$d2 B2=$b2 A1=$a1 C1=$c1"
      val pairs = PairDiagram.create("5-5-,-5-5", "checker", absRows = 7, absCols = 11, stitches = stitches)
      File(s"target/test/permutations/D2_$d2-B2_$b2-A1_$a1-C1_$c1.svg")
        .writeAll(prolog + render(nudge(ThreadDiagram(pairs.get))))
    }
    System.exit(0)
  }
  def nudge(d: Diagram): Diagram = nudgeNodes(d, Point(200, 100)).get
}
