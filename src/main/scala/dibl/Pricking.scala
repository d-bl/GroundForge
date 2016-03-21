package dibl

import dibl.Matrix._

import scala.scalajs.js.annotation.JSExport

@JSExport
object Pricking {

  @JSExport
  def get(key: String, nr: Int): String = {
    val s = matrixMap.get(key).map(_ (nr)).get // TODO error handling
    val m = toRelSrcNodes(matrix = s, dimensions = key).get
    val (height, width) = dims(key).get
    def cloneRows(i: Int): String = {
      val ii = i + height * 10
      List.range(start = -width * 5, end = 500, step = width * 10).
        map(w => clone(w, i)+clone(w-width*5, ii)).mkString("")
    }
    val clones = List.range(start = -height * 10, end = 500, step = height * 20).
      map(h => cloneRows(h)).mkString("").replace(clone(0,0),"")
    val nameSpaces = "xmlns:xlink='http://www.w3.org/1999/xlink' xmlns='http://www.w3.org/2000/svg'"
    val a4 = "height='1052' width='744'"
    s"<svg version='1.1' id='svg2' $a4 $nameSpaces><g id='g1'>${createOriginal(m)}</g>$clones</svg>"
  }

  def clone(i: Int, j: Int): String = {
    val id = createId(i, j)
    s"<use transform='translate($i,$j)' xlink:href='#g1' id='u$id' height='100%' width='100%' y='0' x='0'/>"
  }

  def createOriginal(m: M): String = {
    var paths = ""
    for {
      i <- m.indices
      j <- m(0).indices
    } paths = paths + createNode((i, j), m(i)(j))
    paths
  }

  def createNode(target: (Int, Int), n: SrcNodes) = {
    if (n.length < 2) ""
    else {
      val (i, j) = target
      val id = createId(i, j)
      createLink(s"p1$id", target, n(0)) +
        createLink(s"p2$id", target, n(1))
    }
  }

  def createId(i: Int, j: Int): String =
    f"${i + 500}%03d${j + 500}%03d"

  def createLink(id: String, target: (Int, Int), src: (Int, Int)): String = {
    val (y, x) = target
    val (dy, dx) = src
    val offset = 120
    val s = s"${offset + (x * 10)},${offset + (y * 10)} ${offset + (dx + x) * 10},${offset + (dy + y) * 10}"
    s"<path id='$id' d='M $s' style='stroke:#000'/>"
  }
}
