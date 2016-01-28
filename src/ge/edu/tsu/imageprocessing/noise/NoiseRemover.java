package ge.edu.tsu.imageprocessing.noise;

import org.opencv.core.Mat;

public interface NoiseRemover {
	public Mat removeNoise(Mat imageMatrix) throws Exception;
}
