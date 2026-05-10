package com.example.app_tt2.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class MatchDto {

    @SerializedName("id")
    private String id;

    @SerializedName("segment")
    private String segment;

    @SerializedName("translation")
    private String translation;

    @SerializedName("source")
    private String source;

    @SerializedName("target")
    private String target;

    @SerializedName("quality")
    private int quality;

    @SerializedName("match")
    private double match;

    public String getId() {
        return id;
    }

    public String getSegment() {
        return segment;
    }

    public String getTranslation() {
        return translation;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public int getQuality() {
        return quality;
    }

    public double getMatch() {
        return match;
    }
}