/*
 Copyright 2018 Jo Pol
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

import dibl.NodeProps.errorNode

/**
 * @param core    Diagram node: a stitch, cross or twist.
 *                core.id is unique in the list returned by
 *                Diagram.tileLinks called by Config.linksOfCenterTile
 *                unless too long plaits were specified.
 *                The order of the list is not defined.
 *                The node with id=a1 may not sit in the top left corner of the tile returned by tileLinks.
 *                The third row/column might be quite common for node-a1.
 *                The node with id=a1 may not sit in the top left corner of the tile returned by tileLinks.
 *                The third row/column might be quite common for node-a1.
 * @param sources Stitches that supply pairs/threads to create the core stitch.
 * @param targets Stitches that that need pairs/threads leaving the core stitch.
 *
 *                A node mapped to the value true means that the link (alias thread)
 *                with the core node has a shortened end
 *                for the over/under effect. All will be false for a pair diagram.
 *                The order of the two elements in each map is not defined.
 */
case class LinkedNode(core: NodeProps,
                      sources: Map[NodeProps, Boolean],
                      targets: Map[NodeProps, Boolean]
                     ) {
  /**
   * Diagram nodes connected to core in a clockwise order starting with an incoming pair/thread
   * ending with an outgoing pair/thread.
   *
   * An empty list indicates problems or not yet implemented.
   * An empty or not properly formatted id for any of the items also spells trouble,
   * the title may contain an error message. A properly formatted id only has letters
   * followed by digits.
   */
  lazy val clockwise: Array[NodeProps] = {
    val s1 = sources.keys.headOption.getOrElse(errorNode("no pairs/threads in"))
    val s2 = sources.keys.lastOption.getOrElse(errorNode("just one pair/thread in"))
    val t1 = targets.keys.headOption.getOrElse(errorNode("no pairs/threads out"))
    val t2 = targets.keys.lastOption.getOrElse(errorNode("just one pair/thread out"))
    (
      core.instructions,
      sources.keys.headOption.map(sources(_)),
      targets.keys.headOption.map(targets(_)),
    ) match {
      case ("cross", Some(true), Some(true)) => Array(s1, s2, t1, t2)
      case ("cross", Some(true), Some(false)) => Array(s1, s2, t2, t1)
      case ("cross", Some(false), Some(true)) => Array(s2, s1, t1, t2)
      case ("cross", Some(false), Some(false)) => Array(s2, s1, t2, t1)
      case (_, None, _) | (_, _, None)=> Array[NodeProps]() // no links in and/or out
      case ("twist", Some(true), Some(true)) => Array(t2, t1, s2, s1)
      case ("twist", Some(true), Some(false)) => Array(t1, t2, s2, s1)
      case ("twist", Some(false), Some(true)) => Array(t2, t1, s1, s2)
      case ("twist", Some(false), Some(false)) => Array(t2, t1, s2, s1)
      case _ => Array[NodeProps]() // TODO calculate from s1.x ... t2.y
    }
  }
}
