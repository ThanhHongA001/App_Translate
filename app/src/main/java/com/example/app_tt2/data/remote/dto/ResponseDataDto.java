package com.example.app_tt2.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseDataDto {

    @SerializedName("translatedText")
    private String translatedText;

    @SerializedName("match")
    private double match;

    public String getTranslatedText() {
        return translatedText;
    }

    public double getMatch() {
        return match;
    }
}