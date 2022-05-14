package dibl

import org.scalatest.{ FlatSpec, Matchers }
import dibl.proto.TilesConfig

import java.io.FileOutputStream
import scala.reflect.io.File

class LegendSpec extends FlatSpec with Matchers {
  "" should "" in {

    val s = "patchWidth=20&patchHeight=20&g1=tc&a1=ctctctcttt&l2=crcrcrclll&k2=ctctc&j2=cttcttc&i2=ctttctttc&h2=crcrc&g2=cttc&f2=clclc&e2=ctttctttc&d2=cttcttc&c2=ctctc&b2=clclclcrrr&l3=ctc&k3=ctc&j3=clllc&i3=crrcrrc&h3=clcrc&g3=cttcttc&f3=crclc&e3=cllcllc&d3=crrrc&c3=ctc&b3=ctc&a3=cc&l4=ctc&k4=cllc&j4=crrrcrrrc&i4=cllcrc&h4=cllcrrc&g4=ctttc&f4=crrcllc&e4=crrclc&d4=clllclllc&c4=crrc&b4=ctc&a4=ccc&l5=clc&k5=rctct&j5=clllcrc&i5=clllcrrc&h5=ctc&g5=ctttctttc&f5=ctc&e5=crrrcllc&d5=crrrclc&c5=lctct&b5=crc&a5=ctc&l6=rctct&k6=ctc&j6=ctc&i6=clcrclc&h6=c&f6=c&e6=clclcrc&d6=ctc&c6=ctc&b6=lctct&a6=cc&tile=5-----5-----,-CDDD632AAAB,566666322222,566666322222,566666322222,566666-22222&shiftColsSW=-6&shiftRowsSW=6&shiftColsSE=6&shiftRowsSE=6"
    val str = PairSvg.legend(TilesConfig(s))
    new FileOutputStream("src/test/resources/tmp.svg").write(str.getBytes)
    str shouldBe ""
  }
}
