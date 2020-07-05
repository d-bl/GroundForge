package dibl.fte.data;

import java.util.List;

public class JFace {
	List<Edge> edges;

	JFace() {
	}

	public JFace(List<Edge> edges) {
		this.edges = edges;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
}
