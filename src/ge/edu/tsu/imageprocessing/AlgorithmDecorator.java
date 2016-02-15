package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;

public abstract class AlgorithmDecorator implements Algorithm {

	protected Algorithm algorithm;

	public AlgorithmDecorator(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public abstract Mat execute(Mat image);

	protected int colorAt(Mat image, int x, int y) {
		return (int) image.get(y, x)[0];
	}

	protected boolean matchesPattern(Mat image, int x, int y, double[][] pattern) throws Exception {

		if (pattern.length == 0)
			throw new Exception("Pattern shouldn't be empty");

		if (pattern.length != pattern[0].length)
			throw new Exception("Pattern should have equal width and height");

		int half = pattern.length / 2;

		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[i].length; j++) {
				if (pattern[i][j] != image.get(y - half + i, x - half + j)[0])
					return false;
			}
		}

		return true;
	}

	protected void applyPattern(Mat image, int x, int y, double[][] pattern) throws Exception {
		if (pattern.length == 0)
			throw new Exception("Pattern shouldn't be empty");

		if (pattern.length != pattern[0].length)
			throw new Exception("Pattern should have equal width and height");

		int half = pattern.length / 2;

		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[i].length; j++) {
				image.put(y - half + i, x - half + j, new double[] { pattern[i][j] });
			}
		}
	}

	protected boolean pointIsBlack(Mat image, int x, int y) {
		return colorAt(image, x, y) == 0;
	}

	protected Graph<Point> buildAreaGraph(Mat image, Point start, Point end) {
		Graph<Point> graph = new Graph<Point>();
		int startY = (int) start.y;
		int endY = (int) end.y;
		int startX = (int) start.x;
		int endX = (int) end.x;

		for (int i = startY; i <= endY; i++) {
			for (int j = startX; j <= endX; j++) {
				if (pointIsBlack(image, j, i)) {
					Point currentPoint = new Point(j, i);

					if (i == startY && j == startX) {
						// left top corner pixel

						if (pointIsBlack(image, j + 1, i))
							graph.put(currentPoint, new Point(j + 1, i));

						if (pointIsBlack(image, j, i + 1))
							graph.put(currentPoint, new Point(j, i + 1));

					} else if (i == startY && j == endX) {
						// right top corner pixel
						if (pointIsBlack(image, j - 1, i))
							graph.put(currentPoint, new Point(j - 1, i));

						if (pointIsBlack(image, j, i + 1))
							graph.put(currentPoint, new Point(j, i + 1));

					} else if (i == endY && j == startX) {
						// left bottom corner pixel

						if (pointIsBlack(image, j + 1, i))
							graph.put(currentPoint, new Point(j + 1, i));

						if (pointIsBlack(image, j, i - 1))
							graph.put(currentPoint, new Point(j, i - 1));

					} else if (i == endY && j == endX) {
						// right bottom corner pixel
						if (pointIsBlack(image, j - 1, i))
							graph.put(currentPoint, new Point(j - 1, i));

						if (pointIsBlack(image, j, i - 1))
							graph.put(currentPoint, new Point(j, i - 1));

					} else {
						for (Point p : getBlackNeighbours(image, j, i)) {
							graph.put(currentPoint, p);
						}
					}
				}
			}
		}

		return graph;
	}

	protected ArrayList<Point> getBlackNeighbours(Mat image, int x, int y) {
		ArrayList<Point> res = new ArrayList<Point>();

		if (colorAt(image, x - 1, y - 1) == 0)
			res.add(new Point(x - 1, y - 1));

		if (colorAt(image, x, y - 1) == 0)
			res.add(new Point(x, y - 1));

		if (colorAt(image, x + 1, y - 1) == 0)
			res.add(new Point(x + 1, y - 1));

		if (colorAt(image, x - 1, y) == 0)
			res.add(new Point(x - 1, y));

		if (colorAt(image, x + 1, y) == 0)
			res.add(new Point(x + 1, y));

		if (colorAt(image, x - 1, y + 1) == 0)
			res.add(new Point(x - 1, y + 1));

		if (colorAt(image, x, y + 1) == 0)
			res.add(new Point(x, y + 1));

		if (colorAt(image, x + 1, y + 1) == 0)
			res.add(new Point(x + 1, y + 1));

		return res;
	}

	protected int calcBlackNeighbours(Mat image, int x, int y) {
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
