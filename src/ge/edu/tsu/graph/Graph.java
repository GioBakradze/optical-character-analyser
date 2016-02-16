package ge.edu.tsu.graph;

import java.util.HashMap;
import java.util.HashSet;

public class Graph<E> {

	private HashMap<E, HashSet<E>> graph;
	private HashSet<E> nodes;
	private HashSet<E> visited;
	private int components;

	public Graph() {
		graph = new HashMap<E, HashSet<E>>();
		nodes = new HashSet<E>();
	}
	
	public void put(E e) {
		nodes.add(e);
		if (!graph.containsKey(e))
			graph.put(e, new HashSet<E>());		
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

	public void removeNode(E e) {
		for (E neighbour : graph.get(e)) {
			graph.get(neighbour).remove(e);
		}
		nodes.remove(e);
		graph.remove(e);
	}

	public HashSet<E> get(E e1) {
		return graph.get(e1);
	}

	public HashSet<E> all() {
		return nodes;
	}

	public boolean isConnected() {

		walk(new GraphListener<E>() {
			@Override
			public void onNode(E e) {
			}
		});

		return components == 1;
	}

	public void walk(GraphListener<E> listener) {
		visited = new HashSet<E>();
		components = 0;
		for (E e : nodes) {
			if (!visited.contains(e)) {
				components++;
				dfs(e, listener);
			}
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
