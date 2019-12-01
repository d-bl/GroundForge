package fte.data;

public class Vector {
	
	double x;
	double y;
	
	public static final double ACC = 100000;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == this) return true;
	    if (!(obj instanceof Vector)) return false;
	    Vector other = (Vector) obj;
	    return (int)(other.x*ACC) == (int)(this.x*ACC) && (int)(other.y*ACC) == (int)(this.y*ACC);
	}
	
	@Override
	public int hashCode() {
		return (int)(x*ACC) + (int)(y*ACC) << 17;
	}
	
	@Override
	public String toString() {
		return x+", "+y;
	}
	
}
