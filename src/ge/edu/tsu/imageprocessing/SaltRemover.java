package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class SaltRemover extends AlgorithmDecorator {

	public SaltRemover(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat image) {
		// after ZhangSuen thinning there are left some lonely pixels
		// which are attached to actual symbols, so we should remove them

		Mat newImage = algorithm.execute(image);
		ArrayList<Point> pointsToRemove = new ArrayList<Point>();

//		for (int i = 1; i < newImage.rows() - 1; i++) {
//			for (int j = 1; j < newImage.cols() - 1; j++) {
//				if (calcBlackNeighbours(newImage, j, i) == 1) {
//					pointsToRemove.add(new Point(j, i));
//				}
//			}
//		}
//
//		for (Point p : pointsToRemove) {
//			newImage.put((int) p.y, (int) p.x, new double[] { 255 });
//		}
		
		while (true) {
			pointsToRemove = new ArrayList<Point>();
			for (int i = 1; i < newImage.rows() - 1; i++) {
				for (int j = 1; j < newImage.cols() - 1; j++) {
					// remove attached points
					try {
						
						// ###########################
						// attached to corners
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 0,   255, 255 },
							new double[] { 255, 0,   255 }, 
							new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 0 },
							new double[] { 255, 0,   255 }, 
							new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 255, 0,   255 }, 
							new double[] { 255, 255, 0 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 255, 0,   255 }, 
							new double[] { 0,   255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						// ###########################
						// attached to sides
						// attached to left
						if (matchesPattern(newImage, j, i, new double[][] { 
								new double[] { 0,   255, 255 },
								new double[] { 0,   0,   255 }, 
								new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 0,   0,   255 }, 
							new double[] { 0,   255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}						
						
						// attached to top
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,   0,   0 },
							new double[] { 255,   0,   255 }, 
							new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 0,     0, 255 },
							new double[] { 255,   0, 255 }, 
							new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}						
						
						// attached to right
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 0 },
							new double[] { 255, 0,   0 }, 
							new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 255, 0,   0 }, 
							new double[] { 255, 255, 0 } })) {
							pointsToRemove.add(new Point(j, i));
						}						
						
						// attached to bottom
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 255, 0,   255 }, 
							new double[] { 0,   0,   255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 255, 0,   255 }, 
							new double[] { 255, 0,   0 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						// ###########################
						// tricky corners
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255, 255 },
							new double[] { 0,   0,   255 }, 
							new double[] { 255, 0,   0 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 0,   0 },
							new double[] { 0,   0,   255 }, 
							new double[] { 255, 255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 0,   255 },
							new double[] { 0,   0,   255 }, 
							new double[] { 0,   255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   255 },
							new double[] { 255,  0,   0 }, 
							new double[] { 255,  255, 0 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   0 },
							new double[] { 0,    0,   255 }, 
							new double[] { 0,    255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if (pointsToRemove.size() == 0) break;
			
			for (Point p : pointsToRemove) {
				newImage.put((int) p.y, (int) p.x, new double[] { 255 });
			}
		}

		return newImage;
	}

}
