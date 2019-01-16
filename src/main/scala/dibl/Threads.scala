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

import java.lang.Math.abs

import dibl.LinkProps._
import dibl.NodeProps._

import scala.annotation.tailrec

case class Threads private
(nodes: (Int, Int, Int, Int),
 threads: (Int, Int, Int, Int)
) {
  private val (n1, n2, n3, n4) = nodes
  private val (t1, t2, t3, t4) = threads

  /** @return (self', new threadNode, new threadLinks )
    */
  def putPin(newNode: Int): (Threads, NodeProps, Seq[LinkProps]) =
    (this, pinNode, pinLinks(Seq(n1, newNode, n4)))

  /** moves the second thread over the third
    *
    * @return (self', new threadNode, new threadLinks )
    */
  def cross(newNode: Int, stitchID: String, idSuffix: Int): (Threads, NodeProps, Seq[LinkProps]) =
    (new Threads((n1, newNode, newNode, n4), (t1, t3, t2, t4)),
      crossNode(stitchID, idSuffix),
      crossedLinks(newNode, n2, n3, t2, t3))

  /** moves the second thread over the first
    *
    * @return (self', new threadNode, new threadLinks )
    */
  def twistLeft(newNode: Int, stitchID: String, idSuffix: Int): (Threads, NodeProps, Seq[LinkProps]) =
    (new Threads((newNode, newNode, n3, n4), (t2, t1, t3, t4)),
      twistNode(stitchID, idSuffix),
      twistedLinks(newNode, n1, n2, t1, t2))

  /** moves the fourth thread over the third
    *
    * @return (self', new threadNode, new threadLinks )
    */
  def twistRight(newNode: Int, stitchID: String, idSuffix: Int): (Threads, NodeProps, Seq[LinkProps]) =
    (new Threads((n1, n2, newNode, newNode), (t1, t2, t4, t3)),
      twistNode(stitchID, idSuffix),
      twistedLinks(newNode, n3, n4, t3, t4))

  def leftPair: Threads = new Threads((n1, n2, n1, n2), (t1, t2, t1, t2))
  def rightPair: Threads = new Threads((n3, n4, n3, n4), (t3, t4, t3, t4))
  def hasSinglePair: Boolean = n1 == n3 && n2 == n4
}

object Threads {

  /** use the right pair from the left and the left pair from the right */
  def apply(left: Threads, right: Threads) = new Threads(
    (left.n3, left.n4, right.n1, right.n2),
    (left.t3, left.t4, right.t1, right.t2)
  )

  /** start a new pair */
  def apply(pairNode: Int, pairNr: Int): Threads = {
    val threadNr = pairNr * 2
    val nodeNr = pairNode * 2
    new Threads(
      (nodeNr, nodeNr + 1, nodeNr, nodeNr + 1),
      (threadNr -1, threadNr, threadNr-1, threadNr)
    )
  }

  def bobbins(available: Iterable[Threads],
              nodes: Seq[NodeProps],
              links: Seq[LinkProps]
             ): (Seq[NodeProps], Seq[LinkProps]) = {

    @tailrec
    def loop(available: Iterable[Threads],
             nodes: Seq[NodeProps],
             links: Seq[LinkProps],
             connect: Seq[Int] = Seq[Int]()
            ): (Seq[NodeProps], Seq[LinkProps]) = {
      if (available.isEmpty) {
        val sortedBobbins = connect.sortBy(i => nodes(i).x)
        (nodes, links ++ transparentLinks(sortedBobbins)
          .filter{ l =>
            // workaround: don't connect bobbins over too long distances
            // it might mean meddling with fringes that should have been foot sides
            val source = nodes(l.source)
            val target = nodes(l.target)
            abs(source.x - target.x) <= 60 && abs(source.y - target.y) <= 60
          }
        )
      } else {
        val h = available.head
        val threadNodes = if (h.hasSinglePair) Seq(h.n1, h.n2) else Seq(h.n1, h.n2, h.n3, h.n4)
        val threads = if (h.hasSinglePair) Seq(h.t1, h.t2) else Seq(h.t1, h.t2, h.t3, h.t4)
        val bobbinNodes: Seq[NodeProps] = threadNodes.indices.map(i => {
          val src = nodes(threadNodes(i))
          bobbinNode(threads(i), src.x, src.y + 100)
        })
        val bobbinLinks: Seq[LinkProps] = threadNodes.indices.map(i =>
          threadLink(
            threadNodes(i),
            nodes.size + i,
            threads(i),
            i % 2 == 0
          )
        )
        val newNodes = bobbinLinks.map(props => props.target)
        loop(available.tail, nodes ++ bobbinNodes, links ++ bobbinLinks, connect ++ newNodes)
      }
    }
    loop(available,nodes,links)
  }
}
