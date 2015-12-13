package android.solleks.degreecalculator;

import java.math.BigDecimal;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class DegreeNumber {

    /**
     * Первые два порядка - секунды
     * Вторые два порядка - минуты
     * Остальные - градусы
     */
    private BigDecimal mNumber;

    DegreeNumber() {
        mNumber = new BigDecimal("0");

        BigDecimal bigDecimal= new BigDecimal("212.3");
        BigDecimal degree = bigDecimal.divideToIntegralValue(new BigDecimal("100"));
        BigDecimal minutes = bigDecimal.remainder(new BigDecimal("100"));
    }

    DegreeNumber(String number) {
        setNumber(number);
    }

    DegreeNumber(BigDecimal number) {
        mNumber = number;
    }

    public DegreeNumber add(DegreeNumber addedNumber) {
        DegreeNumber result = new DegreeNumber(this.getNumber().add(addedNumber.getNumber()));

        if (result.getMinutes().compareTo(new BigDecimal("60")) != -1 &&
                this.getNumber().signum() == 1 &&
                addedNumber.getNumber().signum() == 1) {
            /**
             * Минуты больше 60
             * Оба числа положительные
             * ***
             * Увеличить градусы на 1
             * уменьшить минуты на 60
             * */
            BigDecimal temp = result.getNumber();
            temp = temp.add(new BigDecimal("4000"));
            result.setNumber(temp);
        } 

        return result;
    }

    public BigDecimal getDegrees() {
        return mNumber.divideToIntegralValue(new BigDecimal("10000"));
    }

    public BigDecimal getMinutes() {
        BigDecimal minutes = mNumber.divideToIntegralValue(new BigDecimal("100"));
        return minutes.remainder(new BigDecimal("100")).abs();
    }

    public BigDecimal getSeconds() {
        return mNumber.remainder(new BigDecimal("100")).abs();
    }

    public void setNumber(String number) {
        mNumber = new BigDecimal("0");
        boolean isMinutesAdded = false;
        boolean isMinus = false;
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            switch (number.charAt(i)) {
                case '°':
                    currentNumber.append("0000");
                    if (isMinus)
                        mNumber = mNumber.subtract(new BigDecimal(currentNumber.toString()));
                    else
                        mNumber = mNumber.add(new BigDecimal(currentNumber.toString()));
                    currentNumber = new StringBuilder();

                    break;
                case '\'':
                    if (isMinutesAdded || number.length() > i + 1 && number.charAt(i + 1) == '\'') {
                        // Добавляюся секунды
                        if (isMinus)
                            mNumber = mNumber.subtract(new BigDecimal(currentNumber.toString()));
                        else
                            mNumber = mNumber.add(new BigDecimal(currentNumber.toString()));
                        currentNumber = new StringBuilder();
                        i++;
                    } else {
                        // Добавляются минуты
                        currentNumber.append("00");
                        if (isMinus)
                            mNumber = mNumber.subtract(new BigDecimal(currentNumber.toString()));
                        else
                            mNumber = mNumber.add(new BigDecimal(currentNumber.toString()));
                        currentNumber = new StringBuilder();
                    }
                    break;
                case ',':
                    isMinutesAdded = true;
                    currentNumber.append("00");
                    if (isMinus)
                        mNumber = mNumber.subtract(new BigDecimal(currentNumber.toString()));
                    else
                        mNumber = mNumber.add(new BigDecimal(currentNumber.toString()));
                    currentNumber = new StringBuilder();

                    break;
                case '-':
                    isMinus = true;
                    break;
                default:
                    currentNumber.append(number.charAt(i));
            }
        }
    }

    public void setNumber(BigDecimal number) {
        mNumber = number;
    }

    public BigDecimal getNumber() {
        return mNumber;
    }

    public String getNumberAsText() {
        return mNumber.toString();
    }

    @Override
    public String toString() {
        return getDegrees().toString() + '°' + getMinutes().toString() + '\'' + getSeconds() + "\'\'";
    }
}

