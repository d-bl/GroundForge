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
package dibl.fte

object ClockWise {
  def apply(links: Seq[TopoLink]): Map[String, Seq[TopoLink]] = {
    val linksBySource = links.groupBy(_.sourceId)
    links.groupBy(_.targetId)
      .map { case (id, linksIn) =>
        val linksOut = linksBySource(id)
        (id, linksOut.filter(_.isRightOfSource)
          ++ linksOut.filter(_.isLeftOfSource)
          ++ linksIn.filter(_.isLeftOfTarget)
          ++ linksIn.filter(_.isRightOfTarget)
        )
      }
  }
}
