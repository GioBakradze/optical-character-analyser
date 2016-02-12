package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;
import java.util.HashMap;

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
		ArrayList<Point> pointsToRemove;
		HashMap<Point, double[][]> patternsToApply;
		
		while (true) {
			pointsToRemove = new ArrayList<Point>();
			patternsToApply = new HashMap<Point, double[][]>();
			
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
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   255 },
							new double[] { 255,  0,   0 }, 
							new double[] { 255,  255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   255 },
							new double[] { 0,    0,   255 }, 
							new double[] { 255,  255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 0,    0,   255 }, 
							new double[] { 255,  0,   255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 255,  0,   0 }, 
							new double[] { 255,  0,   255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0, 255 },
							new double[] { 255,  0,   0 }, 
							new double[] { 255,  0,   255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0, 255 },
							new double[] { 0,    0,   255 }, 
							new double[] { 255,  0,   255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255, 255 },
							new double[] { 0,    0,   0 }, 
							new double[] { 255,  0,   255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  0,   255 },
							new double[] { 0,    0,   0 }, 
							new double[] { 255,  255, 255 } })) {
							pointsToRemove.add(new Point(j, i));
						}
						
						/*
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255,  255,   0 },
							new double[] { 0,    0,     0 }, 
							new double[] { 255,  255,   0 } })) {
							
							pointsToRemove.add(new Point(j + 1, i));
						}*/
						
						// patterns to apply
						/* if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 255, 255,   0 },
							new double[] { 255, 0,     0 }, 
							new double[] { 0,   255,   255 } })) {
							
							patternsToApply.put(new Point(j, i), new double[][] { 
								new double[] { 255, 255,  0 },
								new double[] { 255, 255,  0 }, 
								new double[] { 0,   0,    255 } });
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 0,   255,  255 },
							new double[] { 255, 0,    0 }, 
							new double[] { 255, 255,  0 } })) {
							
							patternsToApply.put(new Point(j, i), new double[][] { 
								new double[] { 0,   0,   255 },
								new double[] { 255, 255, 0 }, 
								new double[] { 255, 255, 0 } });
						}
						
						if (matchesPattern(newImage, j, i, new double[][] { 
							new double[] { 0,   255, 255 },
							new double[] { 255, 0,   0 }, 
							new double[] { 255, 0,   255 } })) {
							
							patternsToApply.put(new Point(j, i), new double[][] { 
								new double[] { 0,   0,   255 },
								new double[] { 255, 255, 0 }, 
								new double[] { 255, 0,   255 } });
						} */
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// problematic characters: ზ, ტ, ძ 
			
			if (pointsToRemove.size() == 0 && patternsToApply.size() == 0) break;
			
			for (Point p : pointsToRemove) {
				newImage.put((int) p.y, (int) p.x, new double[] { 255 });
			}
			
			for (Point p : patternsToApply.keySet()) {
				try {
					applyPattern(image, (int) p.x, (int) p.y, patternsToApply.get(p));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return newImage;
	}

}
