package fte.data;

import java.util.LinkedList;

public class Face {
	LinkedList<Edge> edges;

	Face() {
	}

	void setEdges(LinkedList<Edge> edges) {
		this.edges = edges;
	}

	public LinkedList<Edge> getEdges() {
		return edges;
	}
}
