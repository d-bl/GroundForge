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

  val allClothStitches = Array(
    Array("ctc", "ctc", "ctc"),
    Array("ctc", "ctc", "ctc")
  )
  "Settings.toStitchMatrix" should "default to cloth stitches" in {
    Settings.toStitchMatrix("", 2, 3) shouldBe allClothStitches
  }
  it should "apply a custom default and be case insensitive" in {
    Settings.toStitchMatrix("tc a2=ctc B1=TCtc", 2, 3) shouldBe Array(
      Array("tc", "tctc", "tc"),
      Array("ctc", "tc", "tc")
    )
  }
  it should "ignore an invalid default stitch" in {
    Settings.toStitchMatrix("p", 2, 3) shouldBe allClothStitches
  }
  it should "ignore a node id without a stitch" in {
    Settings.toStitchMatrix("A1=", 2, 3) shouldBe allClothStitches
  }
  it should "ignore a stitch with just a pin" in {
    Settings.toStitchMatrix("A1=p", 2, 3) shouldBe allClothStitches
  }
  it should "ignore an invalid stitch" in {
    Settings.toStitchMatrix("A1=.", 2, 3) shouldBe allClothStitches
  }
  it should "ignore a node with the column out of range" in {
    Settings.toStitchMatrix("D1=tc", 2, 3) shouldBe allClothStitches
  }
  it should "ignore a node with the row out of range" in {
    Settings.toStitchMatrix("a22=tc", 2, 3) shouldBe allClothStitches
  }
  it should "ignore an invalid node" in {
    Settings.toStitchMatrix(".=tc", 2, 3) shouldBe allClothStitches
  }
}
