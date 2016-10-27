package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import ge.edu.tsu.imageprocessing.features.BasicMetadata;
import ge.edu.tsu.imageprocessing.features.Height;
import ge.edu.tsu.imageprocessing.features.Invariants;
import ge.edu.tsu.imageprocessing.features.Position;
import ge.edu.tsu.imageprocessing.features.WhiteComponents;
import ge.edu.tsu.imageprocessing.features.base.SimpleBase;
import ge.edu.tsu.imageprocessing.features.result.HeightResult;
import ge.edu.tsu.imageprocessing.features.result.InvariantsResult;
import ge.edu.tsu.imageprocessing.features.result.PositionResult;
import ge.edu.tsu.imageprocessing.features.result.SimpleSet;
import ge.edu.tsu.imageprocessing.features.result.WhiteComponentsResult;
import ge.edu.tsu.imageprocessing.layout.LayoutCharacter;
import ge.edu.tsu.imageprocessing.layout.LayoutLine;
import ge.edu.tsu.imageprocessing.layout.LayoutWord;

public class CharacterAnalyser {

	public Mat image;

	public CharacterAnalyser(Mat image) {
		this.image = image;
	}

	public static void learnNew(Mat image, ArrayList<LayoutLine> lines, String file) {

		SimpleBase base = new SimpleBase();
		int learningCharCode = (int) 'ა';

		for (LayoutLine line : lines) {
			for (LayoutWord word : line.getWords()) {
				for (LayoutCharacter c : word.getCharacters()) {
					Mat character = image.submat((int) c.getTopLeft().y, (int) c.getBottomRight().y,
							(int) c.getTopLeft().x, (int) c.getBottomRight().x);

					// metadata
					BasicMetadata metadata = new BasicMetadata();
					metadata.lineTop = (int) word.getTopLeft().y;
					metadata.lineBottom = (int) word.getBottomRight().y;
					metadata.characterTop = (int) c.getTopLeft().y;

					// features
					InvariantsResult invs = (new Invariants()).extractFeature(character, null);
					WhiteComponentsResult whites = (new WhiteComponents()).extractFeature(character, null);
					HeightResult height = (new Height()).extractFeature(character, null);
					PositionResult position = (new Position()).extractFeature(character, metadata);

					// set
					SimpleSet set = new SimpleSet(invs, whites, height, position);

					base.addTo((char) learningCharCode, set);
					learningCharCode++;

					// Imgproc.rectangle(image, c.getTopLeft(),
					// c.getBottomRight(), new Scalar(150), 1);
				}
				// Imgproc.rectangle(image, word.getTopLeft(),
				// word.getBottomRight(),
				// new Scalar(150), 1);
			}
		}

		try {
			base.saveTo(file);
		} catch (Exception e) {

		}

	}

	public static void analyseNew(Mat image, ArrayList<LayoutLine> lines, String file) {
		SimpleBase base = new SimpleBase();
		try {
			base.restoreFrom(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (LayoutLine line : lines) {
			for (LayoutWord word : line.getWords()) {
				for (LayoutCharacter c : word.getCharacters()) {
					Mat character = image.submat((int) c.getTopLeft().y, (int) c.getBottomRight().y,
							(int) c.getTopLeft().x, (int) c.getBottomRight().x);

					// metadata
					BasicMetadata metadata = new BasicMetadata();
					metadata.lineTop = (int) word.getTopLeft().y;
					metadata.lineBottom = (int) word.getBottomRight().y;
					metadata.characterTop = (int) c.getTopLeft().y;

					// features
					InvariantsResult invs = (new Invariants()).extractFeature(character, null);
					WhiteComponentsResult whites = (new WhiteComponents()).extractFeature(character, null);
					HeightResult height = (new Height()).extractFeature(character, null);
					PositionResult position = (new Position()).extractFeature(character, metadata);

					// set
					SimpleSet set = new SimpleSet(invs, whites, height, position);

					System.out.print(base.getClosest(set));
					
					Imgproc.rectangle(image, c.getTopLeft(), c.getBottomRight(), new Scalar(150), 1);
					
					// System.out.println(" " + base.getLastSmallestDistance());
					// System.out.println(set);
					// System.out.println();
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
