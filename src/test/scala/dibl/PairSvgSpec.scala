package dibl

import dibl.DiagramSvg.prolog
import dibl.proto.TilesConfig
import org.scalatest.{ FlatSpec, Matchers }

import scala.reflect.io.File

class PairSvgSpec  extends FlatSpec with Matchers {
  "render" should "show trailing twists" in {
    val q = "patchWidth=8&patchHeight=14&b1=cr&c1=ctllcrrc&d1=clclc&b2=ctctctctctctt&d2=ctctctctclcttl&c3=cllclll&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
    val config = TilesConfig(q)
    val str = PairSvg.render(config.getItemMatrix, 400, 500)
    new java.io.File("target/test/PairSvg").mkdirs()
    File(s"target/test/PairSvg/trailing.svg")
      .writeAll(prolog + str)
        str shouldBe a[String]
  }
  it should "show leading twists" in {
    val q = "patchWidth=8&patchHeight=14&b1=tc&c1=ltc&d1=rtc&b2=ttc&d2=lttc&c3=rtc&footside=b,-,a,-&footsideStitch=-&tile=831,4-7,-5-&tileStitch=ctct&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
    val config = TilesConfig(q)
    val str = PairSvg.render(config.getItemMatrix, 400, 500)
    new java.io.File("target/test/PairSvg").mkdirs()
    File(s"target/test/PairSvg/leading.svg")
      .writeAll(prolog + str)
        str shouldBe a[String]
  }
}
