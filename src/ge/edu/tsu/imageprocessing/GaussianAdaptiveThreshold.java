package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GaussianAdaptiveThreshold extends AlgorithmDecorator {

	public GaussianAdaptiveThreshold(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		Mat newImage = algorithm.execute(image);
		Imgproc.adaptiveThreshold(newImage, newImage, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,
				15, 4);
		return newImage;
	}

}
