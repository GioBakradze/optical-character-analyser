package ge.edu.tsu.imageprocessing.features.result;

import java.util.Map;

public class NumberSet implements FeatureSet {

	private InvariantsResult invs;
	private WhiteComponentsResult whites;

	public NumberSet(InvariantsResult invs, WhiteComponentsResult whites) {
		super();

		this.invs = invs;
		this.whites = whites;
	}

	@Override
	public Double distance(FeatureSet set) {
		NumberSet otherSet = (NumberSet) set;

		double res = 0;

		// #########################
		// whites feature
		res = (otherSet.getWhites().count - whites.count);
		res = res * res;

		// #########################
		// invariants feature
		double invsDistance = 0;
		int[] invariants1 = new int[11];
		int[] invariants2 = new int[11];

		for (int i = 0; i < invariants1.length; i++) {
			invariants1[i] = invariants2[i] = 0;
		}

		for (Map.Entry<Integer, Integer> entry : invs.invariants.entrySet()) {
			invariants1[entry.getKey()] = entry.getValue();
		}
		for (Map.Entry<Integer, Integer> entry : otherSet.getInvs().invariants.entrySet()) {
			invariants2[entry.getKey()] = entry.getValue();
		}

		for (int i = 0; i < invariants1.length; i++) {
			invsDistance += Math.pow(invariants1[i] - invariants2[i], 2);
		}

		res += invsDistance;

		return Math.sqrt(res);
	}

	public InvariantsResult getInvs() {
		return invs;
	}

	public WhiteComponentsResult getWhites() {
		return whites;
	}

	@Override
	public String toString() {
		return invs.toString() + "\n" + whites.toString();
	}

}
