package dibl

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Failure

class GallerySpec extends FlatSpec with Matchers {
  "each gallery pattern" should "succeed (one fails because of bug #93)" in {
    Matrices.values.filter { l =>
      val f = l.split(";")
      val cols = if (f.size > 3) f(3).toInt else 9
      PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols).isFailure ||
      PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols +1).isFailure ||
      PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols +2).isFailure ||
      PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols +3).isFailure
    } shouldBe Seq("B-B- -B-B C-C- -C-C;bricks")
  }

  "some patterns" should "document bug #93 by failing" in {
    // by @MAETempels
    PairDiagram.create("5--- -C-B --5- -B-C", "checker", absRows = 9, absCols = 9) shouldBe a[Failure[_]]

    // from Matrices.values
    PairDiagram.create("B-C- ---5 C-B- -5--", "checker", absRows = 9, absCols = 9, shiftUp = 1) shouldBe a[Failure[_]]
    PairDiagram.create("256- ---5 C3B-", "checker", absRows = 9, absCols = 9, shiftUp = 2) shouldBe a[Failure[_]]
    PairDiagram.create("256- ---5 C3B-", "bricks", absRows = 9, absCols = 9, shiftUp = 5) shouldBe a[Failure[_]]
    Seq(
      "6868 ---4 2AA1 -7-7;bricks;9;9;0;0",
      "6868 ---- AAAA -7-7;bricks;9;9;0;0",
      "586- ---5 2AA1 -7-7;bricks;9;9;0;0",
      "-4-4 5--- -C-B 6868;bricks;9;9;0;0",
      "-4-5 5--- -C-B 6-58;bricks;9;9;0;0",
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
    ).foreach { l =>
      val f = l.split(";")
      val cols = if (f.size > 3) f(3).toInt else 9
      PairDiagram.create(f(0), f(1), absRows = 9, absCols = cols, shiftUp = 5).isFailure

    }
  }

  ignore should "work for some set of parameters" in {
    val x: Seq[String] = for{
      shiftLeft <- 0 until 16
      shiftUp <- 0 until 16
      cols <- 8 until 32
    } yield {
      val result = PairDiagram.create("B-B- -B-B C-C- -C-C", "bricks", absRows = 9, cols, shiftLeft, shiftUp)
      s"shiftLeft=$shiftLeft shiftUp=$shiftUp cols=$cols ${result.isSuccess}"
    }
    println (x)
    x.mkString should include ("true")
  }
}
