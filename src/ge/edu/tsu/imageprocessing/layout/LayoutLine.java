package ge.edu.tsu.imageprocessing.layout;

import java.util.ArrayList;

public class LayoutLine {

	private ArrayList<LayoutWord> words;

	public LayoutLine() {
		super();
		words = new ArrayList<>();
	}

	public void addWord(LayoutWord word) {
		words.add(word);
	}

	public ArrayList<LayoutWord> getWords() {
		return words;
	}

}
