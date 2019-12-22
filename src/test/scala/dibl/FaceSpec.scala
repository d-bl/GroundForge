package dibl

import dibl.Face.facesFrom
import dibl.proto.TilesConfig
import fte.data.GraphCreator.inCenterBottomTile
import org.scalatest.{FlatSpec, Matchers}

class FaceSpec extends FlatSpec with Matchers {
  "facesFrom" should "create proper thread link sequences" in {
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
  it should "create proper pair link sequences for brick pattern" in {
    val s = "rose&tileStitch=ctc&patchWidth=12&patchHeight=4&tile=5831,-4-7,&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2"
    implicit val config: TilesConfig = TilesConfig(s)
    implicit val diagram: Diagram = NewPairDiagram.create(config)
    implicit val scale: Int = 1
    val links = diagram.links.filter(inCenterBottomTile)
    facesFrom(links).mkString("\n") shouldBe
      """b1->b2 ; b1->c1,c1->b2
        |d2->a1,a1->b1 ; d2->b1
        |b1->c1 ; b1->b2,b2->d1,d1->c1
        |d1->c1,c1->d2 ; d1->d2
        |c1->b2,b2->a1 ; c1->d2,d2->a1
        |b2->d1 ; b2->a1,a1->d1""".stripMargin
  }
}
