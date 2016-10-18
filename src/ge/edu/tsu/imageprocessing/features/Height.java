package ge.edu.tsu.imageprocessing.features;

import org.opencv.core.Mat;

import ge.edu.tsu.imageprocessing.features.result.HeightResult;

public class Height implements CharacterFeature<HeightResult> {

	@Override
	public HeightResult extractFeature(Mat image, CharacterMetadata metadata) {

		HeightResult res = new HeightResult();
		res.height = (double) image.rows() / (double) image.cols();

		return res;
	}

}
