package com.example.app_tt2.ui.translate;

public class LanguageOption {

    private final String name;
    private final String code;

    public LanguageOption(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name;
    }

    public static LanguageOption vietnamese() {
        return new LanguageOption("Tiếng Việt", "vi");
    }

    public static LanguageOption english() {
        return new LanguageOption("Tiếng Anh", "en");
    }
}