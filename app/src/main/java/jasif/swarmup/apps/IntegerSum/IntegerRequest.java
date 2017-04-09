package jasif.swarmup.apps.IntegerSum;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jasif.swarmup.common.AppInfo;
import jasif.swarmup.common.CommonConstants;
import jasif.swarmup.common.Data;
import jasif.swarmup.delegator.AppRequest;
import jasif.swarmup.delegator.MainSwarm;

/**
 * Created by jasif_phacsin on 16/03/17.
 */

public class IntegerRequest implements AppRequest {
    
    private int numberOfParts = 10;
    long tt1;
    private StringBuffer commonString = null;
    private ArrayList<AppInfo> rowList = null;
    private String path;


    public IntegerRequest (String path) {
        this.path = path;
        Log.d("Path : ", "/sdcard/backups/randNum.txt");
        getRandomNumber("/sdcard/backups/randNum.txt");
    }
    public void setIter(String path) {
        this.path = path;

    }

    public String getIter() {
        return path;
    }

    private void getRandomNumber(String mPath) {
        getRandNum(mPath);
    }

    private void generateRowList(String result) {
        commonString =  new StringBuffer();
        String[] numParts = result.split(":");
        numberOfParts = numParts.length/100000;
        Log.d("Length : ", String.valueOf(numberOfParts));
        commonString.append(numberOfParts);
        commonString.append(':');
        Log.d("rowList : ",commonString.toString());
        rowList = new ArrayList<AppInfo>();
        for(int i=0;i<numberOfParts;i++){
            StringBuffer sBuf = new StringBuffer(commonString);
            sBuf.append(i);
            Log.d("rowLit : ",sBuf.toString()+ " " + String.valueOf(i));
            for(int j=i*1000000;j<(i+1)*1000000;j++) {
                sBuf.append(":");
                sBuf.append(numParts[j]);
            }
            IntegerInfo intInfo = new IntegerInfo(sBuf.toString(), String.valueOf(i));
            rowList.add(intInfo);

        }
        Log.d("rowLit : ",rowList.toString());

    }

    @Override
    public String[] getDistributedString() {
        String[] strArr = new String[numberOfParts];
        for (int i = 0; i < numberOfParts; i++) {
            strArr[i] = rowList.get(i).getStringInfo();
        }
        return strArr;
    }

    @Override
    public int getNumberOfJobs() {
        return numberOfParts;
    }

    @Override
    public ArrayList<AppInfo> getAppInfo() {
        return null;
    }

    @Override
    public int getMode() {
        return CommonConstants.READ_STRING_MODE;
    }

    @Override
    public MainSwarm getMainSwarm() {
        return null;
    }

    @Override
    public void setMainSwarm(MainSwarm pBee) {

    }
    private void getRandNum (String pPath){
        String mPath = pPath;
        FileInputStream fis = null;
            Log.d("Path in IntegerReq : ",mPath);
            try {
                fis = new FileInputStream (new File(mPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            generateRowList(sb.toString());
        }


    public int getNumberOfParts() {
        return numberOfParts;
    }
}
