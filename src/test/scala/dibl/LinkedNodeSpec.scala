package dibl

import dibl.LinkProps.simpleLink
import org.scalatest.{ FlatSpec, Matchers }

class LinkedNodeSpec extends FlatSpec with Matchers {

  // from patchWidth=6&patchHeight=5&tile=5-&tileStitch=ct&&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1
  val threads = Diagram(
    Seq(
      twist("a12", 4, 4), // 0
      twist("a11", 8, 4), // 1
      cross("a10", 6, 6), // 2
      twist("a11", 6, 6), // 3
      twist("a12", 6, 6), // 4
      cross("a10", 4, 8), // 5
      twist("a12", 4, 8), // 6
      cross("a10", 8, 8), // 7
      twist("a11", 8, 8), // 8
    ), Seq(
      whiteStart(0, 2),
      simpleLink(1, 2),
      whiteStart(0, 3),
      simpleLink(2, 3),
      simpleLink(2, 4),
      whiteStart(1, 4),
      simpleLink(3, 5),
      whiteStart(3, 6),
      whiteStart(4, 7),
      simpleLink(4, 8),
    )
  )
  val pairs = Diagram(
    Seq(
      stitch("a12", 0, 0), // 0
      stitch("a11", 2, 0), // 1
      stitch("a10", 1, 1), // 2
      stitch("a11", 0, 2), // 3
      stitch("a12", 2, 2), // 4
      stitch("a10", -1, 3), // 5
      stitch("a12", 0, 4), // 6
      stitch("a10", 3, 5), // 7
      stitch("a11", 2, 6), // 8
    ), Seq(
      simpleLink(0, 2),
      simpleLink(1, 2),
      simpleLink(0, 3),
      simpleLink(2, 3),
      simpleLink(2, 4),
      simpleLink(1, 4),
      simpleLink(3, 5),
      simpleLink(3, 6),
      simpleLink(4, 7),
      simpleLink(4, 8),
    )
  )

  "Diagram.tileLinks" should "determine Seq[LinkedNode]" in {

    val tileLinks = threads.tileLinks(6, 6, 6, 6)
    tileLinks.map(_.core) shouldBe Seq(
      threads.nodes(2), threads.nodes(3), threads.nodes(4),
    )
    tileLinks.map(_.clockwise) shouldBe Seq(
      Array(threads.nodes(0), threads.nodes(1), threads.nodes(4), threads.nodes(3)),
      Array(threads.nodes(0), threads.nodes(2), threads.nodes(6), threads.nodes(5)),
      Array(threads.nodes(2), threads.nodes(1), threads.nodes(7), threads.nodes(8)),
    )
  }

  it should "determine Sclockwise by coordinates" in {

    val tileLinks = pairs.tileLinks(1, 2, 2, 1)
    tileLinks.map(_.core) shouldBe Seq(
      threads.nodes(2), threads.nodes(3), threads.nodes(4),
    )
    tileLinks.map(_.clockwise) shouldBe Seq(
      Array(threads.nodes(0), threads.nodes(1), threads.nodes(4), threads.nodes(3)),
      Array(threads.nodes(0), threads.nodes(2), threads.nodes(6), threads.nodes(5)),
      Array(threads.nodes(2), threads.nodes(1), threads.nodes(7), threads.nodes(8)),
    )
  }

  "LinkedNode.clockwise" should "calculate clockwise by coordinates" in {
    LinkedNode(
      core = stitch("c", 1, 1),
      sources = Map(stitch("s", 0, 0) -> false, stitch("S", 2, 0) -> false),
      targets = Map(stitch("t", 2, 2) -> false, stitch("T", 0, 2) -> false),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "t", "T" )
  }

  // cross
  //    s    S
  // |      /  |
  // |   \     |
  // |    c    |
  // |     \   |
  // |  /      |
  //   T     t
  it should "determine a proper order for cross true true" in {
    LinkedNode(
      core = cross("c", 1, 1),
      sources = Map(twist("s", 1, 1) -> true, twist("S", 1, 1) -> false),
      targets = Map(twist("t", 1, 1) -> true, twist("T", 1, 1) -> false),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "t", "T" )
  }

  it should "determine a proper order for cross false true" in {
    LinkedNode(
      core = cross("c", 1, 1),
      sources = Map( twist("S", 1, 1) -> false, twist("s", 1, 1) -> true),
      targets = Map(twist("t", 1, 1) -> true, twist("T", 1, 1) -> false),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "t", "T" )
  }

  it should "determine a proper order for cross true false" in {
    LinkedNode(
      core = cross("c", 1, 1),
      sources = Map(twist("s", 1, 1) -> true, twist("S", 1, 1) -> false),
      targets = Map(twist("T", 1, 1) -> false, twist("t", 1, 1) -> true),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "t", "T" )
  }

  it should "determine a proper order for cross false false" in {
    LinkedNode(
      core = cross("c", 1, 1),
      sources = Map( twist("S", 1, 1) -> false, twist("s", 1, 1) -> true),
      targets = Map(twist("T", 1, 1) -> false, twist("t", 1, 1) -> true),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "t", "T" )
  }

  //  twist
  //  S     s
  //   \      |  |
  //      /   |  |
  //     c    |  |
  //    /     |  |
  //       \  |  |
  //  t     T
  it should "determine a proper order for twist true true" in {
    LinkedNode(
      core = twist("c", 1, 1),
      sources = Map(twist("s", 1, 1) -> true, twist("S", 1, 1) -> false),
      targets = Map(twist("t", 1, 1) -> true, twist("T", 1, 1) -> false),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "T", "t" )
  }

  it should "determine a proper order for twist false true" in {
    LinkedNode(
      core = twist("c", 1, 1),
      sources = Map( twist("S", 1, 1) -> false, twist("s", 1, 1) -> true),
      targets = Map(twist("t", 1, 1) -> true, twist("T", 1, 1) -> false),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "T", "t" )
  }

  it should "determine a proper order for twist true false" in {
    LinkedNode(
      core = twist("c", 1, 1),
      sources = Map(twist("s", 1, 1) -> true, twist("S", 1, 1) -> false),
      targets = Map(twist("T", 1, 1) -> false, twist("t", 1, 1) -> true),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "T", "t" )
  }

  it should "determine a proper order for twist false false" in {
    LinkedNode(
      core = twist("c", 1, 1),
      sources = Map( twist("S", 1, 1) -> false, twist("s", 1, 1) -> true),
      targets = Map(twist("T", 1, 1) -> false, twist("t", 1, 1) -> true),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "T", "t" )
  }

  it should "be empty if sources is empty" in {
    LinkedNode(
      core = cross("a11", 1, 1),
      sources = Map.empty,
      targets = Map(twist("a11", 1, 1) -> false, twist("a12", 1, 1) -> true),
    ).clockwise shouldBe Array.empty
  }

  private def whiteStart(source: Int, target: Int) = LinkProps.threadLink(source, target, threadNr = 0, whiteStart = true)

  private def cross(id: String, x: Int, y: Int) = NodeProps.node(s"twist - $id", x, y)

  private def twist(id: String, x: Int, y: Int) = NodeProps.node(s"cross - $id", x, y)
  private def stitch(id: String, x: Int, y: Int) = NodeProps.node(s"ctct - $id", x, y)
}
