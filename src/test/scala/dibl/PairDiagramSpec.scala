package dibl

import dibl.PairDiagram.legend
import org.scalatest.{ FlatSpec, Matchers }

class PairDiagramSpec extends FlatSpec with Matchers {
  private val patterns = PatternQueries.gwPatterns.toMap
  "legend" should "explain F11" in {
    legend(patterns("gw-F11")) shouldBe
      """red        ctctt (c2, tileStitch)
        |           tctct (j1, a2, footsideStitch, headsideStitch)
        |blue       ctctctctctt (b1)""".stripMargin
  }
  it should "explain A14b" in {
    legend(patterns("gw-A14b")) shouldBe
      """purple     ctc (g3, h4, f4, g5, j6, d6, k7, g7, c7, j8, d8, g9, h10, f10, g11)
        |red        ctct (tileStitch)
        |           ctctt (a1, h2, f2, i3, e3, k5, c5, l7, a7, l8)
        |           ttctctt (g1, b6, b7, k9, c9, i11, e11, h12, f12)
        |blue       ctctctctctctt (l2, b2, l12, b12)""".stripMargin
  }
  it should "explain H5" in {
    legend(patterns("gw-H5")) shouldBe
      """purple     ctc (tileStitch)
        |           ctcr (a1)
        |           ctcl (a2)""".stripMargin
  }
}
