package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;

public class HeightResult implements FeatureResult, Serializable {

	private static final long serialVersionUID = 1L;
	public Double height;

	@Override
	public Double distance(FeatureResult featureResult) {
		return Math.abs(this.height - ((HeightResult) featureResult).height);
	}

	@Override
	public String toString() {
		return height + "";
	}

}
