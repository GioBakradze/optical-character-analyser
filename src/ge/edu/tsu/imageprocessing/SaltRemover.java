package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class SaltRemover extends AlgorithmDecorator {

	public SaltRemover(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		// after ZhangSuen thinning there are left some lonely pixels
		// which are attached to actual symbols, so we should remove them

		Mat newImage = algorithm.execute(image);
		ArrayList<Point> pointsToRemove = new ArrayList<Point>();

		for (int i = 1; i < newImage.rows() - 1; i++) {
			for (int j = 1; j < newImage.cols() - 1; j++) {
				if (calcBlackNeighbours(newImage, j, i) == 1) {
					pointsToRemove.add(new Point(j, i));
				}
			}
		}

		for (Point p : pointsToRemove) {
			newImage.put((int) p.y, (int) p.x, new double[] { 255 });
		}

		return newImage;
	}

	private int calcBlackNeighbours(Mat image, int x, int y) {
		int res = 0;

		if (colorAt(image, x - 1, y - 1) == 0)
			res++;

		if (colorAt(image, x, y - 1) == 0)
			res++;

		if (colorAt(image, x + 1, y - 1) == 0)
			res++;

		if (colorAt(image, x - 1, y) == 0)
			res++;

		if (colorAt(image, x + 1, y) == 0)
			res++;

		if (colorAt(image, x - 1, y + 1) == 0)
			res++;

		if (colorAt(image, x, y + 1) == 0)
			res++;

		if (colorAt(image, x + 1, y + 1) == 0)
			res++;

		return res;
	}

}
