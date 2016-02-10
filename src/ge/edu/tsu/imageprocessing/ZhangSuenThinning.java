package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

// ##########################################################
// Zhang-Suen thinning algorithm
// https://rosettacode.org/wiki/Zhang-Suen_thinning_algorithm

public class ZhangSuenThinning extends AlgorithmDecorator {

	public ZhangSuenThinning(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat newImage) {

		Mat newnewImage = algorithm.execute(newImage);

		Mat secondImage = new Mat();
		newnewImage.copyTo(secondImage);
		ArrayList<Point> pointsToChange;

		do {
			newnewImage.copyTo(secondImage);

			// step 1
			pointsToChange = new ArrayList<Point>();
			for (int i = 1; i < newImage.rows() - 1; i++) {
				for (int j = 1; j < newImage.cols() - 1; j++) {
					// pixel is black and has eight neighbours
					if (newImage.get(i, j)[0] == 0) {

						int blacks = blackNeighbours(newImage, j, i);
						int trans = transitions(newImage, j, i);
						ArrayList<Point> neigh = getNeighbours(newImage, j, i);
						if (blacks >= 2 && blacks <= 6 && trans == 1) {
							if (isWhite(newImage, neigh.get(0), neigh.get(2), neigh.get(4))
									&& isWhite(newImage, neigh.get(2), neigh.get(4), neigh.get(6))) {
								pointsToChange.add(new Point(j, i));
							}
						}

					}
				}
			}

			for (Point p : pointsToChange) {
				newImage.put((int) p.y, (int) p.x, new double[] { 255 });
			}

			// step 2
			pointsToChange = new ArrayList<Point>();
			for (int i = 1; i < newImage.rows() - 1; i++) {
				for (int j = 1; j < newImage.cols() - 1; j++) {
					// pixel is black and has eight neighbours
					if (newImage.get(i, j)[0] == 0) {

						int blacks = blackNeighbours(newImage, j, i);
						int trans = transitions(newImage, j, i);
						ArrayList<Point> neigh = getNeighbours(newImage, j, i);
						if (blacks >= 2 && blacks <= 6 && trans == 1) {
							if (isWhite(newImage, neigh.get(0), neigh.get(2), neigh.get(6))
									&& isWhite(newImage, neigh.get(0), neigh.get(4), neigh.get(6))) {
								pointsToChange.add(new Point(j, i));
							}
						}

					}
				}
			}

			for (Point p : pointsToChange) {
				newImage.put((int) p.y, (int) p.x, new double[] { 255 });
			}

			boolean allEquals = true;
			for (int i = 0; i < newImage.rows(); i++) {
				for (int j = 0; j < newImage.cols(); j++) {
					if (newImage.get(i, j)[0] != secondImage.get(i, j)[0])
						allEquals = false;
				}
			}

			if (allEquals)
				break;

			newImage.copyTo(secondImage);
		} while (true);

		return newImage;
	}

	private int blackNeighbours(Mat img, int x, int y) {
		int res = 0;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i != x && j != y) {
					if (img.get(j, i)[0] == 0)
						res++;
				}
			}
		}
		return res;
	}

	private int transitions(Mat img, int x, int y) {
		int res = 0;
		ArrayList<Point> neighbours = getNeighbours(img, x, y);

		for (int i = 0; i < neighbours.size() - 1; i++) {
			if (img.get((int) neighbours.get(i).y, (int) neighbours.get(i).x)[0] == 255) {
				if (img.get((int) neighbours.get(i + 1).y, (int) neighbours.get(i + 1).x)[0] == 0) {
					res++;
				}
			}
		}
		return res;
	}

	private ArrayList<Point> getNeighbours(Mat img, int x, int y) {
		ArrayList<Point> res = new ArrayList<Point>();

		res.add(new Point(x, y - 1));
		res.add(new Point(x + 1, y - 1));
		res.add(new Point(x + 1, y));
		res.add(new Point(x + 1, y + 1));
		res.add(new Point(x, y + 1));
		res.add(new Point(x - 1, y + 1));
		res.add(new Point(x - 1, y));
		res.add(new Point(x - 1, y - 1));
		res.add(new Point(x, y - 1));

		return res;
	}

	private boolean isWhite(Mat img, Point p1, Point p2, Point p3) {
		return img.get((int) p1.y, (int) p1.x)[0] == 255 || img.get((int) p2.y, (int) p2.x)[0] == 255
				|| img.get((int) p3.y, (int) p3.x)[0] == 255;
	}

}
