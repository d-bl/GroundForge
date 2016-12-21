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

import org.scalatest.{FlatSpec, Matchers}

class DiagramSpec extends FlatSpec with Matchers {

  "rose ground pair diagram" should "have color and some mid markers" in {

    val d = PairDiagram(Settings(
      "5831 -4-7", "bricks",
      stitches = "ttc,A1=tctc,B2=pttc,C2=ctc",
      absRows = 12, absCols = 11,
      shiftLeft = 1, shiftUp = 3)
    )
    verifyLinks(d)

    // additional twists in the footsides
    val titles = d.nodes
      .map(_.title)
      .filter(!_.startsWith("Pair"))
      .mkString(", ")
    titles should include ("l")
    titles should include ("r")

    d.links.count(_.getOrElse("end","").toString == "green") shouldNot be(0)
    d.links.count(_.getOrElse("start","").toString == "green") shouldNot be(0)
    d.links.count(_.getOrElse("end","").toString == "red") shouldNot be(0)
    d.links.count(_.getOrElse("start","").toString == "red") shouldNot be(0)
    d.links.count(_.getOrElse("end","").toString == "purple") shouldNot be(0)
    d.links.count(_.getOrElse("start","").toString == "purple") shouldNot be(0)
    d.links.count(_.getOrElse("mid","") == 1) shouldNot be(0)
    d.links.count(_.getOrElse("mid","") == 0) shouldNot be(0)
  }

  "5- bricks" should "create diagrams recursively" in {

    val pairDiagram = PairDiagram("tctc", ThreadDiagram(PairDiagram(
      Settings("5-", "bricks", stitches = "tctc", absRows = 6, absCols = 6)
    )))
    verifyLinks(ThreadDiagram(pairDiagram))
    verifyLinks(pairDiagram)
  }

  private def verifyLinks(p: Diagram) = {
    p.nodes.size shouldNot be(0)
    p.links.size shouldNot be(0)
    p.links.exists(_.source >= p.nodes.size) shouldBe false
    p.links.exists(_.source < 0) shouldBe false
    p.links.exists(_.target >= p.nodes.size) shouldBe false
    p.links.exists(_.target < 0) shouldBe false
  }
}
