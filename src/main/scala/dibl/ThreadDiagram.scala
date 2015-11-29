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

import dibl.Matrix._

case class ThreadDiagram private(nodes: Seq[Props],
                                 links: Seq[Props])

object ThreadDiagram {

  def apply(settings: Settings): ThreadDiagram = {
    val nrOfPairLinks = countLinks(settings.absM)
    val nodeMatrix = nrOfPairLinks.indices.map { row =>
      nrOfPairLinks(row).indices.map { col =>
        if (nrOfPairLinks(row)(col)<4) None else Some(
          stitchNodes(settings.stitches(row % ???)(col % ???)))
      }
    }
    val nodeMaps: Seq[(Char, Props)] =
      nodeMatrix.flatten.filter(_.isDefined).flatMap(_.get)
    // TODO add nodes for thread starts and for bobbins
    // TODO create links
    ThreadDiagram(
      nodeMaps.map{case (k,v) => v},
      ???
    )
  }

  def stitchNodes (s: String): Seq[(Char, Props)] = {
    s.replaceAll("t", "lr").replaceAll("rl", "lr").map {
      case c@'l' => c -> Props("title" -> "twist left")
      case c@'r' => c -> Props("title" -> "twist right")
      case c@'p' => c -> Props("pin" -> true)
      case c@'c' => c -> Props("title" -> "cross")
    }
  }
}