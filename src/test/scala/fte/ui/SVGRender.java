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
	public void draw(Graph graph, String fname) {

		SVGGraphics2D g2 = new SVGGraphics2D(500, 500);

		List<Vertex> vertices = graph.getVertices();
		List<Vector> vectors = graph.getTranslationVectors();
		Vector v0 = vectors.get(0);
		Vector v1 = vectors.get(1);

		double offset = 0d;
		if (v1.getX()<0) offset = 4*v1.getX();

		g2.setPaint(Color.GREEN);
		g2.draw(new Line2D.Double(-100.0*offset,0, 100.0*(v0.getX()-offset), 100.0*v0.getY()));
		g2.draw(new Line2D.Double(-100.0*offset,0, 100.0*(v1.getX()-offset), 100.0*v1.getY()));

		// TODO rather use the smallest length to scale the dots/strokes
		double dotR = 10d/Math.sqrt(vertices.size());
		g2.setStroke(new BasicStroke((float) (dotR / 2)));

		for (double row = 0; row < 4; row++) {
			for (double col = 0; col < 4; col++) {
				double shiftCol = row*v0.getX() + col*v1.getX() - offset;
				double shiftRow = row*v0.getY() + col*v1.getY();
				drawRepeat(g2, vertices, shiftCol, shiftRow, dotR, row==0 && col == 0);
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
	
	void drawRepeat(SVGGraphics2D g2, List<Vertex> vertices, double shiftX, double shiftY, double r, boolean first) {

		if (first) g2.setPaint(Color.BLUE);
		else g2.setPaint(Color.BLACK);
		for (Vertex v : vertices) {
			double vx = v.getX()+shiftX;
			double vy = v.getY()+shiftY;

			g2.fill(new Ellipse2D.Double(100.0*vx-r, 100.0*vy-r, 2.0*r, 2.0*r));
			List<Edge> edges = v.getRotation();
			for (Edge e : edges) {
				if (e.getStart().equals(v)) {
					g2.draw(new Line2D.Double(100.0*vx, 100.0*vy, 100.0*(vx+e.getDeltaX()), 100.0*(vy+e.getDeltaY())));
				}
			}
		}
	}
}
