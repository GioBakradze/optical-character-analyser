package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class OtsuThreshold extends AlgorithmDecorator {

	private Mat image;

	public OtsuThreshold(Algorithm algorithm) {
		super(algorithm);
	}

	public OtsuThreshold(Mat image) {
		super(image);
	}

	@Override
	public Mat execute(Mat image) {
		Mat newImage;

		if (algorithm == null)
			newImage = this.image;
		else
			newImage = algorithm.execute(image);

		Imgproc.threshold(newImage, newImage, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY);
		Imgproc.morphologyEx(newImage, newImage, Imgproc.MORPH_CLOSE,
				Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3)));

		return newImage;
	}

}
