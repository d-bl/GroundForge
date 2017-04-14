package dibl

import dibl.Force.{ Point, nudgeNodes }
import dibl.SVG.{ prolog, render }

import scala.reflect.io.File
import scala.util.Try

object Debug {
  def main(args: Array[String]): Unit = {
    val firstPairDiagram = nudge(PairDiagram.create("5-", "bricks", absRows = 7, absCols = 5, stitches = "ct"))
    val firstThreadDiagram = nudge(ThreadDiagram.create(firstPairDiagram))
    val secondPairDiagram = nudge(PairDiagram.create("ctc", firstThreadDiagram))
    val secondThreadDiagram = nudge(ThreadDiagram.create(firstPairDiagram))
    File("target/debugP1.svg").writeAll(prolog + render(firstPairDiagram))
    File("target/debugP2.svg").writeAll(prolog + render(secondPairDiagram))
    File("target/debugT1.svg").writeAll(prolog + render(firstThreadDiagram))
    File("target/debugT2.svg").writeAll(prolog + render(secondThreadDiagram))
    System.exit(0)
  }

  def nudge(d: Try[Diagram]): Diagram = nudgeNodes(d.get, Point(200, 100)).get

  def nudge(d: Diagram): Diagram = nudgeNodes(d, Point(200, 100)).get
}
