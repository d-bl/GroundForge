package dibl

import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.io.File


class NewPairDiagramSpec extends FlatSpec with Matchers {

  def paris4(width: Int, height: Int) = new Config(
    s"patchWidth=$width&patchHeight=$height" +
      s"&shiftColsSE=3&shiftRowsSE=3&shiftColsSW=-3&shiftRowsSW=3" +
      s"&tile=5-O-E-,-E-5-O,5-O-E-&d2=tctct&c1=ctcr&e1=ctcl&b2=rctc&f2=lctc"
  )

  "create" should "not throw" in {
    NewPairDiagram.create(paris4(9,9)) shouldBe a[Diagram]
    NewPairDiagram.create(paris4(12,12)) shouldBe a[Diagram]
    NewPairDiagram.create(paris4(12,9)) shouldBe a[Diagram]
  }

  it should "return something that can be rendered" in {
    val svgString = D3jsSVG.render(NewPairDiagram.create(paris4(9,9)))
    svgString should include ("marker-end: url('#end-purple')")
    svgString should include ("marker-start: url('#start-purple')")
    svgString should include ("marker-end: url('#end-red')")
    svgString should include ("marker-start: url('#start-red')")
    svgString should include ("marker-mid: url('#twist-1')")
  }

  it should "produce valid SVG" in {
    val content = D3jsSVG.render(NewPairDiagram.create(paris4(9,9)))
    File("target/new-diagram.html").writeAll(s"<html><body>$content</body></html>")
  }
}
