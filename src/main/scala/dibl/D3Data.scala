/*
 Copyright 2015 Jo Pol
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

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object D3Data {

  @JSExport
  def get(str: String,
          rows: Int,
          cols: Int,
          shiftLeft: Int,
          shiftUp: Int,
          stitches: String,
          bricks: Boolean
         ): js.Dictionary[js.Array[js.Dictionary[Any]]] = {

    val pairDiagram = PairDiagram(Settings.create(str, bricks,  rows, cols, shiftLeft, shiftUp, stitches))
    val threadDiagram = ThreadDiagram(pairDiagram)
    js.Dictionary(
      "pairNodes" -> toJS(pairDiagram.nodes),
      "pairLinks" -> toJS(pairDiagram.links),
      "threadNodes" -> toJS(threadDiagram.nodes),
      "threadLinks" -> toJS(threadDiagram.links)
    )
  }

  def toJS(items: Seq[Props]
          ): js.Array[js.Dictionary[Any]] = {

    val a = new js.Array[js.Any](items.length).asInstanceOf[js.Array[js.Dictionary[Any]]]
    for {i <- items.indices} {
      a(i) = js.Object().asInstanceOf[js.Dictionary[Any]]
      for {key <- items(i).keys} {
        a(i)(key) = items(i).get(key).get
      }
    }
    a
  }
}
