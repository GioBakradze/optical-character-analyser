package ge.edu.tsu.imageprocessing.features;

import org.opencv.core.Mat;

public interface CharacterFeature<T> {
	public T extractFeature(Mat image, CharacterMetadata metadata);
}
