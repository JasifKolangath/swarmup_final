package jasif.swarmup.apps.mandelbrot;

import jasif.swarmup.common.AppInfo;
import jasif.swarmup.common.CommonConstants;

public class MandelInfo extends AppInfo {

	public MandelInfo(String pString, String pId) {
		super(CommonConstants.READ_STRING_MODE, pString, pId);
	}

	public String toString() {
		return this.getStringInfo();
	}
}
