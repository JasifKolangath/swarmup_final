package jasif.swarmup.apps.mandelbrot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;

import jasif.swarmup.common.CompletedJob;
import jasif.swarmup.common.JobPool;
import jasif.swarmup.delegator.ResultFactory;

public class MandelbrotResult extends ResultFactory {

	int numRows = -1;
	int iterations = -1;
	
	private static MandelbrotResult manResultInstance;
	private HashMap<String, Object> resultMap;

	private MandelbrotResult() {
		resultMap = new HashMap<String, Object>();

	}

	
	public static MandelbrotResult getInstance() {
		if (manResultInstance == null) {
			manResultInstance = new MandelbrotResult();
		}
		return manResultInstance;
	}

	public static MandelbrotResult getInstance(int pN, int pIter) {
		if (manResultInstance == null) {
			manResultInstance = new MandelbrotResult();
		}
		manResultInstance.iterations = pIter;
		manResultInstance.numRows = pN;
		return manResultInstance;
	}

	@Override
	public boolean checkResults(ArrayList<CompletedJob> pdone) {
		boolean res1 = JobPool.getInstance()
				.checkResults(this.resultMap, pdone);
		Mandelbrot m = new Mandelbrot(iterations, 1, numRows);

		Log.d("MandelbrotResult", "TEST1 = " + res1);
		long tt1 = System.currentTimeMillis();
				
		m.generateSet();
		long tt2 = System.currentTimeMillis();
		
		Log.d("MandelbrotResult", "serial time = " + (tt2-tt1));
		
		// sort the pdone list first by index ascending
		Collections.sort(pdone, new MandelResultComparator());

		Iterator<CompletedJob> iter = pdone.iterator();
		int[][] valArr = new int[pdone.size()][];
		int i = 0;
		while (iter.hasNext()) {
			CompletedJob cj = iter.next();
			valArr[i] = cj.intArrayValue;
			i++;
		}
		return m.compareWithDistributed(valArr);
	}

	@Override
	public void addToMap(String pId, Object pResult) {
		this.resultMap.put(pId, (int[]) pResult);

	}


	public int[][] getFinalResultArray(int pMaxN) {
		int[][]results = new int[pMaxN][pMaxN];
		for(int i =0;i<pMaxN;i++){
			results[i] = (int[]) this.resultMap.get(String.valueOf(i));
		}
		
		return results;
	}
}
