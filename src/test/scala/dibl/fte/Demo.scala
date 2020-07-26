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
package dibl.fte

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
    writeSvg(s"$dir/topo-pinwheel-pairs.svg", TopoLink.fromString(
      "b4,a1,lo,ri,1;d4,a1,lo,li,1;b2,a3,lo,ri,0.8;d2,a3,lo,li,1;a1,b1,lo,li,1;b4,b1,ro,ri,1;b1,b2,lo,li,1;c1,b2,lo,ri,1;a3,b4,lo,li,1;c3,b4,lo,ri,0.8;b1,c1,ro,li,1;d4,c1,ro,ri,1;b2,c3,ro,li,1;d3,c3,lo,ri,1;a1,d2,ro,ri,1;c1,d2,ro,li,1;a3,d3,ro,ri,1;d2,d3,ro,li,1;c3,d4,ro,li,1;d3,d4,ro,ri,1"
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
          println
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
          println(s"${fileName.replaceAll(".*/","")} Elapsed time: ${ (System.nanoTime() - t0) * 0.000000001 } sec")
      }
    }
  }

  // rest of the API TODO isolate delta calculation/extraction for javascript
  private def writeSvg(fileName: String, links: Seq[TopoLink]) = GraphCreator
    .graphFrom(links)
    .map { svg => File(fileName).writeAll(svg) }
    .recover { case t: Throwable => Success(println(t)) }
}