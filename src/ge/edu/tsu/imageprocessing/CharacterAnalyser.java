package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import ge.edu.tsu.graph.GraphListener;
import ge.edu.tsu.imageprocessing.detect.InvariantPositionsDetector;
import ge.edu.tsu.imageprocessing.detect.InvariantsDetector;
import ge.edu.tsu.imageprocessing.detect.params.DetectorParams;
import ge.edu.tsu.imageprocessing.detect.params.DetectorResult;
import ge.edu.tsu.imageprocessing.detect.params.InvariantsDetectorResult;
import ge.edu.tsu.imageprocessing.detect.params.InvariantsPositionDetectorParams;
import ge.edu.tsu.imageprocessing.features.Invariants;
import ge.edu.tsu.imageprocessing.features.WhiteComponents;
import ge.edu.tsu.imageprocessing.features.base.SimpleBase;
import ge.edu.tsu.imageprocessing.features.result.InvariantsResult;
import ge.edu.tsu.imageprocessing.features.result.SimpleSet;
import ge.edu.tsu.imageprocessing.features.result.WhiteComponentsResult;

public class CharacterAnalyser {

	public Mat image;

	public CharacterAnalyser(Mat image) {
		this.image = image;
	}

	public static ArrayList<Rect> getBoundingRects(Mat image) {
		Mat localImage = new Mat();
		image.copyTo(localImage);

		ArrayList<Rect> boundingRect = new ArrayList<Rect>();

		Imgproc.cvtColor(localImage, localImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Sobel(localImage, localImage, CvType.CV_8U, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
		Imgproc.threshold(localImage, localImage, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		Imgproc.morphologyEx(localImage, localImage, Imgproc.MORPH_CLOSE,
				Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3)));

		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(localImage, contours, hierarchy, 0, 1);

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

		boundingRect.sort(new Comparator<Rect>() {

			@Override
			public int compare(Rect o1, Rect o2) {
				return o1.x - o2.x + o1.y - o2.y;
			}
		});

		return boundingRect;
	}

	public static ArrayList<ArrayList<Point[]>> isolateGlyphs(Mat image, ArrayList<Rect> boundingRect) {
		Mat localImage = new Mat();
		image.copyTo(localImage);

		ArrayList<ArrayList<Point[]>> res = new ArrayList<ArrayList<Point[]>>();

		for (int i = 0; i < boundingRect.size(); i++) {

			int x = boundingRect.get(i).x - 1;
			int y = boundingRect.get(i).y;
			int w = boundingRect.get(i).width;
			int h = boundingRect.get(i).height;

			double breakCosts[] = new double[w];
			ArrayList<Integer> zeroCols = new ArrayList<Integer>();
			zeroCols.add(x - 2);

			for (int j = 0; j < w; j++) {
				breakCosts[j] = 0;
				for (int k = y; k < y + h; k++) {
					if (localImage.get(k, x + j)[0] == 0 && localImage.get(k, x + j - 1)[0] == 0
							&& localImage.get(k, x + j + 1)[0] == 0) {
						breakCosts[j]++;
					}
				}
			}

			for (int j = 1; j < w - 1; j++) {
				// System.out.println(breakCosts[j - 1] + " " + breakCosts[j] +
				// " " + breakCosts[j + 1]);
				if (breakCosts[j] == 0 && breakCosts[j - 1] > breakCosts[j] && breakCosts[j + 1] > breakCosts[j]
						|| breakCosts[j] == 0 && breakCosts[j - 1] != 0
						|| breakCosts[j] == 0 && breakCosts[j + 1] != 0) {
					zeroCols.add(x + j);
				}
			}
			zeroCols.add(x + w);

			ArrayList<Point[]> pointsList = new ArrayList<Point[]>();
			for (int j = 0; j < zeroCols.size() - 1; j++) {
				pointsList.add(new Point[] { new Point(zeroCols.get(j), y), new Point(zeroCols.get(j + 1), y + h) });
			}
			res.add(pointsList);
		}
		return res;
	}

	public static Mat learn(Mat image, ArrayList<ArrayList<Point[]>> glyphs, String file) {

		Mat localImage = new Mat();
		image.copyTo(localImage);

		float average = 0;
		SimpleBase base = new SimpleBase();
		int learningCharCode = (int) 'ა';

		for (ArrayList<Point[]> word : glyphs) {
			average = 0;
			for (int i = 0; i < word.size(); i++) {
				average += (int) word.get(i)[1].x - (int) word.get(i)[0].x;
			}
			average /= word.size();

			for (int i = 0; i < word.size(); i++) {

				Mat character = localImage.submat((int) word.get(i)[0].y, (int) word.get(i)[1].y,
						(int) word.get(i)[0].x, (int) word.get(i)[1].x);

				if (character.cols() < (int) average)
					continue;

				// Imgproc.rectangle(localImage, word.get(i)[0], word.get(i)[1],
				// new Scalar(150), 1);

				InvariantsResult invs = (new Invariants()).extractFeature(character);
				WhiteComponentsResult whites = (new WhiteComponents()).extractFeature(character);
				SimpleSet set = new SimpleSet(invs, whites);

				System.out.println((char) learningCharCode);
				System.out.println(set);

				base.addTo((char) learningCharCode, set);
				learningCharCode++;
			}
		}

		try {
			base.saveTo(file);
		} catch (Exception e) {

		}
		return localImage;
	}

	public static Mat analyse(Mat image, ArrayList<ArrayList<Point[]>> glyphs, String file) {

		Mat localImage = new Mat();
		image.copyTo(localImage);

		float average = 0;
		SimpleBase base = new SimpleBase();

		try {
			base.restoreFrom(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (ArrayList<Point[]> word : glyphs) {
			average = 0;
			for (int i = 0; i < word.size(); i++) {
				average += (int) word.get(i)[1].x - (int) word.get(i)[0].x;
			}
			average /= word.size();

			for (int i = 0; i < word.size(); i++) {

				Mat character = localImage.submat((int) word.get(i)[0].y, (int) word.get(i)[1].y,
						(int) word.get(i)[0].x, (int) word.get(i)[1].x);

				if (character.cols() < (int) average)
					continue;

				// Imgproc.rectangle(localImage, word.get(i)[0], word.get(i)[1],
				// new Scalar(150), 1);

				InvariantsResult invs = (new Invariants()).extractFeature(character);
				WhiteComponentsResult whites = (new WhiteComponents()).extractFeature(character);

				SimpleSet set = new SimpleSet(invs, whites);
				System.out.print(base.getClosest(set));
				System.out.println("     " + base.getLastSmallestDistance());

			}
		}

		return localImage;
	}
}
