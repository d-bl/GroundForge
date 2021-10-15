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

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

/** Object required by D3js and [[DiagramSvg]]. For D3js see also drawNode and drawLink on
  * https://bl.ocks.org/mbostock/1b64ec067fcfc51e7471d944f51f1611
  */
trait Props {

  /** Conversion for a JavaScript environment such as
    * a browser (e.g. docs/js/draw-graph.js)
    *
    * @return values required by the D3js scripts
    */
  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport
  def toJS(): js.Dictionary[Any]

  // TODO conversion for a JDK nashorn environment (Force.class + force.js, tested with ForceSpec)
  // We need arrays of maps for D3js, the link below demonstrates a map
  // http://stackoverflow.com/questions/24691142/nashorns-scriptobjectmirror#26030296
}
