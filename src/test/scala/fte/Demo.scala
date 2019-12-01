package fte

import dibl.proto.TilesConfig
import fte.data.Graph
import fte.layout.OneFormTorus
import fte.ui.SVGRender

object Demo {
  def main(args: Array[String]): Unit = {

    // patch size must match one checker matrix
    val config = TilesConfig("tile=586-,-4-5,5-21,-5-7&shiftColsSE=4&shiftRowsSE=4&shiftColsSW=0&shiftRowsSW=4&patchWidth=4&patchHeight=4&")
    val matrix = config.getItemMatrix.map(_.map(_.relativeSources))
    val cols = config.centerMatrixCols // also offset to get generated graph within the viewport
    val graph = new Graph(config.centerMatrixRows, cols)

    def addNode(destRow: Int, destCol: Int): Unit = {
      val srcNodes =  matrix(destRow)(destCol)
      if (srcNodes.isEmpty) return
      val (srcRow1, srcCol1) = srcNodes(0)
      val (srcRow2, srcCol2) = srcNodes(1)
      graph.createPairsIn(
        cols + destCol, destRow,
        cols + destCol + srcCol1, cols + destRow + srcRow1,
        cols + destCol + srcCol2, cols + destRow + srcRow2,
      )
    }

    println(matrix.map(_.map(_.mkString("[",",","]")).mkString(",")).mkString("\n"))
    for {
      row <- matrix.indices
      col <- matrix(row).indices
    } yield addNode(row, col)
    println(graph.getEdges)
    println(graph.getVertices)

    if (new OneFormTorus(graph).layout())
      new SVGRender().draw(graph, "target/test/vmi2.svg")
  }
}
