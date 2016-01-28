import org.opencv.core.Core;
import org.opencv.core.Mat;

import ge.edu.tsu.imageprocessing.CharacterRegonizer;
import ge.edu.tsu.imageprocessing.noise.OpenCVNoiseRemover;
import ge.edu.tsu.imageprocessing.segmentation.OpenCVSegmenter;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception {

		CharacterRegonizer recognizer = new CharacterRegonizer(new OpenCVSegmenter(), new OpenCVNoiseRemover(),
				"assets/abc-cropped.jpg");

		recognizer.recognize();
		Mat newImage = recognizer.getImage();

		if (newImage.dataAddr() == 0) {
			System.err.println("unable to load image");
		} else {
			// System.out.println(newImage.channels());

			ImageViewer imageViewer = new ImageViewer();
			imageViewer.show(newImage, "Loaded Image");
		}
	}
}
