package ge.edu.tsu.imageprocessing.detect.params;

import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class InvariantsPositionDetectorParams extends DetectorParams {

	public HashMap<HashMap<Integer, Integer>, Integer> invariantPositions;

	public InvariantsPositionDetectorParams(Mat localImage, Point symbolStartPoint, Point symbolEndPoint,
			HashMap<HashMap<Integer, Integer>, Integer> invariantPositions) {
		super(localImage, symbolStartPoint, symbolEndPoint);
		this.invariantPositions = invariantPositions;
	}

}
