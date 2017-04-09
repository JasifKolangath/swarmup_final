package jasif.swarmup.apps.IntegerSum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import jasif.swarmup.MainActivity;
import jasif.swarmup.R;
import jasif.swarmup.common.Data;

public class IntegerMain extends AppCompatActivity {

    Button select;
    private String path="";
    TextView textView;
    long tt1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integer_main);

        select = (Button) findViewById(R.id.buttonSelect);


        textView = (TextView) findViewById(R.id.text_test);

        final DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        final FilePickerDialog dialog = new FilePickerDialog(IntegerMain.this, properties);
        dialog.setTitle("Select an input File");

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        dialog.setDialogSelectionListener(new DialogSelectionListener() {

            public String path;

            @Override
            public void onSelectedFilePaths(String[] files) {
                this.path = files[0].toString();
                Toast.makeText(getApplicationContext(), "File selection worked :" + path, Toast.LENGTH_SHORT).show();
                //Data.getInstance().setRoot(path);
                Intent intent = new Intent(IntegerMain.this,IntegerSumDelegatorActivity.class);
                intent.putExtra("path",path);
                Log.d("PathGet : ", path);
                //startActivity(intent);
            }
        });

    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String pPath) {
        this.path = pPath;
    }

    public class getRandNum extends AsyncTask<String, Void, String> {
        String mPath = "";
        FileInputStream fis = null;

        @Override
        protected String doInBackground(String... strings) {
            mPath = strings[0];
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
            return sb.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            //String[] numParts = result.split(":");
            tt1 = System.currentTimeMillis();
            String num = result;      //textView.getText().toString();
            addRandNum arn = new addRandNum();
            arn.execute(num);
            //textView.setText(result);
        }
    }

    public class addRandNum extends AsyncTask<String, Void, String> {
        String numberStr = "";
        Long result =1L;

        @Override
        protected void onPreExecute() {
            textView.setText("Calculating...");
        }

        @Override
        protected String doInBackground(String... strnum) {
            numberStr = strnum[0];
            String[] numParts = numberStr.split(":");
            for (String numb: numParts
                 ) {
                result += Integer.parseInt(numb);
            }
            long tt2 = System.currentTimeMillis();

            Log.d("IntegerResult", "serial time = " + (tt2-tt1));
            return result.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }

}
