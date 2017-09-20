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

class StitchesSpec extends FlatSpec with Matchers with TableDrivenPropertyChecks {

  "the stitch at matrix position A1" should "have expected color" in {

    forAll(Table("input" -> "color",

      // ordinary cases
      "" -> "purple", // default is ctc
      "t" -> "",
      "ct" -> "green",
      "tc" -> "green",
      "tct" -> "green",
      "ctc" -> "purple",
      "ctct" -> "red",
      "tctc" -> "red",
      "tctct" -> "red",
      "ttctc" -> "red",
      "ctctt" -> "red",
      "cttc" -> "turquoise",
      "tcttc" -> "turquoise",
      "cttct" -> "turquoise",
      "cltc" -> "brown",
      "crtc" -> "brown",
      "crrc" -> "brown",
      "clllc" -> "brown",
      "ctttc" -> "brown",
      "ctctc" -> "blue",
      "ctctct" -> "blue",
      "tctctc" -> "blue",
      "ctctctc" -> "blue",

      // default colors overridden by user, any order is accepted
      "A1=B3=ct=red" -> "red",
      "brown=ctctc=A1" -> "brown",
      "ctctc=green=A1" -> "green",

      // garbage in is default out
      "clr" -> "", // use t wherever possible
      "crl" -> "",
      "rlc" -> "",
      "lrc" -> "",
      "p" -> "purple", // nothing but a pin becomes ctc
      "ctpctp" -> "purple", // multiple pins becomes ctc
      "ct, ctc" -> "green", // the first solitary stitch is used as default
      "A1=ctct=orange=ct" -> "red", // not supported color is ignored, the first stitch is used
      "A1=green=A2=ctct=blue" -> "green", // the first color is applied
      "A1=kd" -> "purple" // neither a valid color nor a valid stitch

    )) { case (input: String, color: String) =>
      new Stitches(input).colors(1, 1)
        .headOption.flatMap(_.headOption)
        .getOrElse("no A1 at all") shouldBe color
    }
  }

  "the stitch field" should "produce expected matrices" in {

    forAll(Table(("dimensions", "input", "stitches", "colors"),

      ((2, 3), "tc a2=ctc B1=TCtc", Seq( // case insensitive
        Seq("tc", "tctc", "tc"),
        Seq("ctc", "tc", "tc")
      ), Seq(
        Seq("green", "red", "green"),
        Seq("purple", "green", "green")
      )),

      ((2, 3), "a22=tc", Seq( // row out of range is ignored
        Seq("ctc", "ctc", "ctc"),
        Seq("ctc", "ctc", "ctc")
      ), Seq(
        Seq("purple", "purple", "purple"),
        Seq("purple", "purple", "purple")
      )),

      ((2, 3), "AB1=tc", Seq( // column out of range is ignored
        Seq("ctc", "ctc", "ctc"),
        Seq("ctc", "ctc", "ctc")
      ), Seq(
        Seq("purple", "purple", "purple"),
        Seq("purple", "purple", "purple")
      )),

      ((2, 28), "c AB2=ctc", Seq( // more than 26 columns
        Seq("c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c"),
        Seq("c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "c", "ctc")
      ), Seq(
        Seq("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""),
        Seq("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "purple")
      ))

    )) { case ((rows: Int, cols: Int), input: String, stitches: Seq[Seq[String]], colors: Seq[Seq[String]]) =>
      val obj = new Stitches(input)
      obj.instructions(rows, cols) should contain theSameElementsAs stitches
      obj.colors(rows, cols) should contain theSameElementsAs colors
    }
  }
}

