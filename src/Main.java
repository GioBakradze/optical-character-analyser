import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import ge.edu.tsu.imageprocessing.*;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception {

		// ###################
		// open image and detect text regions
		Mat image = Imgcodecs.imread("assets/abc-greek-clean.jpg");
		Mat newImage;
		ArrayList<Rect> boundingRects = CharacterAnalyser.getBoundingRects(image);

		// ###################
		// isolate symbols
		Mat glyphsImage = new Mat();
		image.copyTo(glyphsImage);
		glyphsImage = (new GaussianAdaptiveThreshold(new GaussianBlur(new GrayScale()))).execute(glyphsImage);
		ArrayList<ArrayList<Point[]>> glyphs = CharacterAnalyser.isolateGlyphs(glyphsImage, boundingRects);
		
		// ###################
		// analyse symbols
//		Algorithm invariants = new GaussianAdaptiveThreshold(new GrayScale());
//		newImage = invariants.execute(image);
//		newImage = CharacterAnalyser.analyse(newImage, glyphs);
		
		Algorithm alg = new SimpleSaltRemover(new GraphThinning(new GaussianAdaptiveThreshold(new GrayScale())));
		newImage = alg.execute(image);
		newImage = CharacterAnalyser.analyse(newImage, glyphs);
		
		if (newImage.dataAddr() == 0) {
			System.err.println("unable to load image");
		} else {
			// System.out.println(newImage.channels());

			ImageViewer imageViewer = new ImageViewer();
			imageViewer.show(newImage, "Loaded Image");
		}
	}
}
