package dibl.fte.data;


public class Edge implements Cloneable {
	
	String id;
	Vertex start;
	Vertex end;

	double deltaX;
	double deltaY;
	double weight = 1.0;

	/**
	 * Creates an edge with the specified origin and destination vertices
	 * @param origin Origin vertex
	 * @param destination Destination vertex
	 */
	public Edge(Vertex origin, Vertex destination) {
		super();
		this.start = origin;
		this.end = destination;
		this.deltaX = 0;
		this.deltaY = 0;
		this.id = String.format("%s:%s(%.2f,%.2f)", this.start.id, this.end.id, this.deltaX, this.deltaY);
	}
	
	public Vertex getStart() {
		return start;
	}
	
	public Vertex getEnd() {
		return end;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getDeltaX() {
		return deltaX;
	}
	
	public Edge setDeltaX(double dx) {
		this.deltaX = dx;
		return this;
	}
	
	public double getDeltaY() {
		return deltaY;
	}
	
	public Edge setDeltaY(double dy) {
		this.deltaY = dy;
		return this;
	}

	@Override
	public String toString() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == this) return true;
	    if (!(obj instanceof Edge)) return false;
	    return id.equals(((Edge) obj).id);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
