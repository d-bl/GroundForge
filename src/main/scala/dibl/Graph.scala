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
import scala.collection.mutable.ListBuffer
import scala.scalajs.js.Dictionary
import scala.scalajs.js.annotation.JSExport
import dibl.Matrices._
import scala.scalajs.js
import js.JSConverters._

case class Graph(nodes: Array[Dictionary[Any]],
                 links: Array[Dictionary[Any]])

@JSExport
object Graph {

  @JSExport
  def getData(dim: String = "2x4", nr: Int = 0,
              width: Int = 12, height: Int = 12): Graph = {
    val abs = toAbsSources(toCheckerboard(getRelSources(dim, nr)), width, height)
    new Graph(toJS(toNodes(abs)), toJS(toLinks(abs)))
  }

  def toJS(items: Array[Props]): Array[Dictionary[Any]] = {
    val jsItems: Array[Dictionary[Any]] = Array.ofDim(items.length)
    for (i <- items.indices) {
      jsItems(i) = Dictionary.empty[Any]//[Any].apply(Seq[(String,Any)]())
      for (key <- items(i).keys) {
        jsItems(i)(key) = items(i).get(key).get
      }
    }
    jsItems
  }

  /** Creates nodes for a pair diagram as in https://github.com/jo-pol/DiBL/blob/2136fe12/tensioned/sample.js 
    * <pre>
    * ::v v::
    * > o o <
    * > o o <
    * ::^ ^::
    * </pre>
    * In above ascii art each "o" represent a node for a stitch, matching an element in the matrix.
    * "::" represent invisible nodes created for simplicity. 
    * The other symbols match two nodes for (potential) new pairs or for pairs of bobbins.    
    */
  def toNodes (m: M): Array[Props] = {

    val rows = m.length + 4
    val cols = m(0).length +4

    val dummy = Props()
    val stitch = Props("title" -> "stitch")
    val bobbin = Props("bobbin" -> true)
    var startNr = 0
    def start = {
      startNr += 1
      Props("title" -> s"pair $startNr")
    } 

    val nodes = Array.fill(rows*cols)(dummy)
    for {row <- m.indices
         col <- m(0).indices} {
      nodes((row+2)*cols+col+2) = stitch
    }
    for (col <- m(0).indices) {
      // TODO conditionally skip 
      nodes(              col+2) = start
      nodes(cols         +col+2) = start
      nodes(cols*(rows-2)+col+2) = bobbin
      nodes(cols*(rows-1)+col+2) = bobbin
    }
    for (row <- m.indices) {
      // TODO conditionally swap/skip start/bobbin
      nodes(cols*(row+2)   ) = bobbin
      nodes(cols*(row+2) +1) = start
      nodes(cols*(rows-1)-2) = bobbin
      nodes(cols* rows   -1) = start
    }
    nodes
  }

  /** Creates color-less links for a pair diagram as in https://github.com/jo-pol/DiBL/blob/2136fe12/tensioned/sample.js */
  def  toLinks (m: M): Array[Props] = {

    val cols = m(0).length + 4

    val links = ListBuffer[Props]()
    for (col <- m(0).indices) {
      // links += Props("source" -> ???, "target" -> ???, "border" -> true)
    }
    for (row <- m.indices) {
      // links += Props("source" -> ???, "target" -> ???, "border" -> true)
    }
    for {row <- m.indices
         col <- m(0).indices} {
      val target = cols*(row+2)+col+2
      for ((srcRow,srcCol) <- m(row)(col)) {
        val src = cols*(srcRow+2)+srcCol+2
        links += Props("source" -> src, "target" -> target)
      }
    }
    links.toArray
  }
}
