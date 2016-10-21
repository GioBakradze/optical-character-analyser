package ge.edu.tsu.imageprocessing.layout;

import java.util.ArrayList;

import org.opencv.core.Mat;

public interface LayoutAnalyser {

	public ArrayList<LayoutLine> extractLines(Mat image);

}
