import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ge.edu.tsu.imageprocessing.*;
import ge.edu.tsu.imageprocessing.layout.BasicLayoutAnalyser;
import ge.edu.tsu.imageprocessing.layout.LayoutLine;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception {

		// ###################
		// open image and detect text regions
		Mat image = Imgcodecs.imread("learn/sylfaen-50-test-2.jpg");
		Mat newImage;

		Algorithm alg = new SimpleSaltRemover(
				new GraphThinning(new GaussianAdaptiveThreshold(new GaussianBlur(new GrayScale()))));

		newImage = alg.execute(image);

		BasicLayoutAnalyser layout = new BasicLayoutAnalyser();
		ArrayList<LayoutLine> lines = layout.extractLines(newImage);

//		CharacterAnalyser.learnNew(newImage, lines, "base/characters.base");
		CharacterAnalyser.analyseNew(newImage, lines, "base/characters.base");

		if (newImage.dataAddr() == 0) {
			System.err.println("unable to load image");
		} else {
			new ImageViewer().show(newImage, "Loaded Image");
		}
	}
}
