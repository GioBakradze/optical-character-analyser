package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;

public class SimpleSet implements FeatureSet, Serializable {

	private static final long serialVersionUID = 1L;
	private InvariantsResult invs;
	private WhiteComponentsResult whites;
	private HeightResult height;
	private PositionResult position;

	public SimpleSet(InvariantsResult invs, WhiteComponentsResult whites, HeightResult height,
			PositionResult position) {
		super();

		this.invs = invs;
		this.whites = whites;
		this.height = height;
		this.position = position;
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

		// #########################
		// positions feature
		res += Math.pow(otherSet.getPosition().distance(position), 2);

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

	public PositionResult getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return invs.toString() + "\nwhites:" + whites.toString() + "\nheight:" + height.toString() + "\nposition:"
				+ position.toString();
	}

}
