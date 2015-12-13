package android.solleks.degreecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DegreeNumber d = new DegreeNumber();
        setContentView(R.layout.main_activity);
    }

    public void onClickNumber(View view) {
    }

    public void onClick(View view) {
    }
}
