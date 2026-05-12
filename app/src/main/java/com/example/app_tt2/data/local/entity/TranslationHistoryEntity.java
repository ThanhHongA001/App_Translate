package com.example.app_tt2.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "translation_history")
public class TranslationHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sourceText;
    private String translatedText;
    private String sourceLang;
    private String targetLang;
    private long createdAt;
    private boolean favorite;

    public TranslationHistoryEntity(String sourceText, String translatedText,
                                    String sourceLang, String targetLang,
                                    long createdAt, boolean favorite) {
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.createdAt = createdAt;
        this.favorite = favorite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSourceText() { return sourceText; }
    public String getTranslatedText() { return translatedText; }
    public String getSourceLang() { return sourceLang; }
    public String getTargetLang() { return targetLang; }
    public long getCreatedAt() { return createdAt; }
    public boolean isFavorite() { return favorite; }

    public void setSourceText(String sourceText) { this.sourceText = sourceText; }
    public void setTranslatedText(String translatedText) { this.translatedText = translatedText; }
    public void setSourceLang(String sourceLang) { this.sourceLang = sourceLang; }
    public void setTargetLang(String targetLang) { this.targetLang = targetLang; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
}