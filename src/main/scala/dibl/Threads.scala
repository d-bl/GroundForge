package dibl

import scala.annotation.tailrec

case class Threads private
(nodes: (Int, Int, Int, Int),
 threads: (Int, Int, Int, Int)
) {
  private val (n1, n2, n3, n4) = nodes
  private val (t1, t2, t3, t4) = threads

  /** @return (self', new threadNode, new threadLinks )
    */
  def putPin(newNode: Int): (Threads, Props, Seq[Props]) =
    (this, Threads.pinProps, transparentLinks(Seq(n1, newNode, n4)))

  /** moves the second thread over the third
    *
    * @return (self', new threadNode, new threadLinks )
    */
  def cross(newNode: Int): (Threads, Props, Seq[Props]) =
    (new Threads((n1, newNode, newNode, n4), (t1, t3, t2, t4)),
      Threads.crossedNodeProps,
      Threads.crossedLinksProps(newNode, n2, n3, t2, t3))

  /** moves the second thread over the first
    *
    * @return (self', new threadNode, new threadLinks )
    */
  def twistLeft(newNode: Int): (Threads, Props, Seq[Props]) =
    (new Threads((newNode, newNode, n3, n4), (t2, t1, t3, t4)),
      Threads.twistedNodeProps,
      Threads.twistedLinksProps(newNode, n1, n2, t1, t2))

  /** moves the fourth thread over the third
    *
    * @return (self', new threadNode, new threadLinks )
    */
  def twistRight(newNode: Int): (Threads, Props, Seq[Props]) =
    (new Threads((n1, n2, newNode, newNode), (t1, t2, t4, t3)),
      Threads.twistedNodeProps,
      Threads.twistedLinksProps(newNode, n3, n4, t3, t4))

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
  def apply(pairNode: Int, pairNr: Int) = {
    val threadNr = pairNr * 2
    val nodeNr = pairNode * 2
    new Threads(
      (nodeNr, nodeNr + 1, nodeNr, nodeNr + 1),
      (threadNr -1, threadNr, threadNr-1, threadNr)
    )
  }

  private val pinProps = Props("title" -> "pin", "pin" -> "true")
  private val crossedNodeProps = Props("title" -> "cross")
  private val twistedNodeProps = Props("title" -> "twist")

  private def twistedLinksProps(newNode: Int,
                                leftNode: Int, rightNode: Int,
                                leftThread: Int, rightThread: Int
                               ): Seq[Props] = {
    val curved = leftNode == rightNode
    val x = Props("source" -> leftNode, "target" -> newNode, "end" -> "white", "thread" -> leftThread, "left" -> curved)
    val y = Props("source" -> rightNode, "target" -> newNode, "start" -> "white", "thread" -> rightThread, "right" -> curved)
    Seq(x, y)
  }

  private def crossedLinksProps(newNode: Int,
                                leftNode: Int, rightNode: Int,
                                leftThread: Int, rightThread: Int
                               ): Seq[Props] = {
    val curved = leftNode == rightNode
    val x = Props("source" -> leftNode, "target" -> newNode, "start" -> "white", "thread" -> leftThread, "left" -> curved)
    val y = Props("source" -> rightNode, "target" -> newNode, "end" -> "white", "thread" -> rightThread, "right" -> curved)
    Seq(x, y)
  }

  def bobbins(available: Iterable[Threads],
              nodes: Seq[Props],
              links: Seq[Props]
             ): (Seq[Props], Seq[Props]) = {

    @tailrec
    def loop(available: Iterable[Threads],
             nodes: Seq[Props],
             links: Seq[Props],
             connect: Seq[Int] = Seq[Int]()
            ): (Seq[Props], Seq[Props]) = {
      if (available.isEmpty) {
        val sortedBobbins = connect.sortBy(i => nodes(i).x)
        (nodes, links ++ transparentLinks(sortedBobbins))
      } else {
        val h = available.head
        val threadNodes = if (h.hasSinglePair) Seq(h.n1, h.n2) else Seq(h.n1, h.n2, h.n3, h.n4)
        val threads = if (h.hasSinglePair) Seq(h.t1, h.t2) else Seq(h.t1, h.t2, h.t3, h.t4)
        val bobbinNodes: Seq[Props] = threadNodes.indices.map(i => {
          val src = nodes(threadNodes(i))
          Props("bobbin" -> "true", "x" -> src.x, "y" -> (src.y + 100), "thread" -> threads(i))
        })
        val bobbinLinks: Seq[Props] = threadNodes.indices.map(i => {
          val base = Props("source" -> threadNodes(i), "target" -> (nodes.size + i), "thread" -> threads(i))
          if (i % 2 == 0) base else base ++ Props("start" -> "white")
        })
        val newNodes = bobbinLinks.map(props => props.target)
        loop(available.tail, nodes ++ bobbinNodes, links ++ bobbinLinks, connect ++ newNodes)
      }
    }
    loop(available,nodes,links)
  }
}
