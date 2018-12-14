/*
 Copyright 2017 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html
*/
package dibl;

import scala.Tuple2;
import scala.collection.Seq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static sun.misc.Version.println;

public class Demo4Java {

  private static final File dir = new File("target/test/pattern/");

  public static void main(String[] args) throws Throwable {
    //noinspection ResultOfMethodCallIgnored
    dir.mkdirs();

    String[] urlQueries = { //
        "patchWidth=5&patchHeight=5"
            + "&tile=5-&tileStitch=ct&"
            + "&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1",
        "patchWidth=11&patchHeight=12" //
            + "&tile=B-C-,---5,C-B-,-5--&tileStitch=ct"
            + "&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4",
        // TODO for more examples: see the demo section of the tiles page
        //  patch size must be at least 4 rows/columns larger than the tile
        //  tiling with gaps might not work
    };
    for (int i = 0; i <= urlQueries.length - 1; i++) {
      generateSetOfDiagrams(urlQueries[i], i);
    }
  }

  private static void generateSetOfDiagrams(String urlQuery, int i) throws IOException {
    Config config = Config.create(urlQuery);
    Diagram pairs = NewPairDiagram.create(config);
    generateDiagram(i + "-pairs", "1px", pairs, config, 1);
    Diagram threads = ThreadDiagram.create(pairs);
    generateDiagram(i + "-threads", "2px", threads, config, 2);
    Diagram drostePairs = PairDiagram.create("ctct", threads);
    generateDiagram(i + "-droste-pairs", "1px", drostePairs, config, 2);
    Diagram drosteThreads = ThreadDiagram.create(drostePairs);
    generateDiagram(i + "-droste-threads", "2px", drosteThreads, config, 4);
  }

  private static void generateDiagram(
      String fileName,
      String strokeWidth,
      Diagram diagram,
      Config config,
      Integer scale
  ) throws IOException {
    System.out.println("-------------- "+fileName);

    NodeProps[][] links = config.centerTile(diagram, scale);
    // TODO calculate deltas from links and Config.shiftXxxx

    // TODO create copy of diagram.nodes(): node.withLocation(deltas.fx, deltas.fy)
    Seq<NodeProps> nudgedNodes = diagram.nodes();

    Diagram nudgedDiagram = new Diagram(nudgedNodes, diagram.links());
    String svg = D3jsSVG.render(nudgedDiagram, strokeWidth, true, 744, 1052, 0d);
    new FileOutputStream(dir + "/" + fileName + ".svg") //
        .write((D3jsSVG.prolog() + svg).getBytes());
  }
}
