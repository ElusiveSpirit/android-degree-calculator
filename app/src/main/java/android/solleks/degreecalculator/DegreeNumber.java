package android.solleks.degreecalculator;

import java.math.BigDecimal;

/**
 * Created by Константин on 13.12.2015.
 *
 */
public class DegreeNumber {

    private static boolean toStringTemplate = true;

    private BigDecimal mNumber;
    private BigDecimal mDegrees;
    private BigDecimal mMinutes;
    private BigDecimal mSeconds;

    private int mFraction;

    DegreeNumber() {
        mDegrees = new BigDecimal("0");
        mMinutes = new BigDecimal("0");
        mSeconds = new BigDecimal("0");
        mFraction = 3;
    }

    DegreeNumber(String number) {
        setNumber(number);
    }

    DegreeNumber(BigDecimal degrees, BigDecimal minutes, BigDecimal seconds) {
        mDegrees = degrees;
        mMinutes = minutes;
        mSeconds = seconds;

        if (hasFraction(mDegrees)) mFraction = 0;
        else if (hasFraction(mMinutes)) mFraction = 1;
        else if (hasFraction(mSeconds)) mFraction = 2;
        else mFraction = 3;
    }

    public DegreeNumber add(DegreeNumber addedNumber) {
        DegreeNumber result;
        if (mSeconds.compareTo(new BigDecimal("0")) != 0 || addedNumber.getSeconds().compareTo(new BigDecimal("0")) != 0) {
            // Если секунды не равняются нулю, то приводим к ним

            BigDecimal firstNumber = mDegrees
                    .multiply(new BigDecimal("3600"))
                    .add(mMinutes.multiply(new BigDecimal("60")))
                    .add(mSeconds);
            BigDecimal secondNumber = addedNumber.getDegrees()
                    .multiply(new BigDecimal("3600"))
                    .add(addedNumber.getSignedMinutes().multiply(new BigDecimal("60")))
                    .add(addedNumber.getSignedSeconds());

            BigDecimal resultNumber = firstNumber.add(secondNumber);

            BigDecimal seconds = resultNumber.remainder(new BigDecimal("60"));
            resultNumber = resultNumber.divide(new BigDecimal("60"), 0, BigDecimal.ROUND_DOWN);
            BigDecimal minutes = resultNumber.remainder(new BigDecimal("60"));
            BigDecimal degrees = resultNumber.divide(new BigDecimal("60"), 0, BigDecimal.ROUND_DOWN);

            result = new DegreeNumber(degrees, minutes, seconds);
        } else if (mMinutes.compareTo(new BigDecimal("0")) != 0 || addedNumber.getMinutes().compareTo(new BigDecimal("0")) != 0) {
            // Если минуты не равняются нулю, то приводим к ним
            BigDecimal firstNumber = mDegrees
                    .multiply(new BigDecimal("60"))
                    .add(mMinutes);
            BigDecimal secondNumber = addedNumber.getDegrees()
                    .multiply(new BigDecimal("60"))
                    .add(addedNumber.getSignedMinutes());

            BigDecimal resultNumber = firstNumber.add(secondNumber);

            BigDecimal minutes = resultNumber.remainder(new BigDecimal("60"));
            BigDecimal degrees = resultNumber.divide(new BigDecimal("60"), 0, BigDecimal.ROUND_DOWN);

            result = new DegreeNumber(degrees, minutes, new BigDecimal("0"));
        } else {
            // Оперируем только с градусами
            result = new DegreeNumber(mDegrees.add(addedNumber.getDegrees()), new BigDecimal("0"), new BigDecimal("0"));
        }


        return result;
    }

    public boolean hasFraction(BigDecimal a) {
        return !(a.subtract(a.setScale(0, BigDecimal.ROUND_HALF_EVEN))
                .compareTo(new BigDecimal("0")) == 0);
    }

    public BigDecimal getDegrees() {
        return mDegrees;
    }

    public BigDecimal getMinutes() {
        return mMinutes.abs();
    }

    public BigDecimal getSeconds() {
        return mSeconds.abs();
    }

    public BigDecimal getSignedSeconds() {
        return mSeconds;
    }

    public BigDecimal getSignedMinutes() {
        return mMinutes;
    }


    public void setNumber(String number) {
        mDegrees = new BigDecimal("0");
        mMinutes = new BigDecimal("0");
        mSeconds = new BigDecimal("0");
        mFraction = 3;

        boolean isMinutesAdded = false;
        boolean isMinus = false;
        StringBuilder currentNumber = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            switch (number.charAt(i)) {
                case '°':
                    if (isMinus)
                        mDegrees = mDegrees.subtract(new BigDecimal(currentNumber.toString()));
                    else
                        mDegrees = mDegrees.add(new BigDecimal(currentNumber.toString()));

                    if (hasFraction(mDegrees)) mFraction = 0;

                    currentNumber = new StringBuilder();

                    break;
                case '\'':
                    if (!isMinutesAdded && i != number.length() - 2 && number.charAt(i - 1) != '\'') {
                        // Добавляются минуты
                        isMinutesAdded = true;
                        if (isMinus)
                            mMinutes = mMinutes.subtract(new BigDecimal(currentNumber.toString()));
                        else
                            mMinutes = mMinutes.add(new BigDecimal(currentNumber.toString()));

                        if (hasFraction(mMinutes)) mFraction = 1;

                        currentNumber = new StringBuilder();
                    } else if (number.charAt(i - 1) == '\'') {
                        // Добавляются секунды
                        if (isMinus)
                            mSeconds = mSeconds.subtract(new BigDecimal(currentNumber.toString()));
                        else
                            mSeconds = mSeconds.add(new BigDecimal(currentNumber.toString()));

                        if (hasFraction(mSeconds)) mFraction = 2;

                        currentNumber = new StringBuilder();
                    }
                    break;
                case ',':
                    currentNumber.append('.');
                    break;
                case '-':
                    isMinus = true;
                    break;
                default:
                    currentNumber.append(number.charAt(i));
            }
        }
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
        StringBuilder builder = new StringBuilder();
        if (mDegrees.compareTo(new BigDecimal("0")) != 0 || toStringTemplate) {
            builder.append(getDegrees());
            builder.append('°');
        }
        if (mFraction >= 1 &&
                (mMinutes.compareTo(new BigDecimal("0")) != 0 ||
                (mSeconds.compareTo(new BigDecimal("0")) != 0 && builder.length() > 0) ||
                toStringTemplate)) {
            if (builder.length() > 0) {
                builder.append(getMinutes());
            } else {
                builder.append(getSignedMinutes());
            }
            builder.append('\'');
        }
        if (mFraction >= 2 &&
                (mSeconds.compareTo(new BigDecimal("0")) != 0 ||
                toStringTemplate)) {
            if (builder.length() > 0) {
                builder.append(getSeconds());
            } else {
                builder.append(getSignedSeconds());
            }
            builder.append("''");
        }
        for (int i = 0; i < builder.length(); i++)
            if (builder.charAt(i) == '.'){
                builder.setCharAt(i, ',');
            }
        return builder.toString();
    }


    public static void setToStringTemplate(boolean toStringTemplate) {
        DegreeNumber.toStringTemplate = toStringTemplate;
    }

    public static boolean getToStringTemplate() {
        return toStringTemplate;
    }
}

