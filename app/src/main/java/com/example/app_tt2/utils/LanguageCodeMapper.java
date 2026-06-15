package com.example.app_tt2.utils;

import java.util.Locale;

public final class LanguageCodeMapper {

    private LanguageCodeMapper() {
    }

    public static Locale toLocale(String languageCode) {
        if (languageCode == null || languageCode.trim().isEmpty()) {
            return Locale.US;
        }

        String code = normalize(languageCode);

        switch (code) {
            case "vi":
            case "vi-vn":
            case "vietnamese":
            case "tieng-viet":
            case "tiếng-việt":
                return new Locale("vi", "VN");

            case "en":
            case "en-us":
            case "english":
            case "tieng-anh":
            case "tiếng-anh":
                return Locale.US;

            case "en-gb":
                return Locale.UK;

            case "fr":
            case "fr-fr":
            case "french":
                return Locale.FRANCE;

            case "de":
            case "de-de":
            case "german":
                return Locale.GERMANY;

            case "ja":
            case "ja-jp":
            case "japanese":
                return Locale.JAPAN;

            case "ko":
            case "ko-kr":
            case "korean":
                return Locale.KOREA;

            case "zh":
            case "zh-cn":
            case "chinese":
                return Locale.SIMPLIFIED_CHINESE;

            case "zh-tw":
                return Locale.TRADITIONAL_CHINESE;

            case "auto":
            case "auto-detect":
            case "detect":
                return Locale.US;

            default:
                return parseLocale(code);
        }
    }

    public static String toLanguageTag(String languageCode) {
        return toLocale(languageCode).toLanguageTag();
    }

    public static String toMyMemoryCode(String languageCode) {
        Locale locale = toLocale(languageCode);
        String language = locale.getLanguage();

        if (language == null || language.trim().isEmpty()) {
            return "en";
        }

        if ("vi".equals(language)) {
            return "vi";
        }

        if ("en".equals(language)) {
            return "en";
        }

        if ("fr".equals(language)) {
            return "fr";
        }

        if ("de".equals(language)) {
            return "de";
        }

        if ("ja".equals(language)) {
            return "ja";
        }

        if ("ko".equals(language)) {
            return "ko";
        }

        if ("zh".equals(language)) {
            String country = locale.getCountry();

            if ("TW".equalsIgnoreCase(country)) {
                return "zh-TW";
            }

            return "zh-CN";
        }

        return language;
    }

    private static Locale parseLocale(String code) {
        if (code.contains("-")) {
            String[] parts = code.split("-");

            if (parts.length >= 2) {
                String language = parts[0];
                String country = parts[1].toUpperCase(Locale.US);

                if (!language.trim().isEmpty() && !country.trim().isEmpty()) {
                    return new Locale(language, country);
                }
            }
        }

        Locale locale = Locale.forLanguageTag(code);

        if (locale.getLanguage() == null || locale.getLanguage().trim().isEmpty()) {
            return Locale.US;
        }

        return locale;
    }

    private static String normalize(String languageCode) {
        return languageCode
                .trim()
                .toLowerCase(Locale.US)
                .replace("_", "-")
                .replace(" ", "-");
    }
}