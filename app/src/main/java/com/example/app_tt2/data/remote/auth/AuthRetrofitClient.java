package com.example.app_tt2.data.remote.auth;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class AuthRetrofitClient {

    private static final String DEFAULT_BASE_URL = "http://10.0.2.2:3000/";
    private static Retrofit retrofit;
    private static String baseUrl = DEFAULT_BASE_URL;

    private AuthRetrofitClient() {
    }

    public static void setBaseUrl(String newBaseUrl) {
        if (newBaseUrl == null || newBaseUrl.trim().isEmpty()) {
            return;
        }
        String normalized = newBaseUrl.endsWith("/") ? newBaseUrl : newBaseUrl + "/";
        if (!normalized.equals(baseUrl)) {
            baseUrl = normalized;
            retrofit = null;
        }
    }

    public static AuthApiService getAuthApiService() {
        return getRetrofit().create(AuthApiService.class);
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String accessToken = AuthTokenStore.getAccessToken();
                        if (accessToken == null || accessToken.trim().isEmpty()) {
                            return chain.proceed(chain.request());
                        }

                        return chain.proceed(
                                chain.request()
                                        .newBuilder()
                                        .addHeader("Authorization", "Bearer " + accessToken)
                                        .build()
                        );
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
