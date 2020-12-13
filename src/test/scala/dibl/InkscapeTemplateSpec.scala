package dibl

import org.scalatest.{ FlatSpec, Matchers }

class InkscapeTemplateSpec extends FlatSpec with Matchers {
  "fromUrl" should "render rose bias" in {
    InkscapeTemplate.fromUrl("patchWidth=12&patchHeight=8&tile=1483,8-48,8314,488-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4") shouldBe
      """CHECKER	4	4
        |[7.0,4.0,6.0,5.0,7.0,5.0]
        |[4.0,5.0,3.0,6.0,4.0,6.0]
        |[4.0,6.0,5.0,6.0,4.0,7.0]
        |[4.0,7.0,5.0,7.0,4.0,8.0]
        |[6.0,5.0,7.0,5.0,6.0,6.0]
        |[6.0,4.0,7.0,4.0,6.0,5.0]
        |[7.0,6.0,6.0,6.0,8.0,6.0]
        |[7.0,5.0,8.0,5.0,7.0,6.0]
        |[6.0,7.0,5.0,8.0,6.0,8.0]
        |[4.0,4.0,3.0,4.0,4.0,5.0]
        |[6.0,6.0,5.0,6.0,6.0,7.0]
        |[5.0,4.0,4.0,4.0,6.0,4.0]
        |[5.0,7.0,6.0,7.0,5.0,8.0]
        |[5.0,6.0,4.0,7.0,5.0,7.0]""".stripMargin
  }
  it should "render torchon" in {
    InkscapeTemplate.fromUrl("patchWidth=11&patchHeight=11&tile=5-,-5&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2") shouldBe
      """Checker\t2\t
        |???""".stripMargin
  }
}