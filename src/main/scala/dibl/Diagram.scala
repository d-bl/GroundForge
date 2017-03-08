/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/
package dibl

import java.util.concurrent.TimeUnit

import dibl.Force.Point

import scala.scalajs.js.annotation.JSExport
import scala.util.Try

@JSExport
case class Diagram(nodes: Seq[Props],
                   links: Seq[Props]) {

  @JSExport
  def node(i: Int): Props = nodes(i)

  @JSExport
  def link(i: Int): Props = links(i)

  /** Calculates new node positions in a dedicated JavaScript engine, NOT THREAD SAFE!
    */
  def nudgeNodes(center: Point = Point(0, 0),
                 timeout: Long = 20,
                 timeUnit: TimeUnit = TimeUnit.SECONDS
                ): Try[Diagram] =
    Force
      .simulate(this, center, timeout, timeUnit)
      .map(nudgedPositions =>
        Diagram(mergeBack(nudgedPositions), links)
      )

  private def mergeBack(nudgedPositions: Array[Point]
                       ) =
    nodes
      .indices
      .map(i => nodes(i).keys.map {
        case key@"x" => key -> nudgedPositions(i).x.round.toInt
        case key@"y" => key -> nudgedPositions(i).y.round.toInt
        case key => key -> nodes(i)(key)
      }.toMap)

}