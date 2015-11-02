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
                 links: Array[HashMap[String,Any]]){
  private val startLinks = links.filter(_.getOrElse("start","").toString.startsWith("pair"))
  for (i <- startLinks.indices){
    val source = startLinks(i).get("source").get.asInstanceOf[Int]
    nodes(source) = Props("title" -> s"Pair ${i+1}")
  }
}

@JSExport
object Graph {

  def apply(set: String = "2x4",
            nrInSet: Int = 0,
            rows: Int = 12,
            cols: Int = 11
            ): Graph = {
    val rel: M = toCheckerboard(getRelSources(set, nrInSet))
    val abs: M = toAbsWithMargins(rel,rows,cols)
    val nrOfLinks = countLinks(abs)
    val nodeNrs = assignNodeNrs(abs, nrOfLinks)
    Graph(toNodes(abs, nrOfLinks), toLinks(abs, nodeNrs))
  }

  def assignNodeNrs(abs: M, nrOfLinks: Array[Array[Int]]): Array[Array[Int]] = {
    val nodeNrs = Array.fill(abs.length, abs(0).length)(0)
    var nodeNr = 0
    for {row <- nodeNrs.indices
         col <- nodeNrs(0).indices
    } {
      if (nrOfLinks(row)(col) > 0) {
        nodeNrs(row)(col) = nodeNr
        nodeNr += 1
      }
    }
    nodeNrs
  }

  @JSExport
  def getD3Data(set: String = "2x4",
                nrInSet: Int = 0,
                rows: Int = 11,
                cols: Int = 10): Dictionary[Any] = {
    val result = js.Object().asInstanceOf[Dictionary[Any]]
    val g = Graph(set, nrInSet, rows, cols)
    result("nodes") = toJS(g.nodes)
    result("links") = toJS(g.links)
    result
  }

  def toJS(items: Array[HashMap[String,Any]]): Array[Dictionary[Any]] = {
    val jsItems = Array.fill(items.length)(js.Object().asInstanceOf[Dictionary[Any]])
    for {i <- items.indices
         key <- items(i).keys} {
      jsItems(i)(key) = items(i).get(key).get
    }
    jsItems
  }

  /** Creates nodes for a pair diagram as in https://github.com/jo-pol/DiBL/blob/gh-pages/tensioned/sample.js */
  def toNodes (m: M, nrOfLinks: Array[Array[Int]]): Array[Props] = {

    val stitch = Props("title" -> "stitch")
    val bobbin = Props("bobbin" -> true)
    val nodes = ListBuffer[Props]()
    for {row <- m.indices
         col <- m(0).indices
        } {
      if (nrOfLinks(row)(col) > 0) {
        nodes += (if (inMargin(m, row, col)) bobbin else stitch)
      }
    }
    nodes.toArray
  }

  /** Creates links for a pair diagram as in https://github.com/jo-pol/DiBL/blob/gh-pages/tensioned/sample.js */
  def  toLinks (m: M, nodeNrs: Array[Array[Int]]): Array[Props] = {

    val cols = m(0).length
    val links = ListBuffer[Props]()
    connectLoosePairs(1,m(2).flatten.filter(inTopMargin))
    for {row <- m.indices
         col <- m(0).indices
         i <- m(row)(col).indices} {
      val(srcRow,srcCol) = m(row)(col)(i)
      links += Props("source" -> nodeNrs(srcRow)(srcCol),
                     "target" -> nodeNrs(row)(col),
                     "start" -> (if (inMargin(m,srcRow,srcCol)) "pair" else "red"),
                     "end" -> (if (inMargin(m,row,col)) "green" else "red"))
    }
    // keep more loose ends in order
    // connectLoosePairs(2,column(m,2)/* TODO add target to the sources */.flatten
    //   .filter(n => inLeftMargin(n)&& (!inTopMargin(n))).toArray
    //   )
  
    def connectLoosePairs (start: Int, sources: SrcNodes): Unit = {
      for (i <- start until sources.length) {
        val (srcRow,srcCol) = sources(i-1)
        val (row,col) = sources(i)
        links += Props("source" -> nodeNrs(srcRow)(srcCol),
                       "target" -> nodeNrs(row)(col),
                       "border" -> true)
      }
    }
    links.toArray
  }

  def inTopMargin (node: (Int, Int)): Boolean = {
    val (row,_) = node
    row < 2
  }

  def inLeftMargin (node: (Int, Int)): Boolean = {
    val (_,col) = node
    col < 2
  }

  def inMargin(m: M, row: Int, col: Int): Boolean = {
    val rows = m.length
    val cols = m(0).length
    row < 2 || col < 2 || col >= cols - 2 || row >= rows - 2
  }
}
