package com.github.leloxo.simplecalculator.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalFormatter {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("#.#####", symbols);

    public static String dfFormat(double number) {
        return df.format(number);
    }
}