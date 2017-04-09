package jasif.swarmup.common;

/**
 * Created by jasif_phacsin on 05/04/17.
 */

public class Data {
    private String root;
    private static Data theObject = null;

    public Data() {
    }

    public static Data getInstance() {
        if (theObject == null) {
            theObject = new Data();
        }
        return theObject;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }
}
