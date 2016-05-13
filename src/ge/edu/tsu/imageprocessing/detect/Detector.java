package ge.edu.tsu.imageprocessing.detect;

import ge.edu.tsu.imageprocessing.detect.params.DetectorParams;

public interface Detector {
	public char[] detect(DetectorParams param);
}
