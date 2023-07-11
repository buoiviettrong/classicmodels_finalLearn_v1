package com.nixagh.classicmodels.utils;

import java.text.DecimalFormat;

public class RoundUtil {
    DecimalFormat dfZero = new DecimalFormat("0.00");

    public static Double convert(Double value, Integer decimal) {
        String format = "%." + decimal + "f";
        return Double.parseDouble(String.format(format, value));
    }
}
