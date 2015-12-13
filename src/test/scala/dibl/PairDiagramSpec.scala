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

class PairDiagramSpec extends FlatSpec with Matchers {

  "apply" should "produce nodes and links" in {
    val graph = PairDiagram(Settings("2x4",0,12,12,0,0,""))
    println(graph.links.mkString("\n"))
    println(graph.nodes.map(_.toString).sorted.distinct.mkString("\n"))
    graph.nodes.length should be  > 100
    graph.links.length should be > 200
    // TODO nr of pairs should equal nr of bobbins
  }
  it should "survive empty stitches value" in {
    val graph = PairDiagram(Settings("2x4",0,12,12,0,0,""))
    graph.nodes.length should be  > 100
    graph.links.length should be > 200
  }
  it should "survive invalid stitches value" in {
    val graph = PairDiagram(Settings("2x4",0,12,12,0,0,"A1#$tc"))
    graph.nodes.length should be  > 100
    graph.links.length should be > 200
  }
  it should "properly process hardcoded sample" in {
    val graph = PairDiagram(Settings())
    graph.nodes.length should be  > 40
    graph.links.length should be > 60
  }
}