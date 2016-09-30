package ge.edu.tsu.imageprocessing.features.result;

import java.util.HashMap;

public class InvariantsResult {

	public HashMap<Integer, Integer> invariants;
	public HashMap<HashMap<Integer, Integer>, Integer> invariantsPositions;

	@Override
	public String toString() {
		return invariants.toString() + "\n" + invariantsPositions.toString();
	}

}
