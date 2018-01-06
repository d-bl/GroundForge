package dibl

import org.scalatest.{ FlatSpec, Matchers }

class ConfigSpec extends FlatSpec with Matchers {

  "empty query" should "return emtpty dashed matrix" in {
    new Config("").totalMatrix.map(_.mkString).mkString("\n")+"\n" shouldBe
      ("----------------\n" * 12)
  }

  "custom checker braid" should "show 4 rows" in {
    val specs = new Config("footside=-7,A1&tile=8,1&headside=D,-" +
      "&repeatHeight=4&repeatWidth=3" +
      "&shiftColsSE=1&shiftRowsSE=2&shiftColsSW=0&shiftRowsSW=2")
    specs.totalMatrix.map(_.mkString).mkString("\n").toLowerCase shouldBe
      """---7888d--
        |--a1111---
        |---7888d--
        |--a1111---""".stripMargin
  }

  "custom checker braid" should "show 12 rows" in {
    val specs = new Config("footside=-7,A1&tile=8,1,8,1&headside=8D,4-" +
      "&repeatWidth=3&repeatHeight=12" +
      "&shiftColsSE=10&shiftRowsSE=4&shiftColsSW=0&shiftRowsSW=4&")
    specs.totalMatrix.map(_.mkString).mkString("\n").toLowerCase shouldBe
      """---7888d--
        |--a1111---
        |---7888d--
        |--a1111---""".stripMargin
  }
}
