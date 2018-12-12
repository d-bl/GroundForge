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

import scala.collection.Seq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo4Java {

  private static final File dir = new File("target/test/pattern/");

  public static void main(String[] args) throws Throwable {
    //noinspection ResultOfMethodCallIgnored
    dir.mkdirs();

    String urlQuerys[] = { //
        "patchWidth=11&patchHeight=12"//
            + "&tile=B-C-,---5,C-B-,-5--&tileStitch=ctct"
            + "&footside=-7,B4,17,17&footsideStitch=ctctt"
            + "&headside=4-,7C,48,48&headsideStitch=ctctt"
            + "&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4",
        "patchWidth=11&patchHeight=12" //
            + "&tile=B-C-,---5,C-B-,-5--&tileStitch=ctct"
            + "&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4"
        // TODO for more examples: see the demo section of the tiles page
    };
    for (int i = 0; i <= urlQuerys.length - 1; i++) {
      generateSetOfDiagrams(urlQuerys[i], i);
    }
  }

  private static void generateSetOfDiagrams(String urlQuery, int i) throws IOException {
    Diagram pairs = NewPairDiagram.create(Config.create(urlQuery));
    generateSingleDiagram(i + "-pairs", "1px", pairs.nodes(), pairs.links());
    Diagram threads = ThreadDiagram.create(pairs);
    generateSingleDiagram(i + "-threads", "2px", threads.nodes(), threads.links());
    Diagram drostePairs = PairDiagram.create("ctct", threads);
    generateSingleDiagram(i + "-droste-pairs", "1px", drostePairs.nodes(), drostePairs.links());
    Diagram drosteThreads = ThreadDiagram.create(drostePairs);
    generateSingleDiagram(i + "-droste-threads", "2px", drosteThreads.nodes(), drosteThreads.links());
  }

  private static void generateSingleDiagram(String fileName,
                                            String strokeWidth,
                                            Seq<NodeProps> nudgedNodes,
                                            Seq<LinkProps> links)
      throws IOException {

    Diagram nudgedDiagram = new Diagram(nudgedNodes, links);
    String svg = D3jsSVG.render(nudgedDiagram, strokeWidth, true, 744, 1052, 0d);
    new FileOutputStream(dir + "/" + fileName + ".svg") //
        .write((D3jsSVG.prolog() + svg).getBytes());
  }
}
