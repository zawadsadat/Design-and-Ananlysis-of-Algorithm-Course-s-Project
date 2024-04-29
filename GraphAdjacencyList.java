import java.io.File;
import java.util.*;

class GraphAdjacencyList {
	private int numVertices; 
	private LinkedList<Integer> graphAdjacencyList[];

	GraphAdjacencyList(int numVertices) {
		this.numVertices = numVertices;
		graphAdjacencyList = new LinkedList[numVertices + 1];
		for (int i = 1; i <= numVertices; i++)
			graphAdjacencyList[i] = new LinkedList<Integer>();
	}

	void addEdge(int source, int destination) {
		graphAdjacencyList[source].add(destination);
	}

	void DFS(int start, boolean visitedVertices[], LinkedList<Integer> sscList) {
		visitedVertices[start] = true;
		sscList.add(start);
		int nextNeighbour;
		Iterator<Integer> neighbours = graphAdjacencyList[start].iterator();
		while (neighbours.hasNext()) {
			nextNeighbour = neighbours.next();
			if (!visitedVertices[nextNeighbour])
				DFS(nextNeighbour, visitedVertices, sscList);
		}
	}

	GraphAdjacencyList Transpose() {
		GraphAdjacencyList graph = new GraphAdjacencyList(numVertices);
		for (int vertex = 1; vertex <= numVertices; vertex++) {
			Iterator<Integer> neighbours = graphAdjacencyList[vertex].listIterator();
			while (neighbours.hasNext())
				graph.addEdge(neighbours.next(), vertex);
		}
		return graph;
	}

	void firstDFS(int start, boolean visitedVertices[], Stack<Integer> stack) {
		visitedVertices[start] = true;
		int nextNeighbour;
		Iterator<Integer> neighbours = graphAdjacencyList[start].iterator();
		while (neighbours.hasNext()) {
			nextNeighbour = neighbours.next();
			if (!visitedVertices[nextNeighbour])
				firstDFS(nextNeighbour, visitedVertices, stack);
		}
		stack.push(start);
	}

	// Strongly connected component
	LinkedList<LinkedList<Integer>> SCC() {
		Stack<Integer> stack = new Stack<Integer>();
		LinkedList<LinkedList<Integer>> sscLists = new LinkedList<LinkedList<Integer>>();
		boolean visitedVertices[] = new boolean[numVertices + 1];
		for (int i = 1; i <= numVertices; i++)
			visitedVertices[i] = false;

		for (int i = 1; i <= numVertices; i++)
			if (visitedVertices[i] == false)
				firstDFS(i, visitedVertices, stack);

		GraphAdjacencyList transposeGraph = Transpose();

		for (int i = 1; i <= numVertices; i++)
			visitedVertices[i] = false;

		while (!stack.empty()) {
			int vertex = (int) stack.pop();
			LinkedList<Integer> sscList = new LinkedList<Integer>();
			if (!visitedVertices[vertex]) {
				transposeGraph.DFS(vertex, visitedVertices, sscList);
				sscLists.add(sscList);
			}
		}
		return sscLists;
	}

	public static void main(String args[]) {

		try {
			File file = new File("input.txt");
			Scanner sc = new Scanner(file);
			GraphAdjacencyList g = new GraphAdjacencyList(sc.nextInt());
			while (sc.hasNextInt()) {
				g.addEdge(sc.nextInt(), sc.nextInt());
			}

			LinkedList<LinkedList<Integer>> sscLists = g.SCC();
			Iterator<LinkedList<Integer>> sscListsIterator = sscLists.iterator();
			while (sscListsIterator.hasNext()) {
				LinkedList<Integer> sscList = sscListsIterator.next();
				Collections.sort(sscList);
				Iterator<Integer> sscListIterator = sscList.iterator();
				while (sscListIterator.hasNext()) {
					System.out.print(sscListIterator.next() + " ");
				}
				System.out.print("\n");
			}

			sc.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}