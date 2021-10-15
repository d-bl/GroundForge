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

import dibl.DiagramSvg.{prolog, render}
import dibl.Force.Point
import dibl.proto.TilesConfig

import scala.reflect.io.File

object FlandersThumbs extends DemoFixture {
  def main(args: Array[String]): Unit = {
    val stitches = Seq("ct", "cl", "cr", "ctc", "clc", "crc", "ctct", "clct", "crct", "ctcl", "clcl", "crcl", "ctcr", "clcr", "crcr")
    val symmetricStitches = Seq("ct", "ctc", "ctct")
    val patterns = for {
      a1 <- stitches
      b1 <- symmetricStitches
      c1 = flip(a1)
      a2 <- stitches
      c2 = flip(a2)
      b3 <- symmetricStitches
    } yield (s"$testDir/$a1-$b1-$c1-$a2-$c2-$b3.svg",
      s"a1=$a1&b1=$b1&c1=$c1&a2=$a2&c2=$c2&b3=$b3&")
    val filtered = patterns // 50625/2025 with all/symmetric stitches for B1/B3
      .filter{case (_,q)=> q.contains("=ct&")} // 1241
      .filter{case (_,q)=> q.contains("b1=ctc") || q.contains("b3=ctc") } // 1016
    println(filtered.size)
    // nudging results stabilize after some 150-200 patterns, repeat these
    (filtered.slice(0,200) ++ filtered)
      .foreach { case (fileName, q) => create(fileName, q)}
    System.exit(0)
  }

  private def create(fileName: String, q: String): Unit = {
    Force.nudgeNodes(
      ThreadDiagram(NewPairDiagram.create(TilesConfig(q +
        "tile=831,4-7,-5-&patchWidth=12&patchHeight=14&" +
        "shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"))),
      Point(150, 150), timeout = 40
    ).map(diagram => File(fileName).writeAll(prolog + render(diagram, width = 300, height = 300)))
  }

  private def flip(a: String) = {
    a.replaceAll("l", "R")
      .replaceAll("r", "L")
      .toLowerCase()
  }
}
