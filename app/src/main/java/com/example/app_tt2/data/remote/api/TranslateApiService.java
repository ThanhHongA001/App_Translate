package com.example.app_tt2.data.remote.api;

import com.example.app_tt2.data.remote.dto.TranslateResponseDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateApiService {

    @GET("get")
    Call<TranslateResponseDto> translate(
            @Query("q") String text,
            @Query("langpair") String langPair,
            @Query("mt") int machineTranslation
    );
}
