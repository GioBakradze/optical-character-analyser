package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;

public class WhiteComponentsResult implements FeatureResult, Serializable {

	private static final long serialVersionUID = 1L;
	public Integer count;

	@Override
	public String toString() {
		return count.toString();
	}

	@Override
	public Double distance(FeatureResult featureResult) {

		WhiteComponentsResult other = (WhiteComponentsResult) featureResult;

		return new Double(count - other.count);
	}
}
