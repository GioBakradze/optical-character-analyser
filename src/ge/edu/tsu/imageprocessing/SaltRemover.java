package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;

public class SaltRemover extends AlgorithmDecorator {

	public SaltRemover(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		Mat newImage = algorithm.execute(image);
		int pointsToRemove;

		// first we need to thin bulky places
		// this cycle simply creates local graph for each black point
		// then checks if removal of central point creates two or more
		// components, if not then we can safely remove the point
		for (;;) {
			pointsToRemove = 0;

			for (int i = 1; i < newImage.rows() - 1; i++) {
				for (int j = 1; j < newImage.cols() - 1; j++) {

					Point currentPoint = new Point(j, i);

					if (pointIsBlack(newImage, j, i)) {
						Graph<Point> gr = buildAreaGraph(newImage, new Point(j - 1, i - 1), new Point(j + 1, i + 1));

						if (gr.all().size() == 2)
							continue;

						gr.removeNode(currentPoint);
						if (gr.isConnected()) {
							setColorAt(newImage, j, i, COLOR_WHITE);
							pointsToRemove++;
						}
					}
				}
			}

			if (pointsToRemove == 0)
				break;
		}

		// now we have to remove corner pixels
		while (true) {
			pointsToRemove = 0;

			for (int i = 1; i < newImage.rows() - 1; i++) {
				for (int j = 1; j < newImage.cols() - 1; j++) {

					try {
						if (matchesPattern(newImage, j, i, new double[][] { new double[] { 0, 255, 255 },
								new double[] { 255, 0, 255 }, new double[] { 255, 255, 255 } })) {

							pointsToRemove++;
							setColorAt(newImage, j, i, COLOR_WHITE);
						}

						if (matchesPattern(newImage, j, i, new double[][] { new double[] { 255, 255, 0 },
								new double[] { 255, 0, 255 }, new double[] { 255, 255, 255 } })) {

							pointsToRemove++;
							setColorAt(newImage, j, i, COLOR_WHITE);
						}

						if (matchesPattern(newImage, j, i, new double[][] { new double[] { 255, 255, 255 },
								new double[] { 255, 0, 255 }, new double[] { 255, 255, 0 } })) {

							pointsToRemove++;
							setColorAt(newImage, j, i, COLOR_WHITE);
						}

						if (matchesPattern(newImage, j, i, new double[][] { new double[] { 255, 255, 255 },
								new double[] { 255, 0, 255 }, new double[] { 0, 255, 255 } })) {

							pointsToRemove++;
							setColorAt(newImage, j, i, COLOR_WHITE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
			if (pointsToRemove == 0)
				break;
		}

		return newImage;
	}

}
