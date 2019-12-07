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

import fte.data.GraphCreator
import fte.ui.SVGRender

import scala.util.{ Failure, Success, Try }

object Demo {
  def main(args: Array[String]): Unit = {
    Seq(
      "bandage&patchWidth=3&patchHeight=4&tile=1,8&tileStitch=ctc&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2",
      "torchon&patchWidth=6&patchHeight=4&tile=5-,-5&tileStitch=ctc&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
      "pinwheel&patchWidth=12&patchHeight=8&tile=586-,-4-5,5-21,-5-7&shiftColsSE=4&shiftRowsSE=4&shiftColsSW=0&shiftRowsSW=4&",
      "whiting=F14_P193&patchWidth=24&patchHeight=28&tile=-XX-XX-5,C-X-X-B-,-C---B-5,5-C-B-5-,-5X-X5-5,5XX-XX5-,-XX-XX-5,C-----B-,-CD-AB--,A11588D-,-78-14--,A11588D-,-78-14--,A11588D-&shiftColsSW=0&shiftRowsSW=14&shiftColsSE=8&shiftRowsSE=14",
      "braid&patchWidth=18&patchHeight=8&tile=-B-C-y,B---cx,xC-B-x,m-5-b-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=6&shiftRowsSE=4&a4=llcttct&e4=rrcttctrr",
    ).zipWithIndex.foreach { case (query, i) =>
      Try(GraphCreator.fromPairs(query)) match {
        case Success(None) => println(s"$i has no solution")
        case Failure(e) => e.printStackTrace()
        case Success(Some(graph)) =>
          new SVGRender().draw(graph, s"target/test/from-pairs-$i.svg")
      }
    }
  }
}