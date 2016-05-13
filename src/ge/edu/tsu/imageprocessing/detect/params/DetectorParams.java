package ge.edu.tsu.imageprocessing.detect.params;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class DetectorParams {
	
	public Mat localImage;
	public Point symbolStartPoint;
	public Point symbolEndPoint;
	
	public DetectorParams(Mat localImage, Point symbolStartPoint, Point symbolEndPoint) {
		super();
		this.localImage = localImage;
		this.symbolStartPoint = symbolStartPoint;
		this.symbolEndPoint = symbolEndPoint;
	}
	
	
	
}
