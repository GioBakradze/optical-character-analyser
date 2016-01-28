package ge.edu.tsu.imageprocessing.noise;

import org.opencv.core.Mat;

public class KFill implements NoiseRemover {

	@Override
	public Mat removeNoise(Mat imageMatrix) throws Exception {

		for (int k = 1; k <= 2; k++) {
			System.out.println("first iteration");
			for (int i = 1; i < imageMatrix.rows() - 1; i++) {
				System.out.println("row - " + i);
				for (int j = 1; j < imageMatrix.cols() - 1; j++) {
					transformPixel(imageMatrix, i, j, k % 2 != 0);
				}
			}
		}

		return imageMatrix;
	}

	private void transformPixel(Mat image, int x, int y, boolean black) {
		int n, r, c;
		double findLevel = 0.0;
		boolean change = false;
		int k = 3;

		n = r = c = 0;

		if (image.get(x, y)[0] == 0 && black) {
			findLevel = 255.0;
			change = true;
		} else if (image.get(x, y)[0] == 255 && !black) {
			findLevel = 0.0;
			change = true;
		}

		n += countLevels(image, x - 1, y - 1, x + 1, y - 1, findLevel);
		n += countLevels(image, x - 1, y, x + 1, y, findLevel);
		n += countLevels(image, x - 1, y, x + 1, y, findLevel);

		r += countLevels(image, x - 1, y - 1, x - 1, y - 1, findLevel);
		r += countLevels(image, x + 1, y - 1, x + 1, y - 1, findLevel);
		r += countLevels(image, x - 1, y + 1, x - 1, y + 1, findLevel);
		r += countLevels(image, x + 1, y + 1, x + 1, y + 1, findLevel);

		double[] border = { image.get(x - 1, y - 1)[0], image.get(x, y - 1)[0], image.get(x + 1, y - 1)[0],
				image.get(x + 1, y)[0], image.get(x + 1, y + 1)[0], image.get(x, y + 1)[0], image.get(x - 1, y - 1)[0],
				image.get(x - 1, y)[0] };

		for (int i = 1; i < border.length; i++) {
			if (border[i] != border[i - 1] && border[i - 1] == findLevel) {
				c++;
			}
		}

		if (border[0] == border[border.length - 1] && border[0] == findLevel)
			c--;

		if (change && c == 1) {
			if (n > 3 * k - k / 3 || (n == 3 * k - k / 3 && r == 2)) {
				image.put(x, y, new double[] { findLevel });
			}
		}
	}

	private int countLevels(Mat image, int x, int y, int x2, int y2, double level) {
		int count = 0;
		for (int i = x; i <= y; i++) {
			for (int j = x2; j <= y2; j++) {
				if (image.get(i, j)[0] == level)
					count++;
			}
		}
		return count;
	}

}
