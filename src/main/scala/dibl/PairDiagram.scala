/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import scala.util.Try

case class PairDiagram private(nodes: Seq[Props],
                               links: Seq[Props])

object PairDiagram {

  def apply(triedSettings: Try[Settings]): PairDiagram = if (triedSettings.isFailure)
    PairDiagram(Seq(Props("title" -> triedSettings.failed.get.getMessage, "bobbin" -> true)), Seq[Props]())
  else {
    val settings: Settings = triedSettings.get
    val fringes = new Fringes(triedSettings.get.absM)
    val newPairs = for {i <- fringes.newPairs.indices} yield ((0, i), fringes.newPairs(i)._2)
    val sources = newPairs.map { case (source, _) => source }
    val plainLinks = fringes.coreLinks ++ fringes.footSides ++ newPairs
    val targets = plainLinks.groupBy { case (_, target) => target }.keys.toArray // TODO too expensive?
    val nodeMap: Map[(Int, Int), Int] = {
      val nodes = sources ++ targets
      nodes.indices.map(n => (nodes(n), n))
    }.toMap

    val nodes = sources.map { case (row, col) =>
      Props(
        "title" -> s"Pair ${1 + nodeMap((row, col))}",
        "y" -> 15 * row,
        "x" -> 15 * col
      )
    } ++ targets.map { case (row, col) => Props(
      "title" -> settings.getTitle(row, col),
      "y" -> 15 * row,
      "x" -> 15 * col
    )}
    val links =
      plainLinks.map { case ((sourceRow, sourceCol), (targetRow, targetCol)) =>
        val sourceStitch = settings.getStitch(sourceRow, sourceCol)
        val targetStitch = settings.getStitch(targetRow, targetCol)
        val toLeftOfTarget = settings.absM(targetRow)(targetCol)(0) == (sourceRow, sourceCol)
        Props(
          "source" -> nodeMap((sourceRow, sourceCol)),
          "target" -> nodeMap((targetRow, targetCol)),
          "start" -> (if (sourceRow < 2) "pair" else marker(sourceStitch)),
          "mid" -> (if (sourceRow < 2) 0 else midMarker(sourceStitch, targetStitch, toLeftOfTarget)),
          "end" -> marker(targetStitch)
        )
      } ++ transparentLinks(sources.indices.toArray) // last for proper thread diagrams
    new PairDiagram(nodes, links)
  }

  def marker(stitch: String): String = {
    if (stitch.endsWith("clrclrc") || stitch.contains("p")) ""
    else if (stitch.endsWith("lrclrc")) "red"
    else if (stitch.endsWith("clrc")) "purple"
    else if (stitch.endsWith("lrc")) "green"
    else if (stitch.startsWith("clrclrc")) ""
    else if (stitch.startsWith("clrclr")) "red"
    else if (stitch.startsWith("clrc")) "purple"
    else if (stitch.startsWith("clr")) "green"
    else ""
  }

  def midMarker(sourceStitch: String, targetStitch: String, toLeftOfTarget: Boolean): Int = {
    val twists = (targetStitch.replaceAll("c.*", "") + sourceStitch.replaceAll(".*c", ""))
      .replaceAll("p", "")
    val c = if (toLeftOfTarget) 'l' else 'r'
    Math.max(0, twists.count(_ == c) - 1)
  }
}
