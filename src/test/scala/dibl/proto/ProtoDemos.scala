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
package dibl.proto

import dibl.{DemoFixture, Patterns}

import scala.reflect.io.File

object ProtoDemos extends DemoFixture {

  def main(args: Array[String]): Unit = {
    Patterns.all.foreach { case (nr: String, q: String) =>
        val config = TilesConfig(q + s"&patchWidth=30&patchHeight=30")
        File(s"$testDir/$nr.svg").writeAll(PrototypeDiagram.create(config))
        // '/C/Program Files/Inkscape/inkscape.exe' 001.svg --export-png=001.png -w78 -h78
        // with a bash shell (comes with git):
        // for i in *.svg; do '/C/Program Files/Inkscape/inkscape.exe' $i --export-png=`echo $i | sed -e 's/svg$/png/'` -w200 -h200; done
      }
  }
}
