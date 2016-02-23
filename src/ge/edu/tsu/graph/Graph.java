package ge.edu.tsu.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Graph<E> {

	private HashMap<E, HashSet<E>> graph;
	private HashSet<E> nodes;
	private HashSet<E> visited;
	private HashSet<E> subTree;
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

			@Override
			public void onNode(E element, E parent) {
			}

			@Override
			public void onSubtree(HashSet<E> subtree) {
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
				listener.onNode(e, null);
				subTree = new HashSet<E>();

				dfs(e, listener);

				listener.onSubtree(subTree);
			}
		}
	}

	private void dfs(E e, GraphListener<E> listener) {
		if (!visited.contains(e)) {
			visited.add(e);
			subTree.add(e);
			listener.onNode(e);

			for (E child : graph.get(e)) {

				if (!visited.contains(child)) {
					listener.onNode(child, e);
					dfs(child, listener);
				}

			}
		}
	}

	// TODO: we need to walk over all elements, it will now iterate only
	// reachable
	// nodes from first node
	public void walkBFS(GraphListener<E> listener) {
		if (nodes.size() == 0)
			return;

		Queue<E> queue = new LinkedList<E>();
		queue.add(nodes.iterator().next());
		visited = new HashSet<E>();
		E currentElement;

		while ((currentElement = queue.poll()) != null) {
			visited.add(currentElement);
			listener.onNode(currentElement);

			for (E child : get(currentElement)) {
				if (!visited.contains(child)) {
					queue.add(child);
					listener.onNode(child, currentElement);
				}
			}

		}

	}

	@Override
	public String toString() {
		return this.graph.toString();
	}

}
