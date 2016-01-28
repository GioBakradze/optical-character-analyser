package ge.edu.tsu.imageprocessing.segmentation;

import org.opencv.core.Mat;

public class PTile extends GrayLevelSegmenter {

	@Override
	public Mat doSegmentation(Mat imageMatrix) throws Exception {
		
		if (imageMatrix.channels() > 1) throw new Exception("segmenter can only handle grayscale images");
		
		int total = imageMatrix.rows() * imageMatrix.cols();
		int accumulated = 0;
		double percentage = 8.8;
		double blackPixels = total * percentage / 100.0;
		int threshold = 0;
		
		// initialize histogram with zeroes
		int histogram[] = new int[260];
		for (int i=0; i < histogram.length; i++) histogram[i] = 0;
		
		// build histogram from existing image, using gray pixel values
		for (int i=0; i < imageMatrix.rows(); i++) {
			for (int j=0; j < imageMatrix.cols(); j++) {				
				histogram[ (int) imageMatrix.get(i, j)[0] ]++;
			}
		}
		
		for (int i=0; i < histogram.length; i++) {
			if (accumulated + histogram[i] > blackPixels) break;			
			accumulated += histogram[i];
			threshold = i;
		}
		
		// System.out.println(threshold);
		
		for (int i=0; i < imageMatrix.rows(); i++) {
			for (int j=0; j < imageMatrix.cols(); j++) {
				if (imageMatrix.get(i, j)[0] <= threshold) {
					imageMatrix.put(i, j, new double[] {0});
				} else {
					imageMatrix.put(i, j, new double[] {255});
				}
			}
		}		
		
		return imageMatrix;
	}

	
}
