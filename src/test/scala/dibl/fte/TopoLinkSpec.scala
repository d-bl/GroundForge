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
package dibl.fte

import org.scalatest.{ FlatSpec, Matchers }

class TopoLinkSpec extends FlatSpec with Matchers {

  "changeWeight" should "multiply the value" in {
    TopoLink.changeWeight("d4-a1", 0.9, "lo,b4,ri,a1,1.0;lo,d4,li,a1,0.9") shouldBe
      "lo,b4,ri,a1,1.0;lo,d4,li,a1,0.81"
  }

  "removeStitch" should "..." in pendingUntilFixed { // pinwheel
    TopoLink
      .removeStitch("d4", "lo,b4,ri,a1;lo,d4,li,a1;lo,b2,ri,a3;lo,d2,li,a3;lo,a1,li,b1;ro,b4,ri,b1;lo,b1,li,b2;lo,c1,ri,b2;lo,a3,li,b4;lo,c3,ri,b4;ro,b1,li,c1;ro,d4,ri,c1;ro,b2,li,c3;lo,d3,ri,c3;ro,a1,ri,d2;ro,c1,li,d2;ro,a3,ri,d3;ro,d2,li,d3;ro,c3,li,d4;ro,d3,ri,d4")
      .mkString(";") shouldBe
      "ro,d3,li,a1,1.0;ro,c3,ri,c1,1.0;lo,b4,ri,a1,1.0;lo,b2,ri,a3,1.0;lo,d2,li,a3,1.0;lo,a1,li,b1,1.0;ro,b4,ri,b1,1.0;lo,b1,li,b2,1.0;lo,c1,ri,b2,1.0;lo,a3,li,b4,1.0;lo,c3,ri,b4,1.0;ro,b1,li,c1,1.0;ro,b2,li,c3,1.0;lo,d3,ri,c3,1.0;ro,a1,ri,d2,1.0;ro,c1,li,d2,1.0;ro,a3,ri,d3,1.0;ro,d2,li,d3,1.0"
  }

  "addStitch" should "..." in pendingUntilFixed { // pinwheel
    TopoLink.addStitch("ro,d4,ri,c1;ro,a1,ri,d2", "lo,b4,ri,a1;lo,d4,li,a1;lo,b2,ri,a3;lo,d2,li,a3;lo,a1,li,b1;ro,b4,ri,b1;lo,b1,li,b2;lo,c1,ri,b2;lo,a3,li,b4;lo,c3,ri,b4;ro,b1,li,c1;ro,d4,ri,c1;ro,b2,li,c3;lo,d3,ri,c3;ro,a1,ri,d2;ro,c1,li,d2;ro,a3,ri,d3;ro,d2,li,d3;ro,c3,li,d4;ro,d3,ri,d4") shouldBe
      "lo,b4,ri,a1,1.0;lo,d4,li,a1,1.0;lo,b2,ri,a3,1.0;lo,d2,li,a3,1.0;lo,a1,li,b1,1.0;ro,b4,ri,b1,1.0;lo,b1,li,b2,1.0;lo,c1,ri,b2,1.0;lo,a3,li,b4,1.0;lo,c3,ri,b4,1.0;ro,b1,li,c1,1.0;ro,b2,li,c3,1.0;lo,d3,ri,c3,1.0;ro,c1,li,d2,1.0;ro,a3,ri,d3,1.0;ro,d2,li,d3,1.0;ro,c3,li,d4,1.0;ro,d3,ri,d4,1.0;lo,d4,li,x,1.0;lo,a1,ri,x,1.0;lo,x,li,c1,1.0;lo,x,ri,d2,1.0"
  }
}
