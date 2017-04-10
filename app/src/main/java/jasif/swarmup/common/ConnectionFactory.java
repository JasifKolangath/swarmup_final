package jasif.swarmup.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jasif.swarmup.R;
import jasif.swarmup.delegator.ReceivedResults;
import jasif.swarmup.delegator.ResultsRead;
import jasif.swarmup.delegator.WorkerInfo;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * 
 */
public class ConnectionFactory {
	private static ConnectionFactory theInstance = null;
	private static ArrayAdapter<String> newDevicesArrayAdapter = null;
	/**
	 * workerMap contains the String address (whether BT or WifiDirect) and
	 * WorkerInfo
	 */
	private static HashMap<String, WorkerInfo> workerMap = new HashMap<String, WorkerInfo>();
	private static HashMap<String, WifiP2PdeviceWrapper> wifiDirectDevicesMap = new HashMap<String, WifiP2PdeviceWrapper>();;
	private static ArrayList<WorkerInfo> connectedWorkerList =  new ArrayList<WorkerInfo>();;
	private static ArrayList<String> silentWorkers =  new ArrayList<String>();
	private BroadcastReceiver bcastReceiver = null;
	private int numConnected = 0;
	private int connectionState = CommonConstants.STATE_NONE;
	private long picoNetTime = 0;
	public boolean isStealing = true;
	public boolean isJobDone = false;
	private ResultsRead callbackObj = null;
	private ArrayList<ReceivedResults> resultArr = new ArrayList<ReceivedResults>();
	// private int intReads = 0;

	private boolean booLock = false;
	// private boolean isReceiverInit = false;

	// public MyLock lock = new Peterson();
	public Lock relock = new ReentrantLock();
	public int connectionMode = -1;
	public static int WIFI_MODE = 1;

	// ExecutorService threadPoolWriter = Executors.newFixedThreadPool(3);


	public synchronized static ConnectionFactory getInstance() {
		if (theInstance == null) {
			theInstance = new ConnectionFactory();
			theInstance.connectionMode = WIFI_MODE;
		}
		return theInstance;
	}

	public int getNumberOfWorkers() {
		return numConnected;
	}

	public void setReadResults(ResultsRead pI) {
		this.callbackObj = pI;
		// this.relock.lock();
		synchronized (this.resultArr) {
			if (this.resultArr.size() > 0) {
				Iterator<ReceivedResults> iter = this.resultArr.iterator();
				while (iter.hasNext()) {
					ReceivedResults rs = iter.next();
					this.callbackObj.onResultsRead(rs);
				}
				this.resultArr.clear();
			}
		}
		// this.relock.unlock();
	}

	public ResultsRead getResultsReader() {
		return this.callbackObj;
	}


	public HashMap<String, WorkerInfo> getWorkerDeviceMap() {
		return workerMap;
	}

	public HashMap<String, WifiP2PdeviceWrapper> getWifiDirectDeviceMap() {
		return wifiDirectDevicesMap;
	}


	/**
	 * Checks if all connected devices are mapped with wifiDirect MACs.
	 * 
	 * @return
	 */
	public boolean checkWifiDiretBtMapping() {
		// this.workerMap.
		Iterator<Entry<String, WorkerInfo>> iter = this.getWorkerDeviceMap()
				.entrySet().iterator();

		while (iter.hasNext()) {

			Entry<String, WorkerInfo> entry = iter.next();

			// String btMac = (String)entry.getKey();

			WorkerInfo val = (WorkerInfo) entry.getValue();

			if (val != null && val.isConnected) {
				// Log.d("checkWifiDiretBtMapping","wifid : "+val.getWiFiDirectAddress()+" BtName: "+val.getDevice().getName());
				if (val.getWiFiDirectAddress() == null) {
					return false;
				} else {
					if (val.getWiFiDirectAddress().isEmpty()) {
						return false;
					}
				}
			}
			// System.out.println("key,val: " + key + "," + val);

		}
		return true;
	}

	public int getWifiDiretBtMapped() {
		// this.workerMap.
		int count = 0;
		Iterator<Entry<String, WorkerInfo>> iter = this.getWorkerDeviceMap()
				.entrySet().iterator();

		while (iter.hasNext()) {

			Entry<String, WorkerInfo> entry = iter.next();

			// String btMac = (String)entry.getKey();

			WorkerInfo val = (WorkerInfo) entry.getValue();

			if (val != null && val.isConnected) {
				// Log.d("checkWifiDiretBtMapping","wifid : "+val.getWiFiDirectAddress()+" BtName: "+val.getDevice().getName());
				if (val.getWiFiDirectAddress() != null) {
					if (!val.getWiFiDirectAddress().isEmpty()) {
						count++;
					}
				}

			}
		}
		// System.out.println("key,val: " + key + "," + val);

		// }
		return count;
	}

