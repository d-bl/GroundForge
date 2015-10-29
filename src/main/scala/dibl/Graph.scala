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

import dibl.Matrix._

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.scalajs.js.Dictionary
import scala.scalajs.js.annotation.JSExport

case class Graph(nodes: Array[HashMap[String,Any]],
                 links: Array[HashMap[String,Any]]) {
  private val startLinks = links.filter(_.getOrElse("start","").toString.startsWith("pair"))
  for (i <- startLinks.indices){
    val source = startLinks(i).get("source").get.asInstanceOf[Int]
    nodes(source) = Props("title" -> s"Pair ${i+1}")
  }
}

@JSExport
object Graph {

  def apply(abs: M): Graph = Graph(toNodes(abs), toLinks(abs))
  def apply(dim: String = "2x4",
            nr: Int = 0,
            width: Int = 12,
            height: Int = 11
            ): Graph = Graph(Matrix(getRelSources(dim, nr),
                                    width,
                                    height))

  @JSExport
  def getD3Data(dim: String = "2x4",
                nr: Int = 0,
                width: Int = 12,
                height: Int = 10): Dictionary[Any] = {
    val result = js.Object().asInstanceOf[Dictionary[Any]]
    val g = Graph(dim, nr, width, height)
    result("nodes") = toJS(g.nodes)
    result("links") = toJS(g.links)
    result
  }

  def toJS(items: Array[HashMap[String,Any]]): Array[Dictionary[Any]] = {
    val jsItems = Array.fill(items.length)(js.Object().asInstanceOf[Dictionary[Any]])
    for (i <- items.indices) {
      for (key <- items(i).keys) {
        jsItems(i)(key) = items(i).get(key).get
      }
    }
    jsItems
  }

  /** Creates nodes for a pair diagram as in https://github.com/jo-pol/DiBL/blob/gh-pages/tensioned/sample.js 
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

    val nodes = Array.fill(rows*cols)(dummy)
    for {row <- m.indices
         col <- m(0).indices} {
      if (m(row)(col).length > 0)
        nodes((row+2)*cols+col+2) = stitch
    }
    for (col <- m(0).indices) {
      nodes(cols*(rows-3)+col+2) = bobbin
      nodes(cols*(rows-2)+col+2) = bobbin
    }
    for (row <- m.indices) {
      nodes(cols*(row+2)   ) = bobbin
      nodes(cols*(rows-1)-2) = bobbin
    }
    nodes
  }

  /** Creates links for a pair diagram as in https://github.com/jo-pol/DiBL/blob/gh-pages/tensioned/sample.js */
  def  toLinks (m: M): Array[Props] = {

    val cols = m(0).length + 4
    val links = ListBuffer[Props]()
    for {row <- m.indices
                       col <- m(0).indices
                       i <- m(row)(col).indices} {
      val(srcRow,srcCol) = m(row)(col)(i)
      val ds = if (srcRow < 0) (srcCol - col + 1)%2 else 0
      val dt = if (row == m.length - 1) i else 0
      links += Props("source" -> (cols * (srcRow + 2 - ds) + srcCol + 2),
                     "target" -> (cols * (   row + 2 + dt) +    col + 2),
                     "start" -> (if (srcRow < 0 || srcCol < 0 || srcCol+4 >= cols)
                                 "pair" else "red"),
                     "end" -> (if (row+1 < m.length) "red" else ""))
    }
    // keep thread number tooltips on nodes in proper order by connecting them
    var lastSource = -1
    val startLinks = links.filter(l => l.get("start").get.asInstanceOf[String].startsWith("pair")
                                    && l.get("target").get.asInstanceOf[Int] < 3*cols
                                 )
    for (i <- startLinks.indices){
      val source = startLinks(i).get("source").get.asInstanceOf[Int]
      if (lastSource >= 0)
        links += Props("source" -> lastSource,
                       "target" -> source,
                       "border" -> true)
      lastSource = source
    }
    links.toArray
  }
}
