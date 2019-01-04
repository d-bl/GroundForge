package vmi.graph.data;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Graph implements Cloneable {

	List<Vertex> vertices;
	List<Edge> edges;
	List<Face> faces;
	List<Vector> vectors;

	/**
	 * Initializes a graph embedding by creating empty lists of edges, vertices and a rotation system
	 */
	public Graph(){
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public List<Edge> getEdges() {
		return edges;
	}
	
	public List<Face> getFaces() {
		return createFaceData();
	}
	
	public List<Vector> getTranslationVectors() {
		return vectors;
	}
	
	public void setTranslationVectors(Vector v1, Vector v2) {
		vectors = new ArrayList<Vector>();
		vectors.add(v1);
		vectors.add(v2);
	}
	
	private Vertex createVertex(float x, float y) {
		Vertex v = new Vertex(x, y);
		int index = vertices.indexOf(v);
		if (index != -1)
			return vertices.get(index);
		vertices.add(v);
		return v;
	}
	
	private Edge createEdge(Vertex start, Vertex end, double dx, double dy) {
		Edge	e = new Edge(start, end, dx, dy);
		edges.add(e);
		start.addEdge(e);
		end.addEdge(e);
		return e;
	}

	private List<Face> createFaceData() {

		if (faces != null) return faces;

		faces = new ArrayList<Face>();

		// keep track of how many times an edge is visited
		ArrayList<Edge> forward = new ArrayList<Edge>();
		ArrayList<Edge> reverse = new ArrayList<Edge>();

		Edge faceStart = null;

		do {

			if (faceStart != null) {

				Edge e = faceStart;
				Vertex prev = faceStart.start;
				if (forward.contains(faceStart)) {
					prev = faceStart.end;
				}

				Face face = new Face();
				LinkedList<Edge> edges = new LinkedList<Edge>();
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
					if (e.equals(faceStart)) {
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

	public static Graph readFromFile(File file) {
		Scanner input;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException ex) {
			System.out.println("Error reading file "+file.getAbsolutePath());
			return null;
		}
		
		try {
			return getGraph(input);

		} finally {
			input.close();
		}
	}

	public static Graph readFromString(String content) {
		Scanner input = new Scanner(new ByteArrayInputStream(content.getBytes()));

		try {
			return getGraph(input);

		} finally {
			input.close();
		}
	}

	@Nullable private static Graph getGraph(Scanner input) {
		// First line of file gives row count and column count
		int rowCount = 0;
		int colCount = 0;
		if (input.hasNextLine()) {
			String line = input.nextLine();
			String[] values = line.split("\\s+");
			// Only look at the last two values, may be some style information at front
			rowCount = Integer.parseInt(values[values.length-2]);
			colCount = Integer.parseInt(values[values.length-1]);
		}

		if (rowCount < 1 || colCount < 1) return null;

		Graph g = new Graph();

		while (input.hasNextLine()) {
			String line = input.nextLine()+"\n";
			String[] blocks = line.split("\\s+");
			for (int i = 0; i < blocks.length; i++) {
				// remove [] brackets
				String block = blocks[i].substring(1, blocks[i].length()-1);
				String[] values = block.split(",");

				// source
				int col = Integer.parseInt(values[0]);
				int row = Integer.parseInt(values[1]);
				Vertex source = g.createVertex(mod(col, colCount), mod(row, rowCount));

				// dest 1
				int out1col = Integer.parseInt(values[2]);
				int out1row = Integer.parseInt(values[3]);
				Vertex dest1 = g.createVertex(mod(out1col, colCount), mod(out1row, rowCount));
				g.createEdge(source, dest1, out1col-col, out1row-row);

				// dest 2
				int out2col = Integer.parseInt(values[4]);
				int out2row = Integer.parseInt(values[5]);
				Vertex dest2 = g.createVertex(mod(out2col, colCount), mod(out2row, rowCount));
				g.createEdge(source, dest2, out2col-col, out2row-row);
			}

		}

		g.createFaceData();

		return g;
	}

	/**
	 * Handle modulo of negative numbers in a different way from Java
	 */
	static int mod(int a, int b) {
		int result = a % b;
		if (result < 0) result += b;
		return result;
	}
}
