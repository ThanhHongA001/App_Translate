package com.example.app_tt2.data.remote.auth;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class AuthRequestFactory {

    private AuthRequestFactory() {
    }

    public static JsonObject login(String email, String password) {
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        return body;
    }

    public static JsonObject refreshToken(String refreshToken) {
        JsonObject body = new JsonObject();
        body.addProperty("refreshToken", refreshToken);
        return body;
    }

    public static JsonObject logout(String refreshToken) {
        return refreshToken(refreshToken);
    }

    public static JsonObject role(String code, String name, String description) {
        JsonObject body = new JsonObject();
        body.addProperty("code", code);
        body.addProperty("name", name);
        if (description != null) {
            body.addProperty("description", description);
        }
        return body;
    }

    public static JsonObject permissionIds(String... permissionIds) {
        JsonObject body = new JsonObject();
        JsonArray ids = new JsonArray();
        if (permissionIds != null) {
            for (String permissionId : permissionIds) {
                ids.add(permissionId);
            }
        }
        body.add("permissionIds", ids);
        return body;
    }

    public static JsonObject tenantOwner(String fullName, String email, String password, String tenantName) {
        JsonObject owner = new JsonObject();
        owner.addProperty("fullName", fullName);
        owner.addProperty("email", email);
        owner.addProperty("password", password);

        JsonObject tenant = new JsonObject();
        tenant.addProperty("name", tenantName);

        JsonObject body = new JsonObject();
        body.add("owner", owner);
        body.add("tenant", tenant);
        return body;
    }
}
