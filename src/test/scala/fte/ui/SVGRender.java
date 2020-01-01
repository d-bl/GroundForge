package fte.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import fte.data.Vector;
import org.jfree.graphics2d.svg.SVGGraphics2D;

import fte.data.Edge;
import fte.data.Graph;
import fte.data.Vertex;

public class SVGRender {

	Graph graph;
	
	static final double R = 3.0;
	
	public void draw(Graph g, String fname) {

		SVGGraphics2D g2 = new SVGGraphics2D(500, 500);
		
		this.graph = g;
		
		List<Vertex> vertices = graph.getVertices();
		List<Vector> vectors = graph.getTranslationVectors();
		Vector v0 = vectors.get(0);
		Vector v1 = vectors.get(1);

		g2.setPaint(Color.GREEN);
		g2.draw(new Line2D.Double(0,0, 100.0*v0.getX(), 100.0*v0.getY()));
		g2.draw(new Line2D.Double(0,0, 100.0*v1.getX(), 100.0*v1.getY()));
		g2.setPaint(Color.red);
		g2.fill(new Ellipse2D.Double(100.0*v0.getX()-R, 100.0*v0.getY()-R, 2.0*R, 2.0*R));
		g2.setPaint(Color.blue);
		g2.fill(new Ellipse2D.Double(100.0*Math.abs(v1.getX())-R, 100.0*v1.getY()-R, 2.0*R, 2.0*R));

		double offset = 0d;
		if (v1.getX()<0) offset = 4*v1.getX();
		for (double r = 0; r < 4; r++) {
			for (double c = 0; c < 4; c++) {
				double shiftX = r*v0.getX() + c*v1.getX() - offset;
				double shiftY = r*v0.getY() + c*v1.getY();
				drawRepeat(g2, vertices, shiftX, shiftY);
			}
		}

		String svgElement = g2.getSVGElement();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname));
			writer.write(svgElement);
			writer.close();
		} catch (IOException e) {
			System.out.println("file io error");
		}
	}
	
	void drawRepeat(SVGGraphics2D g2, List<Vertex> vertices, double shiftX, double shiftY) {

		// TODO rather use the smallest length to scale the dots
		double r = 10d/Math.sqrt(vertices.size());
		g2.setStroke(new BasicStroke((float) (r / 2)));

		for (Vertex v : vertices) {
			double vx = v.getX()+shiftX;
			double vy = v.getY()+shiftY;
			
			g2.setPaint(Color.red);
			g2.fill(new Ellipse2D.Double(100.0*vx-r, 100.0*vy-r, 2.0*r, 2.0*r));
			
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
