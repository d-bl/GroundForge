/*
 Copyright 2016 Jo Pol
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

class StitchesSpec extends FlatSpec with Matchers {

  val allClothStitches = Seq(
    Seq("ctc", "ctc", "ctc"),
    Seq("ctc", "ctc", "ctc")
  )
  "Stitches.toMatrix" should "default to cloth stitches" in {
    new Stitches("").instructions(2, 3) shouldBe allClothStitches
  }
  it should "apply a custom default and be case insensitive" in {
    new Stitches("tc a2=ctc B1=TCtc").instructions(2, 3) shouldBe Seq(
      Seq("tc", "tctc", "tc"),
      Seq("ctc", "tc", "tc")
    )
  }
  it should "ignore an invalid default stitch" in {
    new Stitches("p").instructions(2, 3) shouldBe allClothStitches
  }
  it should "ignore a node id without a stitch" in {
    new Stitches("A1=").instructions(2, 3) shouldBe allClothStitches
  }
  it should "ignore a stitch with just a pin" in {
    new Stitches("A1=p").instructions(2, 3) shouldBe allClothStitches
  }
  it should "ignore an invalid stitch" in {
    new Stitches("A1=.").instructions(2, 3) shouldBe allClothStitches
  }
  it should "ignore a node with the column out of range" in {
    new Stitches("D1=tc").instructions(2, 3) shouldBe allClothStitches
  }
  it should "ignore a node with the row out of range" in {
    new Stitches("a22=tc").instructions(2, 3) shouldBe allClothStitches
  }
  it should "ignore an invalid node" in {
    new Stitches(".=tc").instructions(2, 3) shouldBe allClothStitches
  }

  it should "start counting at one" in {
    new Stitches("ct A1=B3=ctc").instructions(3,2) shouldBe
    Seq(
      Seq("ctc","ct"),
      Seq("ct","ct"),
      Seq("ct","ctc")
    )
  }
}
