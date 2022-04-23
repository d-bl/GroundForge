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

import dibl.Stitches.defaultColorName
import dibl.proto.{ Item, TilesConfig }

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }

@JSExportTopLevel("PairSvg") object PairSvg {

  private def circle(r: Double): String = s"M $r,0 A $r,$r 0 0 1 0,$r $r,$r 0 0 1 -$r,0 $r,$r 0 0 1 0,-$r $r,$r 0 0 1 $r,0 Z"

  private def square(d: Double) = s"M -$d,-$d $d,-$d $d,$d -$d,$d Z"

  private def squareSE(d: Double) = s"M $d,-$d $d,$d -$d,$d Z"

  private def squareSW(d: Double) = s"M -$d,-$d $d,$d -$d,$d Z"

  private def squareNW(d: Double) = s"M -$d,-$d $d,-$d -$d,$d Z"

  private def squareNE(d: Double) = s"M -$d,-$d $d,-$d Z"

  private def diamond(d: Double) = s"M -$d,0 0,$d $d,0 0,-$d Z"

  private def diamondS(d: Double) = s"M -$d,0 0,$d $d,0 Z"

  private def diamondE(d: Double) = s"M 0,$d $d,0 0,-$d Z"

  private def diamondN(d: Double) = s"M -$d,0 $d,0 0,-$d Z"

  private def diamondW(d: Double) = s"M -$d,0 0,$d 0,-$d Z"

  val markerDefinitions: String = {
    def pairMarker(idSuffix: String = "pair", shape: String = diamond(5)) =
      s"""<marker id="start-$idSuffix"
         | viewBox="-7 -7 14 14"
         | markerWidth="12"
         | markerHeight="12"
         | orient="auto"
         | markerUnits="userSpaceOnUse">
         | <path d="$shape"
         |  fill="#000"
         |  style="opacity: 0.5;"></path>
         |</marker>
         |""".stripMargin.stripLineEnd.replaceAll("[\r\n]", "")

    def twistMark(count: Int) =
      s"""<marker id="twist-$count"
         | viewBox="-2 -2 4 4"
         | markerWidth="5"
         | markerHeight="5"
         | orient="auto"
         | markerUnits="userSpaceOnUse">
         | <path d="${
        if (count == 1) "M 0,2 0,-2"
        else "M -1,2 V -2 M 1,2 1,-2"
      }"
         |  fill="#000"
         |  stroke="#000"
         |  stroke-width="1px"></path>
         |</marker>""".stripMargin.stripLineEnd.replaceAll("[\r\n]", "")

    s"""<defs>
       |  ${ pairMarker() }
       |  ${ twistMark(1) }
       |  ${ twistMark(2) }
       |</defs>""".stripMargin.stripLineEnd.replaceAll("[\r\n]", "")
  }

  private def renderLinks(items: Seq[Seq[Item]]): String = {
    val nrOfRows = items.size
    val nrOfCols = items.head.size
    val links = for {
      targetRow <- items.indices
      targetCol <- items(targetRow).indices
      targetItem = items(targetRow)(targetCol)
      if !targetItem.noStitch && targetItem.relativeSources.nonEmpty
      ((relSrcRow, relSrcCol), i) <- targetItem.relativeSources.zipWithIndex // i: left/right incoming pair
      sourceRow = relSrcRow + targetRow
      sourceCol = relSrcCol + targetCol
      if sourceRow >= 0 && sourceRow < nrOfRows && sourceCol > 0 && sourceCol < nrOfCols
    } yield (targetRow, targetCol, i, sourceRow, sourceCol)

    val targetsPerSource = links
      .groupBy { case (_, _, _, sourceRow, sourceCol) => (sourceRow, sourceCol) }
      .mapValues(_
        .map { case (targetRow, targetCol, _, _, _) => (targetRow, targetCol) }
        .sortBy { case (targetRow, targetCol) =>
          targetCol * 1000 + targetRow // TODO is this left to right?
        })

    links.map { case (targetRow, targetCol, i, sourceRow, sourceCol) =>
      val targetItem = items(targetRow)(targetCol)
      val leadingTwistsOfTarget = targetItem.stitch
        .replaceAll("c.*", "")
        .replaceAll(if (i == 0) "[^tl]"
                    else "[^tr]", "")
      val targetsOfSource = targetsPerSource(sourceRow, sourceCol)// TODO find wich of the two matches the targetItem
      val trailingTwistsOfSrc = items(sourceRow)(sourceCol).stitch
        .replaceAll(".*c", "")
        .replaceAll(if (i == 0) "[^tr]" // TODO false assumption
                    else "[^tl]", "")
      val twists = if (i == 0) (trailingTwistsOfSrc.replaceAll("[^tr]", "") + leadingTwistsOfTarget.replaceAll("[^tl]", ""))
                   else (trailingTwistsOfSrc.replaceAll("[^tl]", "") + leadingTwistsOfTarget.replaceAll("[^tr]", ""))
      println(s"${ targetItem.id } $i [x,y] [$sourceCol,$sourceCol] [$targetCol,$targetRow] ($trailingTwistsOfSrc,$leadingTwistsOfTarget) $twists")
      pathDescription(scale(sourceCol), scale(sourceRow), scale(targetCol), scale(targetRow), opacity = 1, twists.length)
    }.mkString
  }.mkString

