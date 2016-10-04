package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// TODO: add interface for distance counting between two results
public class InvariantsResult implements FeatureResult, Serializable {

	private static final long serialVersionUID = 1L;
	public HashMap<Integer, Integer> invariants;
	public HashMap<HashMap<Integer, Integer>, Integer> invariantsPositions;

	@Override
	public String toString() {
		return invariants.toString() + "\n" + invariantsPositions.toString();
	}

	@Override
	public Double distance(FeatureResult featureResult) {
		// TODO Auto-generated method stub
		InvariantsResult other = (InvariantsResult) featureResult;
		
		double invsDistance = 0;
		int[] invariants1 = new int[11];
		int[] invariants2 = new int[11];

		for (int i = 0; i < invariants1.length; i++) {
			invariants1[i] = invariants2[i] = 0;
		}

		for (Map.Entry<Integer, Integer> entry : invariants.entrySet()) {
			invariants1[entry.getKey()] = entry.getValue();
		}
		for (Map.Entry<Integer, Integer> entry : other.invariants.entrySet()) {
			invariants2[entry.getKey()] = entry.getValue();
		}

		for (int i = 0; i < invariants1.length; i++) {
			invsDistance += Math.pow(invariants1[i] - invariants2[i], 2);
		}
		
		
		return Math.sqrt(invsDistance);
	}

}
