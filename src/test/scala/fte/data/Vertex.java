package fte.data;

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
	    rotation= new ArrayList<>();
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
		return id+":("+x+","+y+")";
	}
	
	void addEdge(Edge e) {
		rotation.add(e);
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
