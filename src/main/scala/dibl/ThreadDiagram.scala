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
import scala.collection.immutable.HashMap

case class ThreadDiagram private(nodes: Seq[Props],
                                 links: Seq[Props])

object ThreadDiagram {

  def apply(pairDiagram: PairDiagram): ThreadDiagram = {

    implicit val instructions = pairDiagram.nodes.map(
      _.title.replaceAll(" .*", "").toLowerCase.replaceAll("t","lr")
    )
    val sourceToTargets = mapSourceToTargets(pairDiagram.links)
    val startPins = getStartPins(pairDiagram.nodes)
      .toArray // works around some of the index-out-of-bounds exceptions in JavaScript
    val startThreadNodes = startPins.flatMap{ i =>
      val n = startPins(i) * 2
      val x = Props("startOf" -> s"thread${n + 1}")
      val y = Props("startOf" -> s"thread${n + 2}")
      Seq(x,y)
    }
    // TODO create rows until no more possible stitches,
    val (availablePairs, nodes, links) = createRow(
      possibleStitches(startPins, pairDiagram.links.map(l => (l.source, l.target))),
      //getPossibleStitches(startPins, sourceToTargets),
      startPinsToThreadNodes(startPins),
      startThreadNodes
    )
    ThreadDiagram(nodes, links ++ transparentLinks(startThreadNodes.indices))
  }

  def mapSourceToTargets(pairLinks: Seq[Props]): Map[Int, Seq[Int]] =
    pairLinks
      .map(props => props.source -> props.target)
      .groupBy(_._2)
      .map { case (t, links) =>
        t -> links.map { case (s, _) => s }
      }

  def getPossibleStitches(availableSources: Seq[Int],
                          sourceToTargets: Map[Int,Seq[Int]]
                         ): Seq[(Int, Seq[Int])] = {
    // links starting at available sources
    availableSources.map(src => (src , sourceToTargets.get(src))).filter(_._2.isDefined)
      .map{case (src , linkOptions) => (src,linkOptions.get) }
    // TODO find sources with same targets
  }

  def startPinsToThreadNodes(startPairNodeNrs: Seq[Int]): Map[Int, ThreadNodes] =
    startPairNodeNrs.indices.map(i =>
      startPairNodeNrs(i) -> ThreadNodes(i)
    ).toMap

  def getStartPins(nodes: Seq[Props]): IndexedSeq[Int] =
    nodes.indices
      .filter(nodes(_).title.startsWith("Pair"))
      .map(nodes(_).title.replace("Pair ", "").toInt - 1)

  @tailrec
  def createRow(possibleStitches: Seq[TargetToSrcs],
                availablePairs: Map[Int, ThreadNodes],
                nodes: Seq[Props],
                links: Seq[Props] = Seq[Props]()
               )(implicit instructions: Seq[String]
               ): (Map[Int, ThreadNodes], Seq[Props], Seq[Props]) =
    if (possibleStitches.isEmpty) (availablePairs, nodes, links)
    else {
      val (pairTarget, (leftPairSource, rightPairSource)) = possibleStitches.head
      val left = availablePairs(leftPairSource)
      val right = availablePairs(rightPairSource)
      val (newNodes, accNodes, accLinks) = {
        val startNodes = ThreadNodes(left, right)
        createStitch(instructions(pairTarget), startNodes, nodes, links)
      }
      val replaced = {
        val replacement = {
          val noMoreLeft = left._1 == left._3 && left._2 == left._4
          val noMoreRight = right._1 == right._3 && right._2 == right._4
          val newLeft = ThreadNodes(left._1, left._2, left._1, left._2)
          val newRight = ThreadNodes(right._3, right._4, right._3, right._4)

          if (noMoreLeft)
            if (noMoreRight) HashMap(pairTarget -> newNodes)
            else HashMap(pairTarget -> newNodes, rightPairSource -> newRight)
          else if (noMoreRight) HashMap(pairTarget -> newNodes, leftPairSource -> newLeft)
          else HashMap(pairTarget -> newNodes, rightPairSource -> newRight)
        }
        availablePairs.slice(0, leftPairSource) ++ replacement ++
          availablePairs.slice(leftPairSource + 1, availablePairs.size)
      }
      createRow(possibleStitches.tail, replaced, accNodes, accLinks)
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
        val nx = Props("source" -> b, "target" -> newNode, "start" -> "white")
        val ny = Props("source" -> c, "target" -> newNode, "end" -> "white")
        createStitch(
          instructions.tail,
          ThreadNodes(a, newNode, newNode, d),
          nodes :+ Props("title" -> "cross"),
          nx +: ny +: links
        )
      } else {
        val (x, y) = if (instruction == 'l') (a, b) else (c, d)
        val nx = Props("source" -> x, "target" -> newNode, "end" -> "white")
        val ny = Props("source" -> y, "target" -> newNode, "start" -> "white")
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
                       pairLinks: Seq[(Int, Int)]
                      ): List[TargetToSrcs] = pairLinks
    // get links starting at nodes in startPairs
    .filter(pairLink => startPairs.contains(pairLink._1))
    // get those ending at the same targets
    .groupBy(_._2)
    .filter(_._2.length == 2)
    // now we have a map of targets to their incoming links
    // as the links have the same target we only want their sources
    .toList.map { case (target: Int, pairLinks: Seq[(Int, Int)]) =>
      val ints = pairLinks.map(_._1)
      TargetToSrcs(target, (ints.head,ints.last))
    }
}