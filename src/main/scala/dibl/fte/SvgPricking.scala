package dibl.fte

import dibl.fte.GraphCreator.Locations

object SvgPricking {
  def apply(nodes: Locations,
            links: Seq[TopoLink],
            tileVectors: Seq[(Double, Double)],
           ): String = {

    implicit val width: Double = 3
    implicit val color: String = "rgb(0,0,0)"
    val lines = links
      .map(l => l -> (nodes(l.sourceId) -> nodes(l.targetId)))
      .map { case (TopoLink(s, t, _, _), ((x1, y1), (x2, y2))) =>
        line(s"$s-$t", x1, y1, x2, y2)
      } ++ tileVectors
      .map { case (dx, dy) =>
        line(s"${ links.head.sourceId }", 0, 0, dx, dy)(3, "rgb(0,255,0)")
      }
    s"""<svg xmlns="http://www.w3.org/2000/svg" width="500" height="500">
       |${ lines.mkString("\n") }
       |</svg>
       |""".stripMargin
  }

  private def line(classAttr: String, x1: Double, y1: Double, x2: Double, y2: Double)
                  (implicit width: Double, color: String): String = {
    s"""<line class="$classAttr" x1="${ 100 * x1 + 200 }" y1="${ 100 * y1 + 200 }" x2="${ 100 * x2 + 200 }" y2="${ 100 * y2 + 200 }" style="stroke-width: ${ width / 2 };stroke: $color" />"""
  }
}
