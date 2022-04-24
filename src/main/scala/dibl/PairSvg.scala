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

  private def circle(r: Double = 5): String = s"M $r,0 A $r,$r 0 0 1 0,$r $r,$r 0 0 1 -$r,0 $r,$r 0 0 1 0,-$r $r,$r 0 0 1 $r,0 Z"

  private def square(d: Double = 4.5) = s"M -$d,-$d $d,-$d $d,$d -$d,$d Z"

  private def squareSE(d: Double = 4.5) = s"M $d,-$d $d,$d -$d,$d Z"

  private def squareSW(d: Double = 4.5) = s"M -$d,-$d $d,$d -$d,$d Z"

  private def squareNW(d: Double = 4.5) = s"M -$d,-$d $d,-$d -$d,$d Z"

  private def squareNE(d: Double = 4.5) = s"M -$d,-$d $d,-$d $d,$d Z"

  private def diamond(d: Double = 5.8) = s"M -$d,0 0,$d $d,0 0,-$d Z"

  private def diamondS(d: Double) = s"M -$d,0 0,$d $d,0 Z"

  private def diamondE(d: Double) = s"M 0,$d $d,0 0,-$d Z"

  private def diamondN(d: Double) = s"M -$d,0 $d,0 0,-$d Z"

  private def diamondW(d: Double) = s"M -$d,0 0,$d 0,-$d Z"

  private def twistMark(count: Int) =
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

  private val grey = "#CCCCCC"
  private val aqua = "#00F090"
  private val violet = "#C030F0"
  private val red = "#FF0000"
  private val green = "#008000"
  private val blue = "#1080C0"

  implicit class TwistString(val stitch: String) extends AnyVal {

    def shapeDef(): Seq[String] = {
      val str = stitch
        .replaceAll("^[tlr]*", "") // we allow a mix of open/closed definitions, we render closed
        .replaceAll("p", "") // ignore pins
      str match {
        case "c" => Seq(grey)
        case "cr" => Seq(grey, "/", green)
        case "cl" => Seq(grey, "\\", green)
        case _ if str.matches("ct[tlr]*") => Seq(green)
        case "ctc" => Seq(violet)
        case "ctcr" => Seq(violet, "/", red)
        case "ctcl" => Seq(violet, "\\", red)
        case _ if str.matches("crc[lr]*") => Seq(grey, "/", violet)
        case _ if str.matches("clc[lr]*") => Seq(grey, "\\", violet)
        case _ if str.matches("crct[lr]*") => Seq(grey, "/", red)
        case _ if str.matches("clct[lr]*") => Seq(grey, "\\", red)
        case _ if str.matches("ctct[tlr]*") => Seq(red)
        case _ if str.matches("ctc(tc)+[tlr]*") => Seq(blue) // plaits, including fixing stitch
        case _ if str.matches("cttc[tlr]*") => Seq(aqua) // turning stitch
        case _ => Seq()
      }
    }

    def twistsOfPair(pairNr: Int): String = {
      val search = if (pairNr == 0) "[^tl]"
                   else "[^tr]"
      stitch.replaceAll(search, "")
    }
  }

  private def renderLinks(items: Seq[Seq[Item]]): String = {
    val nrOfRows = items.size
    val nrOfCols = items.head.size
    val links = for {
      targetRow <- items.indices
      targetCol <- items(targetRow).indices
      targetItem = items(targetRow)(targetCol)
      if !targetItem.noStitch && targetItem.relativeSources.nonEmpty
      ((relSrcRow, relSrcCol), pairNrIntoTarget) <- targetItem.relativeSources.zipWithIndex // i: left/right incoming pair
      sourceRow = relSrcRow + targetRow
      sourceCol = relSrcCol + targetCol
      if sourceRow >= 0 && sourceRow < nrOfRows && sourceCol > 0 && sourceCol < nrOfCols
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
      val targetItem = items(targetRow)(targetCol)
      val leadingTwistsOfTarget = targetItem
        .stitch.replaceAll("c.*", "")
        .twistsOfPair(pairNrIntoTarget)
      val trailingTwistsOfSrc = targetsPerSource(sourceRow, sourceCol)
        .find { case ((row, col), _) => targetRow == row && targetCol == col }
        .map { case ((_, _), pairNrLeavingSrc) =>
          items(sourceRow)(sourceCol)
            .stitch.replaceAll(".*c", "")
            .twistsOfPair(pairNrLeavingSrc)
        }.getOrElse("")
      val twists = trailingTwistsOfSrc + leadingTwistsOfTarget
      //      println(s"${ targetItem.id } $pairNrIntoTarget [x,y] [$sourceCol,$sourceCol] [$targetCol,$targetRow] ($trailingTwistsOfSrc,$leadingTwistsOfTarget) $twists")
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

  private def scale(c: Int) = (c + 2) * 15

  private def renderNodes(items: Seq[Seq[Item]]): String = {
    for {
      row <- items.indices
      col <- items(row).indices
      targetItem = items(row)(col)
      if !targetItem.noStitch
    } yield {
      val transform = s"""transform="translate(${ scale(col) },${ scale(row) })""""
      val title = s"""<title>${ targetItem.stitch } - ${ targetItem.id }</title>"""

      def style(color: String) = s"""style="fill: $color; stroke: none; opacity: 0.8""""

      def shape(color: String, shape: String) = s"""<path d="$shape" ${ style(color) }></path>"""

      def group(shapes: String) = s"""<g onclick='paint(this)' class="node" $transform>$title$shapes</g>"""

      def singleShape(color: String, shape: String) =
        s"""<path onclick='paint(this)' class="node" d="$shape"${ style(color) }$transform >$title</path>"""

      targetItem.stitch.shapeDef() match {
        case Seq(color) => singleShape(color, square())
        case Seq(color1, "/", color2) => group(shape(color1, squareNW()) + shape(color2, squareSE()))
        case Seq(color1, "\\", color2) => group(shape(color1, squareNE()) + shape(color2, squareSW()))
        case _ => singleShape(defaultColorName(targetItem.stitch), diamond())
      }
    }.mkString
  }.mkString

  /** Prefix required when writing to an SVG file */
  val prolog = "<?xml version='1.0' encoding='UTF-8'?>"

  /** @return an SVG document as String */
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
       |<defs>
       |  ${ twistMark(1) }
       |  ${ twistMark(2) }
       |</defs>
       |${ renderLinks(config.getItemMatrix) }
       |${ renderNodes(config.getItemMatrix) }
       |</svg>""".stripMargin
  }
}
