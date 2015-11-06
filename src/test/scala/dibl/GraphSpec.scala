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

import org.scalatest._

import scala.scalajs.js.Dictionary

class GraphSpec extends FlatSpec with Matchers {

  "apply" should "produce nodes and links" in {
    val graph = Graph("2x4",0,12,12)
    graph.nodes.length should equal (154)
    graph.links.length should equal(273)
  }

  "getD3Data" should "produce nodes and links" in {
    val graph = Graph.getD3Data("2x4",0,12,12)
    graph.get("nodes").get.asInstanceOf[Dictionary[Any]].size should equal (154)
    graph.get("links").get.asInstanceOf[Dictionary[Any]].size should equal(250)
  }

  ignore should "run with JVM used to build scala.js libraries" in {
    val data = Graph.getD3Data()
  }
}