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

class PairDiagramSpec extends FlatSpec with Matchers {

  "rose ground" should "have color and some mid markers" in {
    val triedS = Settings("5831 -4-7", "bricks", stitches = "A1=tctc,C1=ttc,A2=ttc,B2=pttc,C2=ctc,D2=ttc",
      absRows = 12, absCols = 11, shiftLeft = 1, shiftUp = 3)
    val d = PairDiagram(triedS)
    d.nodes.size shouldNot be(0)
    d.links.size shouldNot be(0)

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
  "486- -486 6-48 86-4" should "not fail but does" in {

  }
}
