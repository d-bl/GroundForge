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
import org.apache.commons.io.FileUtils
import org.scalatest.{FlatSpec, Matchers}

class PatternSpec extends FlatSpec with Matchers {
  "get" should "succeed" in {
    matrixMap.keys.foreach{ key =>
      for (nr <- matrixMap.get(key).get.indices){
        val m = matrixMap.get(key).map(_ (nr)).get
        val (height,  width) = dims(key).get
        val s = new PatternSheet().add(m, isBrick = true, rows = height,  cols = width).toSvgDoc()
        FileUtils.write(new File(s"target/patterns/${key}_$nr.svg"), s)
        s should include ("#008")
      }
    }
  }

  "rose" should "succeed" in {
    val patterns = new PatternSheet
    patterns.add("5831-4-7", isBrick = true, rows = 2, cols = 4)
    patterns.add("-43734-7", isBrick = true, rows = 2, cols = 4)
    patterns.add("5831-4-73158-7-4", isBrick = false, rows = 4, cols = 4)
    patterns.add("4830--77", isBrick = true, rows = 2, cols = 4)
    FileUtils.write(new File(s"target/patterns/rose.svg"), patterns.toSvgDoc())
  }

  "pattern sheet" should "succeed" in {
    val patterns = new PatternSheet
    patterns.add("586--4-55-21-5-7", isBrick = true, rows = 4, cols = 4)
    patterns.add("586--4-55-21-5-7", isBrick = true, rows = 4, cols = 4)
    patterns.add("4831-1175-7-86-5", isBrick = false, rows = 4, cols = 4)
    patterns.add("48322483", isBrick = true, rows = 2, cols = 4)
    patterns.add("588--4-56-58-214", isBrick = false, rows = 4, cols = 4)
    FileUtils.write(new File(s"target/patterns/pattern-sheet.svg"), patterns.toSvgDoc())
  }

  "minimal" should "succeed" in {
    val patterns = new PatternSheet(1, "width='340' height='330'")
    patterns.add("586--4-55-21-5-7", isBrick = true, rows = 4, cols = 4)
    FileUtils.write(new File(s"target/patterns/minimal.svg"), patterns.toSvgDoc())
  }
}
