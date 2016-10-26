package ge.edu.tsu.imageprocessing.layout;

import org.opencv.core.Point;

public class LayoutCharacter {

	private Point topLeft;
	private Point bottomRight;

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

}
