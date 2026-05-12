package com.example.app_tt2.domain.evaluator;

import com.example.app_tt2.utils.TextUtils;

public class AccuracyEstimator {

    public int estimate(String sourceText, String translatedText) {
        if (TextUtils.isEmpty(sourceText) || TextUtils.isEmpty(translatedText)) {
            return 0;
        }

        int sourceLength = sourceText.trim().length();
        int translatedLength = translatedText.trim().length();

        if (translatedLength == 0) return 0;

        double ratio = (double) translatedLength / sourceLength;

        int score = 100;

        if (ratio < 0.25 || ratio > 4.0) {
            score -= 45;
        } else if (ratio < 0.45 || ratio > 2.5) {
            score -= 25;
        } else if (ratio < 0.65 || ratio > 1.8) {
            score -= 10;
        }

        if (translatedText.equalsIgnoreCase(sourceText)) {
            score -= 35;
        }

        if (translatedText.contains("null") || translatedText.contains("undefined")) {
            score -= 40;
        }

        return clamp(score);
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }
}