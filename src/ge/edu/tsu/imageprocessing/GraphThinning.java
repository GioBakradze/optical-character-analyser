package ge.edu.tsu.imageprocessing;

import java.util.ArrayList;
import java.util.HashSet;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;

/**
 * 
 * Implementation of Suzuki's graph based thinning for preserving topological
 * properties of symbols
 *
 * TODO: need to implement edge removal in the future
 * at this point we can get away with only removing nodes
 */
public class GraphThinning extends AlgorithmDecorator {
	
	ArrayList<Point> nodesToRemove;
	Graph<Point> imageGraph;
	
	public GraphThinning(Algorithm algorithm) {
		super(algorithm);
	}

	@Override
	public Mat execute(Mat sourceImage) {
		Mat newImage = algorithm.execute(sourceImage);
		Mat image = new Mat();
		newImage.copyTo(image);

		imageGraph = buildAreaGraph(image, new Point(0, 0),
				new Point(image.cols() - 1, image.rows() - 1));

		// border nodes removal
		for (int k = 1; k <= 2; k++) {

			// removeEdgesFromConcaveCorners(image, imageGraph, new double[][] {
			// new double[] { -1, 0, -1, -1 },
			// new double[] { -1, 0, 255, -1 },
			// new double[] { -1, 0, 0, 0 },
			// new double[] { 0, -1, -1, -1 } }, new Point(-1,-1), new Point(0,
			// 0));

			// removeEdgesFromConcaveCorners(image, imageGraph, new double[][] {
			// new double[] { -1, -1, -1, -1 },
			// new double[] { -1, -1, -1, -1 },
			// new double[] { -1, -1, -1, -1 },
			// new double[] { -1, -1, -1, -1 } });

			nodesToRemove = new ArrayList<Point>();

			imageGraph.walk(new GraphListener<Point>() {
				@Override
				public void onSubtree(HashSet<Point> subtree) {
				}

				@Override
				public void onNode(Point element, Point parent) {
				}

				@Override
				public void onNode(Point e) {
					if (connectivityNumber(imageGraph, e) == 1)
						nodesToRemove.add(e);
				}
			});

			runSubCycle(image, imageGraph, nodesToRemove, new Point(-1, 0));
			runSubCycle(image, imageGraph, nodesToRemove, new Point(0, -1));
			runSubCycle(image, imageGraph, nodesToRemove, new Point(1, 0));
			runSubCycle(image, imageGraph, nodesToRemove, new Point(0, 1));

			// for (Point p : nodesToRemove) {
			// imageGraph.removeNode(p);
			// setColorAt(image, (int) p.x, (int) p.y, COLOR_GRAY);
			// }

		}
		return image;
	}

	private void runSubCycle(Mat imageMat, Graph<Point> imageGraph,
			ArrayList<Point> nodesToRemove, Point sidePoint) {
		ArrayList<Point> subcycleNodes = new ArrayList<Point>();
		for (Point p : nodesToRemove) {
			if (imageGraph.nodeExists(p)
					&& imageGraph.edgeExists(p, new Point(p.x + sidePoint.x,
							p.y + sidePoint.y))
					&& !imageGraph.edgeExists(p, new Point(p.x - sidePoint.x,
							p.y - sidePoint.y))) {

				if (imageGraph.get(p).size() != 1 && connectivityNumber(imageGraph, p) == 1) {
					subcycleNodes.add(p);
				}

			}
		}

		for (Point p : subcycleNodes) {
			imageGraph.removeNode(p);
			setColorAt(imageMat, (int) p.x, (int) p.y, COLOR_WHITE);
		}
	}

	private int connectivityNumber(Graph<Point> graph, Point point) {

		int edges[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0 };
		double x = point.x;
		double y = point.y;
		int res = 0;

		for (Point p : graph.get(point)) {
			edges[edgeNumber(point, p)] = 1;
		}

		if (graph.edgeExists(new Point(x - 1, y), new Point(x, y - 1)))
			edges[12] = 1;

		if (graph.edgeExists(new Point(x + 1, y), new Point(x, y - 1)))
			edges[11] = 1;

		if (graph.edgeExists(new Point(x - 1, y), new Point(x, y + 1)))
			edges[9] = 1;

		if (graph.edgeExists(new Point(x + 1, y), new Point(x, y + 1)))
			edges[10] = 1;

		if (edges[0] == 1)
			edges[8] = 1;

		// these are extra edges
		if (graph.edgeExists(new Point(x, y - 1), new Point(x - 1, y - 1)))
			edges[13] = 1;

		if (graph.edgeExists(new Point(x - 1, y), new Point(x - 1, y - 1)))
			edges[14] = 1;

		if (graph.edgeExists(new Point(x, y - 1), new Point(x + 1, y - 1)))
			edges[15] = 1;

		if (graph.edgeExists(new Point(x + 1, y), new Point(x + 1, y - 1)))
			edges[16] = 1;

		if (graph.edgeExists(new Point(x + 1, y), new Point(x + 1, y + 1)))
			edges[17] = 1;

		if (graph.edgeExists(new Point(x, y + 1), new Point(x + 1, y + 1)))
			edges[18] = 1;

		if (graph.edgeExists(new Point(x - 1, y), new Point(x - 1, y + 1)))
			edges[19] = 1;

		if (graph.edgeExists(new Point(x, y + 1), new Point(x - 1, y + 1)))
			edges[20] = 1;

		for (int i = 0; i < edges.length; i++)
			res += edges[i];

		if (res == edges.length)
			return 0;

		// calculate connectivity number
		res = 0;

		for (int i = 0; i <= 7; i++)
			res += edges[i];
		for (int i = 0; i <= 7; i++)
			res -= edges[i] * edges[i + 1];
		for (int i = 0; i <= 3; i++)
			res += edges[2 * i] * edges[2 * i + 1] * edges[2 * i + 2];
		for (int i = 0; i <= 3; i++)
			res -= edges[2 * i] * edges[2 * i + 2] * edges[i + 9];

		return res;
	}

	private int edgeNumber(Point p1, Point p2) {
		double x = p1.x;
		double y = p1.y;
		double x2 = p2.x;
		double y2 = p2.y;

		if (x - 1 == x2 && y == y2)
			return 0;

		if (x - 1 == x2 && y + 1 == y2)
			return 1;

		if (x == x2 && y + 1 == y2)
			return 2;

		if (x + 1 == x2 && y + 1 == y2)
			return 3;

		if (x + 1 == x2 && y == y2)
			return 4;

		if (x + 1 == x2 && y - 1 == y2)
			return 5;

		if (x == x2 && y - 1 == y2)
			return 6;

		if (x - 1 == x2 && y - 1 == y2)
			return 7;

		return 7;
	}

}
