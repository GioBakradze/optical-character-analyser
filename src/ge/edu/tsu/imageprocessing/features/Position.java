package ge.edu.tsu.imageprocessing.features;

import org.opencv.core.Mat;

import ge.edu.tsu.imageprocessing.features.result.PositionResult;

public class Position implements CharacterFeature<PositionResult> {

	@Override
	public PositionResult extractFeature(Mat image, CharacterMetadata metadata) {
		BasicMetadata meta = ((BasicMetadata) metadata);
		return new PositionResult(meta.characterTop - meta.lineTop);
	}

}
