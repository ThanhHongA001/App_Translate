package com.example.app_tt2.data.remote.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateResponseDto {

    @SerializedName("responseData")
    private ResponseDataDto responseData;

    @SerializedName("quotaFinished")
    private Boolean quotaFinished;

    @SerializedName("responseDetails")
    private String responseDetails;

    @SerializedName("responseStatus")
    private Integer responseStatus;

    @SerializedName("responderId")
    private String responderId;

    @SerializedName("exception_code")
    private String exceptionCode;

    @SerializedName("matches")
    private List<MatchDto> matches;

    public ResponseDataDto getResponseData() {
        return responseData;
    }

    public Boolean getQuotaFinished() {
        return quotaFinished;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public String getResponderId() {
        return responderId;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public List<MatchDto> getMatches() {
        return matches;
    }

    public boolean isSuccess() {
        return responseStatus == null || responseStatus == 200;
    }
}