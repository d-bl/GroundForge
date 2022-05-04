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

  private def circle(r: Double = 4.7): String = s"M $r,0 A $r,$r 0 0 1 0,$r $r,$r 0 0 1 -$r,0 $r,$r 0 0 1 0,-$r $r,$r 0 0 1 $r,0 Z"

  private def square(d: Double = 4.3) = s"M -$d,-$d $d,-$d $d,$d -$d,$d Z"

  private def squareSE(d: Double = 4.3) = s"M $d,-$d $d,$d -$d,$d Z"

  private def squarePortrait(d: Double = 4.3) = s"m -${ d / 2 },-$d h $d v ${ 2 * d } h -$d z"

  private def squareSW(d: Double = 4.3) = s"M -$d,-$d $d,$d -$d,$d Z"

  private def squareNW(d: Double = 4.3) = s"M -$d,-$d $d,-$d -$d,$d Z"

  private def squareNE(d: Double = 4.3) = s"M -$d,-$d $d,-$d $d,$d Z"

  private def squareLeft(d: Double = 4.3) = s"M -$d,-$d H 0 v ${ 2 * d } h -$d z"

  private def squareRight(d: Double = 4.3) = s"M 0,-$d H $d V $d H 0 Z"

  private def squareTop(d: Double = 4.3) = s"M $d,0 V -$d h -${ 2 * d } v $d z"

  private def squareBottom(d: Double = 4.3) = s"M $d,$d V 0 h -${ 2 * d } V $d Z"

  private def diamond(d: Double = 5.5) = s"M -$d,0 0,$d $d,0 0,-$d Z"

  private def diamondTopLeft(d: Double = 5.5) = s"M -$d,0 0,-$d v $d z"

  private def diamondTopRight(d: Double = 5.5) = s"M $d,0 0,-$d v $d z"

  private def diamondBottomLeft(d: Double = 5.5) = s"m -$d,0 $d,$d v -$d z"

  private def diamondBottomRight(d: Double = 5.5) = s"m $d,0 -$d,$d v -$d z"

  private def diamondBottom(d: Double = 5.5) = s"M -$d,0 0,$d $d,0 Z"

  private def diamondRight(d: Double = 5.5) = s"M 0,$d $d,0 0,-$d Z"

  private def diamondTop(d: Double = 5.5) = s"M -$d,0 $d,0 0,-$d Z"

  private def diamondLeft(d: Double = 5.5) = s"M -$d,0 0,$d 0,-$d Z"

  private def twistMark(count: Int) = {
    val d = if (count == 1) "M 0,2 0,-2"
            else if (count == 2) "M -1,2 V -2 M 1,2 1,-2"
                 else "M -1.5,2 V -2 M 1.5,2 1.5,-2  M 0,2 0,-2"
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
      pathDescription(s"r${ sourceRow }c$sourceCol-r${ targetRow }c$targetCol",scale(sourceCol), scale(sourceRow), scale(targetCol), scale(targetRow), opacity = 1, twists.length)
    }.mkString
  }.mkString

  @JSExport
  def pathDescription(id: String, sX: Double, sY: Double, tX: Double, tY: Double, opacity: Double, twists: Int): String = {
    val t = twists match {
      case 0 => ""
      case 1 | 2 | 3 => s"""; marker-mid: url("#twist-$twists")"""
      case _ => """; marker-mid: url("#twist-3")"""
    }
    val d = if (twists <= 0) s"M $sX,$sY $tX,$tY"
            else s"M $sX,$sY ${ sX + (tX - sX) / 2 } ${ sY + (tY - sY) / 2 } $tX,$tY"
    s"<path id='$id' class='link' d='$d' style='stroke: #000; stroke-width: 1px; fill: none; opacity: $opacity$t'></path>"
  }

  private def scale(c: Int) = (c + 2) * 15

  implicit class StitchString(val stitch: String) extends AnyVal {

    def twistsOfPair(pairNr: Int): String = {
      val search = if (pairNr == 0) "[^tl]"
                   else "[^tr]"
      stitch.replaceAll(search, "")
    }
  }

  private def renderNodes(items: Seq[Seq[Item]]): String = {
    for {
      row <- items.indices
      col <- items(row).indices
      targetItem = items(row)(col)
      if !targetItem.noStitch
    } yield {
      val transform = s"""transform="translate(${ scale(col) },${ scale(row) })""""
      val title = s"""<title>${ targetItem.stitch } - ${ targetItem.id }</title>"""

      def style(color: String) = s"""style="fill: $color; stroke: none; opacity: 0.9""""

      def shape(color: String, shape: String) = s"""<path d="$shape" ${ style(color) }></path>"""

      def group(shapes: String) = s"""<g id='r${ row }c$col' onclick='paint(this)' class="node" $transform>$title$shapes</g>"""

      val pale = "#f7f7f7" // center one of color brewer list
      val black = "#000000"

      def colour(nrOfTwists: Int) = {
        // https://colorbrewer2.org/?type=diverging&scheme=RdBu&n=5
        val colours = Seq("#ca0020", "#f4a582", /* pale */ "#92c5de", "#0571b0")
        colours(Math.min(colours.length - 1, nrOfTwists))
      }

      def colourRight(s: String) = colour(s.replaceAll("l", "").length)

      def colourLeft(s: String) = colour(s.replaceAll("r", "").length)

      def shapeDef(stitch: String): Seq[String] = {
        val str = stitch
          .replaceAll("^[tlr]*", "") // ignore leading twists
          .replaceAll("[tlr]*$", "") // ignore trailing twists
          .replaceAll("p", "") // ignore pins
          .replaceAll("t", "lr")
        val cs = str.replaceAll("[^c]", "").length
        val twists = str.split("c").filterNot(_.isEmpty).map(_.sortBy(identity))
//        println(s"$stitch ${ twists.mkString }")
        (cs, twists) match {
          case (_, Array()) => // just one or more c's
            Seq(black, "<->", colour(cs))
          case (2, Array(lr)) => // c.c
            Seq(colourLeft(lr), "<|>", colourRight(lr))
          case (3, Array(lrBottom)) if str.startsWith("cc") => // cc.c
            Seq(colour(0), colour(0), colourLeft(lrBottom), colourRight(lrBottom))
          case (3, Array(lrTop)) => // c.cc
            Seq(colourLeft(lrTop), colourRight(lrTop), colour(0), colour(0))
          case (3, Array(lrTop, lrBottom)) => // c.c.c
            Seq(colourLeft(lrTop), colourRight(lrTop), colourLeft(lrBottom), colourRight(lrBottom))
          case (nrOfCs, lrs) if nrOfCs > 3 && lrs.distinct.sameElements(Array("lr")) => // plait
            Seq(black, "|")
          case (nrOfCs, _) if nrOfCs > 3 && str.matches("c(rrc)?(llcrrc)+(llc)?") => // tallie
            Seq(black, "[]", black)
          case _ => Seq() // anything else
        }
      }

      shapeDef(targetItem.stitch) match {
        case Seq(color, "|") => group(shape(color, squarePortrait()))
        case Seq(color1, "<|>", color2) => group(shape(color1, squareLeft()) + shape(color2, squareRight()))
        case Seq(color1, "<->", color2) => group(shape(color1, diamondTop()) + shape(color2, diamondBottom()))
        case Seq(color1, "[]", color2) => group(shape(color1, squareLeft()) + shape(color2, squareRight()))
        case Seq(topLeft, topRight, bottomLeft, bottomRight) =>
//          println(s"${ targetItem.stitch } $topLeft $topRight $bottomLeft $bottomRight")
          group(shape(topLeft, diamondTopLeft()) +
            shape(topRight, diamondTopRight()) +
            shape(bottomLeft, diamondBottomLeft()) +
            shape(bottomRight, diamondBottomRight())
          )
        case defs =>
//          println(s"${ targetItem.stitch } $defs")
          group(shape(pale, circle())) // fall back
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
             zoom: Long = 2,
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
       |  ${ twistMark(3) }
       |</defs>
       |<g transform="matrix($zoom,0,0,$zoom,0,0)">
       |${ renderLinks(config.getItemMatrix) }
       |${ renderNodes(config.getItemMatrix) }
       |</g>
       |</svg>""".stripMargin
  }
}
