package dibl

import dibl.proto.TilesConfig

object InkscapeTemplateDemos extends DemoFixture {
  def main(args: Array[String]): Unit = {
    // patchWidth: 3 * tile-width;
    // patchHeight: 2 * tile-height
    val cfg = TilesConfig("patchWidth=12&patchHeight=8&d1=ctc&c1=ctc&b1=ctc&a1=ctc&d2=ctc&c2=ctcllctc&a2=ctcrrctc&d3=ctc&c3=ctc&b3=ctc&a3=ctc&c4=ctc&b4=ctc&a4=ctc&tile=1483,8-48,8314,488-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4")
    val width = cfg.centerMatrixCols
    val height = cfg.centerMatrixRows
    val diagram = NewPairDiagram.create(cfg)
    val scale = 15
    val scaledWidth = width * scale
    val scaledHeight = height * scale

    def inCenterBottomTile(link: LinkProps) = {
      val n = diagram.nodes(link.source)
      n.x >= scaledWidth && n.x < 2 * scaledWidth && n.y >= scaledHeight && n.y < 2 * scaledHeight
    }

    val links = diagram.links
      .filter(inCenterBottomTile)
      .groupBy(_.source)
      .map { case (src, targets) =>
        val Seq(l1, l2) = targets
        val s = diagram.nodes(src)
        val t1 = diagram.nodes(l1.target)
        val t2 = diagram.nodes(l2.target)
        s"[${ s.x / scale },${ s.y / scale },${ t1.x / scale },${ t1.y / scale },${ t2.x / scale },${ t2.y / scale }]"
      }
    println(links.mkString(s"CHECKER\t$width\t$height\n", "\n", ""))
  }
}