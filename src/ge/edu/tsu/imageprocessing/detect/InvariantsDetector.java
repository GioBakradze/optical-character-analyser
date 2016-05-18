package ge.edu.tsu.imageprocessing.detect;

import java.util.HashMap;
import java.util.HashSet;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import ge.edu.tsu.graph.Graph;
import ge.edu.tsu.graph.GraphListener;
import ge.edu.tsu.imageprocessing.AlgorithmDecorator;
import ge.edu.tsu.imageprocessing.detect.params.DetectorParams;
import ge.edu.tsu.imageprocessing.detect.params.DetectorResult;
import ge.edu.tsu.imageprocessing.detect.params.InvariantsDetectorResult;

public class InvariantsDetector implements Detector {

	private HashMap<HashMap<Integer, Integer>, char[]> symbolMap = new HashMap<HashMap<Integer, Integer>, char[]>();

	public InvariantsDetector() {
		HashMap<Integer, Integer> key;

		key = new HashMap<Integer, Integer>();
		symbolMap.put(key, new char[] {});

		key = new HashMap<Integer, Integer>();
		key.put(1, 2);
		symbolMap.put(key, new char[] { 'ა', 'ე', 'ი', 'ს' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 1);
		key.put(3, 1);
		symbolMap.put(key, new char[] { 'ბ', 'გ', 'თ', 'მ', 'ნ', 'ძ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 2);
		key.put(3, 2);
		symbolMap.put(key, new char[] { 'დ', 'ფ', 'შ', 'ჩ', 'წ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 3);
		key.put(3, 1);
		symbolMap.put(key, new char[] { 'ვ', 'კ', 'პ', 'ჟ', 'უ', 'ქ', 'ყ', 'ც' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 1);
		key.put(5, 1);
		symbolMap.put(key, new char[] { 'ზ', 'ტ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 5);
		key.put(3, 3);
		symbolMap.put(key, new char[] { 'ლ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 3);
		key.put(3, 2);
		symbolMap.put(key, new char[] { 'ო' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 4);
		key.put(3, 2);
		symbolMap.put(key, new char[] { 'რ', 'ღ', 'ჰ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 4);
		key.put(6, 1);
		symbolMap.put(key, new char[] { 'ჭ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 4);
		key.put(6, 1);
		symbolMap.put(key, new char[] { 'ჭ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 2);
		key.put(4, 1);
		symbolMap.put(key, new char[] { 'ხ' });

		key = new HashMap<Integer, Integer>();
		key.put(1, 5);
		key.put(3, 1);
		key.put(4, 1);
		symbolMap.put(key, new char[] { 'ჯ' });

	}

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
	public DetectorResult detect(DetectorParams param) {

		Mat localImage = param.localImage;

		Graph<Point> graph = AlgorithmDecorator.buildAreaGraph(localImage, param.symbolStartPoint,
				param.symbolEndPoint);
		Graph<Point> componentsGraph = new Graph<Point>();
		HashMap<Integer, Integer> invariants = new HashMap<Integer, Integer>();
		HashMap<HashMap<Integer, Integer>, Integer> invariantsPositions = new HashMap<HashMap<Integer, Integer>, Integer>();

		graph.walkBFS(new GraphListener<Point>() {
			@Override
			public void onNode(Point e) {
				if (graph.get(e).size() == 1) {
					// localImage.put((int) e.y, (int) e.x, new double[] {210});
					if (invariants.containsKey(1))
						invariants.put(1, invariants.get(1) + 1);
					else
						invariants.put(1, 1);

					int pos = pointPosition(param.symbolStartPoint, param.symbolEndPoint, e);
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

				int pos = pointPosition(param.symbolStartPoint, param.symbolEndPoint, subtree.iterator().next());

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

		InvariantsDetectorResult res = new InvariantsDetectorResult();

		if (symbolMap.containsKey(invariants)) {
			res.symbols = symbolMap.get(invariants);
		} else {
			res.symbols = new char[] { '?' };
		}

		res.invariantsPositions = invariantsPositions;
		return res;
	}

}
