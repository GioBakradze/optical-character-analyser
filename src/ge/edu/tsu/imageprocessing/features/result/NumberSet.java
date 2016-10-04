package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;
import java.util.Map;

public class NumberSet implements FeatureSet, Serializable {

	private static final long serialVersionUID = 1L;
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
		res = (otherSet.getWhites().distance(whites));
		res = res * res;

		// #########################
		// invariants feature
		res += Math.pow(otherSet.getInvs().distance(invs), 2);

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
