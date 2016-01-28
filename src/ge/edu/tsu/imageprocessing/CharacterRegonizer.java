package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

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

public class CharacterRegonizer {

	private GrayLevelSegmenter segmenter;
	private NoiseRemover unnoise;
	private Mat image;

	public CharacterRegonizer(GrayLevelSegmenter segmenter, NoiseRemover unnoise, String imagePath) {
		this.segmenter = segmenter;
		this.unnoise = unnoise;
		this.image = Imgcodecs.imread(imagePath);
	}

	public void recognize() throws Exception {
		// pre-processing

		// this.image = segmenter.toGrayScale(this.image);
		// Core.bitwise_not(this.image, this.image);
		// this.image = segmenter.doSegmentation(this.image);
		// this.image = unnoise.removeNoise(this.image);

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

		// Mat edges = new Mat();
		// Imgproc.Canny(this.image, edges, 10, 100);
		// this.image = edges;
		// Imgproc.medianBlur(this.image, this.image, 3);

		// draw bounding rects
		for (int i = 0; i < boundingRect.size(); i++) {

			int x = boundingRect.get(i).x - 1;
			int y = boundingRect.get(i).y;
			int w = boundingRect.get(i).width;
			int h = boundingRect.get(i).height;
			Scalar color = new Scalar(100);

			// Imgproc.line(this.image, new Point(x, y), new Point(x + w, y),
			// color);
			// Imgproc.line(this.image, new Point(x, y + h), new Point(x + w, y
			// + h), color);
			// Imgproc.line(this.image, new Point(x, y), new Point(x, y + h),
			// color);
			// Imgproc.line(this.image, new Point(x + w, y), new Point(x + w, y
			// + h), color);

			// Imgproc.rectangle(this.image, new Point(x, y), new Point(x + w, y
			// + h), color, 1);

			// isolate glyphs
			double breakCosts[] = new double[w];
			ArrayList<Integer> zeroCols = new ArrayList<Integer>();
			zeroCols.add(x - 2);

			for (int j = 0; j < w; j++) {
				breakCosts[j] = 0;
				for (int k = y; k < y + h; k++) {
					if (this.image.get(k, x + j)[0] == 0 && this.image.get(k, x + j - 1)[0] == 0
							&& this.image.get(k, x + j + 1)[0] == 0) {
						breakCosts[j]++;
					}
				}
			}

			for (int j = 1; j < w - 1; j++) {
				if (breakCosts[j] == 0 && breakCosts[j - 1] > breakCosts[j] && breakCosts[j + 1] > breakCosts[j]
						|| breakCosts[j] == 0 && breakCosts[j - 1] != 0
						|| breakCosts[j] == 0 && breakCosts[j + 1] != 0) {
					// Imgproc.line(this.image, new Point(x + j, y), new Point(x
					// + j, y + h), color);
					zeroCols.add(x + j);
				}
			}

			zeroCols.add(x + w);
			
			Mat tmpImage = new Mat(200, 1000, CvType.CV_8UC1);
			
			// blocks
			for (int j = 0; j < zeroCols.size() - 1; j++) {
				Graph<Point> symbol = new Graph<Point>();
				HashMap<Integer, Integer> invariants = new HashMap<Integer, Integer>();
				HashMap<Point, ArrayList<Point>> blackRowsList = new HashMap<Point, ArrayList<Point>>();
				LinkedHashSet<ArrayList<Point>> blackRowsOrderedList = new LinkedHashSet<ArrayList<Point>>();

				// rows of block
				for (int l = y; l <= y + h; l++) {
					ArrayList<Point> blacksRow = new ArrayList<Point>();

					// columns of block
					for (int k = zeroCols.get(j); k < zeroCols.get(j + 1); k++) {
						if (this.image.get(l, k)[0] == 255) {
							blacksRow = new ArrayList<Point>();
						}

						if (image.get(l, k)[0] == 0) {
							blacksRow.add(new Point(k, l));
							blackRowsList.put(new Point(k, l), blacksRow);
							blackRowsOrderedList.add(blacksRow);
						}
					}
				}

				for (ArrayList<Point> row : blackRowsOrderedList) {
					Point middle = row.get(row.size() / 2);
					// image.put((int) row.get(row.size()/2).y, (int)
					// row.get(row.size()/2).x, new double[] {100});

					// build graph
					for (Point p : row) {
						if (image.get((int) p.y - 1, (int) p.x)[0] == 0) {
							Point tmp = new Point(p.x, p.y - 1);
							symbol.put(middle, blackRowsList.get(tmp).get(blackRowsList.get(tmp).size() / 2));
						}
					}
				}

				symbol.walk(new GraphListener<Point>() {
					@Override
					public void onNode(Point p) {
						image.put((int) p.y, (int) p.x, new double[] { 100 });
						tmpImage.put((int) p.y, (int) p.x, new double[] { 100 });
						int size = symbol.get(p).size();
						if (size != 2) {
							if (!invariants.containsKey(size))
								invariants.put(size, 0);
							invariants.put(size, invariants.get(size) + 1);
							// image.put((int) p.y, (int) p.x, new double[] {
							// 100 });
						}
					}
				});

				System.out.println(invariants);

			}
			
//			this.image = tmpImage;
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