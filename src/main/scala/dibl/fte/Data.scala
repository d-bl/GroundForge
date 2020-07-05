package dibl.fte

object Data {
  case class Cell(col: Int, value: Double)

  def apply(faces: Seq[Face], nodes: Map[String, Seq[TopoLink]], links: Seq[TopoLink]): Seq[Seq[Double]] = {
    val nodeIds = nodes.keys.toSeq

    def linkCell(link: TopoLink, value: Double) = Cell(links.indexOf(link), value)

    def nodeCell(id: String, value: Double) = Cell(nodeIds.indexOf(id), value)

    val cells1 = faces.map { face =>
      val left = face.leftArc.map(linkCell(_, 1))
      val right = face.rightArc.map(linkCell(_, -1))
      left ++ right
    }
    val cells2 = nodes.map { case (id, links) =>
      links.map(link => nodeCell(id, value(id, link))) // TODO value * weight
    }.toSeq
    (cells1 ++ cells2)
      .map { cells => // TODO functional approach?
        val row = new Array[Double](links.size)
        cells.foreach(cell =>
          row(cell.col) = cell.value
        )
        row.toSeq
      }
  }

  private def value(nodeId: String, link: TopoLink) = {
    if (link.sourceId == nodeId) 1
    else -1
  }
}
