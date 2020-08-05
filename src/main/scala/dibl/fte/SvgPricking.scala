/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl.fte

import scala.scalajs.js.{Array, Dictionary}
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("SvgPricking") object SvgPricking {
  @JSExport
  def create(topoLinks: Seq[TopoLink], jsNullspace: Dictionary[Any]): String = {
    println("SvgPrincking.apply")
    val rows = jsNullspace("m").toString.toInt
    val cols = jsNullspace("n").toString.toInt
    val nullspace = jsNullspace("val").asInstanceOf[Array[Double]].sliding(cols).toSeq.map(_.toSeq)
    if (cols != 2 || rows != topoLinks.size) {
      println(s"nullSpace dimensions are ($rows,$cols) expected (${ topoLinks.size },2)")
      "whoops"
    } else {
      val deltas = (0 until rows)
        .map(i => topoLinks(i) -> Delta(nullspace(i).head, nullspace(i)(1)))
        .toMap
      apply(deltas)
    }
  }

  // TODO make implicit like scale
  private val offsetX = 300
  private val offsetY = 250

  def apply(deltas: Map[TopoLink, Delta]): String = {
    val topoLinks = deltas.keys
    val startId = topoLinks.head.sourceId
    val nodes = Locations.create(Map(startId -> (0, 0)), deltas)
    val tileVectors = TileVector(startId, deltas).toSeq

    implicit val scale: Int = 100
    val tile = deltas.map { case (tl @ TopoLink(s, t, _, _, _), Delta(dx, dy)) =>
      val (x1, y1) = nodes(s)
      val l = line(x1, y1, x1 - dx, y1 - dy, s"""id="$s-$t" style="stroke:rgb(0,0,0);stroke-width:4" """)
        .replace("/>", s"><title>$tl</title></line>")
      s"""<a href="#" onclick="clickedLink(this); return false;">$l</a>"""
    }
    val dots = nodes.map{ case (id,(x,y)) =>
      val c = s"""<circle id="$id" cx="${ scale * x + offsetX}" cy="${ scale * y + offsetY}" r="5" style="fill:rgb(225,0,0);opacity:0.65"><title>$id</title></circle>"""
      s"""<a href="#" onclick="clickedDot(this); return false;">$c</a>"""
    }
    val clones = if (tileVectors.isEmpty) Seq("")
                 else {
                   val vectorLines = tileVectors.map { case (dx, dy) =>
                     line(0, 0, dx, dy, """style="stroke:rgb(0,255,0);stroke-width:3" """)
                   }
                   val (dx1, dy1) = tileVectors.head
                   val (dx2, dy2) = tileVectors.tail.headOption.getOrElse((-dy1 * 4, dx1 * 4))
                   val clones = for {
                     i <- -3 * scale to 6 * scale by scale
                     j <- -3 * scale to 6 * scale by scale
                   } yield {
                     if (i == 0 && j == 0) ""
                     else s"""<use transform="translate(${ i * dx1 + j * dx2 },${ i * dy1 + j * dy2 })" xlink:href="#tile" style="opacity:0.65"/>"""
                   }
                   vectorLines ++ clones
                 }
    s"""<svg
       |  xmlns="http://www.w3.org/2000/svg"
       |  xmlns:xlink="http://www.w3.org/1999/xlink"
       |  id="svg2" version="1.1"
       |  width="${ 5 * scale }" height="${ 5 * scale }"
       |>
       |<g id="tile">
       |${ tile.mkString("\n") }
       |${ dots.mkString("\n") }
       |</g>
       |${ clones.mkString("\n") }
       |</svg>
       |""".stripMargin
  }

  private def line(x1: Double, y1: Double, x2: Double, y2: Double, attrs: String)(implicit scale: Int): String = {
    s"""  <line x1="${ scale * x1 + offsetX }" y1="${ scale * y1 + offsetY }" x2="${ scale * x2 + offsetX }" y2="${ scale * y2 + offsetY }" $attrs/>"""
  }
}
