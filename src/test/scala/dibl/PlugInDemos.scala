package dibl

import dibl.proto.TilesConfig

object PlugInDemos extends DemoFixture {
  def main(args: Array[String]): Unit = {
    // patch(aka swatch) size >= tile size; tile layout is simple G.W-F4
    val cfg = TilesConfig("patchWidth=4&patchHeight=4&d1=ctc&c1=ctc&b1=ctc&a1=ctc&d2=ctc&c2=ctcllctc&a2=ctcrrctc&d3=ctc&c3=ctc&b3=ctc&a3=ctc&c4=ctc&b4=ctc&a4=ctc&tile=1483,8-48,8314,488-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4")

    def node(row: Int, col: Int) = {
      val sources = cfg.getItemMatrix(row)(col).relativeSources
      if (sources.length != 2) ""
      else {
        val Array((x1, y1), (x2, y2)) = sources
        s"[$x1,$y1,$row,$col,$x2,$y2]"
      }
    }

    val width = cfg.centerMatrixCols
    val height = cfg.centerMatrixRows
    val links = for {
      row <- 0 until height
      col <- 0 until width
    } yield node(row, col)
    println(links.filterNot(_.isEmpty).mkString(s"$width\t$height\n", "\n", ""))
  }
}
