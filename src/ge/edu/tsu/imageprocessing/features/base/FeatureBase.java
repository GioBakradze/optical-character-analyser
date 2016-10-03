package ge.edu.tsu.imageprocessing.features.base;

import java.io.IOException;
import java.util.ArrayList;

import ge.edu.tsu.imageprocessing.features.result.FeatureSet;

public interface FeatureBase<T> {

	public Character getClosest(FeatureSet set);

	public ArrayList<Character> getSorted(FeatureSet set);

	public void addTo(Character ch, FeatureSet set);

	public void saveTo(String file) throws IOException;

	public void restoreFrom(String file) throws IOException, ClassNotFoundException;

	public T getBase();

}
