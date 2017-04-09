package jasif.swarmup.apps.IntegerSum;

import android.app.Activity;
import android.util.Log;

import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.CompletedJob;
import jasif.swarmup.common.FileFactory;
import jasif.swarmup.common.Job;
import jasif.swarmup.common.JobInitializer;
import jasif.swarmup.common.JobPool;
import jasif.swarmup.delegator.MainSwarm;
import jasif.swarmup.delegator.ResultFactory;

/**
 * Created by jasif_phacsin on 16/03/17.
 */

public class IntegerSumMainSwarm extends MainSwarm {
    private int numberOfParts;
    private int index;


    public IntegerSumMainSwarm(Activity pAct) {
        super(pAct);
    }

    public void setStealMode() {
        JobInitializer.getInstance(getParentContext()).setStealMode(
                CommonConstants.READ_STRING_MODE);
        JobPool.getInstance().setStealMode(CommonConstants.READ_STRING_MODE);
    }

    @Override
    public CompletedJob doAppSpecificJob(Object pParam) {
        if(pParam!=null) {
            if(pParam instanceof Job) {
                Job job = (Job) pParam;
                String param = job.jobParams;
                String[] integerAttr = FileFactory.getInstance().tokenize(param,":",100000);
                numberOfParts = Integer.parseInt(integerAttr[0]);
                index = Integer.parseInt(integerAttr[1]);
                String s = " doing work from index " + index;
                Log.d("IntegerMainSwarm", s);
                CompletedJob cj = new CompletedJob(CommonConstants.READ_INT_MODE,
                        String.valueOf(index), index, null);
                cj.id = String.valueOf(index);
                cj.intValue = addOneLine(param);
                IntegerResult.getInstance().addToMap(String.valueOf(index), cj.intValue);
                JobPool.getInstance().incrementDoneJobCount();
                IntegerResult.getInstance().incrementDeleDoneJobs();
                return cj;
            }
        }
        return null;
    }

    private int addOneLine(String param) {
        Long result =0L;
        String[] integerAttr = FileFactory.getInstance().tokenize(param,":",100000);
        for (int i= 2;i<100000;i++) {
            result+= Integer.parseInt(integerAttr[i]);
        }
        return Integer.parseInt(result.toString());
    }

    @Override
    public ResultFactory getResultFactory() {
        return null;
    }
}
