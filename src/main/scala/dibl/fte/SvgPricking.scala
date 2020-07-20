package dibl.fte

import dibl.fte.GraphCreator.Locations

object SvgPricking {
  def apply(nodes: Locations,
            deltas: Map[TopoLink, Delta],
            tileVectors: Seq[(Double, Double)],
           ): String = {

    implicit val width: Double = 3
    implicit val color: String = "rgb(0,0,0)"
    val tile = deltas.map { case (TopoLink(s, t, _, _), Delta(dx, dy)) =>
      val (x1, y1) = nodes(s)
      line(s"$s-$t", x1, y1, x1 + dx, y1 + dy)
    }
    val vectors = tileVectors.map { case (dx, dy) =>
      line(s"$dx $dy", 0, 0, dx, dy)(3, "rgb(0,255,0)")
    }
    val (dx1, dy1) = tileVectors.head
    val (dx2, dy2) = tileVectors.tail.headOption.getOrElse((-dy1 * 4, dx1 * 4))
    val clones = for {
      i <- -200 to 400 by 100
      j <- -200 to 400 by 100
    } yield {
      s"""<use transform="translate(${ i * dx1 + j * dx2 },${ i * dy1 + j * dy2 })" xlink:href="#tile" style="opacity:0.5"/>"""
    }
    s"""<svg
       |  xmlns="http://www.w3.org/2000/svg"
       |  xmlns:svg="http://www.w3.org/2000/svg"
       |  xmlns:xlink="http://www.w3.org/1999/xlink"
       |  id="svg2" version="1.1"
       |  width="500" height="500"
       |>
       |<svg:g id="tile">
       |${ tile.mkString("\n") }
       |</svg:g>
       |${ vectors.mkString("\n") }
       |${ clones.mkString("\n") }
       |</svg>
       |""".stripMargin
  }

  private def line(classAttr: String, x1: Double, y1: Double, x2: Double, y2: Double)
                  (implicit width: Double, color: String): String = {
    s"""  <line class="$classAttr" x1="${ 100 * x1 + 200 }" y1="${ 100 * y1 + 200 }" x2="${ 100 * x2 + 200 }" y2="${ 100 * y2 + 200 }" style="stroke-width: ${ width / 2 };stroke: $color" />"""
  }
}
