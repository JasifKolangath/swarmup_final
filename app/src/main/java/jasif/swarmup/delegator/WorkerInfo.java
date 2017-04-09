package jasif.swarmup.delegator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.ConnectionFactory;
import jasif.swarmup.common.FileFactory;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.net.wifi.p2p.WifiP2pDevice;

public class WorkerInfo {
	private BluetoothDevice btdevice = null;
	private BluetoothSocket btsocket = null;
	private Socket socket = null;
	private String wifiDirectAddress = null;
	private WifiP2pDevice wifip2pDevice = null;
	public boolean isConnected = false;
	public boolean hasJobs = true;
	public int jobsDone = 0;
	public int connection_mode = -1;
	private ObjectOutputStream oos = null;


	public WorkerInfo(String pMac) {
		this.wifiDirectAddress = pMac;
	}

	public WorkerInfo(Socket pSocket) {
		this.socket = pSocket;
	}

	public WorkerInfo(WifiP2pDevice pdev) {
		this.wifip2pDevice = pdev;
	}

	public WorkerInfo(WifiP2pDevice pdev, int pMode) {
		this.wifip2pDevice = pdev;
		this.connection_mode = pMode;
	}

	public WorkerInfo(Socket pSocket, String pWifiMac, int pMode) {
		this.socket = pSocket;
		this.wifiDirectAddress = pWifiMac;
		this.connection_mode = pMode;
	}

	public WorkerInfo(BluetoothDevice pDev) {
		this.btdevice = pDev;
	}

	public BluetoothDevice getBtDevice() {
		return btdevice;
	}

	public String getWiFiDirectAddress() {
		if (wifiDirectAddress == null) {
			if (wifip2pDevice != null) {
				return wifip2pDevice.deviceAddress;
			}
		}
		return wifiDirectAddress;
	}

	public String getAddress() {
		if (this.connection_mode == ConnectionFactory.WIFI_MODE) {
			if (this.wifiDirectAddress != null)
				return this.wifiDirectAddress;
			if (this.wifip2pDevice != null)
				return this.wifip2pDevice.deviceAddress;
		}
		return "";

	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket pSocket) {
		this.socket = pSocket;
	}

	public void setObjectOutputStream(ObjectOutputStream pOos) {
		this.oos = pOos;
	}

	public void setwifiDirectAddress(String pwifiDirectAddress) {
		this.wifiDirectAddress = pwifiDirectAddress;
	}

	public BluetoothSocket getBTSocket() {
		return btsocket;
	}


	public String toString() {
		if (this.connection_mode == ConnectionFactory.WIFI_MODE
				) {
			if(this.wifip2pDevice!=null){
				return this.wifip2pDevice.deviceName;
			}
			return "";
		}
		return "";
	}

	public void disconnectAsDelegator() throws IOException {
		if (this.connection_mode == ConnectionFactory.WIFI_MODE
				&& this.socket != null) {
			this.socket.getInputStream().close();
			this.socket.getOutputStream().close();
			this.socket.close();
		}
	}

	public void terminateStealing() throws IOException {
		if (this.connection_mode == ConnectionFactory.WIFI_MODE) {
			if (this.oos != null) {
				this.oos.writeInt(CommonConstants.READ_INT_MODE);
				this.oos.writeInt(CommonConstants.TERM_STEALING);
			}
		}
	}
	
	public void sayNoJobsToSteal() throws IOException {
		if(this.connection_mode == ConnectionFactory.WIFI_MODE) {
			if (this.oos != null) {
				this.oos.writeInt(CommonConstants.READ_INT_MODE);
				this.oos.writeInt(CommonConstants.NO_JOBS_TO_STEAL);
			}
		}
	}

}
