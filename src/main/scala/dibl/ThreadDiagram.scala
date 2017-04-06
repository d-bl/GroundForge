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

import dibl.LinkProps.transparentLinks
import dibl.NodeProps.{errorNode, node, threadStartNode}

import scala.annotation.tailrec
import scala.collection.immutable.HashMap

object ThreadDiagram {
  def apply(pairDiagram: Diagram): Diagram = {

    val pairLinks = pairDiagram.links.map(l => (l.source, l.target))
    val instructions = pairDiagram.nodes.map(_.instructions)
    val xy: Seq[NodeProps] = pairDiagram.nodes.map(n => node(n.x*2, n.y*2))

    @tailrec
    def createRows(possibleStitches: Seq[TargetToSrcs],
                   availablePairs: Map[Int, Threads],
                   nodes: Seq[NodeProps],
                   links: Seq[LinkProps] = Seq[LinkProps]()
                  ): (Map[Int, Threads], Seq[NodeProps], Seq[LinkProps]) =
      if (possibleStitches.isEmpty) {
        val next = nextPossibleStitches(availablePairs.keys.toArray)
        if (next.isEmpty)
          (availablePairs, nodes, links)
        else
          createRows(next, availablePairs, nodes, links)
      } else {
        val (pairTarget, (leftPairSource, rightPairSource)) = possibleStitches.head
        //println( f"creating $pairTarget%3d with $leftPairSource%3d and $rightPairSource%3d of ${availablePairs.keySet.mkString(",")}")
        def availableKeys: String = availablePairs.keySet.mkString(",")
        if (!Set(leftPairSource, rightPairSource).subsetOf(availablePairs.keySet)) {
          val msg = s"Need a new pair from the footside? Missing $leftPairSource and/or $rightPairSource in $availableKeys"
          createRows(possibleStitches.tail, availablePairs, nodes :+ errorNode(msg), links)
        } else if (instructions(pairTarget)=="pair"){
          val msg = s"Two pairs starting at same node? target=$pairTarget leftSource=$leftPairSource rightSource=$rightPairSource available $availableKeys"
          createRows(possibleStitches.tail, availablePairs - leftPairSource - rightPairSource, nodes :+ errorNode(msg), links)
        } else {
          val left = availablePairs(leftPairSource)
          val right = availablePairs(rightPairSource)
          val (newPairs, accNodes, accLinks) =
            createStitch(instructions(pairTarget), xy(pairTarget), Threads(left, right), nodes, links)

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
      }.filter(node => !pairNodes.contains(node._1))//don't make the same stitch twice
    }

    if (pairLinks.isEmpty)
      Diagram(Seq(errorNode("invalid pair diagram")), Seq[LinkProps]())
    else {
      val startPins = pairNodeNrToPairNr(pairDiagram.nodes)
      val startPairNodeNrs = startPins.keys.toArray
      val threadStartNodes = startPins.flatMap { case (_, t) =>
        Seq(threadStartNode(t * 2 + 1), threadStartNode(t * 2 + 2))
      }.toArray
      val nodesByThreadNr = threadStartNodes.indices.sortBy(threadStartNodes(_).startOf)
      val (availablePairs, nodes, links) = createRows(
        nextPossibleStitches(startPairNodeNrs),
        startPinsToThreadNodes(startPairNodeNrs, pairDiagram.nodes),
        threadStartNodes
      )
      val (allNodes,allLinks) = Threads.bobbins(availablePairs.values, nodes, links)
      Diagram(
        allNodes,
        markStartLinks(allLinks, allNodes) ++ transparentLinks(nodesByThreadNr)
      )
    }
  }

  private def pairNodeNrToPairNr(nodes: Seq[NodeProps]): Map[Int, Int] =
    nodes.indices.toArray
      .filter(nodes(_).title.startsWith("Pair"))
      .map(n => n -> (nodes(n).title.replace("Pair ", "").toInt - 1)).toMap

  private def markStartLinks(links: Seq[LinkProps], nodes: Seq[NodeProps]): Seq[LinkProps] =
    links.map(link =>
      if (nodes(link.source).startOf == 0) link
      else link.markedAsStart
    )

  private def startPinsToThreadNodes(startPairNodeNrs: Seq[Int],
                                     pairNodes: Seq[NodeProps]
                                    ): Map[Int, Threads] =
    startPairNodeNrs.indices.map(i =>
    {
      val nodeNr = startPairNodeNrs(i)
      val pairNr = pairNodes(nodeNr).title.split(" ")(1).toInt
      nodeNr -> Threads(i,pairNr)
    }
    ).toMap

  @tailrec
  private def createStitch(instructions: String,
                           pairNode: NodeProps,
                           threads: Threads,
                           threadNodes: Seq[NodeProps],
                           threadLinks: Seq[LinkProps]
                          ): (Threads, Seq[NodeProps], Seq[LinkProps]) =
    if (instructions.isEmpty) (threads, threadNodes, threadLinks)
    else {
      val (t,n,l) = instructions.head match {
        case 'p' => threads.putPin(threadNodes.length)
        case 'c' => threads.cross(threadNodes.length)
        case 'l' => threads.twistLeft(threadNodes.length)
        case 'r' => threads.twistRight(threadNodes.length)
        case _ =>
          val msg = s"$instructions? expecting p/c/l/r. nodes=${threads.nodes} threads=${threads.threads}"
          (threads,errorNode(msg),Seq[LinkProps]())
      }
      createStitch(instructions.tail, pairNode, t, threadNodes :+ n.withLocationOf(pairNode), l ++ threadLinks)
    }
}