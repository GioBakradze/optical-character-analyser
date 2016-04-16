package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;

public class SimpleNoiseRemover extends AlgorithmDecorator {

	public SimpleNoiseRemover(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat sourceImage) {
		Mat newImage = algorithm.execute(sourceImage);
		Mat image = new Mat();
		newImage.copyTo(image);
		
		for (int i=0; i < image.rows(); i++) {
			for (int j=0; j < image.cols(); j++) {
				if (colorAt(image, j, i) > 50) {
					setColorAt(image, j, i, COLOR_WHITE);
				} else {
					setColorAt(image, j, i, COLOR_BLACK);
				}
			}
		}
		
		return image;
	}

}
