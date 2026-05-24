package com.alumipro.mobile.utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class FormatUtils {
    private static final Locale LOCALE_CO = new Locale("es", "CO");

    private FormatUtils() {
    }

    public static String currency(double value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(LOCALE_CO);
        return format.format(value);
    }

    public static String safe(String value) {
        return value == null || value.trim().isEmpty() ? "Sin dato" : value.trim();
    }

    public static String compactDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Sin fecha";
        }
        try {
            if (value.length() >= 10) {
                String base = value.substring(0, 10);
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy", LOCALE_CO);
                Date date = input.parse(base);
                return output.format(date);
            }
        } catch (Exception ignored) {
        }
        return value;
    }

    public static String compactDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Sin fecha";
        }
        try {
            String normalized = value.replace("T", " ");
            if (normalized.length() >= 19) {
                normalized = normalized.substring(0, 19);
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm", LOCALE_CO);
            Date date = input.parse(normalized);
            return output.format(date);
        } catch (Exception ignored) {
        }
        return value;
    }
}
