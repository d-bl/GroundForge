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
  private val whoopsShifted = Props("title" -> "many shifted patterns are too buggy", "bobbin" -> true)
  private val whoopsInvalidPD = Props("title" -> "invalid pair diagram", "bobbin" -> true)
  private val whoops = Props("title" -> "Sorry, ran into a bug.", "bobbin" -> true)

  def apply(pairDiagram: PairDiagram): ThreadDiagram = {

    val pairNodes = pairDiagram.nodes
    implicit val pairLinks = pairDiagram.links.map(l => (l.source, l.target))
    implicit val instructions = pairNodes.map(_.instructions)
    val startPins = pairNodeNrToThreadNr(pairNodes)
    val startPairNodeNrs = startPins.keys.toSeq
    val startThreadNodes = startPins.flatMap { case (_, threadNr) =>
      val n = threadNr * 2
      val x = Props("startOf" -> s"thread${n + 1}")
      val y = Props("startOf" -> s"thread${n + 2}")
      Seq(x,y)
    }.toSeq
    val nodesByThreadNr = startThreadNodes.indices.sortBy(startThreadNodes(_).startOf)
    if (nodesByThreadNr.isEmpty)
      ThreadDiagram(Seq(whoopsInvalidPD), Seq[Props]())
    else if (startThreadNodes(nodesByThreadNr.head).startOf != 1)
      ThreadDiagram(Seq(whoopsShifted), Seq[Props]())
    else {
      val (_, nodes, links) = createRow(
        nextPossibleStitches(startPairNodeNrs),
        startPinsToThreadNodes(startPairNodeNrs),
        startThreadNodes
      )
      ThreadDiagram(nodes, links ++ transparentLinks(nodesByThreadNr))
    }
  }

  private def pairNodeNrToThreadNr(nodes: Seq[Props]): Map[Int, Int] =
    nodes.indices.toArray
      .filter(nodes(_).title.startsWith("Pair"))
      .map(n => n -> (nodes(n).title.replace("Pair ", "").toInt - 1)).toMap

  private def startPinsToThreadNodes(startPairNodeNrs: Seq[Int]): Map[Int, ThreadNodes] =
    startPairNodeNrs.indices.map(i =>
      startPairNodeNrs(i) -> ThreadNodes(i)
    ).toMap

  private def terminate(nodes: Seq[Props], links: Seq[Props]) =
    (Map[Int, ThreadNodes](), whoops +: nodes, links)

  @tailrec
  private def createRow(possibleStitches: Seq[TargetToSrcs],
                        availablePairs: Map[Int, ThreadNodes],
                        nodes: Seq[Props],
                        links: Seq[Props] = Seq[Props]()
                       )(implicit
                         instructions: Seq[String],
                         pairLinks: Seq[(Int, Int)]
                       ): (Map[Int, ThreadNodes], Seq[Props], Seq[Props]) =
    if (possibleStitches.isEmpty) {
      val ps = possibleStitches
      val next: Seq[TargetToSrcs] = nextPossibleStitches(availablePairs.keys.toSeq)
      val stop = next.isEmpty || nodes.length > 500 // maximized to prevent eternal loop
      //println(s"$stop ${nodes.length} - ${availablePairs.keys.mkString(",")} - ${next.toMap.keys.mkString(",")}")
      if (stop)
        (availablePairs, nodes, links)
      else
        createRow(next, availablePairs, nodes, links)
    } else {
      val (pairTarget, (leftPairSource, rightPairSource)) = possibleStitches.head
      Set(leftPairSource, rightPairSource).subsetOf(availablePairs.keySet)
      if (!Set(leftPairSource, rightPairSource).subsetOf(availablePairs.keySet))
        return terminate(nodes, links) // TODO fix me
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
//        println(s"$leftPairSource -- ${replacement.keys} -- $rightPairSource")
//        println(availablePairs.mkString(" ## "))
        availablePairs - leftPairSource - rightPairSource ++ replacement
      }
//      println(replaced.mkString(" ## "))
      createRow(possibleStitches.tail, replaced, accNodes, accLinks)
    }

  @tailrec
  private def createStitch(instructions: String,
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

  private def nextPossibleStitches(startPairs: Seq[Int]
                                  )(implicit pairLinks: Seq[(Int, Int)]
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