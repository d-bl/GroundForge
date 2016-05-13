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

import java.io.File

import dibl.Matrix._
import dibl.Pattern.doc
import org.apache.commons.io.FileUtils
import org.scalatest.{FlatSpec, Matchers}

class PatternSpec extends FlatSpec {
  "get" should "succeed" in {
    matrixMap.keys.foreach{ key =>
      for (nr <- matrixMap.get(key).get.indices){
        val m = matrixMap.get(key).map(_ (nr)).get
        val (height,  width) = dims(key).get
        val s = new Pattern(m, true, height,  width).patch
        FileUtils.write(new File(s"target/patterns/${key}_$nr.svg"), doc(s))
      }
    }
  }

  "pinwheel brick" should "succeed" in {
    val fileName = s"target/patterns/brick.svg"
    val s = new Pattern("586--4-55-21-5-7", isBrick = true, height = 4, width = 4).patch
    FileUtils.write(new File(fileName), doc(s))
  }

  "checker" should "succeed" in {
    val fileName = s"target/patterns/checker.svg"
    val s = new Pattern("4831-1175-7-86-5", isBrick = false, height = 4, width = 4).patch
    FileUtils.write(new File(fileName), doc(s))
  }

  "multiple patches" should "succeed" in {
    val fileName = s"target/patterns/multi.svg"
    val s =
      new Pattern("586--4-55-21-5-7", true, 4, 4, "g1", 80, 120).patch +
      new Pattern("4831-1175-7-86-5", false, 4, 4, "g2", 420, 450).patch +
      new Pattern("48322483", true, 2, 4, "g3", 80, 780).patch
    FileUtils.write(new File(fileName), doc(s))
  }
}
