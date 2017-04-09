package jasif.swarmup.apps.mandelbrot;

import java.util.ArrayList;

import jasif.swarmup.common.AppInfo;
import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.delegator.AppRequest;
import jasif.swarmup.delegator.MainSwarm;

public class MandelbrotRequest implements AppRequest {

	private int numberOfRows = 300;// gives a numberOfRows*numberOfRows matrix
	private ArrayList<AppInfo> rowList = null;
	private int iter = 5000;
	private double xc = -0.5;// -0.5
	private double yc = 0.0;
	private double size = 2.0;
	private MainSwarm mandelSwarm = null;
	private StringBuffer commonString = null;

	public MandelbrotRequest(int pNumRowsCols, int pIter, double pX,
			double pY, double pSize) {
		this.numberOfRows = pNumRowsCols;
		this.iter = pIter;
		this.xc = pX;
		this.yc = pY;
		this.size = pSize;
		generateRowList();
	}

	public void setIter(int iter) {
		this.iter = iter;
	}

	public int getIter() {
		return iter;
	}

	public MandelbrotRequest() {
		generateRowList();
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}



	
	public double getXc() {
		return xc;
	}

	public double getYc() {
		return yc;
	}

	public double getSize() {
		return size;
	}

	private void generateRowList() {
		commonString = new StringBuffer();
		commonString.append(xc);
		commonString.append(":");
		commonString.append(yc);
		commonString.append(":");
		commonString.append(size);
		commonString.append(":");
		commonString.append(iter);
		commonString.append(":");
		commonString.append(numberOfRows);
		commonString.append(":");

		rowList = new ArrayList<AppInfo>();
		for (int i = 0; i < numberOfRows; i++) {
			StringBuffer sBuf = new StringBuffer(commonString);
			sBuf.append(i);
			MandelInfo minfo = new MandelInfo(sBuf.toString(), String.valueOf(i));
			rowList.add(minfo);
		}
	}

	@Override
	public String[] getDistributedString() {
		String[] strArr = new String[numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			strArr[i] = rowList.get(i).getStringInfo();
		}
		return strArr;
	}

	@Override
	public int getNumberOfJobs() {
		return numberOfRows;
	}

	@Override
	public ArrayList<AppInfo> getAppInfo() {
		return rowList;
	}

	@Override
	public int getMode() {
		return CommonConstants.READ_STRING_MODE;
	}

	@Override
	public MainSwarm getMainSwarm() {
		return mandelSwarm;
	}

	@Override
	public void setMainSwarm(MainSwarm pSwarm) {
		this.mandelSwarm = pSwarm;
	}

}
