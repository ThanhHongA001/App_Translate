# Auth / Hotel API integration

API tu `openapi.yaml` da duoc them vao project tai package:

```text
app/src/main/java/com/example/app_tt2/data/remote/auth
```

## File chinh

- `AuthApiService.java`: Retrofit interface, gom 66 endpoint tu OpenAPI.
- `AuthRetrofitClient.java`: Retrofit client rieng cho auth/hotel API.
- `AuthRequestFactory.java`: helper tao `JsonObject` body.
- `AuthTokenStore.java`: luu access token/refresh token trong runtime de interceptor tu gan header Authorization.
- `AuthServiceRepository.java`: repository interface de app goi API de hon.
- `AuthServiceRepositoryImpl.java`: implementation goi `AuthApiService`.

## Doi base URL

Mac dinh:

```java
http://10.0.2.2:3000/
```

Neu backend khac port/domain:

```java
AuthRetrofitClient.setBaseUrl("http://10.0.2.2:8080/");
```

## Vi du login

```java
AuthServiceRepository repository = new AuthServiceRepositoryImpl();

repository.login("admin@example.com", "password").enqueue(new Callback<JsonObject>() {
    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if (!response.isSuccessful() || response.body() == null) {
            return;
        }

        JsonObject data = response.body().getAsJsonObject("data");
        String accessToken = data.get("accessToken").getAsString();
        String refreshToken = data.get("refreshToken").getAsString();
        AuthTokenStore.saveTokens(accessToken, refreshToken);
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        // Xu ly loi ket noi
    }
});
```

## Vi du goi API can token

Sau khi `AuthTokenStore.saveTokens(...)`, client tu gan header Authorization.

## API tenant/hotel-users

Cac API `hotel-users` can `x-tenant-id`. Repository wrapper da co san:

```java
repository.listHotelUsers(tenantId, null, null, 20, 1).enqueue(...);
repository.getHotelUser(tenantId, userId).enqueue(...);
```
