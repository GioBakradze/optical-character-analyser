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
		
		Graph<Point> imageGraph = buildAreaGraph(image, new Point(0,0), new Point(image.cols() - 1, image.rows() - 1));
		
		
		
		return image;
	}

}
