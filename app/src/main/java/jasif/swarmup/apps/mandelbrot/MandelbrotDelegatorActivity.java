package jasif.swarmup.apps.mandelbrot;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import jasif.swarmup.common.Data;
import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.JobPool;
import jasif.swarmup.delegator.AppRequest;
import jasif.swarmup.delegator.DelegatorActivity;


public class MandelbrotDelegatorActivity extends DelegatorActivity {


	private MandelbrotRequest manRequest = null;


	@Override
	public void initJobs() {
		Log.d("DATA",Data.getInstance().getRoot());
		manRequest = new MandelbrotRequest();
		manRequest.setIter(Integer.parseInt(Data.getInstance().getRoot()));
		manRequest.setMainSwarm(new MandelbrotMainSwarm(this));
		JobPool.getInstance().setStealMode(CommonConstants.READ_STRING_MODE);
	}


	@Override
	public AppRequest getAppRequest() {
		return manRequest;
	}

	@Override
	public void onJobDone() {
		super.onJobDone();
		Intent deleIntent = new Intent(this,
				FinishedMandelbrotDelegatorActivity.class);
		deleIntent.putExtra(MandelConstants.NUMBER_OF_ROWS,
				manRequest.getNumberOfRows());
		deleIntent.putExtra(MandelConstants.NUMBER_OF_ITERATIONS,
				manRequest.getIter());
		this.startActivityForResult(deleIntent,
				MandelConstants.FINISHED_DELEGATOR_MANDEL);
	}

}
