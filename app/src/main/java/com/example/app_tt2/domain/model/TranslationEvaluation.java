package com.example.app_tt2.domain.model;

public class TranslationEvaluation {

    private final int accuracyScore;
    private final int styleScore;
    private final int speedScore;
    private final int overallScore;
    private final String comment;
    private final long createdAt;

    public TranslationEvaluation(
            int accuracyScore,
            int styleScore,
            int speedScore,
            int overallScore,
            String comment,
            long createdAt
    ) {
        this.accuracyScore = accuracyScore;
        this.styleScore = styleScore;
        this.speedScore = speedScore;
        this.overallScore = overallScore;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getAccuracyScore() {
        return accuracyScore;
    }

    public int getStyleScore() {
        return styleScore;
    }

    public int getSpeedScore() {
        return speedScore;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public String getComment() {
        return comment;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}