package dibl

import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

class GallerySpec extends FlatSpec with Matchers {
  "each gallery pattern" should "succeed (one fails because of bug #93)" in {
    Matrices.values.filter { l =>
      val f = l.split(";")
      val cols = if (f.size > 3) f(3).toInt else 9
      PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols).isFailure ||
        PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols + 1).isFailure ||
        PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols + 2).isFailure ||
        PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols + 3).isFailure
    } shouldBe Seq("B-B- -B-B C-C- -C-C;bricks")
  }

  "some patterns" should "document bug #93 by failing" in {
    // discovered by @MAETempels
    PairDiagram.create("5--- -C-B --5- -B-C", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("5831,-4-7,--5-,-B-C", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("5831,-4-7,--5-,-B-C,3158,-7-4,5---,-C-B", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("6--4,-C-4,--58,-B68,-46-,-4-C,58--,68-B", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]

    // from Matrices.values
    PairDiagram.create("---5 C-B- -5-- B-C-", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("C3B- 256- ---5", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("---5 C3B- 256-", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("256- ---5 C3B-", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]
    PairDiagram.create("256- ---5 C3B-", "bricks", absRows = 9, absCols = 9, shiftUp = 5) shouldBe a[Failure[_]]
    Seq(
      "6868 ---4 2AA1 -7-7;bricks",
      "6868 ---- AAAA -7-7;bricks",
      "586- ---5 2AA1 -7-7;bricks",
      "-4-4 5--- -C-B 6868;bricks",
      "-4-5 5--- -C-B 6-58;bricks",
      "B-C- ---5 C-B- -5--;checker",
      "5-5- -5-- B-C- -5-5;bricks",
      "-4-7 5--- -C-B 3158;bricks",
      "586- ---5 2AB- -7-5;bricks",
      "6868 ---4 2AA1 -7-7;bricks",
      "6868 ---7 AA01 -7-7;bricks",
      "6868 ---- AAAA -7-7;bricks",
      "586- ---5 2AA1 -7-7;bricks",
      "-4-4 5--- -C-B 6868;bricks",
      "-4-5 5--- -C-B 6-58;bricks"
    ).map { l =>
      val (matrix, tiling) = l.split(";") match{case Array(m,t) => (m,t)}
      PairDiagram.create(matrix, tiling, absRows = 9, absCols = 9, shiftUp=3).isFailure
    } should contain only true
  }

  it should "work for some set of parameters" in {
    PairDiagram.create("B-B- -B-B C-C- -C-C", "bricks", absRows = 9, absCols = 5) shouldBe a[Success[_]]
    tryConfigurations("B-B- -B-B C-C- -C-C", "bricks").mkString shouldNot include("true")
  }

  private def tryConfigurations(matrix: String, tiling: String) = {
    for {
      shiftLeft <- 0 until 16
      shiftUp <- 0 until 16
      cols <- 8 until 32
    } yield {
      val result = PairDiagram.create(matrix, tiling, absRows = 9, cols, shiftLeft, shiftUp)
      s"left=$shiftLeft,up=$shiftUp,cols=$cols:${result.isSuccess} "
    }
  }
}
