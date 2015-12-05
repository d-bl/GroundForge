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

import scala.annotation.tailrec

case class ThreadDiagram private(nodes: Seq[Props],
                                 links: Seq[Props])

object ThreadDiagram {

  def apply(pairDiagram: PairDiagram): ThreadDiagram = {

    val pairLinks = pairDiagram.links.map(l => (l.source,l.target))
    val instructions = pairDiagram.nodes.map(
      _.title.replaceAll(" .*", "").toLowerCase.replaceAll("t","lr")
    )
    val startPairNodeNrs = pairDiagram.nodes.indices.filter(
      instructions(_).startsWith("pair")
    ).map(n => pairDiagram.nodes(n).title.replace("Pair ","").toInt - 1)
    val startNodes = startPairNodeNrs.flatMap{ i =>
      val n = startPairNodeNrs(i) * 2
      val x = Props("startOf" -> s"thread${n + 1}")
      val y = Props("startOf" -> s"thread${n + 2}")
      Seq(x,y)
    }
    val pairNodesToThreadNodes = startPairNodeNrs.indices.map(i =>
      startPairNodeNrs(i) ->(i * 2, i * 2 + 1)
    ).toMap
    // TODO create rows until no more possible stitches,
    val (nodes, links) = createRow(
      possibleStitches(startPairNodeNrs, pairLinks, instructions),
      pairNodesToThreadNodes,
      startNodes,
      transparentLinks(startNodes.indices)
    )
    ThreadDiagram(nodes, links)
  }

  @tailrec
  def createRow(stitches: Seq[(String, (Int, Int))],
                pairNodesToThreadNodes: Map[Int, (Int, Int)],
                nodes: Seq[Props],
                links: Seq[Props]
                     ): (Seq[Props], Seq[Props]) =
    if (stitches.isEmpty) (nodes, links)
    // TODO also return pairNodesToThreadNodes after replacing ... with endNodes
      // apply the typ-alias AvailableNodes
    else {
      val (instructions, (left, right)) = stitches.head
      val (a,b) = pairNodesToThreadNodes(left)
      val (c,d) = pairNodesToThreadNodes(right)
      val (_, accNodes, accLinks) = createStitch(instructions, (a,b,c,d), nodes,links)
      createRow(stitches.tail, pairNodesToThreadNodes, accNodes, accLinks)
    }

  @tailrec
  def createStitch(instructions: String,
                   threadNodes: ThreadNodes,
                   nodes: Seq[Props],
                   links: Seq[Props]
                  ): (ThreadNodes, Seq[Props], Seq[Props]) =
    if (instructions.isEmpty) (threadNodes, nodes, links)
    else {
      val instruction = instructions.head
      val (a, b, c, d) = threadNodes
      val newNode = nodes.length
      if (instruction == 'p') {
        createStitch(
          instructions.tail,
          ThreadNodes(a, b, c, d),
          nodes :+ Props("pin" -> "true"),
          transparentLinks(Seq(a, newNode, d)) ++ links
        )
      }else if (instruction == 'c') {
        val nx = Props("source" -> b, "target" -> newNode, "end" -> "white")
        val ny = Props("source" -> c, "target" -> newNode, "start" -> "white")
        createStitch(
          instructions.tail,
          ThreadNodes(a, newNode, newNode, d),
          nodes :+ Props("title" -> "cross"),
          nx +: ny +: links
        )
      } else {
        val (x, y) = if (instruction == 'l') (a, b) else (c, d)
        val nx = Props("source" -> x, "target" -> newNode, "start" -> "white")
        val ny = Props("source" -> y, "target" -> newNode, "end" -> "white")
        createStitch(
          instructions.tail,
          if (instruction == 'l') ThreadNodes(newNode, newNode, c, d)
          else ThreadNodes(a, b, newNode, newNode),
          nodes :+ Props("title" -> "twist"),
          nx +: ny +: links
        )
      }
    }

  /** @return something like (("ctc", (0, 1)), ...)
    *         meaning: make a cloth stitch with pairs at nodes (0,1), ...
    */
  def possibleStitches(startPairs: Seq[Int],
                       pairLinks: Seq[(Int, Int)],
                       instructions: Seq[String]
                      ): List[(String, (Int, Int))] = pairLinks
    // get links starting at nodes in startPairs
    .filter(pairLink => startPairs.contains(pairLink._1))
    // get those ending at the same targets
    .groupBy(_._2)
    .filter(_._2.length == 2)
    // now we have a map of targets to their incoming links
    // as the links have the same target we only want their sources
    .toList.map { case (target: Int, pairLinks: Seq[(Int, Int)]) =>
      val ints = pairLinks.map(_._1)
      (instructions(target), (ints.head,ints.last))
    }
}