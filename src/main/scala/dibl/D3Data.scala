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

import scala.collection.immutable.HashMap
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
object D3Data {

  @JSExport
  def get(set: String,
          nrInSet: Int,
          rows: Int,
          cols: Int,
          shiftLeft: Int,
          shiftUp: Int,
          stitches: String
         ): js.Dictionary[js.Array[js.Dictionary[Any]]] = {
    val pairDiagram = PairDiagram(Settings(set, nrInSet, rows, cols, shiftLeft, shiftUp, stitches).get)
    val threadDiagram = ThreadDiagram(pairDiagram)
    js.Dictionary(
      "nodes" -> toJS(pairDiagram.nodes),
      "links" -> toJS(pairDiagram.links),
      "threadNodes" -> toJS(threadDiagram.nodes),
      "threadLinks" -> toJS(threadDiagram.links)
    )
  }

  @JSExport
  def hardCodedPattern(): js.Dictionary[js.Array[js.Dictionary[Any]]] = {
    val diagram = PairDiagram(Settings().get)
    js.Dictionary(
      "nodes" -> toJS(diagram.nodes),
      "links" -> toJS(diagram.links)
    )
  }

  def toJS(items: Seq[HashMap[String,Any]]
          ): js.Array[js.Dictionary[Any]] = {

    val a = js.Array[js.Any](items.length).asInstanceOf[js.Array[js.Dictionary[Any]]]
    for {i <- items.indices} {
      a(i) = js.Object().asInstanceOf[js.Dictionary[Any]]
      for {key <- items(i).keys} {
        a(i)(key) = items(i).get(key).get
      }
    }
    a
  }
}
