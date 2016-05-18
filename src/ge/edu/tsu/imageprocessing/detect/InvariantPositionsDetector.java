package ge.edu.tsu.imageprocessing.detect;

import java.util.HashMap;

import ge.edu.tsu.imageprocessing.detect.params.DetectorParams;
import ge.edu.tsu.imageprocessing.detect.params.DetectorResult;
import ge.edu.tsu.imageprocessing.detect.params.InvariantsPositionDetectorParams;

public class InvariantPositionsDetector implements Detector {
	
	private HashMap< HashMap<HashMap<Integer, Integer>, Integer>, char[] > symbolMap = new HashMap<>();
	
	private void putSubkey(int[][] input, char[] symbols) {

		HashMap<HashMap<Integer, Integer>, Integer> key;
		HashMap<Integer, Integer> subkey;
		
		key = new HashMap<>();
		
		for (int i=0; i < input.length; i++) {
			
			
			subkey = new HashMap<>();
			subkey.put(input[i][0], input[i][1]);
			key.put(subkey, input[i][2]);
		}
		
		symbolMap.put(key, symbols);
	}
	
	public InvariantPositionsDetector() {
		
		putSubkey(new int[][] { 
			new int[] {3, 1, 1},
			new int[] {2, 1, 1}
		}, new char[] {'ა', 'ე'});

		putSubkey(new int[][] { 
			new int[] {1, 3, 1},
			new int[] {2, 1, 1}
		}, new char[] {'ბ', 'მ', 'ძ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 1, 1},
			new int[] {4, 3, 1}
		}, new char[] {'გ'});
		
		putSubkey(new int[][] { 
			new int[] {3, 3, 1},
			new int[] {2, 3, 1},
			new int[] {3, 1, 1},
			new int[] {4, 1, 1}
		}, new char[] {'დ'});
		
		putSubkey(new int[][] { 
			new int[] {3, 1, 2},
			new int[] {2, 1, 1},
			new int[] {4, 3, 1}
		}, new char[] {'ვ', 'კ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {2, 5, 1}
		}, new char[] {'ზ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 3, 1},
			new int[] {4, 1, 1}
		}, new char[] {'თ'});
		
		putSubkey(new int[][] { 
			new int[] {3, 1, 1},
			new int[] {4, 1, 1}
		}, new char[] {'ი'});
		
		putSubkey(new int[][] { 
			new int[] {3, 3, 1},
			new int[] {2, 3, 1},
			new int[] {3, 1, 2},
			new int[] {1, 3, 1},
			new int[] {4, 1, 3}
		}, new char[] {'ლ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {2, 3, 1}
		}, new char[] {'ნ'});
		
		putSubkey(new int[][] { 
			new int[] {3, 3, 1},
			new int[] {3, 1, 1},
			new int[] {2, 1, 1},
			new int[] {4, 1, 1},
			new int[] {4, 3, 1}
		}, new char[] {'ო'});
		
		
		putSubkey(new int[][] { 
			new int[] {1, 3, 1},
			new int[] {2, 1, 3}
		}, new char[] {'პ'});
		
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {3, 1, 2},
			new int[] {1, 3, 1}
		}, new char[] {'ჟ', 'ქ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {2, 3, 1},
			new int[] {3, 1, 1},
			new int[] {1, 3, 1},
			new int[] {2, 1, 1},
			new int[] {4, 1, 1}
		}, new char[] {'რ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {2, 1, 1}
		}, new char[] {'ს'});
		
		putSubkey(new int[][] { 
			new int[] {2, 1, 1},
			new int[] {1, 5, 1}
		}, new char[] {'ტ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {3, 1, 1},
			new int[] {1, 3, 1},
			new int[] {2, 1, 1}
		}, new char[] {'უ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 3, 1},
			new int[] {3, 1, 1},
			new int[] {4, 1, 1},
			new int[] {4, 3, 1}
		}, new char[] {'ფ'});
		
		putSubkey(new int[][] { 
			new int[] {3, 3, 1},
			new int[] {2, 3, 1},
			new int[] {3, 1, 1},
			new int[] {2, 1, 1},
			new int[] {4, 1, 2}
		}, new char[] {'ღ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {3, 1, 1},
			new int[] {2, 1, 1},
			new int[] {4, 3, 1}
		}, new char[] {'ყ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 3, 1},
			new int[] {1, 3, 1},
			new int[] {2, 1, 2}
		}, new char[] {'შ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 3, 2},
			new int[] {3, 1, 1},
			new int[] {4, 1, 1}
		}, new char[] {'ჩ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 3, 2},
			new int[] {3, 1, 1},
			new int[] {4, 1, 1}
		}, new char[] {'ჩ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {2, 1, 1},
			new int[] {4, 1, 1},
			new int[] {4, 3, 1}
		}, new char[] {'ც'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {2, 3, 2},
			new int[] {2, 1, 1}
		}, new char[] {'წ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {3, 1, 1},
			new int[] {2, 1, 1},
			new int[] {4, 1, 1},
			new int[] {1, 6, 1}
		}, new char[] {'ჭ'});
		
		putSubkey(new int[][] { 
			new int[] {2, 1, 2},
			new int[] {2, 4, 1}
		}, new char[] {'ხ'});
		
		putSubkey(new int[][] { 
			new int[] {1, 1, 1},
			new int[] {3, 3, 1},
			new int[] {3, 1, 2},
			new int[] {2, 1, 1},
			new int[] {4, 1, 1},
			new int[] {3, 4, 1}
		}, new char[] {'ჯ'});
		
		putSubkey(new int[][] { 
			new int[] {3, 1, 1},
			new int[] {1, 3, 2},
			new int[] {2, 1, 3}
		}, new char[] {'ჰ'});
	}
	
	@Override
	public DetectorResult detect(DetectorParams param) {
		InvariantsPositionDetectorParams p = (InvariantsPositionDetectorParams) param;
		DetectorResult res = new DetectorResult();
		res.symbols = new char[] {}; 
		
		if (!symbolMap.containsKey(p.invariantPositions))
			return res;
		
		res.symbols = symbolMap.get(p.invariantPositions);
		return res;
	}

}
