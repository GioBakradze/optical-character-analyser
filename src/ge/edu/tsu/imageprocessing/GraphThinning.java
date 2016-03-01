package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;

/**
 * 
 * Implementation of Suzuki's graph based thinning
 * for preserving topological properties of symbols
 *
 */
public class GraphThinning extends AlgorithmDecorator {

	public GraphThinning(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat sourceImage) {
		Mat newImage = algorithm.execute(sourceImage);
		Mat image = new Mat();
		newImage.copyTo(image);
		
//		Graph<Point> imageGraph = buildAreaGraph(image, new Point(0,0), new Point(image.cols() - 1, image.rows() - 1));
		
		for (int i=2; i < image.rows() - 2; i++) {
			for (int j=2; j < image.cols() - 2; j++) {
				try {
					if (matchesPattern(image, j, i, new double[][] {
							new double[] {-1,   0,  -1,  -1},
							new double[] {-1,   0,  255,  -1},
							new double[] {-1,   0,   0,   0},
							new double[] {0,   -1,  -1,  -1}
					})) {
						setColorAt(image, j, i, COLOR_GRAY);
					}
				} catch(Exception e) {}
			}
		}
		
		
		return image;
	}

}
