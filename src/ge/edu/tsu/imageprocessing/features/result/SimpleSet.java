package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;

public class SimpleSet implements FeatureSet, Serializable {

	private static final long serialVersionUID = 1L;
	private InvariantsResult invs;
	private WhiteComponentsResult whites;
	private HeightResult height;

	public SimpleSet(InvariantsResult invs, WhiteComponentsResult whites, HeightResult height) {
		super();

		this.invs = invs;
		this.whites = whites;
		this.height = height;
	}

	@Override
	public Double distance(FeatureSet set) {
		SimpleSet otherSet = (SimpleSet) set;

		double res = 0;

		// #########################
		// whites feature
		res = (otherSet.getWhites().distance(whites));
		res = res * res;

		// #########################
		// invariants feature
		res += Math.pow(otherSet.getInvs().distance(invs), 2);

		// #########################
		// heights feature
		res += Math.pow(otherSet.getHeight().distance(height), 2);

		return Math.sqrt(res);
	}

	public InvariantsResult getInvs() {
		return invs;
	}

	public WhiteComponentsResult getWhites() {
		return whites;
	}

	public HeightResult getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return invs.toString() + "\n" + whites.toString() + '\n' + height.toString();
	}

}
