package dibl.fte.layout;

import dibl.fte.data.Edge;
import dibl.fte.data.JFace;
import dibl.fte.data.Vertex;

import java.util.List;

public class JData {

  static public double[][] create(List<JFace> faces, List<Vertex> vertices, List<Edge> edges) {
    int m = edges.size();
    double[][] data = new double[m][m];
    int index = 0;
    // edge orientation around each face
    for (JFace face : faces) {
      List<Edge> elist = face.getEdges();
      for (Edge e : elist) {
        int col = edges.indexOf(e);
        data[index][col] = e.forward(face) ? 1 : -1;
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
    return data;
  }
}
