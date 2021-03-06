package ge.edu.tsu.imageprocessing.layout;

import java.util.ArrayList;

import org.opencv.core.Point;

public class LayoutWord {

	private Point topLeft;
	private Point bottomRight;
	private ArrayList<LayoutCharacter> characters;

	public LayoutWord() {
		super();
		characters = new ArrayList<>();
	}

	public void addCharacter(LayoutCharacter character) {
		characters.add(character);
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public Point getBottomRight() {
		return bottomRight;
	}

	public void setTopLeft(Point topLeft) {
		this.topLeft = topLeft;
	}

	public void setBottomRight(Point bottomRight) {
		this.bottomRight = bottomRight;
	}

	public ArrayList<LayoutCharacter> getCharacters() {
		return characters;
	}

}
