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

import dibl.D3jsSVG.{ prolog, render }
import dibl.Force.{ Point, nudgeNodes }
import dibl.proto.TilesConfig

import scala.reflect.io.File

object Permutations {
  def main(args: Array[String]): Unit = {
    new java.io.File("target/test/permutations").mkdirs()
    val stitches = Seq("ct", "ctct", "crclct", "clcrclc", "ctctc", "ctclctc")
    for {
      a <- stitches
      b <- stitches.filterNot(_==a)
    } {
      create(s"net-$a-$b", s"a1=$a&b2=$b&patchWidth=9&patchHeight=9&tile=5-,-5&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2")
      create(s"weaving-$a-$b", s"a1=$a&a2=$b&patchWidth=9&patchHeight=9&tile=1,8&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2")
    }
    System.exit(0)
  }

  private def create(name: String, q: String): Unit = {
    File(s"target/test/permutations/$name.svg")
      .writeAll(prolog + render(nudge(ThreadDiagram(NewPairDiagram.create(TilesConfig(q))))))
  }

  def nudge(d: Diagram): Diagram = nudgeNodes(d, Point(200, 100)).get
}
