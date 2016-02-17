package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MedianBlur extends AlgorithmDecorator {

	public MedianBlur(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		Mat newImage = algorithm.execute(image);

		Imgproc.medianBlur(newImage, newImage, 3);

		return newImage;
	}

}
