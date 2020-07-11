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

import java.io.FileOutputStream

import dibl.fte.GraphCreator.fromThreadDiagram
import dibl.fte.data.{ Graph, Vertex }

import scala.collection.JavaConverters._
import scala.util.{ Failure, Try }

object SvgCreator {
  def main(args: Array[String]): Unit = {
    Try(fromThreadDiagram(args.last).foreach { graph =>
      new FileOutputStream(args.head)
        .write(draw(graph).getBytes)
    }) match {
      case Failure(e) => e.printStackTrace()
      case _ =>
    }
  }

  def draw(graph: Graph): String = {
    draw(
      graph.getVertices.asScala,
      graph.getTranslationVectors.asScala.map(v => v.getX -> v.getY),
    )
  }

  private def draw(vertices: Seq[Vertex], vectors: Seq[(Double, Double)]) = {
    val ((v0x, v0y), (v1x, v1y)) = {
      (vectors.head, vectors.last)
    }
    val offset = if (v1x < 0) 4 * v1x
                 else 0
    val width: Double = 10d / Math.sqrt(vertices.size)
    val repeats: Seq[Seq[String]] =
      for {
        row <- 0 until 4
        col <- 0 until 4
      } yield {
        val color = if (row == 0 && col == 0) "rgb(0,0,255)"
                    else "rgb(0,0,0)"
        vertices.map(
          dotWithEdges(_, row * v0x + col * v1x - offset, row * v0y + col * v1y)(width, color)
        )
      }
    s"""<svg xmlns="http://www.w3.org/2000/svg" width="500" height="500">
       |${ line(-offset, 0, v0x - offset, v0y)(width = 3, "rgb(0,255,0)") }
       |${ line(-offset, 0, v1x - offset, v1y)(width = 3, "rgb(0,255,0)") }
       |${ repeats.map(_.mkString).mkString }
       |</svg>
       |""".stripMargin
  }

  private def dotWithEdges(v: Vertex, shiftX: Double, shiftY: Double)
                          (implicit r: Double, color: String): String = {
    val vx: Double = v.getX + shiftX
    val vy: Double = v.getY + shiftY
    val lines = v.getRotation.asScala
      .withFilter(_.getStart == v)
      .map(e => line(vx, vy, vx + e.getDeltaX, vy + e.getDeltaY))
      .mkString("\n")
    s"""${ dot(vx, vy) }
       |$lines
       |""".stripMargin
  }

  private def line(x1: Double, y1: Double, x2: Double, y2: Double)
                  (implicit width: Double, color: String): String = {
    s"""<line x1="${ 100 * x1 }" y1="${ 100 * y1 }" x2="${ 100 * x2 }" y2="${ 100 * y2 }" style="stroke-width: ${ width / 2 };stroke: $color" />"""
  }

  private def dot(x: Double, y: Double)
                 (implicit r: Double, color: String = "rgb(0,0,255)"): String = {
    s"""<ellipse cx="${ 100 * x }" cy="${ 100 * y }" rx="$r" ry="$r" style="fill: $color" />"""
  }
}
