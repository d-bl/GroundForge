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

object Demo {
  def main(args: Array[String]): Unit = {
    Seq( // see links to https://d-bl.github.io/GroundForge/tiles
      // patch sizes must match one checker tile
      // for now foot sides must be provided together with an osculating pair (that connects multiple braids) in center tile
      "tile=586-,-4-5,5-21,-5-7&shiftColsSE=4&shiftRowsSE=4&shiftColsSW=0&shiftRowsSW=4&patchWidth=4&patchHeight=4&",
      // TODO for the next pattern: use edges of pair diagram, not those of prototype
      "tile=-B-C-y,B---cx,xC-B-x,m-5-b-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=6&shiftRowsSE=4&patchWidth=6&patchHeight=4&",
    ).zipWithIndex
      .map { case (query, i) => (i, GraphCreator.from(query)) }
      .foreach {
        case (_, None) =>
        case (i, Some(graph)) =>
          new SVGRender().draw(graph, s"target/test/flat-torus-embedding-$i.svg")
      }
  }
}