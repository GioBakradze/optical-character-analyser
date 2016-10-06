package ge.edu.tsu.imageprocessing.features;

import java.util.HashSet;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;
import ge.edu.tsu.imageprocessing.AlgorithmDecorator;
import ge.edu.tsu.imageprocessing.features.result.WhiteComponentsResult;

public class WhiteComponents implements CharacterFeature<WhiteComponentsResult> {

	private Integer res = 0;

	@Override
	public WhiteComponentsResult extractFeature(Mat image) {
		Mat featureImage = new Mat();
		// image.copyTo(featureImage);
		Core.copyMakeBorder(image, featureImage, 3, 3, 3, 3, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));

		final Point symbolStartPoint = new Point(1, 1);
		final Point symbolEndPoint = new Point(featureImage.cols() - 1, featureImage.rows() - 1);

		Graph<Point> graph = AlgorithmDecorator.buildAreaGraphWhite(featureImage, symbolStartPoint, symbolEndPoint);
		graph.walk(new GraphListener<Point>() {
			@Override
			public void onSubtree(HashSet<Point> subtree) {
				res++;
			}

			@Override
			public void onNode(Point element, Point parent) {
			}

			@Override
			public void onNode(Point e) {
			}
		});

		WhiteComponentsResult result = new WhiteComponentsResult();
		result.count = res;

		return result;
	}

}
