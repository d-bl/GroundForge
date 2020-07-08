package dibl.fte.layout;

import dibl.fte.data.Edge;
import dibl.fte.data.Vector;
import dibl.fte.data.Vertex;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.List;

public class LocationsDFS {
  private List<Vertex> vertices;
  private ArrayList<Vector>vectors = new ArrayList<>();

  public LocationsDFS(List<Vertex> vertices, List<Edge> edges, SimpleMatrix nullSpace) {
    this.vertices = vertices;

    int m = edges.size();
    for (int r = 0; r < m; r++) {
      edges.get(r).setDeltaX(nullSpace.get(r, 0));
      edges.get(r).setDeltaY(nullSpace.get(r, 1));
    }

    // traverse graph to fill in x and y values
    boolean[] visited = new boolean[m];
    set(vertices.get(0), 0.0, 0.0, visited, vectors);
  }

  public ArrayList<Vector> getVectors() {
    return vectors;
  }

  private void set(Vertex v, double valueX, double valueY, boolean[] visited, ArrayList<Vector> vectors) {
    int vIndex = vertices.indexOf(v);
    if (visited[vIndex]) {
      double dx = valueX - v.getX();
      double dy = valueY - v.getY();
      if ((int)(dx*Vector.ACC) != 0 && (int)(dy*Vector.ACC) != 0) {
        Vector vect = new Vector(dx, dy);
        Vector negvect = new Vector(-dx, -dy);
        if (!vectors.contains(vect) && !vectors.contains(negvect)) {
          vectors.add(vect);
        }
      }
      return;
    }

    visited[vIndex] = true;

    v.setX(valueX);
    v.setY(valueY);


    // Recurse for all adjacent vertices
    List<Edge> incident = v.getRotation();
    for (Edge e : incident) {
      Vertex next = e.getStart();
      double nextValueX = valueX;
      double nextValueY = valueY;

      if (next.equals(v)) {
        next = e.getEnd();
        nextValueX += e.getDeltaX();
        nextValueY += e.getDeltaY();
      } else {
        next = e.getStart();
        nextValueX -= e.getDeltaX();
        nextValueY -= e.getDeltaY();
      }
      set(next, nextValueX, nextValueY, visited, vectors);
    }
  }
}
