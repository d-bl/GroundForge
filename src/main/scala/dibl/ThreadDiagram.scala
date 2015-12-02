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

case class ThreadDiagram private(nodes: Seq[Props],
                                 links: Seq[Props])

object ThreadDiagram {

  private var nr = -1
  private def nextNodeNr: Int = {nr += 1;nr}

  def apply(pairDiagram: PairDiagram): ThreadDiagram = {

    nr = -1 // keep the state inside
    val nodeLists = threadNodes(pairDiagram.nodes)
    val nodes = nodeLists.flatten.map { case (_, nodeProps, _) => nodeProps }
    // TODO properly link the nodes
    val linksInStitches = nodeLists.flatMap { stitch =>
      createLinks(stitch.map { case (_, _, i) => i },"","") ++
        createLinks(stitch.filter { case (c, _, _) => c == 'c' }.map { case (_, _, i) => i },"red","red") ++
        createLinks(stitch.filter { case (c, _, _) => c == 'l' }.map { case (_, _, i) => i },"purple","purple") ++
        createLinks(stitch.filter { case (c, _, _) => c == 'r' }.map { case (_, _, i) => i },"green","green")
    }
    val linksBetweenStitches = pairDiagram.links.flatMap { threadLink => createLinks(Seq(
      nodeLists(sourceOf(threadLink)).map { case (c, props, i) => i }.head,
      nodeLists(targetOf(threadLink)).map { case (c, props, i) => i }.head
    ),"white","white")}
    ThreadDiagram(
      nodes,
      linksInStitches ++ linksBetweenStitches
        //transparentLinks(nodes.indices.filter(nodes(_).get("startOf").isDefined))
    )
  }

  @tailrec
  private def createLinks(nodes: Seq[Int],
                          start: String,
                          end: String,
                          accumulatedLinks: Seq[Props] = Seq[Props]()
                         ): Seq[Props] =
    if (nodes.length < 2) accumulatedLinks
    else createLinks(nodes.tail, start, end, accumulatedLinks :+ Props(
      "source" -> nodes.head,
      "target" -> nodes.tail.head,
      "start" -> start,
      "end" -> end
    ))

  private def threadNodes(pairNodes: Seq[Props]
                         ): Seq[Seq[(Char, Props, Int)]] =
    pairNodes.map(pn =>
      if (pn.get("bobbin").isDefined) endNodes
      else {
        val title = pn.getOrElse("title", "").toString
        if (title.toLowerCase.startsWith("pair"))
          startNodes(title.replaceAll("[^ ]* ", "").toInt)
        else
          stitchNodes(title.replaceAll(" .*", ""))
      }
    )

  private def startNodes(pairNr: Int
                        ): Seq[(Char, Props, Int)] = Seq[(Char, Props, Int)](
    ('x', Props("startOf" -> s"thread${pairNr * 2 - 1}"), nextNodeNr),
    ('y', Props("startOf" -> s"thread${pairNr * 2}"), nextNodeNr))

  private def endNodes: Seq[(Char, Props, Int)] = Seq[(Char, Props, Int)](
    ('x', Props("bobbin" -> true), nextNodeNr),
    ('y', Props("bobbin" -> true), nextNodeNr))

  private def stitchNodes(str: String
                         ): Seq[(Char, Props, Int)] =
    str.replaceAll("t", "lr").map {
      case c@'l' => (c, Props("title" -> "twist left"), nextNodeNr)
      case c@'r' => (c, Props("title" -> "twist right"), nextNodeNr)
      case c@'p' => (c, Props("pin" -> true), nextNodeNr)
      case c@'c' => (c, Props("title" -> "cross"), nextNodeNr)
    }
}