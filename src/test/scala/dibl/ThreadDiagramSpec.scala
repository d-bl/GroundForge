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
    val errors = new StringBuffer()
    matrixMap.keys.foreach { key =>
      for (i <- matrixMap.get(key).get.indices) {
        println(s"--------- $key $i")
        val s = Settings(key, i, absRows = 8, absCols = 5)
        val x = for {
          p <- Try(PairDiagram(s))
          t <- Try(ThreadDiagram(p))
          _ <- Try(traverse(t.nodes))
          _ <- Try(traverse(t.links))
        } yield ()
        if (x.isFailure)errors.append(s"$key $i ${x.failed.get}\n")
      }
    }
    errors.toString shouldBe ""
  }

  "invalid key" should "render a single node" in {
    val pd = ThreadDiagram(PairDiagram(Settings("xxx", 1, absRows = 8, absCols = 9)))
    pd.links.length shouldBe 0
    pd.nodes.length shouldBe 1
    pd.nodes.head("title") shouldBe "invalid pair diagram"
  }

  "invalid dimension" should "render a single node" in {
    val pd = ThreadDiagram(PairDiagram(Settings("2x4", 1, absRows = -8, absCols = 9)))
    pd.links.length shouldBe 0
    pd.nodes.length shouldBe 1
    pd.nodes.head("title") shouldBe "invalid pair diagram"
  }

  "tmp" should "not throw an exception" in {
    val settings = Settings()
    val nodeNrs = PairDiagram.assignNodeNrs(settings.get.absM, settings.get.nrOfPairLinks)
    nodeNrs.foreach(nodes => println(s"${nodes.mkString(", ")}"))
    println()
    ThreadDiagram(PairDiagram(settings))
  }

  def traverse(items: Seq[Props]) = {
    // mimics D3Data.toJS, which requires a specific JVM
    val a = new Array[Any](items.length)
    for {
      i <- items.indices
      key <- items(i).keys
    } a(i) = items(i)(key)
  }

}
