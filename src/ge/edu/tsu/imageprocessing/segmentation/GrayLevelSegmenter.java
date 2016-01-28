package ge.edu.tsu.imageprocessing.segmentation;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public abstract class GrayLevelSegmenter {
	public abstract Mat doSegmentation(Mat imageMatrix) throws Exception;
	
	public Mat toGrayScale(Mat image) {
		if (image.channels() == 1) return image;
		
		Mat grayScaleImage = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(image, grayScaleImage, Imgproc.COLOR_RGB2GRAY);
		
		return grayScaleImage;
	}
}
