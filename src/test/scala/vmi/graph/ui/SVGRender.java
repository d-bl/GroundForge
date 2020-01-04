package vmi.graph.ui;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jfree.graphics2d.svg.SVGGraphics2D;

import vmi.graph.data.Edge;
import vmi.graph.data.Graph;
import vmi.graph.data.Vector;
import vmi.graph.data.Vertex;

public class SVGRender {

	Graph graph;
	
	static final double R = 3.0;
	
	public void draw(Graph g) {

		SVGGraphics2D g2 = new SVGGraphics2D(500, 500);
		
		this.graph = g;
		
		List<Vertex> vertices = graph.getVertices();
		List<Vector> vectors = graph.getTranslationVectors();
		Vector v0 = vectors.get(0);
		Vector v1 = vectors.get(1);
		
		
		for (double r = 0; r < 4; r++) {
			for (double c = 0; c < 4; c++) {
				double shiftX = r*v0.getX() + c*v1.getX();
				double shiftY = r*v0.getY() + c*v1.getY();
				drawRepeat(g2, vertices, shiftX, shiftY);
			}
		}
		
		
		
		g2.setPaint(Color.GREEN);
		g2.draw(new Line2D.Double(0,0, 100.0*v0.getX(), 100.0*v0.getY()));
		g2.draw(new Line2D.Double(0,0, 100.0*v1.getX(), 100.0*v1.getY()));
		
		String svgElement = g2.getSVGElement();
		String fname = "E:\\AAA\\Lace_Out\\TutteTests\\test.svg";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname));
			writer.write(svgElement.toString());
			writer.close();
		} catch (IOException e) {
			System.out.println("file io error");
		}
	}
	
	void drawRepeat(SVGGraphics2D g2, List<Vertex> vertices, double shiftX, double shiftY) {
		
		for (Vertex v : vertices) {
			double vx = v.getX()+shiftX;
			double vy = v.getY()+shiftY;
			
			g2.setPaint(Color.BLACK);
			g2.fill(new Ellipse2D.Double(100.0*vx-R, 100.0*vy-R, 2.0*R, 2.0*R));
			
			List<Edge> edges = v.getRotation();
			for (Edge e : edges) {
				if (e.getStart().equals(v)) {
					g2.setPaint(Color.BLACK);
					g2.draw(new Line2D.Double(100.0*vx, 100.0*vy, 100.0*(vx+e.getDeltaX()), 100.0*(vy+e.getDeltaY())));
				}
				
			}
		}
	}
}
