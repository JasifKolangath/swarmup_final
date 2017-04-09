package jasif.swarmup.apps.IntegerSum;

import jasif.swarmup.common.CommonConstants;

/**
 * Created by jasif_phacsin on 16/03/17.
 */

public class IntegerSum {
    int lowerLimit = 0;
    int upperLimit = 0;
    int numParts = IntConstants.FINISHED_DELEGATOR_INT;
    int interval = 0;
    public void IntegerSum(int ll, int ul) {
        this.lowerLimit = ll;
        this.upperLimit = ul;
    }

    public void addNums() {
        interval = (this.upperLimit-this.lowerLimit)/numParts;

    }
}
