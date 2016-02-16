package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;

public class SaltRemover extends AlgorithmDecorator {

	public SaltRemover(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		// after ZhangSuen thinning there are left some lonely pixels
		// which are attached to actual symbols, so we should remove them

		Mat newImage = algorithm.execute(image);
		ArrayList<Point> pointsToRemove;

		while (true) {
			pointsToRemove = new ArrayList<Point>();

			for (int i = 1; i < newImage.rows() - 1; i++) {
				for (int j = 1; j < newImage.cols() - 1; j++) {

					if (pointIsBlack(newImage, j, i)) {
						Point currentPoint = new Point(j, i); 
						Graph<Point> gr = buildAreaGraph(newImage, new Point(j - 1, i - 1), new Point(j + 1, i + 1));
						gr.removeNode(currentPoint);
						if (gr.isConnected()) {
							newImage.put(i, j, new double[] { 255 });
//							pointsToRemove.add(currentPoint);
						}
					}
				}
			}

			if (pointsToRemove.size() == 0)
				break;

			for (Point p : pointsToRemove) {
				newImage.put((int) p.y, (int) p.x, new double[] { 255 });
			}
			break;
		}

		return newImage;
	}

}
