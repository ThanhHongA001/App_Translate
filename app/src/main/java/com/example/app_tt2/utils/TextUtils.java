package com.example.app_tt2.utils;

public final class TextUtils {

    private TextUtils() {
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String safe(String text) {
        return text == null ? "" : text.trim();
    }

    public static String normalizeSpaces(String text) {
        if (text == null) {
            return "";
        }

        return text.trim().replaceAll("\\s+", " ");
    }

    public static boolean isSameText(String a, String b) {
        return safe(a).equalsIgnoreCase(safe(b));
    }
}