package ge.edu.tsu.imageprocessing.features;

import java.util.HashMap;
import java.util.HashSet;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;
import ge.edu.tsu.imageprocessing.AlgorithmDecorator;
import ge.edu.tsu.imageprocessing.features.result.InvariantsResult;

public class Invariants implements CharacterFeature<InvariantsResult> {

	HashMap<Integer, Integer> invariants;
	Graph<Point> graph;
	HashMap<HashMap<Integer, Integer>, Integer> invariantsPositions;
	Graph<Point> componentsGraph;
	
	private int pointPosition(Point start, Point end, Point point) {
		double recX, recY, pX, pY, halfX, halfY;
		recX = end.x - start.x;
		recY = end.y - start.y;
		pX = point.x - start.x;
		pY = point.y - start.y;

		halfX = recX / 2;
		halfY = recY / 2;

		if (pX > halfX) {
			if (pY > halfY) {
				return 4;
			} else {
				return 1;
			}
		} else {
			if (pY > halfY) {
				return 3;
			} else {
				return 2;
			}
		}
	}

	private void increaseCountInHashMap(HashMap<HashMap<Integer, Integer>, Integer> map,
			HashMap<Integer, Integer> obj) {
		if (map.containsKey(obj))
			map.put(obj, map.get(obj) + 1);
		else
			map.put(obj, 1);
	}
	
	@Override
	public InvariantsResult extractFeature(Mat image) {
		
		Mat featureImage = new Mat();
		image.copyTo(featureImage);
		Mat localImage = featureImage;
		
		final Point symbolStartPoint = new Point(0, 0);
		final Point symbolEndPoint = new Point(localImage.cols() - 1, localImage.rows() - 1);
		
		graph = AlgorithmDecorator.buildAreaGraph(localImage, symbolStartPoint, symbolEndPoint);
		componentsGraph = new Graph<Point>();
		invariants = new HashMap<Integer, Integer>();
		invariantsPositions = new HashMap<HashMap<Integer, Integer>, Integer>();

		graph.walkBFS(new GraphListener<Point>() {
			@Override
			public void onNode(Point e) {
				if (graph.get(e).size() == 1) {
					// localImage.put((int) e.y, (int) e.x, new double[] {210});
					if (invariants.containsKey(1))
						invariants.put(1, invariants.get(1) + 1);
					else
						invariants.put(1, 1);

					int pos = pointPosition(symbolStartPoint, symbolEndPoint, e);
					HashMap<Integer, Integer> invPosCouple = new HashMap<Integer, Integer>();
					invPosCouple.put(pos, 1);
					increaseCountInHashMap(invariantsPositions, invPosCouple);
				}
			}

			// TODO: some problem on finding components
			// detected on greek Omega
			@Override
			public void onNode(Point element, Point parent) {
				if (parent != null) {
					if (graph.get(element).size() > 2 || graph.get(parent).size() > 2) {
						componentsGraph.put(element, parent);
					}
				}
			}

			@Override
			public void onSubtree(HashSet<Point> subtree) {
			}
		});

		componentsGraph.walk(new GraphListener<Point>() {
			@Override
			public void onSubtree(HashSet<Point> subtree) {
				int currentInvs = 0;
				for (Point p : subtree) {
					if (graph.get(p).size() <= 2) {
						currentInvs++;
					}
				}

				if (currentInvs <= 2)
					return;

				if (invariants.containsKey(currentInvs))
					invariants.put(currentInvs, invariants.get(currentInvs) + 1);
				else
					invariants.put(currentInvs, 1);

				int pos = pointPosition(symbolStartPoint, symbolEndPoint, subtree.iterator().next());

				HashMap<Integer, Integer> invPosCouple = new HashMap<Integer, Integer>();
				invPosCouple.put(pos, currentInvs);

				increaseCountInHashMap(invariantsPositions, invPosCouple);
			}

			@Override
			public void onNode(Point element, Point parent) {
			}

			@Override
			public void onNode(Point e) {
			}
		});
		
		InvariantsResult res = new InvariantsResult();
		res.invariants = invariants;
		res.invariantsPositions = invariantsPositions;		
		
		return res;
	}

}
