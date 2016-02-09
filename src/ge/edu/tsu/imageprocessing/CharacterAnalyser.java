package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;
import ge.edu.tsu.imageprocessing.noise.NoiseRemover;
import ge.edu.tsu.imageprocessing.segmentation.GrayLevelSegmenter;

// TODO: კითხვები
// სხვადასხვა ფონტში ბ-ს აქვს სხვადასხვა ფორმა, როგორია b-ს ტოპოლოგიური ინვარიანტები
// როგორია ვ-ს ტოპოლოგიური ინვარიანტები? საიდან უნდა დავთვალოთ ან წინასწარ როგორ განვსაზღვროთ

public class CharacterAnalyser {

	private GrayLevelSegmenter segmenter;
	private NoiseRemover unnoise;
	private Mat image;

	public CharacterAnalyser(GrayLevelSegmenter segmenter, NoiseRemover unnoise, String imagePath) {
		this.segmenter = segmenter;
		this.unnoise = unnoise;
		this.image = Imgcodecs.imread(imagePath);
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

	public void recognize() throws Exception {
		ArrayList<Rect> boundingRect = new ArrayList<Rect>();
		Mat imgGray = new Mat(), imgSobel = new Mat(), imgThreshold = new Mat(), element = new Mat();

		Imgproc.cvtColor(this.image, imgGray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Sobel(imgGray, imgSobel, CvType.CV_8U, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
		Imgproc.threshold(imgSobel, imgThreshold, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
		Imgproc.morphologyEx(imgThreshold, imgThreshold, Imgproc.MORPH_CLOSE, element);

		// find bounding rects
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(imgThreshold, contours, hierarchy, 0, 1);

		ArrayList<MatOfPoint2f> contoursPoly = new ArrayList<MatOfPoint2f>();
		for (int i = 0; i < contours.size(); i++) {
			contoursPoly.add(new MatOfPoint2f());
			if (contours.get(i).rows() > 20) {
				// if (contours.get(i).rows() > 100) {
				Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly.get(i), 3.0, true);
				Rect r = Imgproc.boundingRect(new MatOfPoint(contoursPoly.get(i).toArray()));

				// if (r.width > r.height) boundingRect.add(r);
				boundingRect.add(r);
			}
		}

		Imgproc.cvtColor(this.image, this.image, Imgproc.COLOR_BGR2GRAY);
		// Imgproc.threshold(this.image, this.image, 0, 255, Imgproc.THRESH_OTSU
		// + Imgproc.THRESH_BINARY);
		// Imgproc.morphologyEx(this.image, this.image, Imgproc.MORPH_OPEN,
		// Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));

		// Imgproc.blur(this.image, this.image, new Size(3.0, 3.0));
		Imgproc.GaussianBlur(this.image, this.image, new Size(3.0, 3.0), 0);
		Imgproc.adaptiveThreshold(this.image, this.image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
				Imgproc.THRESH_BINARY, 15, 4);

		Mat secondImage = new Mat();
		image.copyTo(secondImage);

		// Core.bitwise_not(secondImage, secondImage);

		// ##########################################################
		// Zhang-Suen thinning algorithm
		// https://rosettacode.org/wiki/Zhang-Suen_thinning_algorithm
		ArrayList<Point> pointsToChange;

		for (int l = 1; l <= 10; l++) {
			// step 1
			pointsToChange = new ArrayList<Point>();
			for (int i = 1; i < image.rows() - 1; i++) {
				for (int j = 1; j < image.cols() - 1; j++) {
					// pixel is black and has eight neighbours
					if (image.get(i, j)[0] == 0) {

						int blacks = blackNeighbours(image, j, i);
						int trans = transitions(image, j, i);
						ArrayList<Point> neigh = getNeighbours(image, j, i);
						if (blacks >= 2 && blacks <= 6 && trans == 1) {
							if (isWhite(image, neigh.get(0), neigh.get(2), neigh.get(4))
									&& isWhite(image, neigh.get(2), neigh.get(4), neigh.get(6))) {
								pointsToChange.add(new Point(j, i));
							}
						}

					}
				}
			}

			for (Point p : pointsToChange) {
				image.put((int) p.y, (int) p.x, new double[] { 255 });
			}

			// step 2
			pointsToChange = new ArrayList<Point>();
			for (int i = 1; i < image.rows() - 1; i++) {
				for (int j = 1; j < image.cols() - 1; j++) {
					// pixel is black and has eight neighbours
					if (image.get(i, j)[0] == 0) {

						int blacks = blackNeighbours(image, j, i);
						int trans = transitions(image, j, i);
						ArrayList<Point> neigh = getNeighbours(image, j, i);
						if (blacks >= 2 && blacks <= 6 && trans == 1) {
							if (isWhite(image, neigh.get(0), neigh.get(2), neigh.get(6))
									&& isWhite(image, neigh.get(0), neigh.get(4), neigh.get(6))) {
								pointsToChange.add(new Point(j, i));
							}
						}

					}
				}
			}

			for (Point p : pointsToChange) {
				image.put((int) p.y, (int) p.x, new double[] { 255 });
			}
		}

		// Mat edges = new Mat();
		// Imgproc.Canny(this.image, edges, 10, 100);
		// this.image = edges;
		// Imgproc.medianBlur(this.image, this.image, 3);

		// draw bounding rects
		/*
		 * for (int i = 0; i < boundingRect.size(); i++) {
		 * 
		 * int x = boundingRect.get(i).x - 1; int y = boundingRect.get(i).y; int
		 * w = boundingRect.get(i).width; int h = boundingRect.get(i).height;
		 * Scalar color = new Scalar(100);
		 * 
		 * // Imgproc.line(this.image, new Point(x, y), new Point(x + w, y), //
		 * color); // Imgproc.line(this.image, new Point(x, y + h), new Point(x
		 * + w, y // + h), color); // Imgproc.line(this.image, new Point(x, y),
		 * new Point(x, y + h), // color); // Imgproc.line(this.image, new
		 * Point(x + w, y), new Point(x + w, y // + h), color);
		 * 
		 * // Imgproc.rectangle(this.image, new Point(x, y), new Point(x + w, y
		 * // + h), color, 1);
		 * 
		 * // isolate glyphs double breakCosts[] = new double[w];
		 * ArrayList<Integer> zeroCols = new ArrayList<Integer>();
		 * zeroCols.add(x - 2);
		 * 
		 * for (int j = 0; j < w; j++) { breakCosts[j] = 0; for (int k = y; k <
		 * y + h; k++) { if (this.image.get(k, x + j)[0] == 0 &&
		 * this.image.get(k, x + j - 1)[0] == 0 && this.image.get(k, x + j +
		 * 1)[0] == 0) { breakCosts[j]++; } } }
		 * 
		 * for (int j = 1; j < w - 1; j++) { if (breakCosts[j] == 0 &&
		 * breakCosts[j - 1] > breakCosts[j] && breakCosts[j + 1] >
		 * breakCosts[j] || breakCosts[j] == 0 && breakCosts[j - 1] != 0 ||
		 * breakCosts[j] == 0 && breakCosts[j + 1] != 0) { //
		 * Imgproc.line(this.image, new Point(x + j, y), new Point(x // + j, y +
		 * h), color); zeroCols.add(x + j); } }
		 * 
		 * zeroCols.add(x + w);
		 * 
		 * // blocks for (int j = 0; j < zeroCols.size() - 1; j++) {
		 * Graph<Point> symbol = new Graph<Point>(); HashMap<Integer, Integer>
		 * invariants = new HashMap<Integer, Integer>(); HashMap<Point,
		 * ArrayList<Point>> blackRowsList = new HashMap<Point,
		 * ArrayList<Point>>(); LinkedHashSet<ArrayList<Point>>
		 * blackRowsOrderedList = new LinkedHashSet<ArrayList<Point>>();
		 * 
		 * // rows of block for (int l = y; l <= y + h; l++) { ArrayList<Point>
		 * blacksRow = new ArrayList<Point>();
		 * 
		 * // columns of block for (int k = zeroCols.get(j); k < zeroCols.get(j
		 * + 1); k++) { if (this.image.get(l, k)[0] == 255 || k ==
		 * zeroCols.get(j + 1) - 1) { if (blacksRow.size() != 0)
		 * blackRowsOrderedList.add(blacksRow); blacksRow = new
		 * ArrayList<Point>(); }
		 * 
		 * if (image.get(l, k)[0] == 0) { blacksRow.add(new Point(k, l));
		 * blackRowsList.put(new Point(k, l), blacksRow); } } }
		 * 
		 * removeExtraRows(blackRowsOrderedList, blackRowsList, true);
		 * removeExtraRows(blackRowsOrderedList, blackRowsList, false);
		 * 
		 * for (ArrayList<Point> row : blackRowsOrderedList) { Point middle =
		 * row.get(row.size() / 2);
		 * 
		 * // build graph for (Point p : row) { if
		 * (!blackRowsList.containsKey(p)) break; if (image.get((int) p.y - 1,
		 * (int) p.x)[0] == 0) { Point tmp = new Point(p.x, p.y - 1);
		 * 
		 * if (blackRowsList.containsKey(tmp)) symbol.put(middle,
		 * blackRowsList.get(tmp).get(blackRowsList.get(tmp).size() / 2)); } } }
		 * 
		 * symbol.walk(new GraphListener<Point>() {
		 * 
		 * @Override public void onNode(Point p) { image.put((int) p.y, (int)
		 * p.x, new double[] { 100 }); int size = symbol.get(p).size(); if (size
		 * != 2) { if (!invariants.containsKey(size)) invariants.put(size, 0);
		 * invariants.put(size, invariants.get(size) + 1); // image.put((int)
		 * p.y, (int) p.x, new double[] { // 100 }); } } });
		 * 
		 * System.out.println(invariants); System.out.println("=========");
		 * 
		 * } }
		 */
	}

	private void removeExtraRows(LinkedHashSet<ArrayList<Point>> blackRowsOrderedList,
			HashMap<Point, ArrayList<Point>> blackRowsList, boolean upper) {

		if (blackRowsOrderedList.size() == 0 || blackRowsList.size() == 0)
			return;

		Iterator<ArrayList<Point>> it = blackRowsOrderedList.iterator();
		int nextY = 0;
		if (upper) {
			nextY = 1;
		} else {
			nextY = -1;
			it = new LinkedList<ArrayList<Point>>(blackRowsOrderedList).descendingIterator();
		}

		boolean breakCycle = false;
		for (; it.hasNext();) {
			ArrayList<Point> row = it.next();
			for (Point p : row) {
				if (image.get((int) p.y + nextY, (int) p.x)[0] == 0) {
					Point tmp = new Point(p.x, p.y + nextY);

					if (!blackRowsList.containsKey(tmp))
						continue;

					if (blackRowsList.get(tmp).size() >= row.size()) {
						for (Point p2 : row) {
							blackRowsList.remove(p2);
						}
						it.remove();
						break;
					} else {
						breakCycle = true;
						break;
					}
				}
			}
			if (breakCycle)
				break;
		}
	}

	public Mat getImage() {
		return this.image;
	}

	private double computeSkew() {
		Mat lines = new Mat();
		double skewAngle = 0.0;
		Imgproc.HoughLinesP(this.image, lines, 1, Math.PI / 180.0, 100, this.image.cols() / 2.0, 20);

		for (int i = 0; i < lines.rows() - 1; i++) {

			// Imgproc.line(this.image, new Point(lines.get(i, 0)[2],
			// lines.get(i, 0)[3]),
			// new Point(lines.get(i, 0)[0], lines.get(i, 0)[1]), new
			// Scalar(230));

			skewAngle += Math.atan2(lines.get(i, 0)[3] - lines.get(i, 0)[1], lines.get(i, 0)[2] - lines.get(i, 0)[0]);
		}

		skewAngle /= lines.rows();

		return skewAngle * 180 / Math.PI;
		// return skewAngle;
	}

	private void deskew(double skewAngle) {

		System.out.println(skewAngle);

		ArrayList<Point> list = new ArrayList<Point>();

		for (int i = 0; i < this.image.rows(); i++) {
			for (int j = 0; j < this.image.cols(); j++) {
				if (this.image.get(i, j)[0] == 0.0) {
					list.add(new Point(j, i));
				}
			}
		}

		MatOfPoint2f points = new MatOfPoint2f();
		points.fromList(list);

		RotatedRect box = Imgproc.minAreaRect(points);
		Mat rotMat = Imgproc.getRotationMatrix2D(box.center, skewAngle, 1);

		// rotate
		Mat rotated = new Mat();
		Imgproc.warpAffine(this.image, rotated, rotMat, this.image.size(), Imgproc.INTER_CUBIC);
		this.image = rotated;

		// Crop image
		// Mat cropped = new Mat();
		// Size box_size = box.size;
		// if (box.angle < -45.) {
		// double aux = box_size.width;
		// box_size.width = box_size.height;
		// box_size.height = aux;
		// }
		//
		// Imgproc.getRectSubPix(rotated, box_size, box.center, cropped);
		// this.image = cropped;
	}

}
