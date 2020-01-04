package vmi.graph.test;

import vmi.graph.data.Graph;
import vmi.graph.layout.OneFormTorus;
import vmi.graph.ui.SVGRender;

public class FindLocations {

  public static void main(String[] args) {

    Graph g = Graph.readFromString("CHECKER\t4\t4\n" //
        + "[0,0,1,0,-1,0]\t[1,0,2,0,0,1]\t[2,0,1,1,3,1]\t[3,0,2,0,3,1]\t\n"
        + "[0,1,1,1,-1,2]\t[1,1,1,2,2,2]\t[3,1,4,1,2,2]\t\n"
        + "[0,2,-1,3,0,3]\t[1,2,0,2,1,3]\t[2,2,3,2,1,2]\t[3,2,4,2,2,3]\t\n"
        + "[0,3,1,3,0,4]\t[1,3,2,3,0,4]\t[2,3,3,3,1,4]\t[3,3,4,3,3,4]\t\n");

    if (g != null) {
      OneFormTorus t = new OneFormTorus(g);
      if (t.layout()) {
        SVGRender r = new SVGRender();
        r.draw(g);
      }
    }
  }
}
