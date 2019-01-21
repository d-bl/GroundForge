package vmi.graph.test;

import java.io.File;

import graph.data.Graph;
import vmi.graph.layout.OneFormTorus;
import graph.ui.SVGRender;

public class FindLocations {
	
public static void main(String[] args) {

	Graph g = SetupTestFile();
	
	if (g != null) {
		OneFormTorus t = new OneFormTorus(g);
		if (t.layout()) {
			SVGRender r = new SVGRender();
			r.draw(g);
		}
	}
}


public static Graph SetupTestFile() {
	String folder = "E:\\AAA\\Lace_Out\\tesselace\\";
	File file = new File(folder+"4x4_122.txt");  
	return Graph.readFromFile(file);
}
}
