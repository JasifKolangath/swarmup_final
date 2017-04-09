package jasif.swarmup.delegator;

import java.util.ArrayList;

import jasif.swarmup.common.AppInfo;



public interface AppRequest {

	public String[] getDistributedString();//remove this
	
	public int getNumberOfJobs();
	
	public ArrayList<AppInfo> getAppInfo();
	
//	public String getAppInfoString();
	
	public int getMode();
	
	public MainSwarm getMainSwarm();
	
	public void setMainSwarm(MainSwarm pBee);
//	public int getStealMode();
}
