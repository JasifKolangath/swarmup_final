package jasif.swarmup.apps.facematch;

import jasif.swarmup.worker.WorkerActivity;
import jasif.swarmup.worker.WorkerSwarm;

public class FaceMatchWorkerActivity extends WorkerActivity{

	@Override
	public WorkerSwarm getWorkerSwarm() {
		return new FaceMatchWorkerSwarm(this, null, null, 2, false);
	}

}
