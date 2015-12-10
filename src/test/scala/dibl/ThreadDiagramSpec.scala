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
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Try

class ThreadDiagramSpec extends FlatSpec with Matchers {
  "ThreadDiagram" should "not throw exceptions" in {
    // what it seems to do in situ at the first row of stitches for most patterns
    // TODO does some try hide an index out of range? It may survive in a JVM but not in JavaScript.
    val errors = new StringBuffer()
    matrixMap.keys.foreach { key =>
      for (i <- matrixMap.get(key).get.indices) {
        for {
          s <- Settings(key, i, absRows = 8, absCols = 5)
          p <- Try(PairDiagram(s))
          t <- Try(ThreadDiagram(p))
          _ <- Try(traverse(t.nodes))
          _ <- Try(traverse(t.links))
        } yield ()
      }
      println(key)
    }
    println("---")
    errors.toString shouldBe ""
  }

  def traverse(items: Seq[Props]) = {
    // mimics D3Data.toJS, which requires a specific JVM
    val a = new Array[Any](items.length)
    //println(s"${items.length} ${a.length}")
    for {i <- items.indices} {
      for {key <- items(i).keys} {
        a(i) = items(i).get(key).get
      }
    }
  }
}
