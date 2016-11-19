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
      absRows = 8, absCols = 7, shiftLeft = 1, shiftUp = 3)
    val d = PairDiagram(triedS)
    d.nodes.size shouldNot be(0)
    d.links.size shouldNot be(0)
    val nonTransparent = d.links.filter(_.getOrElse("border",false) == false)
    nonTransparent.count(_ ("end") == "green") shouldNot be(0)
    nonTransparent.count(_ ("start") == "green") shouldNot be(0)
    nonTransparent.count(_ ("end") == "red") shouldNot be(0)
    nonTransparent.count(_ ("start") == "red") shouldNot be(0)
    nonTransparent.count(_ ("end") == "purple") shouldNot be(0)
    nonTransparent.count(_ ("start") == "purple") shouldNot be(0)
    nonTransparent.count(_ ("mid") == 0) shouldNot be(0)
    nonTransparent.count(_ ("mid") == 1) shouldNot be(0)
  }
}
