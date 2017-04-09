package jasif.swarmup.apps.IntegerSum;

import jasif.swarmup.common.AppInfo;
import jasif.swarmup.common.CommonConstants;

/**
 * Created by jasif_phacsin on 29/03/17.
 */

public class IntegerInfo extends AppInfo{

    public IntegerInfo(String pString, String pId) {
        super(CommonConstants.READ_STRING_MODE, pString, pId);
    }

    public String toString() {
        return this.getStringInfo();
    }
}
