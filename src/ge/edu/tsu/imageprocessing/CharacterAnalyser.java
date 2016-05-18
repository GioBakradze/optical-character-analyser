package ge.edu.tsu.imageprocessing;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;
import ge.edu.tsu.imageprocessing.detect.InvariantPositionsDetector;
import ge.edu.tsu.imageprocessing.detect.InvariantsDetector;
import ge.edu.tsu.imageprocessing.detect.params.DetectorParams;
import ge.edu.tsu.imageprocessing.detect.params.DetectorResult;
import ge.edu.tsu.imageprocessing.detect.params.InvariantsDetectorResult;
import ge.edu.tsu.imageprocessing.detect.params.InvariantsPositionDetectorParams;

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

	public static Mat analyse(Mat image, ArrayList<ArrayList<Point[]>> glyphs) {
		Mat localImage = new Mat();
		image.copyTo(localImage);

		int c = 'ა';

		String output = "";

		for (ArrayList<Point[]> word : glyphs) {
			for (int i = 0; i < word.size(); i++) {

				// Imgproc.rectangle(localImage, word.get(i)[0], word.get(i)[1],
				// new Scalar(150), 1);

				// apply detectors
				DetectorResult res;
				res = new InvariantsDetector().detect(new DetectorParams(localImage, word.get(i)[0], word.get(i)[1]));

				if (res.symbols.length != 0) {
					if (res.symbols.length == 1) {
						output += res.symbols[0];
					} else {
						DetectorResult res2 = new InvariantPositionsDetector()
								.detect(new InvariantsPositionDetectorParams(localImage, word.get(i)[0], word.get(i)[1],
										((InvariantsDetectorResult) res).invariantsPositions));

						if (res2.symbols.length == 1) {
							output += res2.symbols[0];
						} else {
							output += "(" + new String(res2.symbols.length == 0 ? res.symbols : res2.symbols) + ")";
						}
					}
					// System.out.println((char) c + " > " + new
					// String(res.symbols));
					// c++;
				}

			}
			output += " ";
		}

		try {
			long name = System.currentTimeMillis();
			Writer out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("./outputs/" + name + ".txt"), "UTF8"));
			out.append(output);
			out.flush();
			out.close();

			// System.out.println(output);
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return localImage;
	}
}
