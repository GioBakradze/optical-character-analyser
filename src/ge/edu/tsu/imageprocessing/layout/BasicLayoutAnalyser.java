package ge.edu.tsu.imageprocessing.layout;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import ge.edu.tsu.imageprocessing.AlgorithmDecorator;
import ge.edu.tsu.imageprocessing.Colors;

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

	private int[] blackPointsCountX(Mat image, int startY, int endY, int startX, int endX) {

		startY = startY == -1 ? 0 : startY;
		endY = endY == -1 ? image.rows() : endY;
		startX = startX == -1 ? 0 : startX;
		endX = endX == -1 ? image.cols() : endX;

		int[] res = new int[image.cols()];

		for (int i = 0; i < res.length; i++) {
			res[i] = 0;
		}

		for (int j = startX; j < endX; j++) {
			for (int i = startY; i < endY; i++) {
				if (Colors.getInstance().pointIsBlack(image, j, i))
					res[j]++;
			}
		}

		return res;
	}

	private int[] blackPointsCountY(Mat image, int startY, int endY, int startX, int endX) {

		startY = startY == -1 ? 0 : startY;
		endY = endY == -1 ? image.rows() : endY;
		startX = startX == -1 ? 0 : startX;
		endX = endX == -1 ? image.cols() : endX;

		int[] res = new int[image.rows()];

		for (int i = 0; i < res.length; i++) {
			res[i] = 0;
		}

		for (int i = startY; i < endY; i++) {
			for (int j = startX; j < endX; j++) {
				if (Colors.getInstance().pointIsBlack(image, j, i))
					res[i]++;
			}
		}

		return res;
	}

	@Override
	public ArrayList<LayoutLine> extractLines(Mat image) {

		// *****************************************
		// extract lines upper and lower bounds
		int[] counts = blackPointsCountY(image, -1, -1, -1, -1);
		ArrayList<int[]> lines = new ArrayList<>();

		for (int i = 0; i < counts.length - 1; i++) {
			if (counts[i] == 0 && counts[i + 1] != 0) {
				int[] line = new int[2];
				line[0] = i;
				lines.add(line);
			}
		}
		for (int i = counts.length - 1, k = lines.size() - 1; i > 0; i--) {
			if (counts[i] == 0 && counts[i - 1] != 0) {
				lines.get(k)[1] = i;
				k--;
			}
		}

		// *****************************************
		// extract lines left and right bounds

		for (int[] line : lines) {
			int[] borders = blackPointsCountX(image, line[0], line[1], -1, -1);
			ArrayList<Integer> characters = new ArrayList<>();
			ArrayList<Integer> words = new ArrayList<>();
			
			for (int i=0; i < borders.length - 1; i++) {
				if (borders[i] == 0 && borders[i + 1] != 0) {
					characters.add(i);
				} 
				
				if (borders[i] != 0 && borders[i + 1] == 0) {
					characters.add(i + 1);
				}
			}
			
			double average = 0;
			double count = 0;
			
			for (int i=1; i < characters.size() - 1; i += 2) {
				average += characters.get(i + 1) - characters.get(i);
				count++;
			}
			average /= count;
//			average = 7.127659574468085;
			
			words.add(characters.get(0));
			for (int i=1; i < characters.size() - 1; i += 2) {
				if (characters.get(i + 1) - characters.get(i) >= average) {
					Imgproc.line(image, new Point(characters.get(i), line[0]), new Point(characters.get(i), line[1]), new Scalar(150));
					Imgproc.line(image, new Point(characters.get(i + 1), line[0]), new Point(characters.get(i + 1), line[1]), new Scalar(150));
				}
			}
			
			System.out.println(average);
			
//			Imgproc.line(image, new Point(0, line[0]), new Point(500, line[0]), new Scalar(150));
//			Imgproc.line(image, new Point(0, line[1]), new Point(500, line[1]), new Scalar(150));
		}
		
		
		// *****************************************
		// result
		ArrayList<LayoutLine> document = new ArrayList<>();

		return document;
	}

}
