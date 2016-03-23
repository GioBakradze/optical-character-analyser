package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;

public class SimpleSaltRemover extends AlgorithmDecorator {

	public SimpleSaltRemover(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		Mat newImage = algorithm.execute(image);
		
		for (int i = 1; i < newImage.rows() - 1; i++) {
			for (int j = 1; j < newImage.cols() - 1; j++) {

				try {
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   255 },
							new double[] { 0,    0,   255 }, 
							new double[] { 255,  255, 255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   255 },
							new double[] { 255,  0,   0 }, 
							new double[] { 255,  255, 255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 255,  0,   0 }, 
							new double[] { 255,  0,   255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 0,    0,   255 }, 
							new double[] { 255,  0,   255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 255,    0,   255 }, 
							new double[] { 0,    255,   255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 0 },
							new double[] { 255,    0,   255 }, 
							new double[] { 255,    255,   255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 0,  255, 255 },
							new double[] { 255,    0,   255 }, 
							new double[] { 255,    255,   255 } })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
					
					if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 255,    0,   255 }, 
							new double[] { 255,    255,   0} })) {
						setColorAt(newImage, j, i, COLOR_WHITE);
					}
				} catch (Exception e) {
					
				}
			}
		}
		
		return newImage;
	}

}
