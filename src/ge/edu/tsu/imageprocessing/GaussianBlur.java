package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class GaussianBlur extends AlgorithmDecorator {

	public GaussianBlur(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		Mat newImage = algorithm.execute(image);
		Imgproc.GaussianBlur(newImage, newImage, new Size(3.0, 3.0), 0);
		return newImage;
	}

}
