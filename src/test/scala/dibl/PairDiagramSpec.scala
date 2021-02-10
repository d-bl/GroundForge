package dibl

import dibl.PairDiagram.legend
import org.scalatest.{ FlatSpec, Matchers }

class PairDiagramSpec extends FlatSpec with Matchers {
  private val patterns = PatternQueries.gwPatterns.toMap
  "legend" should "" in {
    legend(patterns("gw-F11")) shouldBe "red: ctctt, tctct; blue: ctctctctctt"
    legend(patterns("gw-H5")) shouldBe "purple: ctc, ctcl, ctcr"
    legend(patterns("gw-A14b")) shouldBe "purple: ctc; red: ctct, ctctt, ttctctt; blue: ctctctctctctt"
  }
}
