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

class PatternSpec extends FlatSpec with Matchers {

  "patterns" should "not mix up dimensions" in {
    val patterns = new PatternSheet
    patterns.add("88 11", "bricks")
    patterns.add("66,99+22\n00", "bricks")
    val svgString = patterns.toSvgDoc()
    val links = svgString.split("\n").filter(_.contains("href='http")).mkString("\n")
    links should include ("?matrix=88%0D11&amp;tiles=bricks'")
    links should include ("?matrix=66%0D99%0D22%0D00&amp;tiles=bricks'")
  }

  it should "produce a single patch" in {
    val patterns = new PatternSheet
    patterns.add("88 11", "bricks")
    val svgString = patterns.toSvgDoc()
    val links = svgString.split("\n").filter(_.contains("href='http"))
    links.length shouldBe 1
  }

  it should "process double length horizontal lines" in {
    val patterns = new PatternSheet
    patterns.add("-5---5-5 5-O-E-5-", "bricks")
    val svgString = patterns.toSvgDoc()
    val links = svgString.split("\n").filter(_.contains("href='http"))
    links.length shouldBe 1
  }
}
