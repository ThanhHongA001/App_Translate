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
    private String quality;

    @SerializedName("match")
    private String match;

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

    public String getQuality() {
        return quality;
    }

    public String getMatch() {
        return match;
    }

    public double getMatchScore() {
        if (match == null) {
            return 0.0;
        }

        try {
            double value = Double.parseDouble(match.trim());
            return value > 1.0 ? value / 100.0 : value;
        } catch (NumberFormatException exception) {
            return 0.0;
        }
    }
}
