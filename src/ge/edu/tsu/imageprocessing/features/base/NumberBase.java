package ge.edu.tsu.imageprocessing.features.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ge.edu.tsu.imageprocessing.features.result.FeatureSet;

public class NumberBase implements FeatureBase<HashMap<Character, FeatureSet>>, Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<Character, FeatureSet> base = new HashMap<>();

	@Override
	public Character getClosest(FeatureSet set) {
		double smallest = 99999;
		double tmpDistance = -1;
		Character c = null;

		for (Map.Entry<Character, FeatureSet> entry : base.entrySet()) {
			tmpDistance = entry.getValue().distance(set);
			if (tmpDistance < smallest) {
				c = entry.getKey();
				smallest = tmpDistance;
			}
		}
		
		System.out.print(smallest + "    ");

		return c;
	}

	@Override
	public ArrayList<Character> getSorted(FeatureSet set) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTo(Character ch, FeatureSet set) {
		base.put(ch, set);
	}

	@Override
	public void saveTo(String file) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(this);
		out.close();
		fileOut.close();
	}

	@Override
	public void restoreFrom(String file) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		NumberBase e = (NumberBase) in.readObject();
		in.close();
		fileIn.close();
		this.base = e.getBase();

	}

	@Override
	public HashMap<Character, FeatureSet> getBase() {
		return base;
	}
	
	@Override
	public String toString() {
		return base.toString();
	}

}
