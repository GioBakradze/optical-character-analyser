package ge.edu.tsu.imageprocessing.layout;

import org.opencv.core.Point;

public class LayoutWord {

	private Point topLeft;
	private Point bottomRight;

	public LayoutWord(Point topLeft, Point bottomRight) {
		super();
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}

	public Point getTopLeft() {
		return topLeft;
	}

	public Point getBottomRight() {
		return bottomRight;
	}

}
