package dibl.fte

import scala.annotation.tailrec

object Locations {

  @tailrec
  def create(nodes: Locations, deltas: Deltas): Locations = {
    // 45 calls for 663 nodes of whiting=F14_P193&tileStitch=crcrctclclcr
    // print(s" ${nodes.size}-${deltas.size} ")

    val nodeIds = nodes.keys.toSeq
    val candidates = deltas.filter {
      case (TopoLink(source, target, _, _), _) =>
        nodeIds.contains(source) && !nodeIds.contains(target)
    }
    if (candidates.isEmpty) nodes
    else {
      val newNodes = candidates.map {
        case (TopoLink(source, target, _, _), Delta(dx, dy)) =>
          val (x, y) = nodes(source)
          target -> (x + dx, y + dy)
      } ++ nodes
      val newNodeIds = newNodes.keys.toSeq
      create(
        newNodes,
        deltas.filterNot(d => newNodeIds.contains(d._1.sourceId) && newNodeIds.contains(d._1.targetId))
        // less complex to calculate but depleting deltas less
        // (doesn't influence number of calls but reduces the filter to find candidates):
        // deltas.filterNot(delta => candidates.contains(delta._1))
      )
    }
  }

}
