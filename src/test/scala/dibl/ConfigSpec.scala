package dibl

import org.scalatest.{ FlatSpec, Matchers }

class ConfigSpec extends FlatSpec with Matchers {

  "empty string" should "return emtpty dashed matrix" in {
    new Config("").encodedMatrix + "," shouldBe
      ("----------------," * 12)
  }

  "valid matrix" should "produce a valid pair diagram and different stitches" in {
    val config = new Config(
      "footside=-7,A1" +
        "&tile=8,1,8,1" +
        "&headside=8D,4-" +
        "&repeatWidth=3&repeatHeight=6" +
        "&shiftColsSE=1&shiftRowsSE=4&shiftColsSW=0&shiftRowsSW=4" +
        "&c2=tctct&i1=ctct&e3=ct"
    )
    val stitches = config.itemMatrix.flatten.map(_.stitch)
    stitches.count(_ == "tctct") shouldBe 3
    stitches.count(_ == "ctct") shouldBe 3
    stitches.count(_ == "ct") shouldBe 3 // TODO would expect 9, like the real thing

    config.encodedMatrix shouldBe
      """---78888D--
        |--A11114---
        |---78888D--
        |--A11114---
        |---78888D--
        |--A11114---""".stripMargin.replaceAll("[\r\n]+",",")
    val d = PairDiagram.get(config.encodedMatrix, "checker", config.totalRows, config.totalCols, 0, 2, "")
    d.nodes.size shouldBe 44 // anything more than 1 is OK
  }
}
