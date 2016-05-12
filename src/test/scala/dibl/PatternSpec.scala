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

import dibl.Matrix.{matrixMap, toRelSrcNodes}
import org.apache.commons.io.FileUtils
import org.scalatest.{FlatSpec, Matchers}

class PatternSpec extends FlatSpec {
  "get" should "succeed" in {
    matrixMap.keys.foreach{ key =>
      for (i <- matrixMap.get(key).get.indices){
        val fileName = s"target/patterns/${key}_$i.svg"
        FileUtils.write(new File(fileName), Pattern.get(key, i))
      }
    }
  }
  "pinwheel brick" should "succeed" in {
    val fileName = s"target/patterns/brick.svg"
    val doc = Pattern.createDoc("586--4-55-21-5-7")(isBrick = true, (4, 4))
    FileUtils.write(new File(fileName), doc)
  }
  "checker" should "succeed" in {
    val fileName = s"target/patterns/checker.svg"
    val doc = Pattern.createDoc("4831-1175-7-86-5")(isBrick = false, (4, 4))
    FileUtils.write(new File(fileName), doc)
  }
}
