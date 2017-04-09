package jasif.swarmup.apps.IntegerSum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import jasif.swarmup.R;

public class FinishedIntegerSumDelegatorActivity extends AppCompatActivity {
    TextView integerText;
    private int numParts = -1;
    private int results = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integer_sum_finished_dele_layout);
        integerText = (TextView) findViewById(R.id.intResult);

        numParts = getIntent().getIntExtra(IntConstants.NUMBER_OF_PARTS, 0);
        results = IntegerResult.getFinalResult();

        Long finalResult = finalSum();

    }

    private Long finalSum() {

        return null;
    }
}
