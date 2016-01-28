package ge.edu.tsu.graph;

import java.util.HashMap;
import java.util.HashSet;

public class Graph<E> {

	private HashMap<E, HashSet<E>> graph;
	private HashSet<E> nodes;
	private HashSet<E> visited;

	public Graph() {
		graph = new HashMap<E, HashSet<E>>();
		nodes = new HashSet<E>();
	}

	public void put(E e1, E e2) {

		if (!graph.containsKey(e1))
			graph.put(e1, new HashSet<E>());

		if (!graph.containsKey(e2))
			graph.put(e2, new HashSet<E>());

		graph.get(e1).add(e2);
		graph.get(e2).add(e1);
		
		nodes.add(e1);
		nodes.add(e2);
	}
	
	public HashSet<E> get(E e1) {
		return graph.get(e1);
	}
	
	public HashSet<E> all() {
		return nodes;
	}
	
	public void walk(GraphListener<E> listener) {
		visited = new HashSet<E>();
		for (E e : nodes) {
			if (!visited.contains(e)) dfs(e, listener);
		}
	}
	
	private void dfs(E e, GraphListener<E> listener) {
		if (!visited.contains(e)) {
			visited.add(e);
			listener.onNode(e);
			
			for (E child : graph.get(e)) {
				dfs(child, listener);
			}
		}		
	}

	@Override
	public String toString() {
		return this.graph.toString();
	}

}
