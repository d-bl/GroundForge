/*
 hexColor2016 Jo Pol
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

import dibl.Matrix.toValidMatrixLines
import org.scalatest.{FlatSpec, Matchers}

class FringesDemos extends FlatSpec with Matchers {

  new File("target/test/").mkdirs()

  def write(file: File, content: String) = {
    val fos = new FileOutputStream(file)
    try {
      fos.write(content.getBytes("UTF8"))
    } finally {
      fos.close()
    }
  }

  "pattern sheet" should "succeed" in {

    val lines = toValidMatrixLines("588- -4-5 6-58 -214").get
    val longerLines: Array[String] = lines.map(s => (s * 6).take(26))
    val moreLines: String = longerLines.mkString("", " ", " ") * 5
    val svgDoc = PatternSheet().add(moreLines, "checker").toSvgDoc()

    val fringes = new Fringes(Settings(lines.mkString(" "), "checker", 20, 25).get.absM)
    val newPairs =
      showFringes(fringes.newPairs, "#F00") +
      showFringes(fringes.reusedLeft, "#0F0") +
      showFringes(fringes.reusedRight, "#0F0")

    write(new File("target/test/fringes.svg"), svgDoc.replace("</svg>",newPairs+"</svg>"))
    svgDoc should include("<g id ='GFP0'>")
  }

  def showFringes(links: Seq[((Int,Int),(Int,Int))], color: String): String = {
    (for {((sourceRow, sourceCol), (targetRow, targetCol)) <- links
    } yield
      s"""    <path
          |      style='stroke:$color;fill:none'
          |      d='M ${15 + sourceCol * 10},${85 + sourceRow * 10} ${25 + targetCol * 10},${85 + targetRow * 10}'
          |    />
          |""".stripMargin
      ).mkString
  }
}
