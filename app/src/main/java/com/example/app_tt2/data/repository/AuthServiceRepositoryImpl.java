package com.example.app_tt2.data.repository;

import com.example.app_tt2.data.remote.auth.AuthApiService;
import com.example.app_tt2.data.remote.auth.AuthRequestFactory;
import com.example.app_tt2.data.remote.auth.AuthRetrofitClient;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class AuthServiceRepositoryImpl implements AuthServiceRepository {

    private final AuthApiService apiService;

    public AuthServiceRepositoryImpl() {
        this(AuthRetrofitClient.getAuthApiService());
    }

    public AuthServiceRepositoryImpl(AuthApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Call<JsonObject> health() {
        return apiService.healthController_getHealth();
    }

    @Override
    public Call<JsonObject> login(String email, String password) {
        return apiService.authController_login(AuthRequestFactory.login(email, password));
    }

    @Override
    public Call<JsonObject> refresh(String refreshToken) {
        return apiService.authController_refresh(AuthRequestFactory.refreshToken(refreshToken));
    }

    @Override
    public Call<JsonObject> logout(String refreshToken) {
        return apiService.authController_logout(AuthRequestFactory.logout(refreshToken));
    }

    @Override
    public Call<JsonObject> me() {
        return apiService.authController_me();
    }

    @Override
    public Call<JsonObject> listRoles() {
        return apiService.rolesController_listRoles();
    }

    @Override
    public Call<JsonObject> createRole(String code, String name, String description) {
        return apiService.rolesController_createRole(AuthRequestFactory.role(code, name, description));
    }

    @Override
    public Call<JsonObject> listPermissions(String keyword, String path, String method) {
        return apiService.permissionsController_listPermissions(keyword, path, method);
    }

    @Override
    public Call<JsonObject> listHotelUsers(String tenantId,
                                           String keyword,
                                           String status,
                                           Integer limit,
                                           Integer page) {
        return apiService.hotelUsersController_listHotelUsers(
                tenantId,
                keyword,
                status,
                limit,
                page,
                tenantId
        );
    }

    @Override
    public Call<JsonObject> getHotelUser(String tenantId, String userId) {
        return apiService.hotelUsersController_getHotelUser(userId, tenantId, tenantId);
    }
}
