package jasif.swarmup.apps.IntegerSum;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.Data;
import jasif.swarmup.common.JobPool;
import jasif.swarmup.delegator.AppRequest;
import jasif.swarmup.delegator.DelegatorActivity;

/**
 * Created by jasif_phacsin on 16/03/17.
 */

public class IntegerSumDelegatorActivity extends DelegatorActivity {

    private IntegerRequest intRequest = null;

    @Override
    public void initJobs() {
        //Log.d("DATA", getIntent().getStringExtra("path"));
        intRequest = new IntegerRequest("/sdcard/backups/randNum.txt");
        //intRequest.setIter("/sdcard/backups/randNum.txt");
        intRequest.setMainSwarm(new IntegerSumMainSwarm(this));
        JobPool.getInstance().setStealMode(CommonConstants.READ_STRING_MODE);
    }




    @Override
    public AppRequest getAppRequest() {
        return intRequest;
    }

    @Override
    public void onJobDone() {
        super.onJobDone();
        Intent deleIntent = new Intent(this,
                FinishedIntegerSumDelegatorActivity.class);
        deleIntent.putExtra(IntConstants.NUMBER_OF_PARTS,
                intRequest.getNumberOfParts());
        this.startActivityForResult(deleIntent,
                IntConstants.FINISHED_DELEGATOR_INT);

    }
}
