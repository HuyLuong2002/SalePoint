package com.example.salepoint.dao;

import com.example.salepoint.NotificationData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi {

    @POST("v1/projects/{project_id}/messages:send")
    Call<Void> sendNotification(
            @Header("Authorization") String authToken,
            @Body NotificationData notificationRequest,
            @Header("project_id") String projectId // Thay project_id bằng project_id thực của bạn
    );
}
