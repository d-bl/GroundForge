package dibl

import dibl.PairDiagram.{ drosteLegend, legend }
import org.scalatest.{ FlatSpec, Matchers }

class PairDiagramSpec extends FlatSpec with Matchers {
  private val patterns = PatternQueries.gwPatterns.toMap
  "legend" should "explain F11" in {
    legend(patterns("gw-F11")) shouldBe
      """red       ctctt (c2)
        |          tctct (j1, a2)
        |blue      ctctctctctt (b1)""".stripMargin.replaceAll("[\r\n]+","\n")
  }
  it should "explain A14b" in {
    legend(patterns("gw-A14b")) shouldBe
      """purple    ctc (g3, h4, f4, g5, j6, d6, k7, g7, c7, j8, d8, g9, h10, f10, g11)
        |red       ctctt (a1, h2, f2, i3, e3, k5, c5, l7, a7, l8)
        |          ttctctt (g1, b6, b7, k9, c9, i11, e11, h12, f12)
        |blue      ctctctctctctt (l2, b2, l12, b12)""".stripMargin.replaceAll("[\r\n]+","\n")
  }
  it should "explain H5" in {
    legend(patterns("gw-H5")) shouldBe
      """purple    ctcl (a2)
        |          ctcr (a1)""".stripMargin.replaceAll("[\r\n]+","\n")
  }
  it should "do a custom pattern" in {
    legend("patchWidth=7&patchHeight=7&a1=ct&b2=ct&tile=5-,-5&footsideStitch=ctctt&tileStitch=ct&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2") shouldBe
      """green     ct (a1, b2)""".stripMargin.replaceAll("[\r\n]+","\n")
  }
  it should "do a custom pattern with black" in {
    legend("patchWidth=7&patchHeight=7&a1=ctc&b2=cct&tile=5-,-5&footsideStitch=ctctt&tileStitch=ctct&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2") shouldBe
      """purple    ctc (a1)
        |black     cct (b2)""".stripMargin.replaceAll("[\r\n]+","\n")
  }
  "drosteLegend" should "not skip the overall default" in {
    drosteLegend(
      """cttc
        |twist=ct
        |cross=ctc
        |a1=b2=ctctc"""
        .stripMargin) shouldBe
      """purple    ctc (cross)
        |turquoise cttc (default)
        |green     ct (twist)
        |blue      ctctc (a1, b2)""".stripMargin.replaceAll("[\r\n]+","\n")
  }
}
