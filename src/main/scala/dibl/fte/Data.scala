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

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }
import scala.util.{ Failure, Success, Try }

@JSExportTopLevel("TopoLink") object Data {
  case class Cell(col: Int, value: Double)

  @JSExport
  def create(links: Seq[TopoLink]): Seq[Seq[Double]] = {
    apply(links).recoverWith {case t =>
      t.printStackTrace()
      Failure(t)
    }.getOrElse(Seq[Seq[Double]]())
  }

  def apply(links: Seq[TopoLink]): Try[Seq[Seq[Double]]] = {

    val duplicates = links.diff(links.distinct)
    if (duplicates.nonEmpty)
      return Failure(new IllegalArgumentException(s"Duplicated links: $duplicates"))
    if (links.exists(l => l.sourceId == l.targetId))
      return Failure(new IllegalArgumentException(s"Links with same start as end: ${ links.mkString(";") }"))

    val faces: Seq[Face] = Face(links)
    val nodes: Map[String, Seq[TopoLink]] = ClockWise(links)

    def cell(link: TopoLink, value: Double) = Cell(links.indexOf(link), value)

    val cells1 = faces.map { face =>
      val left = face.leftArc.map(cell(_, 1))
      val right = face.rightArc.map(cell(_, -1))
      left ++ right
    }
    val cells2 = nodes.map { case (id, links) =>
      links.map(link => cell(link, value(id, link) * link.weight))
    }.toSeq
    Success((cells1 ++ cells2)
      .map { cells => // TODO functional approach?
        val row = new Array[Double](links.size)
        cells.foreach(cell =>
          row(cell.col) = cell.value
        )
        row.toSeq
      })
  }

  private def value(nodeId: String, link: TopoLink) = {
    if (link.sourceId == nodeId) 1
    else -1
  }
}
