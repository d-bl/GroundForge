/*
 Copyright 2016 Jo Pol
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

import dibl.LinkProps.WhiteStart
import dibl.proto.TilesConfig
import dibl.{ Diagram, LinkProps, NewPairDiagram, NodeProps, PairDiagram, ThreadDiagram }

import scala.scalajs.js.annotation.{ JSExport, JSExportTopLevel }
import scala.util.Try

case class TopoLink(isLeftOfSource: Boolean, sourceId: String, isLeftOfTarget: Boolean, targetId: String, weight: Double = 1) {
  val isRightOfTarget: Boolean = !isLeftOfTarget
  val isRightOfSource: Boolean = !isLeftOfSource

  override def toString: String = {
    val out = if (isLeftOfSource) "lo"
              else "ro"
    val in = if (isLeftOfTarget) "li"
             else "ri"
    s"$out,$sourceId,$in,$targetId,$weight"
  }
}

@JSExportTopLevel("TopoLink") object TopoLink {

  @JSExport
  def changeWeight(id: String, change: Double, links: String): String = {
    val Array(startId, endId) = id.split("-")
    asString(fromString(links).map {
      case link @ TopoLink(_, `startId`, _, `endId`, _) =>
        link.copy(weight = if (change == 1) 1
                           else link.weight * change)
      case link => link
    })
  }

  @JSExport
  def asString(links: Seq[TopoLink]): String = links.mkString(";")

  @JSExport
  def fromUrlQuery(urlQuery: String): Seq[TopoLink] = {
    urlQuery.split("&")
      .find(_.matches("topo=.*"))
      .map(s => fromString(s.replace("topo=", "")))
      .getOrElse {
        val pairDiagram = NewPairDiagram.create(TilesConfig(urlQuery))
        if (!urlQuery.matches(".*=[ctlr]+(&.*)?"))
          getTopoLinks(pairDiagram, TilesConfig(urlQuery))
        else fromThreadDiagram(urlQuery)
      }
  }

  /**
    * Deserializes a sequence of TopoLinks
    *
    * @return TopoLink.asString(return-value) returns links,
    *         omitted weight values however will show up as 1.0
    */
  def fromString(links: String): Seq[TopoLink] = {
    def valid(out: String, in: String) = {
      out.matches("[lr]o") || in.matches("[lr]i")
    }

    links
      .replaceAll("""\s+""", "")
      .split(";")
      .toSeq
      .map { link =>
        link.split(",") match {
          case Array(out, src, in, target)
            if valid(out, in) =>
            TopoLink(out == "lo", src, in == "li", target)
          case Array(out, src, in, target, weight)
            if valid(out, in) &&
              weight.matches("[0-9]+(.[0-9]+)?") =>
            TopoLink(out == "lo", src, in == "li", target, weight.toDouble)
          case _ =>
            return Seq.empty
        }
      }
  }

  /**
    * @param urlQuery parameters for: https://d-bl.github.io/GroundForge/tiles?
    *                 For now the tile must be a checker tile and
    *                 the patch size must span 3 columns and 2 rows of checker tiles.
    *                 A simplified ascii-art view of a pattern definition:
    *                 +----+----+----+
    *                 |....|....|....|
    *                 |....|....|....|
    *                 +----+----+----+
    *                 |....|XXXX|....|
    *                 |....|XXXX|....|
    *                 +----+----+----+
    * @return The X's in the pattern definition are added to the returned graph.
    */
  def fromThreadDiagram(urlQuery: String): Seq[TopoLink] = Try {
    val patternConfig = TilesConfig(urlQuery)
    val stitchConfigs = urlQuery.split("&")
      .filter(_.startsWith("droste"))
      .sortBy(identity)
      .map(_.replace("=.*", ""))
    val initialThreadDiagram = ThreadDiagram(NewPairDiagram.create(patternConfig))
    val diagram = stitchConfigs.foldLeft(initialThreadDiagram)(droste)
    val scale = Math.pow(2, stitchConfigs.length + 1).toInt
    getTopoLinks(diagram, patternConfig, scale)
  }.getOrElse(Seq.empty)

  private def getTopoLinks(implicit diagram: Diagram, config: TilesConfig, scale: Int = 1) = {
    TopoLink
      .simplify(diagram.links.filter(inCenterBottomTile))
      .sortBy(l => l.targetId -> l.sourceId)
  }

  private def inCenterBottomTile(link: LinkProps)
                                (implicit diagram: Diagram, scale: Int, config: TilesConfig): Boolean = {
    // The top and side tiles of a diagram may have irregularities along the outer edges.
    // So select links arriving in the center bottom checker tile.
    val cols = config.patchWidth / 3
    val rows = config.patchHeight / 2

    val target = diagram.node(link.target)
    val source = diagram.node(link.source)
    val y = unScale(target.y)
    val x = unScale(target.x)
    y >= rows && x >= cols && x < cols * 2 && !link.withPin && target.id != "" && source.id != ""
  }

  /** Revert [[NewPairDiagram]].toPoint
    *
    * @param i     value for x or y
    * @param scale value 1 or factor to also undo [[ThreadDiagram]].scale
    * @return x/y on canvas reduced to row/col number in the input
    */
  private def unScale(i: Double)(implicit scale: Int): Int = (i / scale / 15 - 2).toInt

  /**
    * fold transformation
    *
    * @param threadDiagram accumulator
    * @param stitchConfig  listItem
    * @return
    */
  private def droste(threadDiagram: Diagram, stitchConfig: String): Diagram = {
    ThreadDiagram(PairDiagram(stitchConfig, threadDiagram))
  }

  /** reduces diagram info of a tile to topological info embedded on a flat torus */
  def simplify(linksInOneTile: Seq[LinkProps])
              (implicit diagram: Diagram): Seq[TopoLink] = {
    implicit val topoLinks: Seq[LinkProps] = linksInOneTile
    linksInOneTile.map { link =>
      TopoLink(isLeftOfSource(link), sourceOf(link).id, isLeftOfTarget(link), targetOf(link).id)
    }
  }

  private def isLeftOfTarget(link: LinkProps)
                            (implicit diagram: Diagram,
                             linksInTile: Seq[LinkProps]
                            ): Boolean = {
    def isSiblingAtTarget(other: LinkProps) = {
      sourceOf(other).id != sourceOf(link).id &&
        targetOf(other).id == targetOf(link).id
    }

    def isLeftPairOfTarget = {
      val otherX: Double = linksInTile
        .find(isSiblingAtTarget) // TODO relatively expensive lookup for large matrices
        .map(other => sourceOf(other).x)
        .getOrElse(0)
      sourceOf(link).x < otherX
    }

    isLeft(
      targetOf(link).instructions,
      link.isInstanceOf[WhiteStart],
      isLeftPairOfTarget _
    )
  }

  private def isLeftOfSource(link: LinkProps)
                            (implicit diagram: Diagram,
                             linksInTile: Seq[LinkProps]
                            ): Boolean = {
    def isSiblingAtSource(other: LinkProps) = {
      targetOf(other).id != targetOf(link).id &&
        sourceOf(other).id == sourceOf(link).id
    }

    def isLeftPairAtSource = {
      val otherX: Double = linksInTile
        .find(isSiblingAtSource) // TODO relatively expensive lookup for large matrices
        .map(other => targetOf(other).x)
        .getOrElse(0)
      targetOf(link).x < otherX
    }

    isLeft(
      sourceOf(link).instructions,
      link.isInstanceOf[WhiteStart],
      isLeftPairAtSource _
    )
  }

  private def isLeft(sourceInstructions: String,
                     isWhiteStart: Boolean,
                     isLeftPair: () => Boolean
                    ) = {
    (sourceInstructions, isWhiteStart) match {
      /*    cross   twist
       *    \  /    \   /
       *     \        /
       *    / \     /  \
       */
      case ("cross", true) => true
      case ("cross", false) => false
      case ("twist", false) => true
      case ("twist", true) => false
      case _ => isLeftPair()
    }
  }

  def sourceOf(l: LinkProps)
              (implicit diagram: Diagram): NodeProps = {
    diagram.node(l.source)
  }

  def targetOf(l: LinkProps)
              (implicit diagram: Diagram): NodeProps = {
    diagram.node(l.target)
  }
}
