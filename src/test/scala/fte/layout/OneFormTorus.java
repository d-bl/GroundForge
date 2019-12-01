package fte.layout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fte.data.Face;
import fte.data.Vector;
import fte.data.Vertex;
import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

import fte.data.Edge;
import fte.data.Face;
import fte.data.Graph;
import fte.data.Vector;
import fte.data.Vertex;

public class OneFormTorus {

	private Graph graph;

	public OneFormTorus(Graph g) {
		this.graph = g;
	}

	public boolean layout() {
		
		List<Edge> edges = graph.getEdges();

		int m = edges.size();
		double[][] data = new double[m][m];
		int index = 0;
		// edge orientation around each face
		List<Face> faces = graph.getFaces();
		for (Face face : faces) {
			LinkedList<Edge> elist = face.getEdges();
			for (Edge e : elist) {
				int col = edges.indexOf(e);
				int value = 1;
				data[index][col] = e.forward(face) ? value : -value;
			}
			index++;
		}

		// edge orientation around each vertex
		List<Vertex> vertices = graph.getVertices();
		for (Vertex v : vertices) {
			List<Edge> es = v.getRotation();
			for (Edge e : es) {
				int col = edges.indexOf(e);
				int value = e.getStart().equals(v) ? 1 : -1;
				data[index][col] = value * e.getWeight();
			}
			index++;
		}

		SimpleMatrix sm = new SimpleMatrix(data);

		SimpleSVD<SimpleMatrix> svd = sm.svd();

		SimpleMatrix nullSpace = svd.nullSpace();

		if (nullSpace.numCols() != 2) {
			System.out.println("WRONG column number " + nullSpace.numCols());
			return false;
		}

		for (int r = 0; r < m; r++) {
			edges.get(r).setDeltaX(nullSpace.get(r, 0));
			edges.get(r).setDeltaY(nullSpace.get(r, 1));
		}

		// traverse graph to fill in x and y values
		boolean visited[] = new boolean[edges.size()];
		ArrayList<Vector>vectors = new ArrayList<Vector>();
		setLocationsDFS(vertices.get(0), 0.0, 0.0, visited, vectors);

		// Find an osculating path
		Vector OP = getOsculatingPath();
		vectors.add(OP);
		
		// rotate so that osculating path is vertical
		double theta = OP.getY() == 0 ? Math.PI / 2.0 : Math.atan(OP.getX() / OP.getY());
		theta = OP.getY() < 0 ? theta + Math.PI : theta;
		rotateGraph(theta, vectors);
		
		if (!findTranslationVectors(vectors, OP)) return false;

		// Move vertices into parallelogram
		moveToParallelogram();

		return true;
	}
	
	private void setLocationsDFS(Vertex v, double valueX, double valueY, boolean visited[], ArrayList<Vector> vectors) {
		List<Vertex> vertices = graph.getVertices();
		int vIndex = vertices.indexOf(v);
		if (visited[vIndex]) {
			double dx = valueX - v.getX();
			double dy = valueY - v.getY();
			if ((int)(dx*Vector.ACC) != 0 && (int)(dy*Vector.ACC) != 0) {
				Vector vect = new Vector(dx, dy);
				Vector negvect = new Vector(-dx, -dy);
				if (!vectors.contains(vect) && !vectors.contains(negvect)) {
					vectors.add(vect);
				}
			}
			return;
		}
		
		visited[vIndex] = true;
		
		v.setX(valueX);
		v.setY(valueY);

        
        // Recurse for all adjacent vertices 
        List<Edge> incident = v.getRotation();
        for (Edge e : incident) {
        	Vertex next = e.getStart();
        	double nextValueX = valueX;
        	double nextValueY = valueY;
        	
        	if (next.equals(v)) {
        		next = e.getEnd();
        		nextValueX += e.getDeltaX();
        		nextValueY += e.getDeltaY();
	        	setLocationsDFS(next, nextValueX, nextValueY, visited, vectors);
        	} else {
        		next = e.getStart();
        		nextValueX -= e.getDeltaX();
        		nextValueY -= e.getDeltaY();
	        	setLocationsDFS(next, nextValueX, nextValueY, visited, vectors);
        	}
        }
    }
	
