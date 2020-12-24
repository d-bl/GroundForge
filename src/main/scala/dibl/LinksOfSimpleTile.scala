package dibl

import dibl.proto.TilesConfig

import java.lang.Math.{ abs, floorMod }
import scala.util.{ Failure, Success, Try }

object LinksOfSimpleTile {
  /**
   * @param f gets either the source or the target from a link
   */
  def apply(cfg: TilesConfig, diagram: Diagram, f: LinkProps => Int)(implicit scale: Int): Try[Seq[LinkProps]] = {
    val width = cfg.centerMatrixCols
    val height = cfg.centerMatrixRows

    val isHorBrick =
      floorMod(abs(cfg.shiftRowsSE), height) == 0 &&
        floorMod(abs(cfg.shiftRowsSW), height) == 0
    val isVerBrick =
      floorMod(abs(cfg.shiftColsSE), width) == 0 &&
        floorMod(abs(cfg.shiftColsSW), width) == 0
    val (simpleWidth, simpleHeight) = (isHorBrick, isVerBrick) match {
      case (true, true) => (width, height)
        // TODO make IDs unique in the next cases
      case (true, false) => (0, 0) //(width, 2 * height)
      case (false, true) => (0, 0) //(2 * width, height)
      case (false, false) => (0, 0)
    }
    val atLeast3x2swatch = simpleWidth * 3 <= cfg.patchWidth && simpleHeight * 2 <= cfg.patchHeight
    val msg = s"Swatch [${ cfg.patchWidth },${ cfg.patchHeight }]" +
      s" tile[${ cfg.centerMatrixCols },${ cfg.centerMatrixRows }]" +
      s" simple[$simpleWidth,$simpleHeight]" +
      s" shiftCols[E=${ cfg.shiftColsSE },W=${ cfg.shiftColsSW }]" +
      s" shiftRows[E=${ cfg.shiftRowsSE },W=${ cfg.shiftRowsSW }]" +
      s" isHorBrick=$isHorBrick, isVerBrick=$isVerBrick"
    if (!atLeast3x2swatch || simpleHeight == 0) {
      val errorMsg = s"Swatch should be at least 3 simple tiles wide and 2 high. $msg"
      println(errorMsg)
      Failure(new Exception(errorMsg))
    }
    else {
      println(msg)
      def inSecondTileFromNW(link: LinkProps) = {
        val n = diagram.nodes(f(link))
        val y = unScale(n.y)
        val x = unScale(n.x)
        y >= simpleHeight && y < simpleHeight * 2 && x >= simpleWidth && x < simpleWidth * 2
      }

      Success(diagram.links.filter(inSecondTileFromNW))
    }
  }

  private def unScale(i: Double)(implicit scale: Int): Int = (i / scale / 15 - 2).toInt
}
