package android.solleks.degreecalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class MainActivity extends AppCompatActivity {

    public static final String TEMPLATE_KEY = "android.solleks.degreecalculator.TEMPLATE_KEY";
    public static final String TEXT_SIZE_KEY = "android.solleks.degreecalculator.TEXT_SIZE_KEY";

    private static final char[] DEGREE_CHARS_NOT_ALLOW = {'°', ',', '\''};
    public static final char DEGREE  = '°';

    private TextView mainText;
    private TextView resultText;

    private ArrayList<DegreeNumber> mNumbersList;
    private StringBuilder currentNumber;
    private DegreeNumber mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mainText = (TextView) findViewById(R.id.textMain);
        resultText = (TextView) findViewById(R.id.textResult);
        Button button_C = (Button) findViewById(R.id.button_clear);
        button_C.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mNumbersList.clear();
                mNumbersList.add(new DegreeNumber());
                currentNumber = new StringBuilder();
                mainText.setText("");
                resultText.setText("");
                return true;
            }
        });

        mNumbersList = new ArrayList<>();
        mNumbersList.add(new DegreeNumber());
        currentNumber = new StringBuilder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(preferences.getString(TEMPLATE_KEY, "").equals(getResources().getStringArray(R.array.templates)[1])) {
            DegreeNumber.setToStringTemplate(1);
        } else {
            DegreeNumber.setToStringTemplate(0);
        }
        if (!resultText.getText().equals("")) {
            resultText.setText(mResult.toString());
        }

        String text_size = preferences.getString(TEXT_SIZE_KEY, getResources().getStringArray(R.array.text_size)[0]);
        float size =  getResources().getDimension(R.dimen.text_main_size);
        if (text_size.equals(getResources().getStringArray(R.array.text_size)[1])) {
            size =  getResources().getDimension(R.dimen.text_middle_size);
        } else if (text_size.equals(getResources().getStringArray(R.array.text_size)[2])) {
            size =  getResources().getDimension(R.dimen.text_little_size);
        }
        mainText.setTextSize(size);
        resultText.setTextSize(size);
    }

    public void onClickNumber(View view) {
        Button button = (Button) view;
        try {
            int a = Integer.parseInt(button.getText().toString());
            if  (currentNumber.length() > 3) {
                if ((currentNumber.charAt(currentNumber.length() - 2) == '\'' &&
                        currentNumber.charAt(currentNumber.length() - 1) == '\'')) return;

                if  ((currentNumber.charAt(currentNumber.length() - 1) != DEGREE &&
                     currentNumber.charAt(currentNumber.length() - 1) != '\'' &&
                     currentNumber.charAt(currentNumber.length() - 1) != ',')
                        &&
                    (currentNumber.charAt(currentNumber.length() - 3) == DEGREE ||
                     currentNumber.charAt(currentNumber.length() - 3) == '\'' ||
                     currentNumber.charAt(currentNumber.length() - 3) == ',')) return;
            }
        } catch (Exception e) {
            // не цифра
        }
        if (view.getId() != R.id.button_clear)
            currentNumber.append(button.getText());
        mainText.append(button.getText());

        setResultText();
    }

    public void setResultText() {
        int lastIndex = currentNumber.length() - 1;
        int numberOfMinutes = getNumberOfChars(currentNumber, '\'');

        if  (mNumbersList.size() > 1 &&
                (currentNumber.charAt(lastIndex) == DEGREE ||
                        (currentNumber.charAt(lastIndex) == '\'' &&
                            (numberOfMinutes == 1 ||
                             numberOfMinutes == 3 ||
                                (numberOfMinutes == 2 &&
                                 currentNumber.charAt(lastIndex - 1) == '\''))))
            ) {
            mNumbersList.set(mNumbersList.size() - 1, new DegreeNumber(currentNumber.toString()));
            mResult = mNumbersList.get(0);
            for (int i = 1; i < mNumbersList.size(); i++)
                mResult = mResult.add(mNumbersList.get(i));
            resultText.setText(mResult.toString());
        } else if (mNumbersList.size() == 1 &&
                (currentNumber.charAt(lastIndex) == DEGREE ||
                        currentNumber.charAt(lastIndex) == '\'')) {
            mNumbersList.set(mNumbersList.size() - 1, new DegreeNumber(currentNumber.toString()));
            resultText.setText(mNumbersList.get(0).getRadians() + "рад");
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_clear :
                String str = mainText.getText().toString();
                if (str.length() > 0) {
                    if (str.length() > 1)
                        str = str.substring(0, str.length() - 1);
                    else
                        str = "";
                    mNumbersList.clear();
                    currentNumber = new StringBuilder();
                    for (int i = 0; i < str.length(); i++) {
                        if (str.charAt(i) == '+' ||
                                str.charAt(i) == '-' && i != 0) {
                            mNumbersList.add(new DegreeNumber(currentNumber.toString()));
                            currentNumber.delete(0, currentNumber.length() - 1);

                        }
                        if (str.charAt(i) != '+')
                            currentNumber.append(str.charAt(i));
                    }

                    mainText.setText(str);
                    mNumbersList.add(new DegreeNumber(currentNumber.toString()));
                    if (str.length() > 0) setResultText();
                    else resultText.setText("");
                }
                break;
            case R.id.button_degree :
                if (!contains(currentNumber, DEGREE_CHARS_NOT_ALLOW) &&
                        currentNumber.length() > 0) {
                    onClickNumber(view);
                }
                break;
            case R.id.button_minute :
                if  (
                        currentNumber.length() > 0 &&
                        currentNumber.charAt(currentNumber.length() - 1) != DEGREE &&
                        currentNumber.charAt(currentNumber.length() - 1) != ',' &&
                        currentNumber.charAt(currentNumber.length() - 1) != '-' &&
                        getNumberOfChars(currentNumber, '\'') < 3 &&
                        !(contains(currentNumber, ',') &&
                        contains(currentNumber, '\''))
                    ) {
                    if (!contains(currentNumber, DEGREE_CHARS_NOT_ALLOW) &&
                        currentNumber.length() > (currentNumber.charAt(0) == '-'? 3 : 2))
                        break;
                    onClickNumber(view);
                }
                break;
            case R.id.button_second :
                if  (
                        currentNumber.length() > 0 &&
                        !contains(currentNumber, '\'') &&
                        !contains(currentNumber, ',') &&
                        currentNumber.charAt(currentNumber.length() - 1) != '-' &&
                        currentNumber.charAt(currentNumber.length() - 1) != DEGREE
                    ) {
                    if (!contains(currentNumber, DEGREE_CHARS_NOT_ALLOW) &&
                            currentNumber.length() > (currentNumber.charAt(0) == '-'? 3 : 2))
                        break;
                    onClickNumber(view);
                }
                break;
            case R.id.button_equal:
                mNumbersList.clear();
                mNumbersList.add(new DegreeNumber(resultText.getText().toString()));
                currentNumber = new StringBuilder(resultText.getText());
                mainText.setText(resultText.getText());
                resultText.setText(mNumbersList.get(0).getRadians() + "рад");
                break;
            case R.id.button_plus :
                if  (
                        currentNumber.length() > 1 &&
                            (currentNumber.charAt(currentNumber.length() - 1) == DEGREE ||
                             currentNumber.charAt(currentNumber.length() - 1) == '\'')
                    ) {
                    mNumbersList.add(new DegreeNumber());
                    currentNumber = new StringBuilder();
                    currentNumber.append(getResources().getString(R.string.plus));
                    mainText.append(getResources().getString(R.string.plus));
                }
                break;
            case R.id.button_minus :
                if (currentNumber.length() == 0) {
                    currentNumber.append(getResources().getString(R.string.minus));
                    mainText.append(getResources().getString(R.string.minus));
                } else if
                        (
                            currentNumber.length() > 1 &&
                                (currentNumber.charAt(currentNumber.length() - 1) == DEGREE ||
                                currentNumber.charAt(currentNumber.length() - 1) == '\'')
                        ) {
                    mNumbersList.add(new DegreeNumber());
                    currentNumber = new StringBuilder();
                    currentNumber.append(getResources().getString(R.string.minus));
                    mainText.append(getResources().getString(R.string.minus));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean contains(StringBuilder string, char letter) {
        for (char ch : string.toString().toCharArray())
            if (ch == letter) return true;
        return false;
    }
    private boolean contains(StringBuilder string, char[] letter) {
        for (char ch : string.toString().toCharArray())
            for (char arrayCh : letter)
                if (ch == arrayCh) return true;
        return false;
    }

    private int getNumberOfChars(StringBuilder string, char letter) {
        int i = 0;
        for (char ch : string.toString().toCharArray())
            if (ch == letter) i++;
        return i;
    }
}
