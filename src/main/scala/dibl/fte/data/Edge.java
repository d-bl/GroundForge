package dibl.fte.data;


public class Edge implements Cloneable {
	
	String id;
	Vertex start;
	Vertex end;
	Face forFace;
	Face revFace;

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
	
	public boolean forward(Face f) {
		// TODO could check whether f is either one of forFace or revFace and throw exception otherwise
		return f.equals(forFace);
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
	
	public void setDeltaX(double dx) {
		this.deltaX = dx;
	}
	
	public double getDeltaY() {
		return deltaY;
	}
	
	public void setDeltaY(double dy) {
		this.deltaY = dy;
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
