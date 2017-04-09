package jasif.swarmup.apps.IntegerSum;

import android.content.Context;

import jasif.swarmup.common.CompletedJob;
import jasif.swarmup.common.JobParams;
import jasif.swarmup.worker.WorkerSwarm;

/**
 * Created by jasif_phacsin on 16/03/17.
 */

public class IntegerSumWorkerSwarm extends WorkerSwarm {
    public IntegerSumWorkerSwarm(Context pAct, String pActivityClass, JobParams pMsg, int pIndex, boolean stolen) {
        super(pAct, pActivityClass, pMsg, pIndex, stolen);
    }

    @Override
    public CompletedJob doAppSpecificJob(Object pParam) {
        return null;
    }
}
