package dibl

import dibl.proto.TilesConfig

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("InkscapeTemplate") object InkscapeTemplate {

  @JSExport
  def fromUrl(query: String): String = {
    fromConfig(TilesConfig(query))
  }

  @JSExport
  def fromConfig(cfg: TilesConfig): String = {
    fromDiagram(cfg, NewPairDiagram.create(cfg))
  }

  @JSExport
  def fromDiagram(cfg: TilesConfig, diagram: Diagram): String = {
    val width = cfg.centerMatrixCols
    val height = cfg.centerMatrixRows
    if (width * 3 > cfg.patchWidth || height * 2 > cfg.patchHeight) return {
      s"""Swatch (alias patch [${cfg.patchWidth},${cfg.patchHeight}]) should be at least 3 tiles [$width,$height] wide and 2 high.
         |  ${ cfg.urlQuery }""".stripMargin
    }
    // TODO check for simple (alias checker) matrix

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
      .withFilter { case (_, targets) => targets.size == 2}
      .map { case (src, targets) =>
        val Seq(l1, l2) = targets
        val s = diagram.nodes(src)
        val t1 = diagram.nodes(l1.target)
        val t2 = diagram.nodes(l2.target)
        s"[${ s.x / scale },${ s.y / scale },${ t1.x / scale },${ t1.y / scale },${ t2.x / scale },${ t2.y / scale }]"
      }
    links.mkString("CHECKER\t"+width+"\t"+height+ "\n", "\n", "")
  }
}
