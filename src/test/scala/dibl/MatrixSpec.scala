/*
 Copyright 2015 Jo Pol
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

import dibl.Matrix._
import org.scalatest._

import scala.reflect.ClassTag

/** checks for typos in HashMap-s and more */
class MatrixSpec extends FlatSpec with Matchers {

  "matrices" should "repeat without internal loose ends" in {
//
    val errors = new StringBuffer()
    for (i <- Matrices.values.indices) {
        val args: Array[String] = Matrices.values(i).split(";")
        val matrix = args(0)
        val tileType = if (args.length > 1) args(1) else ""
        val m = Settings(matrix, tileType, absRows = 40, absCols = 12, shiftLeft = 1, shiftUp = 1).get.absM
        val nrOfLinks = countLinks(m)
        // some rough sifting of matrices with a different a different range for internal
        val topMargin =
          if (tileType=="bricks" && Set("25 66 -4 21 88", "27 88 11", "28 66 88 66 11", "179 66 -4 5- 86" ).contains(matrix)) 1
          else 2
        val bottomMargin =
          if (Matrices.values(i).endsWith(" -4-4 5--- -C-B;bricks")
            || Matrices.values(i).endsWith(" -4-5 5--- -C-B;bricks")
          ) 4
          else 3
        val footsideMargin =
          if (matrix.matches(".*[HE].*")) 4
          else if (matrix.matches(".*[-48].*")) 3
          else 0
        // check the nodes have four or zero links but skip the margins
        for {
          row <- topMargin until nrOfLinks.length - bottomMargin
          col <- footsideMargin until nrOfLinks(0).length - footsideMargin
        } if (!Set(0,4).contains(nrOfLinks(row)(col)))
          errors.append (s"$i: [${Matrices.values(i)}] has ${nrOfLinks(row)(col)} links at ($row,$col)\n")
    }
    errors.toString shouldBe ""
  }

  "separator" should "match anything but Matrix.relSourcesMap.keySet" in {
    val allChars = for {char <- Char.MinValue until Char.MaxValue} yield char
    val nonSeparatorChars = allChars.toString().split(Matrix.separator).mkString("")
    nonSeparatorChars.length shouldBe Matrix.charToRelativeTuples.keySet.size
    // As long as building (with sbt) and testing (with maven) is not integrated,
    // we might consider to add this check as an assert to the matrix class
    // but that would reduce performance of the JavaScript.
  }

  "shift" should "succeed" in {

    val m1 = Array("abcd", "ABCD")
    val m2 = Array("bcda", "BCDA")
    val m3 = Array("BCDA", "bcda")
    val m4 = Array("ABCD", "abcd")
    val mA: M = Array(
      Array(Array((11, 12)), Array((13, 14)), Array((15, 16)), Array((17, 18))),
      Array(Array((21, 22)), Array((23, 24)), Array((25, 26)), Array((27, 28)))
    )
    val mB: M = Array(
      Array(Array((23, 24)), Array((25, 26)), Array((27, 28)), Array((21, 22))),
      Array(Array((13, 14)), Array((15, 16)), Array((17, 18)), Array((11, 12)))
    )

    def myShift(xs: Array[String], left: Int, up: Int): Array[String] =
      shift(xs, up).map(shiftChars(_, left))

    def myShift2[T: ClassTag](xs: Array[Array[T]], left: Int, up: Int): Array[Array[T]] =
      shift(xs, up).map(shift(_, left))

    myShift(m1, left = 1, up = 0) shouldBe m2
    myShift(m1, left = 1, up = 1) shouldBe m3
    myShift(m1, left = 0, up = 1) shouldBe m4
    myShift2(mA, left = 1, up = 1) shouldBe mB
  }
}