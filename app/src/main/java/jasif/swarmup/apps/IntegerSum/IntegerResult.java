package jasif.swarmup.apps.IntegerSum;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import jasif.swarmup.apps.mandelbrot.MandelResultComparator;
import jasif.swarmup.apps.mandelbrot.Mandelbrot;
import jasif.swarmup.common.CompletedJob;
import jasif.swarmup.common.JobPool;
import jasif.swarmup.delegator.ResultFactory;

/**
 * Created by jasif_phacsin on 16/03/17.
 */

public class IntegerResult extends ResultFactory {

    int numParts = -1;
    static int result = 0;

    private static IntegerResult intResultInstance;
    private HashMap<String, Object> resultMap;

    private IntegerResult() {
        resultMap = new HashMap<String, Object>();
    }

    public static IntegerResult getInstance() {
        if(intResultInstance == null) {
            intResultInstance = new IntegerResult();
        }
        return intResultInstance;
    }

    public static IntegerResult getInstance(int pN) {
        if(intResultInstance == null) {
            intResultInstance = new IntegerResult();
        }
        intResultInstance.numParts = pN;
        return intResultInstance;
    }
    @Override
    public boolean checkResults(ArrayList<CompletedJob> pdone) {
        return false;
    }

    @Override
    public void addToMap(String pId, Object pResult) {

    }

    public static int getFinalResult() {

        return result;
    }
}
