package dibl.fte

import org.ejml.simple.SimpleMatrix

import scala.util.{ Failure, Success, Try }

case class Delta(dx: Double, dy: Double)

object Delta {
  // TODO javascript interface to nanolib

  /** version for a java environment */
  def apply(data: Seq[Seq[Double]],
            topoLinks: Seq[TopoLink],
           ): Try[Seq[(TopoLink, Delta)]] = {
    //    println("data " + data.map(_.map(_.toInt).mkString(",")).mkString("; "))
    //    println("summed data " + data.map(_.map(_.toInt).sum).mkString(","))

    val t0 = System.nanoTime
    val nullSpace = new SimpleMatrix(data.map(_.toArray).toArray).svd.nullSpace
    //    println("Elapsed time nullspace: " + (System.nanoTime - t0) * 0.000000001 + "s")

    val cols = nullSpace.numCols()
    val rows = nullSpace.numRows()
    if (cols != 2 && rows != topoLinks.size) {
      val msg = s"nullSpace dimensions ($rows,$cols) expected (${ topoLinks.size },2)"
      println(msg)
      Failure(new Exception(msg))
    }
    else Try((0 until rows)
      .map(i => topoLinks(i) -> Delta(nullSpace.get(i, 0), nullSpace.get(i, 1)))
    )
  }
}
