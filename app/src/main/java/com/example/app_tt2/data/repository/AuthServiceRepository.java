package com.example.app_tt2.data.repository;

import com.google.gson.JsonObject;

import retrofit2.Call;

public interface AuthServiceRepository {

    Call<JsonObject> health();

    Call<JsonObject> login(String email, String password);

    Call<JsonObject> refresh(String refreshToken);

    Call<JsonObject> logout(String refreshToken);

    Call<JsonObject> me();

    Call<JsonObject> listRoles();

    Call<JsonObject> createRole(String code, String name, String description);

    Call<JsonObject> listPermissions(String keyword, String path, String method);

    Call<JsonObject> listHotelUsers(String tenantId,
                                    String keyword,
                                    String status,
                                    Integer limit,
                                    Integer page);

    Call<JsonObject> getHotelUser(String tenantId, String userId);
}
