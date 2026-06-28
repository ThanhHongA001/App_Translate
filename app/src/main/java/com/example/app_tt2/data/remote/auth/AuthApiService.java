package com.example.app_tt2.data.remote.auth;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApiService {

    @GET("health")
    Call<JsonObject> healthController_getHealth();

    @POST("auth/login")
    Call<JsonObject> authController_login(@Body JsonObject body);

    @POST("auth/refresh")
    Call<JsonObject> authController_refresh(@Body JsonObject body);

    @POST("auth/logout")
    Call<JsonObject> authController_logout(@Body JsonObject body);

    @GET("auth/me")
    Call<JsonObject> authController_me();

    @POST("roles")
    Call<JsonObject> rolesController_createRole(@Body JsonObject body);

    @GET("roles")
    Call<JsonObject> rolesController_listRoles();

    @GET("roles/by-name/{name}")
    Call<JsonObject> rolesController_getRoleByName(@Path("name") String name);

    @GET("roles/menus")
    Call<JsonObject> rolesController_getRoleMenus();

    @GET("roles/{id}")
    Call<JsonObject> rolesController_getRole(@Path("id") String id);

    @PATCH("roles/{id}")
    Call<JsonObject> rolesController_updateRole(@Path("id") String id, @Body JsonObject body);

    @DELETE("roles/{id}")
    Call<JsonObject> rolesController_deleteRole(@Path("id") String id);

    @POST("roles/{id}/disable")
    Call<JsonObject> rolesController_disableRole(@Path("id") String id);

    @GET("roles/{id}/permissions")
    Call<JsonObject> rolesController_listRolePermissions(@Path("id") String id);

    @PUT("roles/{id}/permissions")
    Call<JsonObject> rolesController_replacePermissions(@Path("id") String id, @Body JsonObject body);

    @GET("roles/me/permission-modules")
    Call<JsonObject> rolesController_listRolePermissionModules();

    @GET("roles/me/permission-modules/{moduleKey}/permissions")
    Call<JsonObject> rolesController_listRolePermissionModulePermissions(@Query("limit") Integer limit, @Query("page") Integer page, @Path("moduleKey") String moduleKey);

    @POST("roles/{roleId}/modules/{moduleKey}/permissions/grant")
    Call<JsonObject> rolesController_grantRolePermissionModulePermissions(@Path("moduleKey") String moduleKey, @Path("roleId") String roleId, @Body JsonObject body);

    @POST("roles/{roleId}/modules/{moduleKey}/permissions/revoke")
    Call<JsonObject> rolesController_revokeRolePermissionModulePermissions(@Path("moduleKey") String moduleKey, @Path("roleId") String roleId, @Body JsonObject body);

    @GET("permissions")
    Call<JsonObject> permissionsController_listPermissions(@Query("q") String q, @Query("path") String path, @Query("method") String method);

    @GET("permissions/{id}")
    Call<JsonObject> permissionsController_getPermission(@Path("id") String id);

    @GET("tenant-owners")
    Call<JsonObject> tenantOwnersController_listTenantOwners(@Query("q") String q, @Query("limit") Integer limit, @Query("page") Integer page, @Query("tenantUserStatus") String tenantUserStatus, @Query("ownerStatus") String ownerStatus, @Query("tenantId") String tenantId);

    @POST("tenant-owners")
    Call<JsonObject> tenantOwnersController_createTenantOwner(@Body JsonObject body);

    @GET("tenant-owners/{id}")
    Call<JsonObject> tenantOwnersController_getTenantOwner(@Path("id") String id);

    @PATCH("tenant-owners/{id}")
    Call<JsonObject> tenantOwnersController_updateTenantOwner(@Path("id") String id, @Body JsonObject body);

    @POST("hotel-users")
    Call<JsonObject> hotelUsersController_createHotelUser(@Header("x-tenant-id") String x_tenant_id, @Body JsonObject body);

    @GET("hotel-users")
    Call<JsonObject> hotelUsersController_listHotelUsers(@Header("x-tenant-id") String x_tenant_id, @Query("q") String q, @Query("status") String status, @Query("limit") Integer limit, @Query("page") Integer page, @Query("tenantId") String tenantId);

    @GET("hotel-users/{id}")
    Call<JsonObject> hotelUsersController_getHotelUser(@Path("id") String id, @Query("tenantId") String tenantId, @Header("x-tenant-id") String x_tenant_id);

    @PATCH("hotel-users/{id}/status")
    Call<JsonObject> hotelUsersController_updateHotelUserStatus(@Path("id") String id, @Query("tenantId") String tenantId, @Header("x-tenant-id") String x_tenant_id, @Body JsonObject body);

    @POST("hotel-users/{id}/roles")
    Call<JsonObject> hotelUsersController_assignHotelUserRoles(@Path("id") String id, @Query("tenantId") String tenantId, @Header("x-tenant-id") String x_tenant_id, @Body JsonObject body);

    @DELETE("hotel-users/{id}/roles/{roleId}")
    Call<JsonObject> hotelUsersController_revokeHotelUserRole(@Path("id") String id, @Path("roleId") String roleId, @Query("tenantId") String tenantId, @Header("x-tenant-id") String x_tenant_id);

    @POST("hotels")
    Call<JsonObject> hotelsController_createHotel(@Header("x-tenant-id") String x_tenant_id, @Body JsonObject body);

    @GET("hotels")
    Call<JsonObject> hotelsController_listHotels(@Header("x-tenant-id") String x_tenant_id, @Query("q") String q, @Query("limit") Integer limit, @Query("page") Integer page, @Query("tenantId") String tenantId);

    @GET("hotels/{hotelId}")
    Call<JsonObject> hotelsController_getHotel(@Path("hotelId") String hotelId);

    @PATCH("hotels/{hotelId}")
    Call<JsonObject> hotelsController_updateHotel(@Path("hotelId") String hotelId, @Body JsonObject body);

    @POST("hotels/{hotelId}/rooms")
    Call<JsonObject> hotelRoomsController_createRoom(@Path("hotelId") String hotelId, @Body JsonObject body);

    @GET("hotels/{hotelId}/rooms")
    Call<JsonObject> hotelRoomsController_listRooms(@Path("hotelId") String hotelId, @Query("q") String q, @Query("limit") Integer limit, @Query("page") Integer page, @Query("status") String status);

    @POST("hotels/{hotelId}/rooms/bulk")
    Call<JsonObject> hotelRoomsController_createRooms(@Path("hotelId") String hotelId, @Body JsonObject body);

    @PATCH("hotels/{hotelId}/rooms/{roomId}")
    Call<JsonObject> hotelRoomsController_updateRoom(@Path("hotelId") String hotelId, @Path("roomId") String roomId, @Body JsonObject body);

    @POST("hotels/{hotelId}/stays")
    Call<JsonObject> hotelRoomsController_createStay(@Path("hotelId") String hotelId, @Body JsonObject body);

    @POST("hotels/{hotelId}/stays/check-in")
    Call<JsonObject> hotelRoomsController_createAndCheckInStay(@Path("hotelId") String hotelId, @Body JsonObject body);

    @POST("hotels/{hotelId}/stays/{stayId}/check-in")
    Call<JsonObject> hotelRoomsController_checkInStay(@Path("hotelId") String hotelId, @Path("stayId") String stayId);

    @POST("hotels/{hotelId}/stays/{stayId}/check-out")
    Call<JsonObject> hotelRoomsController_checkOutStay(@Path("hotelId") String hotelId, @Path("stayId") String stayId, @Body JsonObject body);

    @POST("hotels/{hotelId}/rooms/{roomId}/qr/rotate")
    Call<JsonObject> hotelRoomsController_rotateQr(@Path("hotelId") String hotelId, @Path("roomId") String roomId, @Body JsonObject body);

    @POST("hotels/{hotelId}/rooms/{roomId}/qr/activate")
    Call<JsonObject> hotelRoomsController_activateQr(@Path("hotelId") String hotelId, @Path("roomId") String roomId);

    @POST("hotels/{hotelId}/rooms/{roomId}/qr/deactivate")
    Call<JsonObject> hotelRoomsController_deactivateQr(@Path("hotelId") String hotelId, @Path("roomId") String roomId, @Body JsonObject body);

    @GET("hotels/{hotelId}/service-categories")
    Call<JsonObject> hotelServicesController_listServiceCategories(@Path("hotelId") String hotelId);

    @POST("hotels/{hotelId}/service-categories")
    Call<JsonObject> hotelServicesController_createServiceCategory(@Path("hotelId") String hotelId, @Body JsonObject body);

    @PATCH("hotels/{hotelId}/service-categories/{categoryId}")
    Call<JsonObject> hotelServicesController_updateServiceCategory(@Path("hotelId") String hotelId, @Path("categoryId") String categoryId, @Body JsonObject body);

    @GET("hotels/{hotelId}/service-items")
    Call<JsonObject> hotelServicesController_listServiceItems(@Path("hotelId") String hotelId);

    @POST("hotels/{hotelId}/service-items")
    Call<JsonObject> hotelServicesController_createServiceItem(@Path("hotelId") String hotelId, @Body JsonObject body);

    @PATCH("hotels/{hotelId}/service-items/{itemId}")
    Call<JsonObject> hotelServicesController_updateServiceItem(@Path("hotelId") String hotelId, @Path("itemId") String itemId, @Body JsonObject body);

    @GET("hotels/{hotelId}/requests")
    Call<JsonObject> hotelRequestsController_listRequests(@Path("hotelId") String hotelId, @Query("priority") String priority);

    @GET("hotels/{hotelId}/requests/summary")
    Call<JsonObject> hotelRequestsController_getRequestsSummary(@Path("hotelId") String hotelId);

    @GET("hotels/{hotelId}/requests/{requestId}")
    Call<JsonObject> hotelRequestsController_getRequestDetail(@Path("hotelId") String hotelId, @Path("requestId") String requestId);

    @PATCH("hotels/{hotelId}/requests/{requestId}/status")
    Call<JsonObject> hotelRequestsController_updateRequestStatus(@Path("hotelId") String hotelId, @Path("requestId") String requestId, @Body JsonObject body);

    @PATCH("hotels/{hotelId}/requests/{requestId}/assignment")
    Call<JsonObject> hotelRequestsController_updateRequestAssignment(@Path("hotelId") String hotelId, @Path("requestId") String requestId, @Body JsonObject body);

    @POST("hotels/{hotelId}/requests/{requestId}/events")
    Call<JsonObject> hotelRequestsController_createRequestEvent(@Path("hotelId") String hotelId, @Path("requestId") String requestId, @Body JsonObject body);

    @POST("guest/qr/scan")
    Call<JsonObject> guestOsController_scanQr(@Body JsonObject body);

    @GET("guest/session/me")
    Call<JsonObject> guestOsController_getCurrentSession();

    @GET("guest/services")
    Call<JsonObject> guestOsController_listServices();

    @GET("guest/service-categories/{categoryId}/services")
    Call<JsonObject> guestOsController_listCategoryServices(@Path("categoryId") String categoryId);

    @POST("guest/requests")
    Call<JsonObject> guestOsController_createRequest(@Body JsonObject body);

    @GET("guest/requests")
    Call<JsonObject> guestOsController_listRequests();

    @PATCH("guest/requests/{requestId}/cancel")
    Call<JsonObject> guestOsController_cancelRequest(@Path("requestId") String requestId);

    @POST("guest/session/close")
    Call<JsonObject> guestOsController_closeSession();
}
