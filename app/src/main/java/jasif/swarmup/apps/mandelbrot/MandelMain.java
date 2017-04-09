package jasif.swarmup.apps.mandelbrot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jasif.swarmup.R;
import jasif.swarmup.common.Data;

public class MandelMain extends AppCompatActivity {

    EditText iter_text = null;
    Button send_iter = null;
    String iter ;
    Intent intent;
    int IterI = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandel_main);

        iter_text = (EditText) findViewById(R.id.iter);
        send_iter = (Button) findViewById(R.id.send_iter);


        send_iter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                iter = iter_text.getText().toString();
                IterI = Integer.parseInt(iter);
                intent = new Intent(getApplicationContext(), MandelbrotDelegatorActivity.class);
                Data.getInstance().setRoot(iter);
                Toast.makeText(getApplicationContext(),Data.getInstance().getRoot(),Toast.LENGTH_SHORT).show();
                if(IterI != 0)
                    startActivity(intent);
            }
        });

    }
}