	private boolean findTranslationVectors(ArrayList<Vector> vectors, Vector OP) {
	
		// Find shortest vector that is not parallel to OP
		Vector minV = null;
		double minS = Double.MAX_VALUE;
		for (Vector v : vectors) {
			double vsize = v.getX()*v.getX() + v.getY()*v.getY(); // Don't bother with sqrt
			if (vsize < minS) {
				// check whether parallel to OP (OP is roughly vertical)
				if (Math.abs(v.getX()) < 0.001) continue;
				minV = v;
				minS = vsize;
			}
		}
		
		graph.setTranslationVectors(OP, minV);
		
		return true;
	}
	
	private Vector getOsculatingPath() {
		Edge e0 = graph.getVertices().get(0).getRotation().get(0);
		double dx = e0.getDeltaX();
		double dy = e0.getDeltaY();
		
		Edge prev = e0;
		Vertex v = prev.getEnd();
		Edge next = v.getNextEdge(prev);
		if (!next.getStart().equals(v)) {
			next = v.getPreviousEdge(prev);
		}
		
		while (!next.equals(e0)) {
			dx += next.getDeltaX();
			dy += next.getDeltaY();
			
			prev = next;
			v = prev.getEnd();
			next = v.getNextEdge(prev);
			if (!next.getStart().equals(v)) {
				next = v.getPreviousEdge(prev);
			}
		}
		
		return new Vector(dx, dy);
	}
	
	private void rotateGraph(double theta, ArrayList<Vector> vectors) {
		double cth = Math.cos(theta);
		double sth = Math.sin(theta);
		
		for (Vertex v : graph.getVertices()) {
			double x = v.getX();
			double y = v.getY();
			v.setX(x*cth - y*sth);
			v.setY(x*sth + y*cth); 
		}
		
		for (Edge e : graph.getEdges()) {
			double x = e.getDeltaX();
			double y = e.getDeltaY();
			e.setDeltaX(x*cth - y*sth);
			e.setDeltaY(x*sth + y*cth); 
		}
		
		for (Vector v : vectors) {
			double x = v.getX();
			double y = v.getY();
			v.setX(x*cth - y*sth);
			v.setY(x*sth + y*cth); 
		}
	}
	
	private void vShearGraph(double m) {
		
		for (Vertex v : graph.getVertices()) {
			double x = v.getX();
			double y = v.getY();
			v.setY(x*m + y); 
		}
		
		for (Edge e : graph.getEdges()) {
			double x = e.getDeltaX();
			double y = e.getDeltaY();
			e.setDeltaY(x*m + y); 
		}
		
		for (Vector v : graph.getTranslationVectors()) {
			double x = v.getX();
			double y = v.getY();
			v.setY(x*m + y); 
		}
	}
	
	private void moveToParallelogram() {
		List<Vector> vectors = graph.getTranslationVectors();
		Vector v0 = vectors.get(0);
		Vector v1 = vectors.get(1);
		boolean vert0 = Math.abs(v0.getX()) < 0.0001;
		
		// remove any vertical shear
		double m = vert0 ? v1.getY()/v1.getX() : v0.getY()/v0.getX();
		vShearGraph(-m);
		
		Vector vShift = vert0 ? new Vector(Math.abs(v0.getX()), Math.abs(v0.getY())) : new Vector(Math.abs(v1.getX()), Math.abs(v1.getY()));
		Vector hShift = vert0 ? new Vector(Math.abs(v1.getX()), Math.abs(v1.getY())) : new Vector(Math.abs(v0.getX()), Math.abs(v0.getY()));
		
		for (Vertex v : graph.getVertices()) {
			
			while (v.getY() <= 0) {
				// shift down
				v.setX(v.getX()+vShift.getX());
				v.setY(v.getY()+vShift.getY());
			}
			while (v.getY() > vShift.getY()) {
				// shift up
				v.setX(v.getX()-vShift.getX());
				v.setY(v.getY()-vShift.getY());
			}
			while (v.getX() <= 0) {
				// shift right
				v.setX(v.getX()+hShift.getX());
				v.setY(v.getY()+hShift.getY());
			}
			while (v.getX() > hShift.getX()) {
				// shift left
				v.setX(v.getX()-hShift.getX());
				v.setY(v.getY()-hShift.getY());
			}
		}
		
		// restore vertical shear if any
		vShearGraph(m);
		
	}
	
}
