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

import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/** An object with links and nodes for a pair diagram and thread diagram.
  * Evaluation of the thread diagram only takes place when accessed.
  * Accessing just the thread diagram will only skip the conversion to raw JavaScript
  * of the pair diagram.
  *
  * @param pairDiagram nodes and links for a color coded pair diagram
  */
@deprecated
case class D3Data (@(JSExport @field) pairDiagram: Diagram) {

  @JSExport
  lazy val threadDiagram = ThreadDiagram(pairDiagram)

  @JSExport
  def pairNodes(): js.Array[js.Dictionary[Any]] = pairDiagram.jsNodes()

  @JSExport
  def pairLinks(): js.Array[js.Dictionary[Any]] = pairDiagram.jsLinks()

  @JSExport
  def threadNodes(): js.Array[js.Dictionary[Any]] = threadDiagram.jsNodes()

  @JSExport
  def threadLinks(): js.Array[js.Dictionary[Any]] = threadDiagram.jsLinks()
}

@JSExportTopLevel("D3Data") object D3Data {

  /** Creates a pair and thread diagrams from values in form fields of docs/index.html
    *
    * @param compactMatrix see legend on matrix tab
    * @param tileType see values for drop down on matrix tab
    * @param stitches see stitches tab
    * @param rows see patch size tab
    * @param cols see patch size tab
    * @param shiftLeft see footside tab
    * @param shiftUp see footside tab
    * @return an object with a pair diagram and thread diagram.
    */
  @JSExport
  def get(compactMatrix: String, rows: Int, cols: Int, shiftLeft: Int, shiftUp: Int, stitches: String, tileType: String
         ): D3Data = {
    val triedDiagram = PairDiagram.create(
      compactMatrix, tileType, rows, cols, shiftLeft, shiftUp, stitches
    )
    new D3Data(triedDiagram.getOrRecover)
  }
  /** Creates a new pair and thread diagram from a thread diagram assuming threads are pairs.
    *
    * @param d3Data object with a thread diagram
    * @param stitch the stitch that replaces each cross and twist
    * @return
    */
  @JSExport
  def get(stitch: String, d3Data: D3Data): D3Data =
    new D3Data(PairDiagram(stitch, d3Data.threadDiagram))
}
