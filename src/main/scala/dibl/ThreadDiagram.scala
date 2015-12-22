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

    val pairLinks = pairDiagram.links.map(l => (l.source, l.target))
    val instructions = pairDiagram.nodes.map(_.instructions)

    @tailrec
    def createRows(possibleStitches: Seq[TargetToSrcs],
                   availablePairs: Map[Int, Threads],
                   nodes: Seq[Props],
                   links: Seq[Props] = Seq[Props]()
                  ): (Map[Int, Threads], Seq[Props], Seq[Props]) =
    if (possibleStitches.isEmpty) {
      val next = nextPossibleStitches(availablePairs.keys.toArray)
      if (next.isEmpty)
        (availablePairs, nodes, links)
      else if (nodes.length > 1000)
        (availablePairs, nodes :+ whoops(s"Eternal loop? ${nodes.length} nodes, ${links.length} links."), links)
      else
        createRows(next, availablePairs, nodes, links)
    } else {
      val (pairTarget, (leftPairSource, rightPairSource)) = possibleStitches.head
      if (!Set(leftPairSource, rightPairSource).subsetOf(availablePairs.keySet)) {
        val msg = s"Need a new pair from the footside? Missing $leftPairSource and/or $rightPairSource in ${availablePairs.keySet.mkString(",")}"
        createRows(possibleStitches.tail, availablePairs, nodes :+ whoops(msg), links)
      } else if (instructions(pairTarget)=="pair"){
        val msg = s"Two pairs starting at same node? target=$pairTarget leftSource=$leftPairSource rightSource=$rightPairSource available ${availablePairs.keySet.mkString(",")}"
        createRows(possibleStitches.tail, availablePairs - leftPairSource - rightPairSource, nodes :+ whoops(msg), links)
      } else {
        val left = availablePairs(leftPairSource)
        val right = availablePairs(rightPairSource)
        val (newPairs, accNodes, accLinks) =
          createStitch(instructions(pairTarget), Threads(left, right), nodes, links)

        val replaced = availablePairs - leftPairSource - rightPairSource ++
          ((left.hasSinglePair, right.hasSinglePair) match {
            case (true, true) => HashMap(pairTarget -> newPairs)
            case (false, true) => HashMap(pairTarget -> newPairs, leftPairSource -> left.leftPair)
            case (true, false) => HashMap(pairTarget -> newPairs, rightPairSource -> right.rightPair)
            case (false, false) => HashMap(pairTarget -> newPairs, leftPairSource -> left.leftPair,
              rightPairSource -> right.rightPair)
          })
        createRows(possibleStitches.tail, replaced, accNodes, accLinks)
      }
    }

    def nextPossibleStitches(pairNodes: Seq[Int]
                            ): Seq[TargetToSrcs] = {
      // grouping the pairLinks by source would allow a more efficient
      // val availableLinks = pairNodes.map(pairLinksBySrc).flatten
      // but the grouping mixes up left and right pairs
      val availableLinks = pairLinks.filter(l => pairNodes.contains(l._1))
      val linksByTarget = availableLinks.sortBy(_._2).groupBy(_._2).filter(_._2.length == 2).toArray
      linksByTarget.map { case (target: Int, pairLinks: Seq[(Int, Int)]) =>
        val ints = pairLinks.map(_._1)
        TargetToSrcs(target, (ints.head, ints.last))
      }
    }

    if (pairLinks.isEmpty)
      ThreadDiagram(Seq(whoops("invalid pair diagram")), Seq[Props]())
    else {
      val startPins = pairNodeNrToPairNr(pairDiagram.nodes)
      val startPairNodeNrs = startPins.keys.toArray
      val threadStartNodes = startPins.flatMap { case (n, t) => startNodes(n, t) }.toArray
      val nodesByThreadNr = threadStartNodes.indices.sortBy(threadStartNodes(_).startOf)
      val (_, nodes, links) = createRows(
        nextPossibleStitches(startPairNodeNrs),
        startPinsToThreadNodes(startPairNodeNrs, pairDiagram.nodes),
        threadStartNodes
      )
      ThreadDiagram(nodes, markStartLinks(links, nodes) ++ transparentLinks(nodesByThreadNr))
    }
  }

  private def whoops(msg: String) = Props("title" -> msg, "bobbin" -> true)

  private def pairNodeNrToPairNr(nodes: Seq[Props]): Map[Int, Int] =
    nodes.indices.toArray
      .filter(nodes(_).title.startsWith("Pair"))
      .map(n => n -> (nodes(n).title.replace("Pair ", "").toInt - 1)).toMap

  def markStartLinks(links: Seq[Props], nodes: Seq[Props]): Seq[Props] =
    links.map(link =>
      if (nodes(link.source).startOf == 0) link
      else link - "start" + ("start" -> "thread")
    )

  def startNodes(nodeNr: Int, threadNr: Int): Seq[HashMap[String, Any]] = {
    val n = threadNr * 2
    val x = Props("startOf" -> s"thread${n + 1}", "title" -> s"thread ${n + 1}")
    val y = Props("startOf" -> s"thread${n + 2}", "title" -> s"thread ${n + 2}")
    Seq(x, y)
  }

  private def startPinsToThreadNodes(startPairNodeNrs: Seq[Int],
                                     pairNodes: Seq[Props]
                                    ): Map[Int, Threads] =
    startPairNodeNrs.indices.map(i =>
      {
        val nodeNr = startPairNodeNrs(i)
        val pairNr = pairNodes(nodeNr).title.split(" ")(1).toInt
        nodeNr -> Threads(i,pairNr)
      }
    ).toMap

  @tailrec
  def createStitch(instructions: String,
                   threads: Threads,
                   nodes: Seq[Props],
                   links: Seq[Props]
                  ): (Threads, Seq[Props], Seq[Props]) =
    if (instructions.isEmpty) (threads, nodes, links)
    else {
      val (t,n,l) = instructions.head match {
        case 'p' => threads.putPin(nodes.length)
        case 'c' => threads.cross(nodes.length)
        case 'l' => threads.twistLeft(nodes.length)
        case 'r' => threads.twistRight(nodes.length)
        case _ =>
          val msg = s"$instructions? expecting p/c/l/r. nodes=${threads.nodes} threads=${threads.threads}"
          (threads,whoops(msg),Seq[Props]())
      }
      createStitch(instructions.tail, t, nodes :+ n, l ++ links)
    }
}