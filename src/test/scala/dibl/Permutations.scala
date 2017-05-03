package dibl

import dibl.Force.{ Point, nudgeNodes }
import dibl.SVG.{ prolog, render }

import scala.reflect.io.File
import scala.util.Try

object Permutations {
  def main(args: Array[String]): Unit = {
    new java.io.File("target/perm").mkdirs()
    val stitches = Seq("ct", "ctct", "crclct", "clcrclc", "ctctc", "ctclctc")
    for {
      d2 <- stitches
      b2 <- stitches.filter(_!=d2)
      a1 <- stitches
      c1 <- stitches.filter(_!=a1)
    } {
      val stitches = s"D2=$d2 B2=$b2 A1=$a1 C1=$c1"
      val pairs = PairDiagram.create("5-5-,-5-5", "checker", absRows = 7, absCols = 11, stitches = stitches)
      File(s"target/permutations/D2_$d2-B2_$b2-A1_$a1-C1_$c1.svg")
        .writeAll(prolog + render(nudge(ThreadDiagram(pairs.get))))
    }
    System.exit(0)
  }
  def nudge(d: Diagram): Diagram = nudgeNodes(d, Point(200, 100)).get
}
