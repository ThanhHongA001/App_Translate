package com.example.app_tt2.domain.evaluator;

import com.example.app_tt2.utils.TextUtils;

public class StyleEstimator {

    public int estimate(String translatedText) {
        if (TextUtils.isEmpty(translatedText)) {
            return 0;
        }

        int score = 100;
        String text = translatedText.trim();

        if (text.length() < 2) {
            score -= 40;
        }

        if (hasRepeatedCharacters(text)) {
            score -= 20;
        }

        if (hasTooManySpaces(text)) {
            score -= 15;
        }

        if (!startsWithLetterOrDigit(text)) {
            score -= 10;
        }

        return clamp(score);
    }

    private boolean hasRepeatedCharacters(String text) {
        return text.matches(".*(.)\\1\\1\\1.*");
    }

    private boolean hasTooManySpaces(String text) {
        return text.contains("   ");
    }

    private boolean startsWithLetterOrDigit(String text) {
        if (text.isEmpty()) return false;
        return Character.isLetterOrDigit(text.charAt(0));
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }
}