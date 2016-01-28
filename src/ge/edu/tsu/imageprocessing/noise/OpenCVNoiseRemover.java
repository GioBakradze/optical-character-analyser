package ge.edu.tsu.imageprocessing.noise;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCVNoiseRemover implements NoiseRemover {

	@Override
	public Mat removeNoise(Mat imageMatrix) throws Exception {
		Mat clearedImage = new Mat(imageMatrix.rows(), imageMatrix.cols(), CvType.CV_8UC1);
		Imgproc.medianBlur(imageMatrix, clearedImage, 3);
		
		return clearedImage;
	}

}
