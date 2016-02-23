package ge.edu.tsu.graph;

import java.util.HashSet;

public interface GraphListener<E> {
	public void onNode(E e);
	public void onNode(E element, E parent);
	public void onSubtree(HashSet<E> subtree);
}
