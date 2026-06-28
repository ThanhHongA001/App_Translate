package com.example.app_tt2.data.remote.auth;

public final class AuthTokenStore {

    private static String accessToken;
    private static String refreshToken;

    private AuthTokenStore() {
    }

    public static void saveTokens(String newAccessToken, String newRefreshToken) {
        accessToken = newAccessToken;
        refreshToken = newRefreshToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void clear() {
        accessToken = null;
        refreshToken = null;
    }
}
