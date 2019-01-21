package dibl

import org.scalatest.{FlatSpec, Matchers}

class TileLinksSpec extends FlatSpec with Matchers {
  // try the config argument as URL-query on the tiles page
  // hover over the stitches in the pair diagram to visualize the clockwise listings of nodes

  "Diagram.tileLinks" should "find links in clockwise order around nodes in an area of a pair diagram" in {
    // TODO Make an N link from this example in the demo section for paris ground.
    val config = TilesConfig("patchWidth=11&patchHeight=12&tile=-5-,B-C&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2")
    val diagram = NewPairDiagram.create(config)
    // TODO replace hard coded boundaries when implemented by Config
    val offsetCols = (1.5 + 3) * 15
    val offsetRows = (1.5 + 5) * 15
    diagram.tileLinks(
      offsetCols,
      offsetRows + 15 * config.centerMatrixRows,
      offsetCols + 15 * config.centerMatrixCols,
      offsetRows,
    ).map { case (core, clockWise: Array[NodeProps]) =>
      core.id -> clockWise.map(_.id).mkString(",")
    } shouldBe Array(
      "a2" -> "c2,b1,c2,b1",
      "b1" -> "c2,a2,c2,a2",
      "c2" -> "b1,a2,b1,a2",
    )
  }

  "Config.linksOfCenterTile" should "call Diagram.tileLinks for a thread diagram" in {
    val config = TilesConfig("patchWidth=6&patchHeight=5&tile=5-&tileStitch=ct&&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1")
    val diagram = ThreadDiagram.create(NewPairDiagram.create(config))
    config.linksOfCenterTile(diagram, 2)
      .map { case (core, clockWise: Array[NodeProps]) =>
        core.id -> clockWise.map(_.id).mkString(",")
      } shouldBe Array(
      "a12" -> "a10,a11,a10,a11",
      "a11" -> "a12,a10,a12,a10",
      "a10" -> "a12,a11,a12,a11",
    )
  }

  it should "work for a rose ground" in {
    val config = TilesConfig("patchWidth=12&patchHeight=12&tile=831,4-7,-5-&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2")
    val diagram = NewPairDiagram.create(config)
    config.linksOfCenterTile(diagram, 1)
      .map { case (core, clockWise: Array[NodeProps]) =>
        core.id -> clockWise.map(_.id).mkString(",")
      } shouldBe Array(
      "a1" -> "b3,c2,b1,a2",
      "b1" -> "a1,c1,c2,a2",
      "c1" -> "a2,b3,c2,b1",
      "a2" -> "a1,b1,b3,c1",
      "c2" -> "b1,c1,a1,b3",
      "c1" -> "a2,b3,c2,b1",
      "b3" -> "a2,c2,a1,c1",
      "a1" -> "b3,c2,b1,a2",
    )
  }
}
