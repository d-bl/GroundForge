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
package fte

import dibl.fte.{ GraphCreator, SvgCreator }

import scala.reflect.io.File
import scala.util.{ Failure, Success, Try }

object Demo {
  def main(args: Array[String]): Unit = {
    val dir = new java.io.File("target/test/fte-demo") {
      mkdirs()
      listFiles().foreach(_.delete())
    }
    val pairDiagram = "tc"
    val drosteDiagram = "tctc"
    for {
      stitch <- Seq(pairDiagram, "ct", "ctc", "ctct", "crcrctclclcr", drosteDiagram)
      query <- Seq(
        s"bandage&tileStitch=$stitch&patchWidth=3&patchHeight=4&tile=1,8&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2",
        s"sheered&tileStitch=$stitch&patchWidth=6&patchHeight=4&tile=l-,-h&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        s"torchon&tileleStitch=$stitch&patchWidth=6&patchHeight=4&tile=5-,-5&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        s"rose&tileStitch=$stitch&patchWidth=12&patchHeight=8&tile=5831,-4-7,3158,-7-4&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4",
        s"pinwheel&tileStitch=$stitch&patchWidth=12&patchHeight=8&tile=586-,-4-5,5-21,-5-7&shiftColsSE=4&shiftRowsSE=4&shiftColsSW=0&shiftRowsSW=4&",
        s"whiting=F14_P193&tileStitch=$stitch&patchWidth=24&patchHeight=28&tile=-XX-XX-5,C-X-X-B-,-C---B-5,5-C-B-5-,-5X-X5-5,5XX-XX5-,-XX-XX-5,C-----B-,-CD-AB--,A11588D-,-78-14--,A11588D-,-78-14--,A11588D-&shiftColsSW=0&shiftRowsSW=14&shiftColsSE=8&shiftRowsSE=14",
        // for now prefixed id's of foot side stitches with X to avoid repeated twists
        s"braid&patchWidth=18&tileStitch=$stitch&patchHeight=8&tile=-B-C-y,B---cx,xC-B-x,m-5-b-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=6&shiftRowsSE=4&Xa4=llcttct&Xe4=rrcttctrr",
      )
      qName = query.replaceAll("&.*", "").replaceAll("[^a-zA-Z0-9]+", "-")
      tail = if (stitch == pairDiagram) "pairs"
             else if (stitch == drosteDiagram) "droste"
             else stitch
      fileName = s"$dir/$qName-$tail.svg"
    } { if (stitch!=drosteDiagram || !query.contains("whiting")) { // skip too large pattern
        val t0 = System.nanoTime()
        Try(if (stitch == pairDiagram) GraphCreator.fromPairDiagram(query)
            else if (stitch.startsWith("t"))
                   GraphCreator.fromThreadDiagram(query + "&droste2=" + stitch)
            else GraphCreator.fromThreadDiagram(query)
        ) match {
          case Success(None) =>
          case Failure(e) => e.printStackTrace()
          case Success(Some(graph)) => File(fileName).writeAll(SvgCreator.draw(graph))
        }
        println(s"Elapsed time: ${ (System.nanoTime() - t0) * 0.000000001 }sec for $query")
      }
    }
  }
}