package android.solleks.degreecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class MainActivity extends Activity {

    private static final char[] DEGREE_CHARS_NOT_ALLOW = {'°', ',', '\''};
    public static final char DEGREE  = '°';

    private TextView mainText;

    private ArrayList<DegreeNumber> mNumbersList;
    private StringBuilder currentNumber;
    private DegreeNumber mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mainText = (TextView) findViewById(R.id.textMain);

        mNumbersList = new ArrayList<>();
        mNumbersList.add(new DegreeNumber());
        currentNumber = new StringBuilder();
    }

    public void onClickNumber(View view) {
        Button button = (Button) view;
        if (view.getId() != R.id.button_clear)
            currentNumber.append(button.getText());
        mainText.append(button.getText());

        int lastIndex = currentNumber.length() - 1;
        int numberOfMinutes = getNumberOfChars(currentNumber, '\'');

        if (mNumbersList.size() > 1 &&
                (currentNumber.charAt(lastIndex) == DEGREE ||
                (currentNumber.charAt(lastIndex) == '\'' &&
                    (numberOfMinutes == 1 ||
                     numberOfMinutes == 3 ||
                    (numberOfMinutes == 2 && currentNumber.charAt(lastIndex - 1) == '\''))))) {
            mNumbersList.set(mNumbersList.size() - 1, new DegreeNumber(currentNumber.toString()));
            mResult = mNumbersList.get(0);
            for (int i = 1; i < mNumbersList.size(); i++)
                mResult = mResult.add(mNumbersList.get(i));
            ((TextView) findViewById(R.id.textResult)).setText(mResult.toString());
        } else if (mNumbersList.size() == 1 &&
                (currentNumber.charAt(lastIndex) == DEGREE ||
                 currentNumber.charAt(lastIndex) == '\'')) {
            mNumbersList.set(mNumbersList.size() - 1, new DegreeNumber(currentNumber.toString()));
        }

      /*  TextView textView = ((TextView) findViewById(R.id.textMain));
        textView.setText(mNumbersList.get(0).toString());
        for (int i = 1; i < mNumbersList.size(); i++) {
            if (mNumbersList.get(i).getNumber().signum() == 1)
                textView.append("+");
            textView.append(mNumbersList.get(i).toString());
        }*/
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_clear :
                mNumbersList.clear();
                mNumbersList.add(new DegreeNumber());
                currentNumber = new StringBuilder();
                mainText.setText("");
                ((TextView) findViewById(R.id.textResult)).setText("");
              /*  if (currentNumber.length() > 0) {
                    currentNumber.deleteCharAt(currentNumber.length() - 1);
                    if (currentNumber.charAt(currentNumber.length() - 1) >= '0' &&
                            currentNumber.charAt(currentNumber.length() - 1) <= '9')
                        onClickNumber(view);
                }*/
                break;
            case R.id.button_degree :
                if (!contains(currentNumber, DEGREE_CHARS_NOT_ALLOW) &&
                        currentNumber.length() > 0) {
                    onClickNumber(view);
                }
                break;
            case R.id.button_minute :
                if (currentNumber.charAt(currentNumber.length() - 1) != DEGREE &&
                        currentNumber.charAt(currentNumber.length() - 1) != ',' &&
                        currentNumber.charAt(currentNumber.length() - 1) != '-' &&
                        currentNumber.toString().split("\'").length - 1 < 3 &&
                        currentNumber.length() > 0) {
                    onClickNumber(view);
                }
                break;
            case R.id.button_second :
                if (!contains(currentNumber, ',') &&
                        currentNumber.charAt(currentNumber.length() - 1) != '-' &&
                        currentNumber.length() > 0) {
                    onClickNumber(view);
                }
                break;
            case R.id.button_equal:
                currentNumber.append(getResources().getString(R.string.equal));
                break;
            case R.id.button_plus :
                if (currentNumber.length() > 1 &&
                        (currentNumber.charAt(currentNumber.length() - 1) == DEGREE ||
                        currentNumber.charAt(currentNumber.length() - 1) == '\'')){
                    mNumbersList.add(new DegreeNumber());
                    currentNumber = new StringBuilder();
                    currentNumber.append(getResources().getString(R.string.plus));
                    mainText.append(getResources().getString(R.string.plus));
                }
                break;
            case R.id.button_minus :
                if (currentNumber.length() == 0)
                    currentNumber.append(getResources().getString(R.string.minus));
                else if (currentNumber.length() > 1 &&
                        (currentNumber.charAt(currentNumber.length() - 1) == DEGREE ||
                        currentNumber.charAt(currentNumber.length() - 1) == '\'')){
                    mNumbersList.add(new DegreeNumber());
                    currentNumber = new StringBuilder();
                    currentNumber.append(getResources().getString(R.string.minus));
                    mainText.append(getResources().getString(R.string.minus));
                }
                break;
        }
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
