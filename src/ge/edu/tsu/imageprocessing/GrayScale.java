package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayScale implements Algorithm {

	@Override
	public Mat execute(Mat image) {
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		return image;
	}

}
