package dibl

import dibl.LinkProps.simpleLink
import org.scalatest.{ FlatSpec, Matchers }

class LinkedNodeSpec extends FlatSpec with Matchers {

  "Diagram.tileLinks" should "determine clockwise by over/under rules" in {
    // from patchWidth=6&patchHeight=5&tile=5-&tileStitch=ct&&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1
    val diagram = Diagram(
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
        simpleLink(0, 3),
        whiteStart(2, 3),
        simpleLink(2, 4),
        whiteStart(1, 4),
        simpleLink(3, 5),
        whiteStart(3, 6),
        whiteStart(4, 7),
        simpleLink(4, 8),
      )
    )
    val tileLinks = diagram.tileLinks(6, 6, 6, 6)
    tileLinks.map(_.core) shouldBe Seq(2,3,4).map(diagram.nodes)
    tileLinks.map(_.clockwise.map(diagram.nodes.indexOf)).toArray shouldBe
      Array(Array(0,1,4,3), Array(0,2,6,5), Array(2,1,7,8)) // node numbers
  }

  it should "determine clockwise by coordinates" in {
    val diagram = Diagram(
      Seq(
        stitch("b1", 0, 0), // 0
        stitch("d1", 2, 0), // 1
        stitch("c2", 1, 1), // 2
        stitch("b3", 0, 2), // 3
        stitch("d3", 2, 2), // 4
        stitch("a4", -1, 3), // 5
        stitch("b5", 0, 4), // 6
        stitch("d6", 3, 5), // 7
        stitch("c7", 2, 6), // 8
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
    val tileLinks = diagram.tileLinks(1, 2, 2, 0)
    tileLinks.map(_.core) shouldBe Seq(2,3,4).map(diagram.nodes)
    tileLinks.map(_.clockwise.map(diagram.nodes.indexOf)).toArray shouldBe
      Array(Array(0,1,4,3), Array(0,2,6,5), Array(2,1,7,8)) // node numbers
  }

  "LinkedNode.clockwise" should "be calculated by coordinates" in {
    LinkedNode(
      core = stitch("c", 1, 1),
      sources = Map(stitch("s", 0, 0) -> false, stitch("S", 2, 0) -> false),
      targets = Map(stitch("t", 2, 2) -> false, stitch("T", 0, 2) -> false),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "t", "T" )
  }

  // cross
  //    S    s
  // |      /  |
  // |   \     |
  // |    c    |
  // |     \   |
  // |  /      |
  //   T     t
  // links with a capital S/T have a white start
  // sources[i] -> true means the link from source to core has a white start.
  // targets[i] -> true means the link from core to target has a white start.
  it should "determine a proper order for cross true true" in {
    LinkedNode(
      core = cross("c"),
      sources = Map(twist("S") -> true, twist("s") -> false),
      targets = Map(twist("T") -> true, twist("t") -> false),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "t", "T" )
  }

  it should "determine a proper order for cross false true" in {
    LinkedNode(
      core = cross("c"),
      sources = Map(twist("S") -> true, twist("s") -> false),
      targets = Map(twist("t") -> false, twist("T") -> true),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "t", "T" )
  }

  it should "determine a proper order for cross true false" in {
    LinkedNode(
      core = cross("c"),
      sources = Map(twist("s") -> false, twist("S") -> true),
      targets = Map(twist("T") -> true, twist("t") -> false),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "t", "T" )
  }

  it should "determine a proper order for cross false false" in {
    LinkedNode(
      core = cross("c"),
      sources = Map(twist("s") -> false, twist("S") -> true),
      targets = Map(twist("t") -> false, twist("T") -> true),
    ).clockwise.map(_.id) shouldBe Array("S", "s", "t", "T" )
  }

  // twist
  // s     S
  //  \      |  |
  //     /   |  |
  //    c    |  |
  //   /     |  |
  //      \  |  |
  // t     T
  // links with a capital S/T have a white start
  it should "determine a proper order for twist true true" in {
    LinkedNode(
      core = twist("c"),
      sources = Map(twist("S") -> true, twist("s") -> false),
      targets = Map(twist("T") -> true, twist("t") -> false),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "T", "t" )
  }

  it should "determine a proper order for twist false true" in {
    LinkedNode(
      core = twist("c"),
      sources = Map(twist("S") -> true, twist("s") -> false),
      targets = Map(twist("t") -> false, twist("T") -> true),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "T", "t" )
  }

  it should "determine a proper order for twist true false" in {
    LinkedNode(
      core = twist("c"),
      sources = Map(twist("s") -> false, twist("S") -> true),
      targets = Map(twist("T") -> true, twist("t") -> false),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "T", "t" )
  }

  it should "determine a proper order for twist false false" in {
    LinkedNode(
      core = twist("c"),
      sources = Map(twist("s") -> false, twist("S") -> true),
      targets = Map(twist("t") -> false, twist("T") -> true),
    ).clockwise.map(_.id) shouldBe Array("s", "S", "T", "t" )
  }

  it should "be empty if sources is empty" in {
    LinkedNode(
      core = cross("a11"),
      sources = Map.empty,
      targets = Map(twist("a11") -> false, twist("a12") -> true),
    ).clockwise shouldBe Array.empty
  }

  private def whiteStart(source: Int, target: Int) = LinkProps.threadLink(source, target, threadNr = 0, whiteStart = true)

  private def cross(id: String, x: Int = 1, y: Int = 1) = NodeProps.node(s"twist - $id", x, y)

  private def twist(id: String, x: Int = 1, y: Int = 1) = NodeProps.node(s"cross - $id", x, y)
  private def stitch(id: String, x: Int, y: Int) = NodeProps.node(s"ctct - $id", x, y)
}
