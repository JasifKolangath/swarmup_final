package jasif.swarmup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import jasif.swarmup.apps.IntegerSum.IntegerMain;
import jasif.swarmup.apps.IntegerSum.IntegerSumDelegatorActivity;
import jasif.swarmup.apps.IntegerSum.IntegerSumWorkerActivity;
import jasif.swarmup.apps.mandelbrot.MandelMain;
import jasif.swarmup.apps.mandelbrot.MandelbrotDelegatorActivity;
import jasif.swarmup.apps.mandelbrot.MandelbrotWorkerActivity;
import jasif.swarmup.common.Data;
import jasif.swarmup.worker.WorkerNotify;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    RadioButton delegate,worker;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SwarmUp");
        setSupportActionBar(toolbar);*/

        CardView integerCard = (CardView) findViewById(R.id.cardViewInteger);
        CardView mandelCard = (CardView) findViewById(R.id.cardViewMandel);
        delegate = (RadioButton) findViewById(R.id.delegate_radio);
        worker = (RadioButton) findViewById(R.id.worker_radio);
        //startActivity(new Intent(this,IntegerSumDelegatorActivity.class));
        integerCard.setOnClickListener(this);
        mandelCard.setOnClickListener(this);
        //faceCard.setOnClickListener(this);
        delegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delegate.isChecked())
                    worker.setChecked(false);
                else
                    worker.setChecked(true);
            }
        });

        worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(worker.isChecked())
                    delegate.setChecked(false);
                else
                    delegate.setChecked(true);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ///getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (!delegate.isChecked() && !worker.isChecked())
            Toast.makeText(getApplicationContext(), "Select Role", Toast.LENGTH_LONG).show();
        else
            switch (v.getId()) {
                case R.id.cardViewInteger:
                    if (delegate.isChecked()) {
                        intent = new Intent(getApplicationContext(), IntegerMain.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getApplicationContext(), IntegerSumWorkerActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.cardViewMandel:
                    if (delegate.isChecked()) {
                        intent = new Intent(getApplicationContext(), MandelMain.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getApplicationContext(), MandelbrotWorkerActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
    }
    public void onDestroy() {
        super.onDestroy();
    }
}
