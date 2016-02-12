package ge.edu.tsu.imageprocessing;

import org.opencv.core.Mat;

public abstract class AlgorithmDecorator implements Algorithm {

	protected Algorithm algorithm;

	public AlgorithmDecorator(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public abstract Mat execute(Mat image);

	protected int colorAt(Mat image, int x, int y) {
		return (int) image.get(y, x)[0];
	}

	protected boolean matchesPattern(Mat image, int x, int y, double[][] pattern) throws Exception {

		if (pattern.length == 0)
			throw new Exception("Pattern shouldn't be empty");

		if (pattern.length != pattern[0].length)
			throw new Exception("Pattern should have equal width and height");

		int half = pattern.length / 2;

		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[i].length; j++) {
				if (pattern[i][j] != image.get(y - half + i, x - half + j)[0])
					return false;
			}
		}

		return true;
	}
	
	protected void applyPattern(Mat image, int x, int y, double[][] pattern) throws Exception {
		if (pattern.length == 0)
			throw new Exception("Pattern shouldn't be empty");

		if (pattern.length != pattern[0].length)
			throw new Exception("Pattern should have equal width and height");

		int half = pattern.length / 2;
		
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[i].length; j++) {
				image.put(y - half + i, x - half + j, new double[] { pattern[i][j] });					
			}
		}
	}

	protected int calcBlackNeighbours(Mat image, int x, int y) {
		int res = 0;

		if (colorAt(image, x - 1, y - 1) == 0)
			res++;

		if (colorAt(image, x, y - 1) == 0)
			res++;

		if (colorAt(image, x + 1, y - 1) == 0)
			res++;

		if (colorAt(image, x - 1, y) == 0)
			res++;

		if (colorAt(image, x + 1, y) == 0)
			res++;

		if (colorAt(image, x - 1, y + 1) == 0)
			res++;

		if (colorAt(image, x, y + 1) == 0)
			res++;

		if (colorAt(image, x + 1, y + 1) == 0)
			res++;

		return res;
	}

}
