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

import scala.util.Try

case class PairDiagram private(nodes: Seq[Props],
                               links: Seq[Props])

object PairDiagram {

  def apply(settings: Try[Settings]): PairDiagram =
    if (settings.isFailure)
      PairDiagram(Seq(Props("title" -> settings.failed.get.getMessage, "bobbin" -> true)), Seq[Props]())
    else {
      val nodeNrs = assignNodeNrs(settings.get.absM, settings.get.nrOfPairLinks)
      val nodes = toNodes(settings.get).toArray
      val links = toLinks(settings.get, nodeNrs, nodes)
      assignPairNrs(nodes, links)
      PairDiagram(nodes, links)
    }

  private def assignPairNrs(nodes: Array[Props], links: Seq[Props]): Unit = {
    // assign numbers to the source node of the first link of each pair
    val startLinks = links.filter(_.getOrElse("start", "").toString.startsWith("pair"))
    for (i <- startLinks.indices) {
      val source = startLinks(i).get("source").get.asInstanceOf[Int]
      nodes(source) = Props("title" -> s"Pair ${i + 1}")
    }
  }

  def assignNodeNrs(abs: M, nrOfLinks: Array[Array[Int]]
                           ): Seq[Seq[Int]] = {
    var nodeNr = -1
    abs.indices.map { row =>
      abs(row).indices.map { col =>
        if (nrOfLinks(row)(col) <= 0) 0
        else {
          nodeNr += 1
          nodeNr
        }
      }.toSeq
    }.toSeq
  }

  /** Creates nodes for a pair diagram
    *
    * @return properties per node as in https://github.com/jo-pol/DiBL/blob/gh-pages/tensioned/sample.js
    */
  def toNodes (s: Settings): Seq[Props] = {

    def isUsed(row: Int, col: Int): Boolean =
      s.nrOfPairLinks(row)(col) > 0

    def isInBottom(row: Int): Boolean =
      row >= s.absM.length - 2

    def isFootside(row: Int, col: Int): Boolean =
      col < 2 || col >= s.absM(0).length - 2

    val bobbinProps = Props("bobbin" -> true)
    val footSideProps = Props("title" -> "ttctc")
    s.absM.indices.flatMap { row =>
      s.absM(row).indices.filter(isUsed(row, _)).map { col =>
        if (isInBottom(row)) bobbinProps
        else if (isFootside(row, col)) footSideProps
        else Props("title" -> s.getTitle(row,col))
      }
    }
  }

  /** Creates links for a pair diagram
    *
    * @param nodeNrs sequence numbers assigned to actually used cells
    * @return properties per link as in https://github.com/jo-pol/DiBL/blob/gh-pages/tensioned/sample.js
    */
  def  toLinks (s: Settings, nodeNrs: Seq[Seq[Int]], nodes: Seq[Props]
               ): Seq[Props] = {

    def startMarker(srcRow: Int, srcCol: Int): String = {
        val srcTitle = getInstructions(srcRow, srcCol)
        if (isStartOfPair(srcRow, srcCol)) "pair"
        else if (srcTitle.endsWith("clrclrc") || srcTitle.contains("p")) ""
        else if (srcTitle.endsWith("lrclrc")) "red"
        else if (srcTitle.endsWith("clrc")) "purple"
        else if (srcTitle.endsWith("lrc")) "green"
        else if (srcTitle.startsWith("clrclrc")) ""
        else if (srcTitle.startsWith("clrclr")) "red"
        else if (srcTitle.startsWith("clrc")) "purple"
        else if (srcTitle.startsWith("clr")) "green"
        else ""
      }

    def endMarker(targetRow: Int, targetCol: Int): String = {
      val targetTitle = getInstructions(targetRow, targetCol)
      if (targetTitle.endsWith("clrclrc") || targetTitle.contains("p")) ""
      else if (targetTitle.endsWith("lrclrc")) "red"
      else if (targetTitle.endsWith("clrc")) "purple"
      else if (targetTitle.endsWith("lrc")) "green"
      else if (targetTitle.startsWith("clrclrc")) ""
      else if (targetTitle.startsWith("clrclr")) "red"
      else if (targetTitle.startsWith("clrc")) "purple"
      else if (targetTitle.startsWith("clr")) "green"
      else ""
    }

    def midMarker(srcRow: Int, srcCol: Int, targetRow: Int, targetCol: Int, pairNr: Int): Int = {
      val targetTwists = getInstructions(targetRow, targetCol).replaceAll("c.*","").replaceAll("p","")
      val sourceTwists = getInstructions(srcRow, srcCol).replaceAll(".*c","").replaceAll("p","")
      val count = if (pairNr == 0)
        targetTwists.count(_ == 'l') + sourceTwists.count(_ == 'r')
      else
        targetTwists.count(_ == 'r') + sourceTwists.count(_ == 'l')
      // TODO current implementation assumes first twist is part of the color code
      // but that only applies to red or green end/start markers
      if (count > 1)
        count - 1
      else 0
    }

    def getInstructions(row: Int, col: Int): String = nodes(nodeNrs(row)(col)).instructions

    def isStartOfPair(r: Int, c: Int): Boolean = r < 2

    val startNodeNrs = s.absM(2).flatten
      .filter{case (r,c) => isStartOfPair(r,c)}
      .map{case (r,c) => nodeNrs(r)(c)}

    s.absM.indices.flatMap { row =>
      s.absM(row).indices.flatMap { col =>
        s.absM(row)(col).indices.map { linkNr =>
          val(srcRow,srcCol) = s.absM(row)(col)(linkNr)
          Props(
            "source" -> nodeNrs(srcRow)(srcCol),
            "target" -> nodeNrs(row)(col),
            "start" -> startMarker(srcRow, srcCol),
            "end" -> endMarker(row, col),
            "mid" -> midMarker(srcRow, srcCol, row, col, linkNr)
          )
        }
      }
    } ++ transparentLinks(startNodeNrs)
  }
}
