package com.example.app_tt2.domain.model;

public class TranslationHistory {

    private final int id;
    private final String sourceText;
    private final String translatedText;
    private final String sourceLang;
    private final String targetLang;
    private final long createdAt;
    private final boolean favorite;

    public TranslationHistory(int id, String sourceText, String translatedText,
                              String sourceLang, String targetLang,
                              long createdAt, boolean favorite) {
        this.id = id;
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.createdAt = createdAt;
        this.favorite = favorite;
    }

    public int getId() { return id; }
    public String getSourceText() { return sourceText; }
    public String getTranslatedText() { return translatedText; }
    public String getSourceLang() { return sourceLang; }
    public String getTargetLang() { return targetLang; }
    public long getCreatedAt() { return createdAt; }
    public boolean isFavorite() { return favorite; }
}