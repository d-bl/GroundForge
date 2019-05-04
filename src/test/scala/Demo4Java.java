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

import dibl.*;
import dibl.proto.TilesConfig;
import dibl.sheet.SheetSVG;
import scala.Tuple2;

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
        "patchWidth=7&patchHeight=16" + "&footside=-5,5-&footsideStitch=-"
            + "&tile=-5-,5-5,-5-,B-C,-5-&tileStitch=ct" + "&headside=5-,-5&headsideStitch=-"
            + "&shiftColsSW=-2&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=4",
        // more examples for testing: see the demo section of the tiles page
        // TODO fix the next patterns
        "patchWidth=8&patchHeight=14" // shared start node in top left corner: trouble for droste
            + "&footside=b,-,a,-&footsideStitch=-" + "&tile=831,4-7,-5-&tileStitch=ctct"
            + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        "patchWidth=7&patchHeight=13" + "&footside=-c14,b4--,-7c3,b-48&footsideStitch=-"
            + "&tile=831,488,-4-&tileStitch=ctc" + "&headside=44-,11C,87-,88C&headsideStitch=-"
            + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        "patchWidth=8&patchHeight=12"
            + "&p1=-&o1=-&n1=-&m1=-&g1=cttct&f1=cttct&e1=cttct&d1=cttct&c1=tctct&b1=-&a1=-&o2=-&m2=-"
            + "&g2=cttct&e2=cttct&c2=tctct&a2=-&p3=-&o3=-&n3=-&m3=-&f3=cttct&d3=cttct&c3=-&b3=-&a3=-&o4=-&m4=tctct&c4=-&a4=-"
            + "&footside=8315,4-7-,1583,7-4-&footsideStitch=-"
            + "&tile=831,4-7,-5-&tileStitch=cttct"
            + "&headside=8315,4-7-,1583,7-4-&headsideStitch=-"
            + "&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        "patchWidth=8&patchHeight=12&p1=-&o1=-&n1=-&m1=-&g1=cttct&f1=cttct&e1=cttct&d1=-&c1=tctct&b1=-&a1=-&o2=-&m2=-&g2=cttct&e2=cttct&c2=tctct&a2=-&p3=-&o3=-&n3=-&m3=-&f3=cttct&d3=cttct&c3=-&b3=-&a3=-&o4=-&m4=tctct&c4=-&a4=-&footside=8315,4-7-,1583,7-4-&tile=831,4-7,-5-&headside=8315,4-7-,1583,7-4-&footsideStitch=-&tileStitch=cttct&headsideStitch=-&shiftColsSW=-2&shiftRowsSW=2&shiftColsSE=2&shiftRowsSE=2",
        "patchWidth=5&patchHeight=1" //
                + "&tile=VWXYZ&tileStitch=ct&"
                + "&shiftColsSW=0&shiftRowsSW=1&shiftColsSE=6&shiftRowsSE=1",
        "patchWidth=17&patchHeight=16&j1=ctcl&f1=ctcr&d1=c&c1=ctct&b1=c&i2=-&g2=-&d2=cr&b2=cl&h3=ctct&c3=ctc&i4=-&g4=-&d4=c&b4=c&j5=ctcr&f5=ctcl&d5=c&c5=tctc&b5=c&tile=-O3E-5---5,-4-7--W-Y-,--5----5--,-B-C--Y-W-,-158-L---H&footsideStitch=tctct&tileStitch=ctc&headsideStitch=tctct&shiftColsSW=-5&shiftRowsSW=5&shiftColsSE=5&shiftRowsSE=5",
        "patchWidth=7&patchHeight=18&i1=-&c1=ct&a1=tctctr&d2=ct&b2=ct&a2=tctctr&c3=ct&a3=-&d4=ct&b4=ct&c5=ct&a5=tctctr&a7=tctctr&footside=B,X,X,-,B,-,B,-,&tile=-5-,5-5,-5-,B-C,-5-&headside=W,-,&footsideStitch=tctctr&tileStitch=ct&headsideStitch=-&shiftColsSW=-2&shiftRowsSW=4&shiftColsSE=2&shiftRowsSE=4",
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
        .write((D3jsSVG.prolog() + dibl.proto.PrototypeDiagram.create(config)).getBytes());

    Diagram pairs = NewPairDiagram.create(config);
    writeNudgedDiagram(i + "-pairs", "1px", pairs, config, 1);
    Diagram threads = ThreadDiagram.create(pairs);
    Diagram nudgedThreads = writeNudgedDiagram(i + "-threads", "2px", threads, config, 2);

    // Note 1: for "ctct" alternatives see:
    // https://d-bl.github.io/GroundForge/help/Tiles#choose-stitches
    // Note 2: the pair diagram reuses the positions calculated for the thread diagram
    // the thread diagram needs original positions without the nudging for the previous thread diagram
    writeDiagram(i + "-droste-pairs", "1px", PairDiagram.create("ctct", nudgedThreads), config.svgBoundsOfCenterTile(2 * 15));
    writeNudgedDiagram(i + "-droste-threads", "2px", ThreadDiagram.create(PairDiagram.create("ctct", threads)), config, 4);
  }

  private static Diagram writeNudgedDiagram(String fileName, String strokeWidth, Diagram diagram,
      TilesConfig config, Integer scale) throws IOException {
    System.out.println("-------------- " + fileName);

    // needs original positions without any nudging applied to previous diagrams
    Tuple2<NodeProps, NodeProps[]>[] linkedNodes = config.linksOfCenterTile(diagram, scale);
    diagram.logTileLinks(linkedNodes);
    if (linkedNodes.length > 0) {
      // TODO compute deltas from logged data
      // showing how to access
      String coreId = linkedNodes[0]._1().id();
      String firstLinkId = linkedNodes[0]._2()[0].id();
    }

    int nrOfNodes = diagram.nodes().size();
    double[][] locations = new double[nrOfNodes][2];
    for (int i = 0; i < nrOfNodes; i++) {
      // TODO apply deltas
      locations[i][0] = diagram.node(i).x();
      locations[i][1] = diagram.node(i).y();
    }

    return writeDiagram(fileName, strokeWidth, diagram.withLocations(locations), config.svgBoundsOfCenterTile(scale * 15));
  }

  private static Diagram writeDiagram(String fileName, String strokeWidth, Diagram diagram, String bounds)
      throws IOException {
    String svg = D3jsSVG.render(diagram, strokeWidth, true, 744, 1052, 0d)
            .replace("</svg>",bounds + "</svg>");
    new FileOutputStream(dir + "/" + fileName + ".svg") //
        .write((D3jsSVG.prolog() + svg).getBytes());
    return diagram;
  }
}
