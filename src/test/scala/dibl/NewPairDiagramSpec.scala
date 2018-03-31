package dibl

import org.scalatest.{FlatSpec, Matchers}

class NewPairDiagramSpec  extends FlatSpec with Matchers {

  val urlQuery = "repeatWidth=9&repeatHeight=9&patchCols=0&patchRows=2&shiftColsSE=3&shiftRowsSE=3&shiftColsSW=-3&shiftRowsSW=3&&tile=5-O-E-,-E-5-O,5-O-E-&"
  val config = new Config(urlQuery)

  "create" should "not throw" in {
    NewPairDiagram.create(config) shouldBe a[Diagram]
  }

  it should "return something that can be rendered" in {
    D3jsSVG.render(NewPairDiagram.create(config)) shouldBe a[String] // TODO should be valid HTML(alias embedded SVG)
  }

  it should "result in valid SVG" ignore {
    D3jsSVG.render(NewPairDiagram.create(config)) shouldBe ""
  }
}
