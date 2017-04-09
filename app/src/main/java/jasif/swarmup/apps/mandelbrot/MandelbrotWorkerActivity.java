package jasif.swarmup.apps.mandelbrot;

import jasif.swarmup.worker.WorkerActivity;
import jasif.swarmup.worker.WorkerSwarm;

public class MandelbrotWorkerActivity extends WorkerActivity {

	@Override
	public WorkerSwarm getWorkerSwarm() {
		return new MandelbrotWorkerSwarm(this, null, null, -1, false);
	}

}
