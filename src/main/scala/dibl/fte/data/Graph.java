package dibl.fte.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph implements Cloneable {

	List<Vertex> vertices;
	List<Edge> edges;
	List<Face> faces;
	List<Vector> vectors;

	/**
	 * Initializes a graph embedding by creating empty lists of edges, vertices and a rotation system
	 */
	public Graph(){
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Edge> getEdges() {
		return edges;
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

	public Edge addNewEdge(Edge e) {
		edges.add(e);
		return e;
	}

	public List<Face> getFaces() {

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
				List<Edge> edges = new LinkedList<>();
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
					if (e!=null && e.equals(faceStart)) {
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
}
