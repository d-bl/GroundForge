package dibl

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

  private val pinProps = Props("pin" -> "true")
  private val crossedNodeProps = Props("title" -> "cross")
  private val twistedNodeProps = Props("title" -> "twist")

  private def twistedLinksProps(newNode: Int,
                                leftNode: Int, rightNode: Int,
                                leftThread: Int, rightThread: Int
                               ): Seq[Props] = {
    val x = Props("source" -> leftNode, "target" -> newNode, "end" -> "white", "thread" -> leftThread)
    val y = Props("source" -> rightNode, "target" -> newNode, "start" -> "white", "thread" -> rightThread)
    Seq(x, y)
  }

  private def crossedLinksProps(newNode: Int,
                                leftNode: Int, rightNode: Int,
                                leftThread: Int, rightThread: Int
                               ): Seq[Props] = {
    val x = Props("source" -> leftNode, "target" -> newNode, "start" -> "white", "thread" -> leftThread)
    val y = Props("source" -> rightNode, "target" -> newNode, "end" -> "white", "thread" -> rightThread)
    Seq(x, y)
  }
}
