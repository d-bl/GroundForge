package dibl

import dibl.Face.facesFrom
import dibl.proto.TilesConfig
import fte.data.GraphCreator.inCenterBottomTile
import org.scalatest.{FlatSpec, Matchers}

class FaceSpec extends FlatSpec with Matchers {
  "facesFrom" should "create proper link sequences" in {
    val s = "torchon&tileStitch=ct&patchWidth=6&patchHeight=4&tile=5-,-5&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
    implicit val config: TilesConfig = TilesConfig(s)
    implicit val diagram: Diagram = ThreadDiagram(NewPairDiagram.create(config))
    implicit val scale: Int = 2
    val links = diagram.links.filter(inCenterBottomTile)
    facesFrom(links).mkString("\n") shouldBe
      """b20->b21,b21->a12,a12->b20 ; b20->b22,b22->a11,a11->b20
        |a12->b21 ; a12->b20,b20->b21
        |a11->b20,b20->b22 ; a11->b22
        |b22->a11 ; b22->a10,a10->a11
        |a10->a11,a11->b22,b22->a10 ; a10->a12,a12->b21,b21->a10
        |b21->a10,a10->a12 ; b21->a12""".stripMargin
  }
}
