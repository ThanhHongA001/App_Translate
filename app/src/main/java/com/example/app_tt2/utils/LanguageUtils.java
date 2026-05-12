package com.example.app_tt2.utils;

public final class LanguageUtils {

    private LanguageUtils() {
    }

    public static String getLanguagePair(String sourceLang, String targetLang) {
        if (TextUtils.isEmpty(sourceLang)) {
            sourceLang = Constants.LANG_EN;
        }

        if (TextUtils.isEmpty(targetLang)) {
            targetLang = Constants.LANG_VI;
        }

        return sourceLang + "|" + targetLang;
    }

    public static String getLanguageName(String code) {
        if (Constants.LANG_EN.equalsIgnoreCase(code)) {
            return "Tiếng Anh";
        }

        if (Constants.LANG_VI.equalsIgnoreCase(code)) {
            return "Tiếng Việt";
        }

        return "Không xác định";
    }

    public static String swapLanguage(String code) {
        if (Constants.LANG_EN.equalsIgnoreCase(code)) {
            return Constants.LANG_VI;
        }

        return Constants.LANG_EN;
    }
}