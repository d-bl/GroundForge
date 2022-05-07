package dibl

import dibl.PairDiagram.drosteLegend
import org.scalatest.{ FlatSpec, Matchers }

class DrosteLegendSpec extends FlatSpec with Matchers {
  private val patterns = PatternQueries.gwPatterns.toMap
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
