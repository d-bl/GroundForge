package dibl.fte

import org.ejml.simple.SimpleMatrix

import scala.util.{ Failure, Success, Try }

/** JVM API, not in src.main to allow the SBT compilation to JavaScript */
object Deltas {

  def apply(data: Seq[Seq[Double]],
            topoLinks: Seq[TopoLink],
           ): Try[Map[TopoLink, Delta]] = {
    //    println("data " + data.map(_.map(_.toInt).mkString(",")).mkString("; "))
    //    println("summed data " + data.map(_.map(_.toInt).sum).mkString(","))

    val t0 = System.nanoTime
    val svd = new SimpleMatrix(data.map(_.toArray).toArray).svd
    //    println("Elapsed time nullspace: " + (System.nanoTime - t0) * 0.000000001 + "s")

    val v = svd.getV
    val x = v.numCols() - 2
    val y = x + 1
    val secondLastOnDiagonalOfW = svd.getW.get(x, x)
    if (secondLastOnDiagonalOfW > 0.001)
      Failure(new Exception(s"no null space, second last diagonal element: $secondLastOnDiagonalOfW"))
    else Success((0 to y)
      .map(i => topoLinks(i) -> Delta(v.get(i, x), v.get(i, y)))
      .toMap
    )
  }
}
