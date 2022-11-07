package ua.ldoin.smartanimals.utils.util.number;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class NumberUtil {

    public static boolean random(double chance) {

        return ThreadLocalRandom.current().nextDouble() <= chance;

    }

    public static float randomInRange(float min, float max) {

        if (min >= max)
            return 0;

        return ((int) (max - min) > 0 ? ThreadLocalRandom.current().nextInt((int) (max - min)) : 0) + min;

    }

    public static float randomInRange(NumberRange range) {

        return randomInRange(range.getLesser(), range.getLarger());

    }

    public static boolean isInRange(NumberRange range, int number) {

        return range.getLesser() <= number && number <= range.getLarger();

    }

    public static float getPercentageOfValue(String percentage, float value) {

        if (percentage.endsWith("%"))
            return getPercentageOfValue(Float.parseFloat(percentage.replace("%", "")), value);

        return getPercentageOfValue(Float.parseFloat(percentage), value);

    }

    public static float getPercentageOfValue(float percentage, float value) {

        return value / 100 * percentage;

    }

    public static float addPercentage(float number, float percentage) {

        return number + getPercentageOfValue(percentage, number);

    }
}