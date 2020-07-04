package dibl.fte.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph implements Cloneable {

	List<Vertex> vertices;
	List<Edge> edges;
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
}
