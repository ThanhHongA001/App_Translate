package com.example.app_tt2.domain.evaluator;

public class TranslationSpeedEstimator {

    public int estimate(long durationMillis) {
        if (durationMillis <= 0) {
            return 100;
        }

        if (durationMillis <= 500) return 100;
        if (durationMillis <= 1000) return 95;
        if (durationMillis <= 2000) return 85;
        if (durationMillis <= 3500) return 75;
        if (durationMillis <= 5000) return 60;
        if (durationMillis <= 8000) return 45;

        return 30;
    }
}