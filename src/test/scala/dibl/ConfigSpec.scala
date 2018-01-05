package dibl

import org.scalatest.{ FlatSpec, Matchers }

class ConfigSpec extends FlatSpec with Matchers {

  "empty query" should "return emtpty dashed matrix" in {
    new Config("").totalMatrix.map(_.mkString).mkString("\n")+"\n" shouldBe
      ("------------\n" * 12)
  }

  "custom checker braid" should "" in {
    val specs = new Config("headside=-7,A1&tile=8,1&footside=D,-&repeatHeight=4&repeatWidth=3")
    specs.totalMatrix.map(_.mkString).mkString("\n") shouldBe
      """---7888d----------
        |--a1111-----------
        |---7888d----------
        |--a1111-----------""".stripMargin
  }
}
