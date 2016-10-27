package ge.edu.tsu.imageprocessing.layout;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.imageprocessing.Colors;

public class BasicLayoutAnalyser implements LayoutAnalyser {

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

		ArrayList<LayoutLine> document = new ArrayList<>();

		for (int[] line : lines) {
			int[] borders = blackPointsCountX(image, line[0], line[1], -1, -1);
			ArrayList<Integer> characters = new ArrayList<>();

			HashMap<Integer, Boolean> startX = new HashMap<>();
			HashMap<Integer, Boolean> endX = new HashMap<>();

			for (int i = 0; i < borders.length - 1; i++) {
				if (borders[i] == 0 && borders[i + 1] != 0) {
					characters.add(i);
				}

				if (borders[i] != 0 && borders[i + 1] == 0) {
					characters.add(i + 1);
				}
			}

			double average = 0;
			double count = 0;

			for (int i = 1; i < characters.size() - 1; i += 2) {
				average += characters.get(i + 1) - characters.get(i);
				count++;
			}
			average /= count;

			startX.put(characters.get(0), true);
			for (int i = 1; i < characters.size() - 1; i += 2) {
				if (characters.get(i + 1) - characters.get(i) >= average) {
					endX.put(characters.get(i), true);
					startX.put(characters.get(i + 1), true);
				}
			}
			endX.put(characters.get(characters.size() - 1), true);

			LayoutLine layoutLine = new LayoutLine();
			LayoutWord layoutWord = null;
			Point topLeft = null, bottomRight = null;

			for (int i = 0; i < characters.size(); i++) {

				int ch = characters.get(i);

				if (startX.containsKey(ch)) {
					topLeft = new Point(ch, line[0]);
					layoutWord = new LayoutWord();
				}

				if (endX.containsKey(ch)) {
					bottomRight = new Point(ch, line[1]);
					layoutWord.setTopLeft(topLeft);
					layoutWord.setBottomRight(bottomRight);
					layoutLine.addWord(layoutWord);
				}

				if ((i + 1) % 2 != 0) {
					LayoutCharacter character = new LayoutCharacter();

					int[] charBlackPointCount = blackPointsCountY(image, line[0], line[1], characters.get(i),
							characters.get(i + 1));

					for (int k = line[0]; k < line[1]; k++) {
						if (charBlackPointCount[k] == 0 && charBlackPointCount[k + 1] != 0) {
							character.setTopLeft(new Point(characters.get(i), k));
						}

						if (charBlackPointCount[k] != 0 && charBlackPointCount[k + 1] == 0) {
							character.setBottomRight(new Point(characters.get(i + 1), k + 1));
						}
					}

					layoutWord.addCharacter(character);
				}
			}

			// for (LayoutWord w : layoutLine.getWords()) {
			// for (LayoutCharacter c : w.getCharacters()) {
			// Imgproc.rectangle(image, c.getTopLeft(), c.getBottomRight(), new
			// Scalar(150), 1);
			// }
			// Imgproc.rectangle(image, w.getTopLeft(), w.getBottomRight(),
			// new Scalar(150), 1);
			// }
			document.add(layoutLine);
		}

		return document;
	}

}
