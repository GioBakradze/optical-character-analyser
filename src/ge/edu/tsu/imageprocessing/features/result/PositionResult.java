package ge.edu.tsu.imageprocessing.features.result;

import java.io.Serializable;

public class PositionResult implements FeatureResult, Serializable {

	private static final long serialVersionUID = 1L;
	public double position;

	public PositionResult(double position) {
		this.position = position;
	}

	@Override
	public Double distance(FeatureResult featureResult) {
		return (double) Math.abs(this.position - ((PositionResult) featureResult).position);
	}

	@Override
	public String toString() {
		return position + "";
	}

}
