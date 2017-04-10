package jasif.swarmup.worker;

import java.io.IOException;
import java.util.ArrayList;

import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.CompletedJob;
import jasif.swarmup.common.Job;
import jasif.swarmup.common.JobParams;
import jasif.swarmup.common.JobPool;
import jasif.swarmup.common.Slave;
import jasif.swarmup.stats.TimeMeter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * A WorkerSwarm represents a Worker device.
 *
 * 
 */
public abstract class WorkerSwarm extends Slave {

	private ArrayList<CompletedJob> doneJobs = new ArrayList<CompletedJob>();

	private int index = -1;
	private boolean isStolen = false;

	public WorkerSwarm(Context pAct, String pActivityClass, JobParams pMsg,
                       int pIndex, boolean stolen) {
		super(true, true, pAct, pActivityClass, pMsg);
		this.index = pIndex;
		this.isStolen = stolen;
	}

	public abstract CompletedJob doAppSpecificJob(Object pParam);


	@Override
	protected void doOwnWorkForWorker() throws IOException {
		long time = System.currentTimeMillis();

		Job job = JobPool.getInstance().getFirst();
		if (job != null) {
			while (job != null) {
				if (job.jobParams != null) {
					this.doneJobs.add(this.doAppSpecificJob(job.jobParams));
				}
				job = JobPool.getInstance().getFirst();

				if (this.doneJobs.size() >= CommonConstants.COMPLETED_JOBS_BUFFER) {
					this.sendResults(false);
				}
			}

			time = System.currentTimeMillis() - time;
			TimeMeter.getInstance().addToCalculateTime(time);

			if (JobPool.getInstance().isJobListEmpty()) {
				this.sendResults(true);
			}
		}

	}

	private void sendResults(boolean isSteal) {
		CompletedJob[] cjobs = null;
		synchronized (this.doneJobs) {
			cjobs = new CompletedJob[this.doneJobs.size()];
			cjobs = this.doneJobs.toArray(cjobs);
			this.doneJobs.clear();
		}
		if (cjobs != null && cjobs.length > 0 && cjobs[0] != null) {
			switch (cjobs[0].mode) {
			case CommonConstants.READ_STRING_MODE:
				try {
					StringBuffer res = new StringBuffer();
					res.append(CommonConstants.RESULT_SYMBOL);
					for (CompletedJob cj : cjobs) {
						res.append(":");
						res.append(cj.stringValue);
						res.append(CommonConstants.APP_REQUEST_SEPERATOR);
						res.append(cj.intValue);

					}
					res.append(CommonConstants.MSG_BREAK);

					Intent resultIntent = new Intent(
							CommonConstants.BROADCAST_WORKER_SEND_RESULTS_ACTION);
					resultIntent.putExtra(CommonConstants.RESULT_STRING_TYPE,
							res.toString());
					this.parentActivity.sendBroadcast(resultIntent);

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case CommonConstants.READ_COMPLETED_JOB_OBJECT_MODE:
				Intent resultIntent = new Intent(
						CommonConstants.BROADCAST_WORKER_SEND_RESULTS_ACTION);
				resultIntent
						.putExtra(CommonConstants.RESULT_OBJECT_TYPE, cjobs);
				this.parentActivity.sendBroadcast(resultIntent);
				break;
			case CommonConstants.READ_COMPLETED_JOB_OBJECT_ARRAY_MODE:

				try {
					ResultTransmitObject[] resObjs = new ResultTransmitObject[cjobs.length];
					int i = 0;

					for (CompletedJob cj : cjobs) {
						resObjs[i] = new ResultTransmitObject();
						resObjs[i].mode = cj.mode;
						resObjs[i].mixedModeArray = cj.mixedModeArray;
						resObjs[i].intValue = cj.intValue;
						resObjs[i].intArrayValue = cj.intArrayValue;
						resObjs[i].stringValue = cj.stringValue;
						resObjs[i].identifier = cj.id;
						i++;
					}

					Intent resultIntent2 = new Intent(
							CommonConstants.BROADCAST_WORKER_SEND_RESULTS_ACTION);

					Bundle bundle = new Bundle();
					bundle.putSerializable(
							CommonConstants.RESULT_COMPLETED_JOB_ARRAY_TYPE,
							resObjs);

					resultIntent2.putExtras(bundle);

					this.parentActivity.sendBroadcast(resultIntent2);

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}

		}

	}

	@Override
	public void run() {
		try {
			this.doOwnWorkForWorker();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
