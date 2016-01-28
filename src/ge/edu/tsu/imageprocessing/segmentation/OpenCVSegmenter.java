package ge.edu.tsu.imageprocessing.segmentation;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCVSegmenter extends GrayLevelSegmenter {

	@Override
	public Mat doSegmentation(Mat imageMatrix) throws Exception {

		if (imageMatrix.channels() > 1)
			throw new Exception("segmenter can only handle grayscale images");

		Mat res = new Mat(imageMatrix.rows(), imageMatrix.cols(), imageMatrix.type());

		Imgproc.adaptiveThreshold(imageMatrix, res, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15,
				4);

		return res;
	}

}
