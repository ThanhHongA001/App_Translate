package com.example.app_tt2.data.repository;

import com.example.app_tt2.data.remote.RetrofitClient;
import com.example.app_tt2.data.remote.api.TranslateApiService;
import com.example.app_tt2.data.remote.dto.ResponseDataDto;
import com.example.app_tt2.data.remote.dto.TranslateResponseDto;
import com.example.app_tt2.domain.model.TranslationResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class TranslateRepositoryImpl implements TranslateRepository {

    private final TranslateApiService apiService;

    public TranslateRepositoryImpl() {
        this.apiService = RetrofitClient.getTranslateApiService();
    }

    @Override
    public TranslationResult translateText(
            String text,
            String sourceLang,
            String targetLang
    ) throws Exception {

        String langPair = sourceLang + "|" + targetLang;

        Call<TranslateResponseDto> call = apiService.translate(text, langPair);
        Response<TranslateResponseDto> response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException("Lỗi HTTP: " + response.code());
        }

        TranslateResponseDto body = response.body();

        if (body == null) {
            throw new IOException("Phản hồi rỗng từ máy chủ.");
        }

        if (body.getResponseStatus() != 200) {
            throw new IOException("Dịch thất bại: " + body.getResponseDetails());
        }

        ResponseDataDto responseData = body.getResponseData();

        if (responseData == null || responseData.getTranslatedText() == null) {
            throw new IOException("Không nhận được kết quả dịch.");
        }

        return new TranslationResult(
                text,
                responseData.getTranslatedText(),
                sourceLang,
                targetLang
        );
    }
}