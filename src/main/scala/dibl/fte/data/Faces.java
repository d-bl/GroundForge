package dibl.fte.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Faces {
  public static List<Face> create(List<Vertex> vertices) {
    List<Face> faces;

    faces = new ArrayList<>();

    // keep track of how many times an edge is visited
    ArrayList<Edge> forward = new ArrayList<>();
    ArrayList<Edge> reverse = new ArrayList<>();

    Edge faceStart = null;

    do {

      if (faceStart != null) {

        Edge e = faceStart;
        Vertex prev = faceStart.start;
        if (forward.contains(faceStart)) {
          prev = faceStart.end;
        }

        Face face = new Face();
        List<Edge> edges = new LinkedList<>();
        while (e != null) {
          edges.add(e);
          if (e.start.equals(prev)) {
            forward.add(e);
            e.forFace = face;
          }
          // Note: for some cases such as 1x1_1, an edge may be traversed both forward and backward
          if (e.end.equals(prev)) {
            reverse.add(e);
            e.revFace = face;
          }

          prev = e.start.equals(prev) ? e.end : e.start;
          e = prev.getNextEdge(e);
          if (e!=null && e.equals(faceStart)) {
            e = null;
          }
        }
        face.setEdges(edges);
        faces.add(face);

      }

      // pick an edge to start
      faceStart = null;
      for (Vertex v : vertices) {
        for (Edge r : v.rotation) {
          if (forward.contains(r) && reverse.contains(r))
            continue;
          faceStart = r;
          break;
        }
      }

    } while (faceStart != null);

    return faces;
  }

}
