package ge.edu.tsu.imageprocessing.layout;

import java.util.ArrayList;

import org.opencv.core.Mat;

import ge.edu.tsu.imageprocessing.AlgorithmDecorator;

public class BasicLayoutAnalyser implements LayoutAnalyser {

	private int findLineTop(Mat image, int offsetX, int offsetY, int toX, int toY) {

		toY = toY == -1 ? image.rows() : toY;
		toX = toX == -1 ? image.cols() : toX;

		for (int i = 0 + offsetY; i < toY; i++) {
			for (int j = 0 + offsetX; j < toX; j++) {
				if (AlgorithmDecorator.pointIsBlack(image, j, i)) {
					return i;
				}
			}
		}
		return 0;
	}

	private int findLineBottom(Mat image, int offsetX, int offsetY, int toX, int toY) {
		toY = toY == -1 ? 0 : toY;
		toX = toX == -1 ? image.cols() : toX;

		for (int i = image.rows() - 1 - offsetY; i >= toY; i--) {
			for (int j = 0 + offsetX; j < toX; j++) {
				if (AlgorithmDecorator.pointIsBlack(image, j, i)) {
					return i;
				}
			}
		}
		return 0;
	}

	@Override
	public ArrayList<LayoutLine> extractLines(Mat image) {

		ArrayList<LayoutLine> document = new ArrayList<>();

		System.out.println(findLineTop(image, 0, 0, -1, -1));

		return document;
	}

}
