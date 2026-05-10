package com.example.app_tt2.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateResponseDto {

    @SerializedName("responseData")
    private ResponseDataDto responseData;

    @SerializedName("quotaFinished")
    private boolean quotaFinished;

    @SerializedName("mtLangSupported")
    private String mtLangSupported;

    @SerializedName("responseDetails")
    private String responseDetails;

    @SerializedName("responseStatus")
    private int responseStatus;

    @SerializedName("responderId")
    private String responderId;

    @SerializedName("matches")
    private List<MatchDto> matches;

    public ResponseDataDto getResponseData() {
        return responseData;
    }

    public boolean isQuotaFinished() {
        return quotaFinished;
    }

    public String getMtLangSupported() {
        return mtLangSupported;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getResponderId() {
        return responderId;
    }

    public List<MatchDto> getMatches() {
        return matches;
    }
}