  @JSExport
  def pathDescription(sX: Double, sY: Double, tX: Double, tY: Double, opacity: Double, twists: Int): String = {
    val t = if (twists <= 1) ""
            else if (twists == 2) """; marker-mid: url("#twist-1")"""
                 else """; marker-mid: url("#twist-2")"""
    val d = if (twists > 1) s"M $sX,$sY ${ sX + (tX - sX) / 2 } ${ sY + (tY - sY) / 2 } $tX,$tY"
            else s"M $sX,$sY $tX,$tY"
    s"<path class='link' d='$d' style='stroke: #000; stroke-width: 2px; fill: none; opacity: $opacity$t'></path>"
  }

  private def renderNodes(items: Seq[Seq[Item]]): String = {
    for {
      row <- items.indices
      col <- items(row).indices
      targetItem = items(row)(col)
      if !targetItem.noStitch
    } yield {
      val (color, shape) = if (targetItem.noStitch) ("#000", circle(10))
                           else targetItem.stitch match {
                             case "ct" | "ctt" | "ctl" |
                                  "ctr" => ("green", square(4.5))
                             case "ctll" => ("green", squareSW(4.5))
                             case "ctrr" => ("green", squareSE(4.5))
                             case "cttt" => ("green", diamond(7.6))
                             case "ctc" => ("purple", square(4.5))
                             case "ctct" | "ctctt" | "ctctl" |
                                  "ctctr" => ("red", square(4.5))
                             case "ctctll" => ("red", squareSW(4.5))
                             case "ctctrr" => ("red", squareSE(4.5))
                             case "ctcttt" => ("red", diamond(7.6))
                             case _ => (defaultColorName(targetItem.stitch), circle(5))
                           }
      val opacity = if (targetItem.noStitch) 0
                    else 0.5
      val event = if (targetItem.noStitch) ""
                  else "onclick='paint(this)'"
      s"""<path $event
         | class="node"
         | d="$shape"
         | style="fill: $color; stroke: none; opacity: $opacity"
         | transform="translate(${ scale(col) },${ scale(row) })"
         |><title>${ targetItem.stitch } - ${ targetItem.id }</title></path>"""
        .stripMargin.stripLineEnd.replaceAll("[\r\n]", "")
    }.mkString
  }.mkString

  private def scale(c: Int) = {
    (c + 2) * 15
  }

  /** Prefix required when writing to an SVG file */
  val prolog = "<?xml version='1.0' encoding='UTF-8'?>"

  /** @param diagram collections of nodes and links
   * @return an SVG document as String
   */
  @JSExport
  def render(config: TilesConfig,
             width: Int = 744,
             height: Int = 1052,
            ): String = {
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
       |$markerDefinitions
       |${ renderLinks(config.getItemMatrix) }
       |${ renderNodes(config.getItemMatrix) }
       |</svg>""".stripMargin
  }
}
