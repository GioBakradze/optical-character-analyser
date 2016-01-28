package ge.edu.tsu.imageprocessing.segmentation;

import org.opencv.core.Mat;

public class MovingAverages extends GrayLevelSegmenter {

	@Override
	public Mat doSegmentation(Mat imageMatrix) throws Exception {
		
		if (imageMatrix.channels() > 1) throw new Exception("segmenter can only handle grayscale images");
		
		
		
		return null;
	}

}
