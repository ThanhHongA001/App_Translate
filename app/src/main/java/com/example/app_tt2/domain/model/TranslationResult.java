package com.example.app_tt2.domain.model;

public class TranslationResult {

    private String sourceText;
    private String translatedText;
    private String sourceLanguage;
    private String targetLanguage;
    private long createdAt;
    private long durationMillis;

    public TranslationResult() {
        this.createdAt = System.currentTimeMillis();
    }

    public TranslationResult(String sourceText,
                             String translatedText,
                             String sourceLanguage,
                             String targetLanguage) {
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.createdAt = System.currentTimeMillis();
    }

    public TranslationResult(String sourceText,
                             String translatedText,
                             String sourceLanguage,
                             String targetLanguage,
                             long createdAt) {
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.createdAt = createdAt;
    }

    public TranslationResult(String sourceText,
                             String translatedText,
                             String sourceLanguage,
                             String targetLanguage,
                             long createdAt,
                             long durationMillis) {
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.createdAt = createdAt;
        this.durationMillis = durationMillis;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getSourceLang() {
        return sourceLanguage;
    }

    public void setSourceLang(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLang() {
        return targetLanguage;
    }

    public void setTargetLang(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }
}




