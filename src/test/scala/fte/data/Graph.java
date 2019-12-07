package fte.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph implements Cloneable {

	List<Vertex> vertices;
	List<Edge> edges;
	List<Face> faces;
	List<Vector> vectors;
	private int rowCount;
	private int colCount;

	/**
	 * Initializes a graph embedding by creating empty lists of edges, vertices and a rotation system
	 */
	public Graph(int rows, int cols){
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
		rowCount = rows;
		colCount = cols;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Face> getFaces() {
		return createFaceData();
	}

	public List<Vector> getTranslationVectors() {
		return vectors;
	}

	public void setTranslationVectors(Vector v1, Vector v2) {
		vectors = new ArrayList<>();
		vectors.add(v1);
		vectors.add(v2);
	}

	public Vertex createVertex(float x, float y) {
		Vertex v = new Vertex(x, y);
		vertices.add(v);
		return v;
	}

	public void createEdge(Vertex start, Vertex end, double dx, double dy) {
		Edge	e = new Edge(start, end, dx, dy);
		edges.add(e);
		start.addEdge(e);
		end.addEdge(e);
	}

	private List<Face> createFaceData() {

		if (faces != null) return faces;

		faces = new ArrayList<>();

		// keep track of how many times an edge is visited
		ArrayList<Edge> forward = new ArrayList<>();
		ArrayList<Edge> reverse = new ArrayList<>();

		Edge faceStart = null;

		do {

			if (faceStart != null) {

				Edge e = faceStart;
				Vertex prev = faceStart.start;
				if (forward.contains(faceStart)) {
					prev = faceStart.end;
				}

				Face face = new Face();
				LinkedList<Edge> edges = new LinkedList<>();
				while (e != null) {
					edges.add(e);
					if (e.start.equals(prev)) {
						forward.add(e);
						e.forFace = face;
					}
					// Note: for some cases such as 1x1_1, an edge may be traversed both forward and backward
					if (e.end.equals(prev)) {
						reverse.add(e);
						e.revFace = face;
					}

					prev = e.start.equals(prev) ? e.end : e.start;
					e = prev.getNextEdge(e);
					if (e.equals(faceStart)) {
						e = null;
					}
				}
				face.setEdges(edges);
				faces.add(face);

			}

			// pick an edge to start
			faceStart = null;
			for (Vertex v : vertices) {
				for (Edge r : v.rotation) {
					if (forward.contains(r) && reverse.contains(r))
						continue;
					faceStart = r;
					break;
				}
			}

		} while (faceStart != null);

		return faces;
	}

	public void addEdge(int destCol, int destRow, int srcCol, int srcRow) {
		//System.out.println(String.format("destCol=%d destRow=%d srcCol=%d srcRow=%d",destCol, destRow, srcCol, srcRow));
		Vertex dest = createVertex(mod(destCol, colCount), mod(destRow, rowCount));
		Vertex src = createVertex(mod(srcCol, colCount), mod(srcRow, rowCount));
		createEdge(src, dest, destCol-srcCol, destRow - srcRow);
	}

	/**
	 * Handle modulo of negative numbers in a different way from Java
	 */
	static int mod(int a, int b) {
		int result = a % b;
		if (result < 0) result += b;
		return result;
	}
}
