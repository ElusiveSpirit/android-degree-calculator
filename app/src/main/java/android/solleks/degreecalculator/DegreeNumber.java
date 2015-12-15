package android.solleks.degreecalculator;

import java.math.BigDecimal;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class DegreeNumber {

    private static int toStringTemplate = 0;

    /**
     * Первые два порядка - секунды
     * Вторые два порядка - минуты
     * Остальные - градусы
     */
    private BigDecimal mNumber;

    DegreeNumber() {
        mNumber = new BigDecimal("0");
    }

    DegreeNumber(String number) {
        setNumber(number);
    }

    DegreeNumber(BigDecimal number) {
        mNumber = number;
    }

    public DegreeNumber add(DegreeNumber addedNumber) {
        DegreeNumber result = new DegreeNumber(this.getNumber().add(addedNumber.getNumber()));


        if (result.getDegrees().compareTo(new BigDecimal("0")) == 0 ||
                (this.getNumber().signum() + addedNumber.getNumber().signum() == 0 &&
                this.getDegrees().compareTo(new BigDecimal("0")) == 0 &&
                addedNumber.getDegrees().compareTo(new BigDecimal("0")) == 0)) {
                // Тёрки вокруг нуля
                if (result.getSignedSeconds().compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Секунды больше или равны 60
                     * Оба числа положительные
                     * ***
                     * Увеличить минуты на 1
                     * уменьшить секунды на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("40"));
                    result.setNumber(temp);
                }
                if (result.getSignedMinutes().compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Минуты больше или равны 60
                     * Оба числа положительные
                     * ***
                     * Увеличить градусы на 1
                     * уменьшить минуты на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("4000"));
                    result.setNumber(temp);
                }

                if (result.getNumber().signum() == -1 &&
                        result.getSeconds().compareTo(new BigDecimal("60")) != -1 ) {
                    /**
                     * Секунды больше или равны 60
                     * Оба числа отрицательные
                     * ***
                     * Уменьшить минуты на 1
                     * Увеличить секунды на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-40"));
                    result.setNumber(temp);
                }
                if (result.getNumber().signum() == -1 &&
                        result.getMinutes().compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Минуты больше или равны 60
                     * Оба числа отрицательные
                     * ***
                     * Уменьшить градусы на 1
                     * Увеличить минуты на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-4000"));
                    result.setNumber(temp);
                }

        } else {

            if ((this.getNumber().signum() == 1 &&
                    addedNumber.getNumber().signum() == 1)) {
                // Оба числа положительные
                if ((this.getSignedSeconds().add(addedNumber.getSignedSeconds())).compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Секунды больше или равны 60
                     * Оба числа положительные
                     * ***
                     * Увеличить минуты на 1
                     * уменьшить секунды на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("40"));
                    result.setNumber(temp);
                }
                if ((this.getSignedMinutes().add(addedNumber.getSignedMinutes())).compareTo(new BigDecimal("60")) != -1 ||
                        result.getMinutes().compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Минуты больше или равны 60
                     * Оба числа положительные
                     * ***
                     * Увеличить градусы на 1
                     * уменьшить минуты на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("4000"));
                    result.setNumber(temp);
                }

            }
            if (this.getNumber().signum() == -1 &&
                    addedNumber.getNumber().signum() == -1) {
                // Оба числа отрицательные
                if ((this.getSeconds().add(addedNumber.getSeconds())).compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Секунды больше или равны 60
                     * Оба числа отрицательные
                     * ***
                     * Уменьшить минуты на 1
                     * Увеличить секунды на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-40"));
                    result.setNumber(temp);
                }
                if ((this.getMinutes().add(addedNumber.getMinutes())).compareTo(new BigDecimal("60")) != -1 ||
                        result.getMinutes().compareTo(new BigDecimal("60")) != -1) {
                    /**
                     * Минуты больше или равны 60
                     * Оба числа отрицательные
                     * ***
                     * Уменьшить градусы на 1
                     * Увеличить минуты на 60
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-4000"));
                    result.setNumber(temp);
                }
            }

            if (this.getNumber().signum() + addedNumber.getNumber().signum() == 0) {
                if ((this.getSignedSeconds().add(addedNumber.getSignedSeconds())).signum() == -1) {
                    // TODO Подумать ещё раз
                    /**
                     * Секунды меньше 00
                     * ***
                     * уменьшить секунды на
                     * уменьшить минуты на 40
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-40"));
                    result.setNumber(temp);
                }
                if ((this.getSignedMinutes().add(addedNumber.getSignedMinutes())).signum() == -1) {
                    // TODO Подумать ещё раз
                    /**
                     * Секунды меньше 00
                     * ***
                     * уменьшить секунды на
                     * уменьшить минуты на 40
                     * */
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-4000"));
                    result.setNumber(temp);
                }
                if (result.getSignedMinutes().compareTo(new BigDecimal("60")) != -1) {
                    BigDecimal temp = result.getNumber();
                    temp = temp.add(new BigDecimal("-4000"));
                    result.setNumber(temp);
                }
            }
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

    public BigDecimal getSignedSeconds() {
        return mNumber.remainder(new BigDecimal("100"));
    }

    public BigDecimal getSignedMinutes() {
        BigDecimal minutes = mNumber.divideToIntegralValue(new BigDecimal("100"));
        return minutes.remainder(new BigDecimal("100"));
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

    public String getRadians() {
        BigDecimal resultD = getDegrees()
                .multiply(new BigDecimal(String.valueOf(Math.PI)))
                .divide(new BigDecimal("180"), 6, BigDecimal.ROUND_HALF_UP);
        BigDecimal resultM = getSignedMinutes()
                .multiply(new BigDecimal(String.valueOf(Math.PI)))
                .divide(new BigDecimal("10800"), 6, BigDecimal.ROUND_HALF_UP);
        BigDecimal resultS = getSignedSeconds()
                .multiply(new BigDecimal(String.valueOf(Math.PI)))
                .divide(new BigDecimal("648000"), 6, BigDecimal.ROUND_HALF_UP);
        BigDecimal result = resultD.add(resultM).add(resultS);
        return result.toString();
    }

    @Override
    public String toString() {
        if (toStringTemplate == 0) {
            if (mNumber.signum() == -1 && getDegrees().compareTo(new BigDecimal("0")) == 0)
                return '-' + getDegrees().toString() + '°' + getMinutes().toString() + '\'' + getSeconds() + "\'\'";
            else
                return getDegrees().toString() + '°' + getMinutes().toString() + '\'' + getSeconds() + "\'\'";
        } else {
            if (mNumber.signum() == -1 && getDegrees().compareTo(new BigDecimal("0")) == 0)
                return '-' + getDegrees().toString() + '°' + getMinutes().toString() + ',' + getSeconds() + "\'";
            else
                return getDegrees().toString() + '°' + getMinutes().toString() + ',' + getSeconds() + "\'";
        }
    }


    public static void setToStringTemplate(int toStringTemplate) {
        if (toStringTemplate < 2)
            DegreeNumber.toStringTemplate = toStringTemplate;
    }

    public static int getToStringTemplate() {
        return toStringTemplate;
    }
}

