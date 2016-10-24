package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;

public class Colors {

	private static Colors instance;

	private Colors() {

	}

	public static Colors getInstance() {

		if (instance == null) {
			instance = new Colors();
		}

		return instance;
	}

	public static int colorAt(Mat image, int x, int y) {
		return (int) image.get(y, x)[0];
	}

	public boolean pointIsBlack(Mat image, int x, int y) {
		return colorAt(image, x, y) == 0;
	}

	public boolean pointIsWhite(Mat image, int x, int y) {
		return colorAt(image, x, y) == 255;
	}
}
