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
import java.util.regex.Pattern;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class MainActivity extends AppCompatActivity {

	public static final String TEMPLATE_KEY = "android.solleks.degreecalculator.TEMPLATE_KEY";
	public static final String TEXT_SIZE_KEY = "android.solleks.degreecalculator.TEXT_SIZE_KEY";

	public static final char DEGREE	= '°';

	private TextView mainText;
	private TextView resultText;

	private ArrayList<DegreeNumber> mNumbersList;
	private StringBuilder currentNumber;

	/**
	 * 0 в градусах
	 * 1 в минутах
	 * 2 в секундах
	 * 3 не установлено
	 *
	 * Не даёт вводить значения в больший индекс
	 */
	private int fractionInSum;
	private boolean fractionTyped;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);

		mainText = (TextView) findViewById(R.id.textMain);
		resultText = (TextView) findViewById(R.id.textResult);
		Button button_C = (Button) findViewById(R.id.button_clear);
		// Полное удаление введенных чисел
		button_C.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mNumbersList.clear();
				mNumbersList.add(new DegreeNumber());
				currentNumber = new StringBuilder();
				mainText.setText("");
				resultText.setText("");
				fractionInSum = 3;
				return true;
			}
		});

		mNumbersList = new ArrayList<>();
		mNumbersList.add(new DegreeNumber());
		currentNumber = new StringBuilder();
		fractionInSum = 3;
		fractionTyped = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

		// Установка шаблона вывода ответа
		DegreeNumber.setToStringTemplate(preferences.getBoolean(TEMPLATE_KEY, true));

		// Установка размера шрифта из настроек
		String text_size = preferences.getString(TEXT_SIZE_KEY, getResources().getStringArray(R.array.text_size)[1]);
		float size = getResources().getDimension(R.dimen.text_middle_size);
		if (text_size.equals(getResources().getStringArray(R.array.text_size)[0])) {
			size = getResources().getDimension(R.dimen.text_main_size);
		} else if (text_size.equals(getResources().getStringArray(R.array.text_size)[2])) {
			size =	getResources().getDimension(R.dimen.text_little_size);
		}
		mainText.setTextSize(size);
		resultText.setTextSize(size);

		// Обновление ответа
		if (mNumbersList.size() > 0)
			setResultText();
	}

	/**
	 * Запись в соответствующие переменные значение нажатой кнопки
	 *
	 * @param view - Нажатая кнопка
	 */
	public void onClickNumber(View view) {
		Button button = (Button) view;

		// Проверка нажатой клавиши
		String buttonChar = button.getText().toString();
		if (buttonChar.matches("\\d")) {
			if	(canNumberTyped(currentNumber.toString() + buttonChar, fractionInSum)) {
				// Добавление цифры к числу и вывод на экран
				currentNumber.append(buttonChar);
				mainText.append(buttonChar);
			} else
				return;
		} else {
			// не цифра
			if (view.getId() != R.id.button_clear) {
				currentNumber.append(button.getText());
				mainText.append(button.getText());
			} else
				return;
		}

		setResultText();
	}

	/**
	 * Выводит в resultText:
	 * 		1. Сумму всех введенных чисел
	 * 		2. Значение в радианах для одного числа
	 * 		3. Пустую строку
	 */
	public void setResultText() {

		if	(mNumbersList.size() > 1 && isCorrectNumber(currentNumber.toString())) {

			mNumbersList.set(mNumbersList.size() - 1, new DegreeNumber(currentNumber.toString()));
			DegreeNumber mResult = mNumbersList.get(0);
			// Суммирование чисел
			for (int i = 1; i < mNumbersList.size(); i++)
				mResult = mResult.add(mNumbersList.get(i));
			// Вывод результата
			resultText.setText(mResult.toString());

		} else if (mNumbersList.size() == 1 && isCorrectNumber(currentNumber.toString())) {

			// Если введено лишь одно число, то происходит вывод радиального значения
			mNumbersList.set(mNumbersList.size() - 1, new DegreeNumber(currentNumber.toString()));
			resultText.setText(String.format("%sрад", mNumbersList.get(0).getRadians()));

		} else {
			resultText.setText("");
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.button_clear :
				String str = mainText.getText().toString();
				if (str.length() > 0) {
					// Стирание последнего символа
					str = str.substring(0, str.length() - 1);
					// Также стирается ещё один символ апострофа, если это были секунды
					if (getNumberOfChars(currentNumber, '\'') == 3 ||
							(fractionInSum == 2 && getNumberOfChars(currentNumber, '\'') == 2))
						str = str.substring(0, str.length() - 1);
					// Происходит затирание и перезапись чисел
					mNumbersList.clear();
					currentNumber = new StringBuilder();
					fractionInSum = 3;
					char ch;
					for (int i = 0; i < str.length(); i++) {
						ch = str.charAt(i);
						if (ch == '+' || ch == '-' && i != 0) {
							mNumbersList.add(new DegreeNumber(currentNumber.toString()));

							// При нахождении запятой изменяется
							// значение fractionInSum на соотвествующее
							if (fractionInSum == 3 && contains(currentNumber, ',')) {
								ch = currentNumber.charAt(currentNumber.length() - 1);
								if (ch == DEGREE)
									fractionInSum = 0;
								else if (ch == '\'') {
									if (currentNumber.charAt(currentNumber.length() - 2) == '\'')
										fractionInSum = 2;
									else
										fractionInSum = 1;
								}
							}

							currentNumber = new StringBuilder();
						}
						if (ch != '+')
							currentNumber.append(str.charAt(i));
					}

					mainText.setText(str);
					mNumbersList.add(new DegreeNumber(currentNumber.toString()));
					if (str.length() > 0)
						setResultText();
					else
						resultText.setText("");
				}
				break;
			case R.id.button_degree :
				if (canDegreeTyped(currentNumber.toString(), fractionInSum)) {
					onClickNumber(view);
				}
				break;
			case R.id.button_minute :
				if (canMinuteTyped(currentNumber.toString(), fractionInSum)){
					onClickNumber(view);
					// Если удовлетворяет описанным ниже случаям, то добавить ещё один апостроф
					if (addMinuteIfNeeded())
						onClickNumber(view);
				}
				break;
			case R.id.button_frac :
				if (canFractionTyped(currentNumber.toString(), fractionInSum)) {
					onClickNumber(view);
					if (fractionInSum == 3)
						fractionTyped = true;
				}
				break;
			case R.id.button_equal:
				if (mNumbersList.size() < 2) break;
				mNumbersList.clear();
				mNumbersList.add(new DegreeNumber(resultText.getText().toString()));
				currentNumber = new StringBuilder(resultText.getText());
				mainText.setText(resultText.getText());
				resultText.setText(String.format("%sрад", mNumbersList.get(0).getRadians()));
				break;
			case R.id.button_plus :
				if	(currentNumber.length() > 1) {
					if (currentNumber.charAt(currentNumber.length() - 1) == DEGREE ||
							currentNumber.charAt(currentNumber.length() - 1) == '\'') {

						if (fractionTyped) {
							fractionTyped = false;
							if (getNumberOfChars(currentNumber, '\'') > 1){
								fractionInSum = 2;
							} else if (getNumberOfChars(currentNumber, '\'') == 1) {
								fractionInSum = 1;
							} else {
								fractionInSum = 0;
							}
						}
						mNumbersList.add(new DegreeNumber());
						currentNumber = new StringBuilder();
						currentNumber.append(getResources().getString(R.string.plus));
						mainText.append(getResources().getString(R.string.plus));
					}

				}
				break;
			case R.id.button_minus :
				switch (currentNumber.length()) {
					case 0 :
						currentNumber.append(getResources().getString(R.string.minus));
						mainText.append(getResources().getString(R.string.minus));
						break;

					default:
						if (currentNumber.charAt(currentNumber.length() - 1) == DEGREE ||
								currentNumber.charAt(currentNumber.length() - 1) == '\'') {

							if (fractionTyped) {
								fractionTyped = false;
								if (getNumberOfChars(currentNumber, '\'') > 1){
									fractionInSum = 2;
								} else if (getNumberOfChars(currentNumber, '\'') == 1) {
									fractionInSum = 1;
								} else {
									fractionInSum = 0;
								}
							}
							mNumbersList.add(new DegreeNumber());
							currentNumber = new StringBuilder();
							currentNumber.append(getResources().getString(R.string.minus));
							mainText.append(getResources().getString(R.string.minus));
						}
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
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * @param str - Строка с числом
	 * @return - возвращает true, если число подходит под шаблон
	 */
	private boolean isCorrectNumber(String str) {
		return str.length() > 0 &&
				str.matches("\\+?\\-?" +
				"(\\d+(,\\d+)?°)?" +
				"([0-5]?[0-9](,\\d+)?')?" +
				"([0-5]?[0-9](,\\d+)?'')?");
	}


	/**
	 * Два случая:
	 	1. Когда введены минуты и секунды и поставлен первый апостроф
	 	2. Когда введёна запятая и fractionInSum == 2
	 *
	 * @return Возвращает true, если необходимо добавить символ минуты
	 */
	private boolean addMinuteIfNeeded() {
		return ((getNumberOfChars(currentNumber, '\'') == 2 &&
				currentNumber.charAt(currentNumber.length() - 2) != '\'') ||
				(contains(currentNumber, ',') && fractionInSum == 2));
	}

	private boolean canNumberTyped(String str, int fraction) {
		ArrayList<String> st = new ArrayList<>();
		switch (fraction) {
			case 0 :
				Pattern p = Pattern.compile("\\+?\\-?\\d*,?\\d*");
				return p.matcher(str).matches();
			case 1 :
				st.add("\\+?\\-?\\d*");
				st.add("\\+?\\-?(\\d+°)?[0-5]?[0-9](,\\d+)?");
				break;
			case 2 :
				st.add("\\+?\\-?\\d*");
				st.add("\\+?\\-?(\\d+°)?[0-5]?[0-9](,\\d+)?");
				st.add("\\+?\\-?(\\d+°)?[0-5]?[0-9]\\'[0-5]?[0-9](,\\d+)?");
				break;
			case 3 :
				st.add("\\+?\\-?\\d*,?\\d*");
				st.add("\\+?\\-?(\\d+°)?[0-5]?[0-9](,\\d+)?");
				st.add("\\+?\\-?(\\d+°)?[0-5]?[0-9]\\'[0-5]?[0-9](,\\d+)?");
		}
		for (String s : st)
			if (Pattern.compile(s).matcher(str).matches())
				return true;
		return false;
	}

	// Возвращает bool значение - можно ли ввести знак "°"
	private boolean canDegreeTyped(String str, int fraction) {
		Pattern p;
		if (fraction == 0 || fraction == 3) {
			p = Pattern.compile("\\+?\\-?\\d+(,\\d+)?");
		} else {
			p = Pattern.compile("\\+?\\-?\\d+");
		}
		return p.matcher(str).matches();
	}

	// Возвращает bool значение - можно ли ввести знак "\'"
	private boolean canMinuteTyped(String str, int fraction) {
		Pattern p;
		if (fraction == 2 || fraction == 3) {
			p = Pattern.compile("\\+?\\-?(\\d+°)?([0-5]?[0-9]')?[0-5]?[0-9](,\\d+)?'?");
		} else if (fraction == 1) {
			p = Pattern.compile("\\+?\\-?(\\d+°)?[0-5]?[0-9](,\\d+)?");
		} else {
			return false;
		}
		return p.matcher(str).matches();
	}

	// Возвращает bool значение - можно ли ввести знак ","
	private boolean canFractionTyped(String str, int fraction) {
		Pattern p;
		if (fraction == 0)
			p = Pattern.compile("\\+?\\-?\\d+");
		else if (fraction == 1)
			p = Pattern.compile("\\+?\\-?(\\d+°)?[0-5]?[0-9]");
		else if (fraction == 2)
			p = Pattern.compile("\\+?\\-?(\\d+°)?([0-5]?[0-9]\')?[0-5]?[0-9]");
		else {
			for (int i = 0; i < 3; i++)
				if (canFractionTyped(str, i))
					return true;
			return false;
		}
		return p.matcher(str).matches();
	}

	private boolean contains(StringBuilder string, char letter) {
		for (char ch : string.toString().toCharArray())
			if (ch == letter) return true;
		return false;
	}

	private int getNumberOfChars(StringBuilder string, char letter) {
		int i = 0;
		for (char ch : string.toString().toCharArray())
			if (ch == letter) i++;
		return i;
	}
}
