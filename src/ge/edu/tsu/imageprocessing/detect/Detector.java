package ge.edu.tsu.imageprocessing.detect;

import ge.edu.tsu.imageprocessing.detect.params.DetectorParams;
import ge.edu.tsu.imageprocessing.detect.params.DetectorResult;

public interface Detector {
	public DetectorResult detect(DetectorParams param);
}
