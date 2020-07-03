package dibl.fte.data;

import java.util.List;

public class Face {
	List<Edge> edges;

	Face() {
	}

	public Face(List<Edge> edges) {
		this.edges = edges;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
}
