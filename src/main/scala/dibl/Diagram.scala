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

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

@JSExport
case class Diagram(nodes: Seq[NodeProps],
                   links: Seq[LinkProps]
                  ) {

  @JSExport
  def node(i: Int): NodeProps = nodes(i)

  @JSExport
  def link(i: Int): LinkProps = links(i)

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport
  def jsNodes(): js.Array[js.Dictionary[Any]] = toJS(nodes)

  //noinspection AccessorLikeMethodIsEmptyParen
  @JSExport
  def jsLinks(): js.Array[js.Dictionary[Any]] = toJS(links)

  private def toJS(items: Seq[Props]): js.Array[js.Dictionary[Any]] = {

    val jsItems = new js.Array[js.Any](items.length).asInstanceOf[js.Array[js.Dictionary[Any]]]
    for {i <- items.indices} {
      jsItems(i) = items(i).toJS()
      jsItems(i)("index") = i
    }
    jsItems
  }

  /**
   * @return Visible links as source/target-tuples of indexes in nodes, stripped of duplicates.
   *         An example: the 8 links in ascii-art ">==<" graph should be reduced to 4 links as "><".
   *         This means recursively replace the sources of "<" links with the sources of "=" links.
   */
  def filterLinks: Seq[(Int, Int)] = {
    val visibleLinks = links
      .withFilter(!_.border)
      .map(l => (l.source, l.target))
    val targetsOfDuplicates = visibleLinks
      .groupBy { case (s: Int, t: Int) => (s, t) }
      .filter { case (_, duplicates) => 1 < duplicates.size }
      .keySet
      .map(_._2)
    val linksByTarget = visibleLinks.groupBy { case (_, t: Int) => t }

    @tailrec
    def FindSource(s: Int): Int = {
      if (targetsOfDuplicates.contains(s))
        FindSource(linksByTarget(s).head._1)
      else s
    }

    visibleLinks
      .withFilter { case (_, t) => !targetsOfDuplicates.contains(t) }
      .map { case (s, t) => (FindSource(s), t) }
  }
}