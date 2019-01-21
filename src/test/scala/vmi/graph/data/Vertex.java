package vmi.graph.data;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Cloneable {
	
	String id;
	double x;
	double y;
	
	/**
	 * A rotation system
	 */
	ArrayList<Edge> rotation;
	
	public Vertex(double x, double y) {
		this.x = x;
	    this.y = y;
	    this.id = String.format("%.2f-%.2f", this.x, this.y);
	    rotation= new ArrayList<Edge>();
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
		// TODO should the id be updated?
		this.id = String.format("%.2f-%.2f", this.x, this.y);
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
		// TODO should the id be updated?
		this.id = String.format("%.2f-%.2f", this.x, this.y);
	}
	
	public List<Edge> getRotation() {
		return rotation;
	}
	
	@Override
	public String toString() {
		String s = id+":("+x+","+y+")";
		return s;
	}
	
	void addEdge(Edge e) {
		if (rotation.contains(e)) return;

		// find the position of this edge in a clockwise rotation system starting at the
		// positive x-axis
		double angle = getAngle(e);
		int index = 0;
		for (Edge f : rotation) {
			double fangle = getAngle(f);
			if (fangle < angle)
				index++;
		}

		rotation.add(index, e);
	}

	double getAngle(Edge e) {
		boolean forward = e.start.equals(this);
		double dx = forward ? e.deltaX : -e.deltaX;
		double dy = forward ? e.deltaY : -e.deltaY;
		double angle = dx == 0 ? Math.PI / 2.0 : Math.atan(Math.abs(dy) / Math.abs(dx));
		if (dx < 0) {
			angle = dy > 0 ? Math.PI + angle : Math.PI - angle;
		} else {
			angle = dy > 0 ? 2.0 * Math.PI - angle : angle;
		}
		return angle;
	}

	public Edge getNextEdge(Edge e) {
		boolean next = false;
		for (Edge r : rotation) {
			if (next) {
				return r;
			}
			if (e.equals(r)) {
				next = true;
			}
		}
		return next ? rotation.get(0) : null;
	}

	public Edge getPreviousEdge(Edge e) {
		Edge prev = rotation.get(rotation.size()-1);
		for (Edge r : rotation) {
			if (e.equals(r)) {
				return prev;
			}
			prev = r;
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vertex) {
			Vertex other = (Vertex)obj;
			return id.equals(other.id);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