	public String getBTAddrressFromWifiDirectAddress(String pWifi) {
		Iterator<Entry<String, WorkerInfo>> iter = this.getWorkerDeviceMap()
				.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<String, WorkerInfo> entry = iter.next();
			WorkerInfo val = (WorkerInfo) entry.getValue();
			if (val != null && val.isConnected) {
				Log.d("getBTAddrressFrss",
						"wifid : " + val.getWiFiDirectAddress() + " BtName: "
								+ val.getBtDevice().getName() + " pWifi:"
								+ pWifi);
				if (val.getWiFiDirectAddress() != null) {
					if (val.getWiFiDirectAddress().equals(pWifi)) {
						return val.getBtDevice().getAddress();
					}
				}

			}
		}
		return null;

	}

	public WorkerInfo getWorkerInfoFromWifiDirectAddress(String pWifi) {
//		Iterator<Entry<String, WorkerInfo>> iter = this.getWorkerDeviceMap()
//				.entrySet().iterator();
//
//		while (iter.hasNext()) {
//			Entry<String, WorkerInfo> entry = iter.next();
//			WorkerInfo val = (WorkerInfo) entry.getValue();
//			if (val != null && val.isConnected) {
//				if (val.getWiFiDirectAddress() != null) {
//					if (val.getWiFiDirectAddress().equals(pWifi)) {
//						return val;
//					}
//				}
//
//			}
//		}
		
		WorkerInfo w = this.getWorkerDeviceMap().get(pWifi);
		if(w!=null){
			if(w.isConnected){
				return w;
			}
		}
		return null;

		
		
	}
	
	public WorkerInfo getWorkerInfoFromAddress(String pAdr) {
			return this.getWorkerDeviceMap().get(pAdr);
//		Iterator<Entry<String, WorkerInfo>> iter = this.getWorkerDeviceMap()
//				.entrySet().iterator();
//
//		while (iter.hasNext()) {
//			Entry<String, WorkerInfo> entry = iter.next();
//			WorkerInfo val = (WorkerInfo) entry.getValue();
//			if (val != null && val.isConnected) {
//				if (val.getWiFiDirectAddress() != null) {
//					if (val.getWiFiDirectAddress().equals(pWifi)) {
//						return val;
//					}
//				}
//
//			}
//		}
//		return null;

	}

	public ArrayList<WorkerInfo> getConnectedWorkerList() {
		return connectedWorkerList;
	}

	public void addToConnectedWorkers(WorkerInfo pInfo) {
		connectedWorkerList.add(pInfo);
	}

	public void onWorkerSilentAtConnectedWorkers(String pAdr) {
		Iterator<WorkerInfo>iter = connectedWorkerList.iterator();
		while(iter.hasNext()){
			WorkerInfo info = iter.next();
			if(info!=null && info.getAddress().equals(pAdr)){
				info.isConnected = false;
			}
		}
		
		if(!silentWorkers.contains(pAdr)){
			silentWorkers.add(pAdr);
		}
		
	}
	
	public void onWorkerSilentAtConnectedWorkersRemove(String pAdr) {
		int i = 0;
		int index = 0;
		Iterator<WorkerInfo>iter = connectedWorkerList.iterator();
		while(iter.hasNext()){
			WorkerInfo info = iter.next();
			if(info!=null && info.getAddress().equals(pAdr)){
				info.isConnected = false;
				index = i;
			}
			i++;
			
		}
		
		connectedWorkerList.remove(index);
		
		if(!silentWorkers.contains(pAdr)){
			silentWorkers.add(pAdr);
		}
		
	}
	public void onWorkerTalkingAgain(String pAdr) {
		if(silentWorkers.contains(pAdr)){
			silentWorkers.remove(pAdr);
		}
		getWorkerDeviceMap().get(pAdr).isConnected = true;
		Iterator<WorkerInfo>iter = connectedWorkerList.iterator();
		while(iter.hasNext()){
			WorkerInfo info = iter.next();
			if(info!=null && info.getAddress().equals(pAdr)){
				info.isConnected = true;
			}
		}
		
	}
	
	public boolean wasISilent(String pAdr){
		return silentWorkers.contains(pAdr);
	}
	public void unregisterReceivers(Activity pAct) {
		if (bcastReceiver != null) {
			pAct.unregisterReceiver(bcastReceiver);
		}
	}


	public void setAsDiscoverable(Activity pAct) {
		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
				CommonConstants.BT_DISCOVERABLE_DURATION);
		// pAct.startActivity(discoverableIntent);
		pAct.startActivityForResult(discoverableIntent,
				CommonConstants.BT_SET_AS_DISCOVERABLE);
		// Log.d("WorkerActivity", "Device is now Discoverable1");
		pAct.setTitle("Device is now Discoverable");
	}

	public void setConnectionState(int pState) {
		this.connectionState = pState;
	}

	public int getConnectionState() {
		return connectionState;
	}


	public synchronized boolean getLock() {
		return this.booLock;

	}

	public synchronized void lock() {
		this.booLock = true;
	}

	public synchronized void unlock() {
		this.booLock = false;
	}


	public void addResult(ReceivedResults pStr) {
		synchronized (resultArr) {
			this.resultArr.add(pStr);
		}

	}


}
