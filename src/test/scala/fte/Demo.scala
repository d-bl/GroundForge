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

import dibl.fte.{ GraphCreator, TopoLink }

import scala.reflect.io.File
import scala.util.Success

object Demo {
  def main(args: Array[String]): Unit = {
    val dir = new java.io.File("target/test/fte-demo") {
      mkdirs()
      listFiles().foreach(_.delete())
    }
    // API intended for the content of a text area in an HTML page
    writeSvg(s"$dir/topo-pinwheel-ct.svg", TopoLink.fromString(
      "b41,a10,f,t;d42,a10,t,f;a10,a11,f,t;d42,a11,t,t;a10,a12,t,f;b41,a12,f,f;b21,a30,f,t;d22,a30,t,f;a30,a31,f,t;d22,a31,t,t;a30,a32,t,f;b21,a32,f,f;a12,b10,t,f;b42,b10,f,t;a12,b11,t,t;b10,b11,f,t;b10,b12,t,f;b42,b12,f,f;b11,b20,t,f;c11,b20,f,t;b11,b21,t,t;b20,b21,f,t;b20,b22,t,f;c11,b22,f,f;a32,b40,t,f;c31,b40,f,t;a32,b41,t,t;b40,b41,f,t;b40,b42,t,f;c31,b42,f,f;b12,c10,t,f;d41,c10,f,t;b12,c11,t,t;c10,c11,f,t;c10,c12,t,f;d41,c12,f,f;b22,c30,t,f;d31,c30,f,t;b22,c31,t,t;c30,c31,f,t;c30,c32,t,f;d31,c32,f,f;a11,d20,f,t;c12,d20,t,f;c12,d21,t,t;d20,d21,f,t;a11,d22,f,f;d20,d22,t,f;a31,d30,f,t;d21,d30,t,f;d21,d31,t,t;d30,d31,f,t;a31,d32,f,f;d30,d32,t,f;c32,d40,t,f;d32,d40,f,t;c32,d41,t,t;d40,d41,f,t;d32,d42,f,f;d40,d42,t,f"
    ))

    // API intended for a direkt bookmark-able link
    writeSvg(s"$dir/topo-torchon-ct.svg", TopoLink.fromUrlQuery(
      "topo=b21,a10,f,t;b22,a10,t,f;a10,a11,f,t;b23,a11,t,f;a10,a12,t,f;b23,a12,f,t;a11,a13,t,f;a12,a13,f,t;a11,b20,f,t;a12,b20,t,f;a13,b21,t,f;b20,b21,f,t;a13,b22,f,t;b20,b22,t,f;b21,b23,t,f;b22,b23,f,t"
      //"topo=b21,a10,f,t;b22,a10,t,f;a10,a11,f,t;a10,a12,t,f;a11,a13,t,f;a12,a13,f,t;a13,b22,f,t;"
    ))

    // permutations over the API intended for links from the tiles page (benchmark)
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
        // for now prefixed id's of foot side stitches with X to avoid repeated twists
        s"braid&patchWidth=18&tileStitch=$stitch&patchHeight=8&tile=-B-C-y,B---cx,xC-B-x,m-5-b-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=6&shiftRowsSE=4&Xa4=llcttct&Xe4=rrcttctrr",
        s"whiting=F14_P193&tileStitch=$stitch&patchWidth=24&patchHeight=28&tile=-XX-XX-5,C-X-X-B-,-C---B-5,5-C-B-5-,-5X-X5-5,5XX-XX5-,-XX-XX-5,C-----B-,-CD-AB--,A11588D-,-78-14--,A11588D-,-78-14--,A11588D-&shiftColsSW=0&shiftRowsSW=14&shiftColsSE=8&shiftRowsSE=14",
      )
    } {
      (stitch, query.replaceAll("[&=].*", "")) match {
        case (`drosteDiagram`, "whiting") => // skip: too large
        case _ =>
          val qName = query.replaceAll("&.*", "").replaceAll("[^a-zA-Z0-9]+", "-")
          val tail = if (stitch == pairDiagram) "pairs"
                     else if (stitch == drosteDiagram) "droste"
                          else stitch
          val fileName = s"$dir/$qName-$tail.svg"
          val t0 = System.nanoTime()
          val links = if (stitch == pairDiagram) TopoLink.fromUrlQuery(query)
                      else if (stitch.startsWith("t"))
                             TopoLink.fromThreadDiagram(query + "&droste2=" + stitch)
                           else TopoLink.fromThreadDiagram(query)
          println(TopoLink.asString(links)) // to be placed in text area of HTML page
          writeSvg(fileName, links)
          println(s"Elapsed time: ${ (System.nanoTime() - t0) * 0.000000001 }sec for $query")
      }
    }
  }

  // rest of the API TODO isolate delta calculation/extraction for javascript
  private def writeSvg(fileName: String, links: Seq[TopoLink]) = GraphCreator
    .graphFrom(links)
    .map { svg => File(fileName).writeAll(svg) }
    .recover { case t: Throwable => Success(println(t)) }
}