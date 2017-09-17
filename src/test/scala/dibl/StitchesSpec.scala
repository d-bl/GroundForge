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

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class StitchesSpec extends FlatSpec with Matchers with TableDrivenPropertyChecks{

  "the stitch at matrix position A1" should "get a proper color" in {

    val colors = Table("input" -> "color",

      // ordinary cases
      "ctct" -> "red",
      "cltct" -> "brown",
      "ctctct" -> "blue",
      "ct" -> "green",
      "tc" -> "green",
      "clr" -> "", // use t wherever possible
      "clr" -> "",
      "rlc" -> "",

      // default colors overridden by user, any order is accepted
      "A1=B3=ct=red" -> "red",
      "brown=ctctc=A1" -> "brown",
      "ctctc=green=A1" -> "green"
    )
    forAll(colors) { (input: String, color: String) =>
      new Stitches(input).colors(1, 1)
        .headOption.flatMap(_.headOption)
        .getOrElse("no A1 at all") shouldBe color
    }
  }

  // garbage in...
  checkColorAtA1("clrc" -> "") // lr is not recognised as t
  checkStitchAtA1("ctc, tc" -> "ctc") // the first default stitch is applied
  checkColorAtA1("ctc, tc" -> "purple")
  checkStitchAtA1("A1=kd" -> "ctc") // neither a valid color nor a valid stitch
  checkColorAtA1("A1=ctc=orange=ct" -> "purple") // not supported color is ignored, the first stitch is used
  checkColorAtA1("A1=green=A2=ctc=blue" -> "green") // the first color is applied
  checkStitchAtA1("l" -> "ctc") // a stitch should have at least a cross
  checkColorAtA1("l" -> "purple")

  "Stitches.instructions" should "start counting at one" in {
    new Stitches("ct A1=B3=ctc")
      .instructions(3, 2) shouldBe Seq(
      Seq("ctc", "ct"),
      Seq("ct", "ct"),
      Seq("ct", "ctc")
    )
  }

  it should "default to cloth stitches" in {
    new Stitches("")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }

  it should "apply a custom default and be case insensitive" in {
    new Stitches("tc a2=ctc B1=TCtc")
      .instructions(2, 3) shouldBe Seq(
      Seq("tc", "tctc", "tc"),
      Seq("ctc", "tc", "tc")
    )
  }
  it should "ignore an invalid default stitch" in {
    new Stitches("p")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a node id without a stitch" in {
    new Stitches("A1=")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a stitch with just a pin" in {
    new Stitches("A1=p")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore an invalid stitch" in {
    new Stitches("A1=.")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a node with the column out of range" in {
    new Stitches("D1=tc")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore a node with the row out of range" in {
    new Stitches("a22=tc")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }
  it should "ignore an invalid node" in {
    new Stitches(".=tc")
      .instructions(2, 3) shouldBe Seq(
      Seq("ctc", "ctc", "ctc"),
      Seq("ctc", "ctc", "ctc")
    )
  }

  private def checkColorAtA1(both: (String, String)): Unit = {
    val (left, right) = both
    s"stitch field: $left" should s"assign color $right to A1" in {
      colorA1(left) shouldBe right
    }
  }

  private def checkStitchAtA1(both: (String, String)): Unit = {
    val (left, right) = both
    s"stitch field: $left" should s"assign stitch $right to A1" in {
      stitchA1(left) shouldBe right
    }
  }


  it should "B" in {
  it.should("")
     colorA1("ctc") shouldBe "purple"
  }

  private def colorA1(stitchesField: String) = {
    new Stitches(stitchesField).colors(1, 1).head.head
  }

  private def stitchA1(stitchesField: String) = {
    new Stitches(stitchesField).instructions(1, 1).head.head
  }
}

