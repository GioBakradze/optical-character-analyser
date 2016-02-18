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
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;

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
		Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
		Imgproc.morphologyEx(localImage, localImage, Imgproc.MORPH_CLOSE, element);

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

		return boundingRect;
	}
	
	public static ArrayList< ArrayList<Point> > isolateGlyphs(Mat image, ArrayList<Rect> boundingRect) {
		Mat localImage = new Mat();
		image.copyTo(localImage);
		
		ArrayList< ArrayList<Point> > res = new ArrayList< ArrayList<Point> >();
		
		for (int i = 0; i < boundingRect.size(); i++) {

			int x = boundingRect.get(i).x - 1;
			int y = boundingRect.get(i).y;
			int w = boundingRect.get(i).width;
			int h = boundingRect.get(i).height;
			Scalar color = new Scalar(100);

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
				if (breakCosts[j] == 0 && breakCosts[j - 1] > breakCosts[j] && breakCosts[j + 1] > breakCosts[j]
						|| breakCosts[j] == 0 && breakCosts[j - 1] != 0
						|| breakCosts[j] == 0 && breakCosts[j + 1] != 0) {
					Imgproc.line(localImage, new Point(x + j, y), new Point(x + j, y + h), color);
					zeroCols.add(x + j);
				}
			}
			zeroCols.add(x + w);
			
			for (int j = 0; j < zeroCols.size() - 1; j++) {
				
			}
		}
		
		return res;
	}

	public static Mat analyse(Mat image, ArrayList< ArrayList<Integer> > glyphs) {
		Mat localImage = new Mat();
		image.copyTo(localImage);
		
		for (ArrayList<Integer> word : glyphs) {
			for (int i=0; i < word.size() - 1; i++) {
				
			}			
		}

		// draw bounding rects
//		for (int i = 0; i < boundingRect.size(); i++) {
//
//			// blocks
//			for (int j = 0; j < zeroCols.size() - 1; j++) {
//				
//				Imgproc.rectangle(localImage, new Point(zeroCols.get(i), y), new Point(zeroCols.get(i + 1), y + h), color, 1);
//				
////				Graph<Point> symbol = new Graph<Point>();
////				HashMap<Integer, Integer> invariants = new HashMap<Integer, Integer>();
////				HashMap<Point, ArrayList<Point>> blackRowsList = new HashMap<Point, ArrayList<Point>>();
////				LinkedHashSet<ArrayList<Point>> blackRowsOrderedList = new LinkedHashSet<ArrayList<Point>>();
////
////				// rows of block
////				for (int l = y; l <= y + h; l++) {
////					ArrayList<Point> blacksRow = new ArrayList<Point>();
////
////					// columns of block
////					for (int k = zeroCols.get(j); k < zeroCols.get(j + 1); k++) {
////						if (localImage.get(l, k)[0] == 255 || k == zeroCols.get(j + 1) - 1) {
////							if (blacksRow.size() != 0)
////								blackRowsOrderedList.add(blacksRow);
////							blacksRow = new ArrayList<Point>();
////						}
////
////						if (image.get(l, k)[0] == 0) {
////							blacksRow.add(new Point(k, l));
////							blackRowsList.put(new Point(k, l), blacksRow);
////						}
////					}
////				}
////
////				for (ArrayList<Point> row : blackRowsOrderedList) {
////					Point middle = row.get(row.size() / 2);
////
////					// build graph
////					for (Point p : row) {
////						if (!blackRowsList.containsKey(p))
////							break;
////						if (image.get((int) p.y - 1, (int) p.x)[0] == 0) {
////							Point tmp = new Point(p.x, p.y - 1);
////
////							if (blackRowsList.containsKey(tmp))
////								symbol.put(middle, blackRowsList.get(tmp).get(blackRowsList.get(tmp).size() / 2));
////						}
////					}
////				}
////				symbol.walk(new GraphListener<Point>() {
////
////					@Override
////					public void onNode(Point p) {
////						image.put((int) p.y, (int) p.x, new double[] { 100 });
////						int size = symbol.get(p).size();
////						if (size != 2) {
////							if (!invariants.containsKey(size))
////								invariants.put(size, 0);
////							invariants.put(size, invariants.get(size) + 1);
////							image.put((int) p.y, (int) p.x, new double[] { 100 });
////						}
////					}
////				});
////				System.out.println(invariants);
////				System.out.println("=========");
//
//			}
//		}
		
		return localImage;
	}
}
