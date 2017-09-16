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

  checkColorAtA1("ctct" ->"red")
  checkColorAtA1("cltct" -> "brown")
  checkColorAtA1("ctctct" -> "blue")
  checkColorAtA1("ct" -> "green")
  checkColorAtA1("tc" -> "green")
  checkColorAtA1("clr" -> "")
  checkColorAtA1("rlc" -> "")

  // default colors overridden by user, any order is accepted
  checkColorAtA1("A1=B3=ct=red" -> "red")
  checkColorAtA1("brown=ctctc=A1" -> "brown")
  checkColorAtA1("ctctc=green=A1" -> "green")

  // garbage in...
  checkStitchAtA1("ctc tc" ->"ctctc")// multiple default stitches get concatenated
  checkStitchAtA1("A1=ctc=rood" ->"ctcr")// valid characters of a not supported color merges with the stitch
  checkStitchAtA1("l" -> "ctc")
  checkColorAtA1("l" -> "purple")

  "Stitches.instructions" should "start counting at one" in {
    new Stitches("ct A1=B3=ctc")
      .instructions(3,2) shouldBe
      Seq(
        Seq("ctc","ct"),
        Seq("ct","ct"),
        Seq("ct","ctc")
      )
  }

  it should "default to cloth stitches" in {
    new Stitches("").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }

  it should "apply a custom default and be case insensitive" in {
    new Stitches("tc a2=ctc B1=TCtc").instructions(2, 3) shouldBe Seq(
      Seq("tc", "tctc", "tc"),
      Seq("ctc", "tc", "tc")
    )
  }
  it should "ignore an invalid default stitch" in {
    new Stitches("p").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a node id without a stitch" in {
    new Stitches("A1=").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a stitch with just a pin" in {
    new Stitches("A1=p").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore an invalid stitch" in {
    new Stitches("A1=.").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a node with the column out of range" in {
    new Stitches("D1=tc").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a node with the row out of range" in {
    new Stitches("a22=tc").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore an invalid node" in {
    new Stitches(".=tc").instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }

   private def checkColorAtA1( both: (String, String)): Unit = {
    val (left, right) = both
    s"stitch field: $left" should s"assign color $right to A1" in {
      new Stitches(left).colors(1, 1).head.head shouldBe right
    }
  }
  private def checkStitchAtA1( both: (String, String)): Unit = {
    val (left, right) = both
    s"stitch field: $left" should s"assign stitch $right to A1" in {
      new Stitches(left).instructions(1, 1).head.head shouldBe right
    }
  }
}

