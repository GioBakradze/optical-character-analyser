import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import ge.edu.tsu.imageprocessing.*;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) throws Exception {

		// new way
		// chain algorithms together
		Algorithm invariants = new SaltRemover(new ZhangSuenThinning(new GaussianAdaptiveThreshold(new GrayScale())));
		Mat newImage = invariants.execute(Imgcodecs.imread("assets/abc.jpg"));

		if (newImage.dataAddr() == 0) {
			System.err.println("unable to load image");
		} else {
			// System.out.println(newImage.channels());

			ImageViewer imageViewer = new ImageViewer();
			imageViewer.show(newImage, "Loaded Image");
		}
	}
}
