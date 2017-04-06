/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html
*/
package dibl

import dibl.Force.{Point, nudgeNodes}
import dibl.SVG.{prolog, render}

import scala.reflect.io.File
import scala.util.Try

object Demo {
  def main(args: Array[String]): Unit = {

    val patternSheet = PatternSheet()
    patternSheet.add("5-", "bricks")
    SafeWriter("target/demoPatternSheet.svg")
      .write(prolog + patternSheet.toSvgDoc)
      .recover {
        case e: Throwable => e.printStackTrace()
      }

    /*
      The first pair diagram has positions initialised using the matrix cells as coordinates.
      These values are an estimate to prevent flipped and rotated diagrams when nudged by a force simulation.
      A thread diagram is created from a pair diagram by replacing each node with a couple of new nodes.
      These new nodes are all placed on the spot of their original node and are also nudged by a force simulation.
      A subsequent transition from a thread diagram to a pair diagram only restyles the nodes,
      their positions don't need nudging to optimise the next step.
     */
    (for {
      pairDiagram <- PairDiagram.create("586- -4-5 5-21 -5-7", "checker", absRows = 9, absCols = 9, stitches = "tctc,D4=llctc,B4=rrctc,A1=ctc,D2=rctc,B2=lctc,C3=ctc")
      nudgedPairDiagram <- nudgeNodes(pairDiagram, center = Point(100, 100))
      _ <- SafeWriter("target/demoP1a.svg").write(prolog + render(pairDiagram))
      _ <- SafeWriter("target/demoP1b.svg").write(prolog + render(nudgedPairDiagram))
      threadDiagram = ThreadDiagram(pairDiagram)
      nudgedThreadDiagram <- nudgeNodes(threadDiagram, center = Point(200, 200))
      _ <- SafeWriter("target/demoT1a.svg").write(prolog + render(threadDiagram, "2px"))
      _ <- SafeWriter("target/demoT1b.svg").write(prolog + render(nudgedThreadDiagram, "2px"))
      _ <- SafeWriter("target/demoP2.svg").write(prolog + render(PairDiagram("ct", nudgedThreadDiagram)))
      _ <- SafeWriter("target/demoP1b.html").write(
        s"""<!DOCTYPE html>
          |<html>
          |<style>
          |${SVG.threadsCSS()}
          |</style>
          |<body>
          |${render(nudgedPairDiagram)}
          |</body>
          |</html>
          |
        """.stripMargin)
    } yield ()).recover {
      case e: Throwable => e.printStackTrace()
    }
    System.exit(0)
    // TODO terminate script engine more elegantly
    // calling https://github.com/d3/d3-force/#simulation_stop
    // after Force.onEnd doesn't seem to help
  }


  private case class SafeWriter(fileName: String) {
    def write(content: String): Try[Unit] = Try(
      File(fileName).writeAll(content)
    )
  }
}
