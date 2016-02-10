package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;

public abstract class AlgorithmDecorator implements Algorithm {

	protected Algorithm algorithm;

	public AlgorithmDecorator(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public abstract Mat execute(Mat image);

	protected int colorAt(Mat image, int x, int y) {
		return (int) image.get(y, x)[0];
	}

}
