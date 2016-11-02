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

import java.io.{File, FileOutputStream}

import org.scalatest.{FlatSpec, Matchers}

class PatternDemos extends FlatSpec with Matchers {

  new File("target/test/").mkdirs()
  def write(file: File, content: String) ={
    val fos = new FileOutputStream(file)
    try {
      fos.write(content.getBytes("UTF8"))
    } finally {
      fos.close()
    }
  }

  "rose" should "succeed" in {
    val patterns = new PatternSheet
    patterns.add("5831 -4-7", "bricks")
    patterns.add("-437 34-7", "bricks")
    patterns.add("5831 -4-7 3158 -7-4", "checker")
    patterns.add("4830 --77", "bricks")
    write(new File("target/test/rose.svg"), patterns.toSvgDoc())
  }

  "pattern sheet" should "succeed" in {
    val patterns = new PatternSheet
    patterns.add("B-5-C- -5-5-- B---C-", "bricks") // double length vertical rows
    patterns.add("-5---5-5 5-O-E-5-", "bricks") // double length horizontal lines

    patterns.add("586- -4-5 5-21 -5-7", "bricks")
    patterns.add("4831 -117 5-7- 86-5", "checker")

    patterns.add("588- -4-5 6-58 -214", "checker")
    patterns.add("5831 -4-7" ,"bricks")

    patterns.add("4832 2483", "bricks")
    patterns.add("5---5-5- -O-E-5-5", "bricks") // double length horizontal lines

    patterns.add("586- -4-5 5-21 -5-777", "checker") // reports an error
    write(new File("target/test/pattern-sheet.svg"), patterns.toSvgDoc())
  }

  it should "not mix up dimensions" in {
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
    write(new File(s"target/test/double-length.svg"), svgString)
    val links = svgString.split("\n").filter(_.contains("href='http"))
    links.length shouldBe 1
  }

  "minimal" should "succeed" in {
    val patterns = PatternSheet(1, "width='340' height='330'")
    patterns.add("586- -4-5 5-21 -5-7","bricks")
    write(new File("target/test/minimal.svg"), patterns.toSvgDoc())
  }
}
