package dibl.fte.layout;

import dibl.fte.data.Edge;
import dibl.fte.data.Face;
import dibl.fte.data.Vertex;

import java.util.Arrays;
import java.util.List;

public class Data {

  static public double[][] create(List<Face> faces, List<Edge> edges, List<Vertex> vertices) {
    int m = edges.size();
    double[][] data = new double[m][m];
    int index = 0;
    // edge orientation around each face
    for (Face face : faces) {
      List<Edge> elist = face.getEdges();
      for (Edge e : elist) {
        int col = edges.indexOf(e);
        int value = 1;
        data[index][col] = e.forward(face) ? value : -value;
      }
      index++;
    }

    // edge orientation around each vertex
    for (Vertex v : vertices) {
      List<Edge> es = v.getRotation();
      for (Edge e : es) {
        int col = edges.indexOf(e);
        int value = e.getStart().equals(v) ? 1 : -1;
        data[index][col] = value * e.getWeight();
      }
      index++;
    }
    System.out.println("data: " + Arrays.deepToString(data));
    return data;
  }
}
