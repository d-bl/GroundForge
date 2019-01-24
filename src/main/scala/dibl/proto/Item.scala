package dibl.proto

import dibl.SrcNodes
import dibl.Stitches.defaultColorValue

case class Item(id: String,
                vectorCode: Char = '-',
                stitch: String = "",
                isOpaque: Boolean = false,
                relativeSources: SrcNodes
               ) {
  lazy val noStitch: Boolean = stitch.isEmpty || stitch == "-"
  lazy val color: Option[String] = Option(defaultColorValue(stitch))
    .filter(_.nonEmpty)
}
object Item {
  def cleanupIgnoredStitches(itemMatrix: Array[Array[Item]]): Unit = {

    def isFringe(row: Int, col: Int) = {
      row < 0 || col < 0 || col >= itemMatrix(row).length
    }

    for {
      row <- itemMatrix.indices
      (a,b) = itemMatrix(row).indices.splitAt(itemMatrix(row).length/2)
      col <- a.reverse ++ b
    } {
      // replace Y with V; the tail of the Y are two links connecting the same two nodes
      def y2v(sharedSource: (Int, Int)) = {
        val (sharedRow, sharedCol) = sharedSource
        val srcRow = row + sharedRow
        val srcCol = col + sharedCol
        if (isFringe(srcRow, srcCol)) false
        else {
          val srcItem = itemMatrix(srcRow)(srcCol)
          val Array((leftRow, leftCol), (rightRow, rightCol)) = srcItem.relativeSources
          val newSrcNodes = SrcNodes((sharedRow + leftRow, sharedCol + leftCol), (sharedRow + rightRow, sharedCol + rightCol))
          // relink the bottom of the Y to the tops
          itemMatrix(row)(col) = itemMatrix(row)(col).copy(relativeSources = newSrcNodes)
          // drop the core of the Y
          itemMatrix(srcRow)(srcCol) = srcItem.copy(relativeSources = SrcNodes())
          true
        }
      }

      /**
       * The current (row,col) is the absolute position of the bottom of the ascii-art graph:
       * .    VV
       * .    V
       * The length of the legs in the top row are reduced to zero when used for replacement.
       * @param relativeSource the tip of one leg of the bottom V
       * @param innerLeg       returns the tip of a leg of one of the top V's
       * @param outerLeg       returns the other leg
       * @return the replacement for relativeSource.
       */
      def indirectSource(relativeSource: (Int, Int),
                         innerLeg: Array[(Int, Int)] => Option[(Int, Int)],
                         outerLeg: Array[(Int, Int)] => Option[(Int, Int)],
                        ): (Int, Int) = {
        val (relativeSourceRow, relativeSourceCol) = relativeSource
        val absSrcRow = row + relativeSourceRow
        val absSrcCol = col + relativeSourceCol
        if (isFringe(absSrcRow, absSrcCol)) relativeSource
        else {
          val srcItem = itemMatrix(absSrcRow)(absSrcCol)
          if (!srcItem.noStitch) relativeSource
          else {
            val innerSrc = innerLeg(srcItem.relativeSources).getOrElse((0, 0))
            val outerSrc = outerLeg(srcItem.relativeSources).getOrElse((0, 0))
            val (srcRow, srcCol) = (innerSrc, outerSrc) match {
              case ((0, 0), (0, 0)) => return relativeSource
              case ((0, 0), _) => outerSrc
              case (_, _) => innerSrc
            }
            // reduce used leg to length zero, next time the other leg will be used
            itemMatrix(absSrcRow)(absSrcCol) = srcItem.copy(
              relativeSources = srcItem.relativeSources.map(src =>
                if (src == (srcRow, srcCol)) (0, 0) // don't follow this path again
                else src // don't change the other path
              ))
            // reconnect bottom leg
            val dCol = if (row + srcRow < 0) 0
                       else srcCol // don't reconnect with something in the fringe
            (relativeSourceRow + srcRow, relativeSourceCol + dCol)
          }
        }
      }

      def replace(item: Item): Boolean = {
        if (item.relativeSources.isEmpty || item.noStitch) false
        else {
          val Array(directLeft, directRight) = item.relativeSources
          if (directLeft == directRight)
            y2v(directLeft)
          else {
            val indirectLeft = indirectSource(directLeft, _.lastOption, _.headOption)
            val indirectRight = indirectSource(directRight, _.headOption, _.lastOption)
            val replacement = SrcNodes(indirectLeft, indirectRight)
            if (item.relativeSources sameElements replacement) false
            else {
              // println(s"replacing ${item.id} at $row,$col : ${item.relativeSources.mkString} -> ${replacement.mkString}")
              itemMatrix(row)(col) = item.copy(relativeSources = replacement)
              true
            }
          }
        }
      }
      while (replace(itemMatrix(row)(col))){}
    }

    // drop ignored stitches
    for {
      row <- itemMatrix.indices
      col <- itemMatrix(row).indices
    } {
      val item = itemMatrix(row)(col)
      if (item.noStitch)
        itemMatrix(row)(col) = item.copy(relativeSources = SrcNodes())
    }
  }

}
