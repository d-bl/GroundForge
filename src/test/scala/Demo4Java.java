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

import dibl.D3jsSVG;
import dibl.Diagram;
import dibl.LinkedNodes;
import dibl.NewPairDiagram;
import dibl.NodeProps;
import dibl.PairDiagram;
import dibl.PrototypeDiagram;
import dibl.SheetSVG;
import dibl.ThreadDiagram;
import dibl.TilesConfig;
import scala.collection.Seq;
import scala.collection.mutable.ArraySeq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo4Java {

  private static final File dir = new File("target/test/pattern/");

  public static void main(String[] args) throws IOException {
    dir.mkdirs();

    String[] urlQueries = { //
        "patchWidth=6&patchHeight=5" //
            + "&tile=5-&tileStitch=ct&"
            + "&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1",
        "patchWidth=12&patchHeight=12" + "&tile=831,4-7,-5-&tileStitch=ct&"
            + "shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        "patchWidth=11&patchHeight=12" //
            + "&tile=B-C-,---5,C-B-,-5--&tileStitch=ct"
            + "&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4",
        "patchWidth=7&patchHeight=16"
            + "&footside=-5,5-&footsideStitch=-"
            + "&tile=-5-,5-5,-5-,B-C,-5-&tileStitch=ct"
            + "&headside=5-,-5&headsideStitch=-"
            + "&shiftColsSW=-2&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=4",
        // more examples for testing: see the demo section of the tiles page
        // TODO fix the next patterns
        "patchWidth=8&patchHeight=14" // shared start node in top left corner: trouble for droste
            + "&footside=b,-,a,-&footsideStitch=-"
            + "&tile=831,4-7,-5-&tileStitch=ctct"
            + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        "patchWidth=7&patchHeight=13"
            + "&footside=-c14,b4--,-7c3,b-48&footsideStitch=-"
            + "&tile=831,488,-4-&tileStitch=ctc"
            + "&headside=44-,11C,87-,88C&headsideStitch=-"
            + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",

    };
    for (int i = 0; i <= urlQueries.length - 1; i++)
      drosteSteps(urlQueries[i], i);

    // squared diagram
    SheetSVG patterns = new SheetSVG(2, "height='100mm' width='110mm'", "GFP");
    patterns.add("586- -789 2111 -4-4", "checker");
    new java.io.FileOutputStream("sheet.svg").write(patterns.toSvgDoc().getBytes());
  }

  private static void drosteSteps(String urlQuery, int i) throws IOException {
    System.out.println("-------------- " + i);
    TilesConfig config = new TilesConfig(urlQuery);
    new FileOutputStream(dir + "/" + i + "-prototype.svg")
        .write((D3jsSVG.prolog() + PrototypeDiagram.create(config)).getBytes());

    Diagram pairs = NewPairDiagram.create(config);
    generateDiagram(i + "-pairs", "1px", pairs, config, 1);
    Diagram threads = ThreadDiagram.create(pairs);
    generateDiagram(i + "-threads", "2px", threads, config, 2);

    // for "ctct" alternatives see:
    // https://d-bl.github.io/GroundForge/help/Choose-Stitches#assign-stitches
    Diagram drostePairs = PairDiagram.create("ctct", threads);
    generateDiagram(i + "-droste-pairs", "1px", drostePairs, config, 2);
    Diagram drosteThreads = ThreadDiagram.create(drostePairs);
    generateDiagram(i + "-droste-threads", "2px", drosteThreads, config, 4);
  }

  private static void generateDiagram(
      String fileName,
      String strokeWidth,
      Diagram diagram,
      TilesConfig config,
      Integer scale
  ) throws IOException {
    System.out.println("-------------- " + fileName);

    // scala-doc for linksOfCenterTile in short:
    // tuples with (source,target) for all links within a tile and to adjacent tiles
    Seq<LinkedNodes> links = config.linksOfCenterTile(diagram, scale);

    diagram.logTileLinks(links);
    //showing how to access TODO compute deltas from logged data
    if(links.nonEmpty())
      diagram.node(links.apply(0).source()).id();

    int nrOfNodes = diagram.nodes().size();
    ArraySeq<NodeProps> nudgedNodes = new ArraySeq<NodeProps>(nrOfNodes);
    for (int i = 0; i < nrOfNodes; i++) {
      NodeProps node = diagram.node(i);
      NodeProps newNode = node.withLocation(node.x(), node.y()); // TODO apply deltas
      nudgedNodes.update(i, newNode);
    }

    Diagram nudgedDiagram = new Diagram(nudgedNodes, diagram.links());
    String svg = D3jsSVG.render(nudgedDiagram, strokeWidth, true, 744, 1052, 0d);
    new FileOutputStream(dir + "/" + fileName + ".svg") //
        .write((D3jsSVG.prolog() + svg).getBytes());
  }
}
