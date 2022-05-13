/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html
*/

package dibl

import dibl.proto.{ Item, TilesConfig }

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("PairSvg") object PairSvg {

  private val diamondSize = 4.6 // half of the diagonal
  private val squareSize = 3.2 // half of the rib length
  private val circleSize = 3.8 // radius

  private def twistMark(count: Int) = {
    val xl = 2 * 0.8
    val xs = 1.5 * 0.8
    // TODO different variables for length and distance
    val d = if (count == 1) s"M 0,$xl 0,-$xl"
            else if (count == 2) s"M -1,$xl V -$xl M 1,$xl 1,-$xl"
                 else s"M -$xs,$xl V -$xl M $xs,$xl $xs,-$xl  M 0,$xl 0,-$xl"
    s"""<marker id="twist-$count"
       | viewBox="-2 -2 4 4"
       | markerWidth="5"
       | markerHeight="5"
       | orient="auto"
       | markerUnits="userSpaceOnUse">
       | <path d="$d"
       |  fill="#000"
       |  stroke="#000"
       |  stroke-width="0.7px"></path>
       |</marker>""".stripMargin
  }

  private def circle(r: Double = circleSize): String = s"M $r,0 A $r,$r 0 0 1 0,$r $r,$r 0 0 1 -$r,0 $r,$r 0 0 1 0,-$r $r,$r 0 0 1 $r,0 Z"

  private def square(d: Double = squareSize) = s"M -$d,-$d $d,-$d $d,$d -$d,$d Z"

  private def squareSE(d: Double = squareSize) = s"M $d,-$d $d,$d -$d,$d Z"

  private def squarePortrait(d: Double = squareSize) = s"m -${ d / 2 },-$d h $d v ${ 2 * d } h -$d z"

  private def squareSW(d: Double = squareSize) = s"M -$d,-$d $d,$d -$d,$d Z"

  private def squareNW(d: Double = squareSize) = s"M -$d,-$d $d,-$d -$d,$d Z"

  private def squareNE(d: Double = squareSize) = s"M -$d,-$d $d,-$d $d,$d Z"

  private def squareLeft(d: Double = squareSize) = s"M -$d,-$d H 0 v ${ 2 * d } h -$d z"

  private def squareRight(d: Double = squareSize) = s"M 0,-$d H $d V $d H 0 Z"

  private def squareTop(d: Double = squareSize) = s"M $d,0 V -$d h -${ 2 * d } v $d z"

  private def squareBottom(d: Double = squareSize) = s"M $d,$d V 0 h -${ 2 * d } V $d Z"

  private def diamond(d: Double = diamondSize) = s"M -$d,0 0,$d $d,0 0,-$d Z"

  private def diamondTopLeft(d: Double = diamondSize) = s"M -$d,0 0,-$d v $d z"

  private def diamondTopRight(d: Double = diamondSize) = s"M $d,0 0,-$d v $d z"

  private def diamondBottomLeft(d: Double = diamondSize) = s"m -$d,0 $d,$d v -$d z"

  private def diamondBottomRight(d: Double = diamondSize) = s"m $d,0 -$d,$d v -$d z"

  private def diamondBottom(d: Double = diamondSize) = s"M -$d,0 0,$d $d,0 Z"

  private def diamondRight(d: Double = diamondSize) = s"M 0,$d $d,0 0,-$d Z"

  private def diamondTop(d: Double = diamondSize) = s"M -$d,0 $d,0 0,-$d Z"

  private def diamondLeft(d: Double = diamondSize) = s"M -$d,0 0,$d 0,-$d Z"

  private def renderLinks(itemMatrix: Seq[Seq[Item]], itemList: Seq[(Int, Int, Item)]): String = {
    val nrOfRows = itemMatrix.size
    val nrOfCols = itemMatrix.head.size
    val links = for {
      (targetRow, targetCol, targetItem) <- itemList
      ((relSrcRow, relSrcCol), pairNrIntoTarget) <- targetItem.relativeSources.zipWithIndex // i: left/right incoming pair
      sourceRow = relSrcRow + targetRow
      sourceCol = relSrcCol + targetCol
      if sourceRow >= 0 && sourceRow < nrOfRows && sourceCol >= 0 && sourceCol < nrOfCols
    } yield (targetRow, targetCol, pairNrIntoTarget, sourceRow, sourceCol)

    /* map: (srcRow,srcCol) -> Seq(((targetRow,targetCol),pairNr)) */
    val targetsPerSource = links
      .groupBy { case (_, _, _, sourceRow, sourceCol) => (sourceRow, sourceCol) }
      .mapValues(_
        .sortBy { case (targetRow, targetCol, _, _, _) => targetCol * 1000 + targetRow }
        .map { case (targetRow, targetCol, _, _, _) => (targetRow, targetCol) }
        .zipWithIndex
      )

    links.map { case (targetRow, targetCol, pairNrIntoTarget, sourceRow, sourceCol) =>
      val targetItem = itemMatrix(targetRow)(targetCol)
      val leadingTwistsOfTarget = targetItem
        .stitch.replaceAll("c.*", "")
        .twistsOfPair(pairNrIntoTarget)
      val trailingTwistsOfSrc = targetsPerSource(sourceRow, sourceCol)
        .find { case ((row, col), _) => targetRow == row && targetCol == col }
        .map { case ((_, _), pairNrLeavingSrc) =>
          itemMatrix(sourceRow)(sourceCol)
            .stitch.replaceAll(".*c", "")
            .twistsOfPair(pairNrLeavingSrc)
        }.getOrElse("")
      val twists = trailingTwistsOfSrc + leadingTwistsOfTarget
      //      println(s"${ targetItem.id } $pairNrIntoTarget [x,y] [$sourceCol,$sourceCol] [$targetCol,$targetRow] ($trailingTwistsOfSrc,$leadingTwistsOfTarget) $twists")
      pathDescription(s"r${ sourceRow }c$sourceCol-r${ targetRow }c$targetCol", scale(sourceCol), scale(sourceRow), scale(targetCol), scale(targetRow), twists.length)
    }.mkString
  }.mkString

  private def pathDescription(id: String, sX: Double, sY: Double, tX: Double, tY: Double, twists: Int): String = {
    val marker = twists match {
      case 0 => ""
      case 1 | 2 | 3 => s"""; marker-mid: url("#twist-$twists")"""
      case _ => """; marker-mid: url("#twist-3")"""
    }
    val d = linkPath(twists > 0, sX, sY, tX, tY)
    val style = s"stroke: #000; stroke-width: 1px; fill: none; opacity: 1$marker"
    s"<path id='$id' class='link' d='$d' style='$style'></path>"
  }

  private def scale(c: Int) = (c + 2) * 15

  implicit class StitchString(val stitch: String) extends AnyVal {

    def twistsOfPair(pairNr: Int): String = {
      val search = if (pairNr == 0) "[^tl]"
                   else "[^tr]"
      stitch.replaceAll(search, "")
    }
  }

  private def renderNodes(itemList: Seq[(Int, Int, Item)]): String = {
    for {
      (row, col, targetItem) <- itemList
    } yield {
      val transform = s"""transform="translate(${ scale(col) },${ scale(row) })""""
      val title = s"""<title>${ targetItem.stitch } - ${ targetItem.id }</title>"""

      s"""<g id='r${ row }c$col' onclick='paint(this)' class="node" $transform>$title${ shapes(targetItem) }</g>"""
    }.mkString
  }.mkString

  private def shapes(targetItem: Item) = {
    def colour(nrOfTwists: Int) = {
      // https://colorbrewer2.org/?type=diverging&scheme=RdBu&n=5
      //                 red        blue       peach     light blue
      val colours = Seq("#ca0020", "#0571b0", "#f4a582", "#92c5de")
      colours(Math.min(colours.length - 1, nrOfTwists))
    }

    def colourRight(s: String) = colour(s.replaceAll("l", "").length)

    def colourLeft(s: String) = colour(s.replaceAll("r", "").length)

    def style(color: String) = s"""style="fill: $color; stroke: none; opacity: 0.85""""

    def shape(color: String, shape: String) = s"""<path d="$shape" ${ style(color) }></path>"""

    def diamond(lrTop: String, lrBottom: String) = {
      shape(colourLeft(lrTop), diamondTopLeft()) +
        shape(colourRight(lrTop), diamondTopRight()) +
        shape(colourLeft(lrBottom), diamondBottomLeft()) +
        shape(colourRight(lrBottom), diamondBottomRight())
    }

    val pale = "#f7f7f7" // center one of color brewer list
    val black = "#000000"

    val str = targetItem.stitch
      .replaceAll("^[tlr]*", "") // ignore leading twists
      .replaceAll("[tlr]*$", "") // ignore trailing twists
      .replaceAll("p", "") // ignore pins
      .replaceAll("t", "lr")
    val cs = str.replaceAll("[^c]", "").length
    val twists = str.split("c").filterNot(_.isEmpty).map(_.sortBy(identity))
    // println(s"$stitch ${ twists.mkString }")

    (cs, twists) match {
      case (_, _) if str == "c" => // just one c
        shape(black, circle(circleSize * 0.85))
      case (2, Array()) => // cc
        shape(colour(0), squareLeft()) + shape(colour(0), squareRight())
      case (3, Array()) => // ccc
        shape(colour(0), diamondLeft()) + shape(colour(0), diamondRight())
      case (2, Array(lr)) => // c.c
        shape(colourLeft(lr), squareLeft()) + shape(colourRight(lr), squareRight())
      case (3, Array(lrBottom)) if str.startsWith("cc") => // cc.c
        diamond(colour(0), lrBottom)
      case (3, Array(lrTop)) => // c.cc
        diamond(lrTop, colour(0))
      case (3, Array(lrTop, lrBottom)) => // c.c.c
        diamond(lrTop, lrBottom)
      case (nrOfCs, _) if nrOfCs > 3 && str.matches("c(lrc)+") => // plait
        shape(black, squarePortrait())
      case (nrOfCs, _) if nrOfCs > 3 && str.matches("c(rrc)?(llcrrc)+(llc)?") => // tallie
        shape(black, squareLeft()) + shape(black, squareRight())
      case _ =>
        shape(pale, circle()) // fall back
    }
  }

  @JSExport
  def legend(config: TilesConfig) = {
    listItems(config.getItemMatrix).zipWithIndex.map { case ((_, _, item),i) =>
      shapes(item) -> item.stitch
    }
  }

  private def listItems(itemMatrix: Seq[Seq[Item]]) = {
    for {
      row <- itemMatrix.indices
      col <- itemMatrix(row).indices
      targetItem = itemMatrix(row)(col)
      if !targetItem.noStitch && targetItem.relativeSources.nonEmpty
    } yield (row, col, targetItem)
  }

  /** Prefix required when writing to an SVG file */
  val prolog = "<?xml version='1.0' encoding='UTF-8'?>"

  @JSExport
  def linkPath(hasTwists: Boolean, sX: Double, sY: Double, tX: Double, tY: Double): String = {
    if (!hasTwists) s"M $sX,$sY $tX,$tY"
    else s"M $sX,$sY ${ sX + (tX - sX) / 2 } ${ sY + (tY - sY) / 2 } $tX,$tY"
  }

  /** @return an SVG document as String */
  @JSExport
  def render(config: TilesConfig,
             width: Int = 744,
             height: Int = 1052,
             zoom: Long = 2,
            ): String = {
    val itemMatrix = config.getItemMatrix
    val itemList = listItems(itemMatrix)
    println(legend(config))
    s"""
       |<svg
       | id="svg2"
       | version="1.1"
       | width="$width"
       | height="$height"
       | pointer-events="all"
       | xmlns="http://www.w3.org/2000/svg"
       | xmlns:svg="http://www.w3.org/2000/svg"
       | xmlns:xlink="http://www.w3.org/1999/xlink"
       |>
       |<defs>
       |  ${ twistMark(1) }
       |  ${ twistMark(2) }
       |  ${ twistMark(3) }
       |</defs>
       |<g transform="matrix($zoom,0,0,$zoom,0,0)">
       |${ renderLinks(itemMatrix, itemList) }
       |${ renderNodes(itemList) }
       |</g>
       |</svg>""".stripMargin
  }
}
