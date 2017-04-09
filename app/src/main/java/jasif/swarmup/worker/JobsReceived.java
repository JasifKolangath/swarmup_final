package jasif.swarmup.worker;

import jasif.swarmup.common.Job;


public interface JobsReceived {
	public void onJobRecieved(Job jobReceived);
	public void onJobRecieved();
}
