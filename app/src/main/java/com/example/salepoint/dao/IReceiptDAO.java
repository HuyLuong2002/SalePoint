package com.example.salepoint.dao;


import com.example.salepoint.model.Receipt;
import com.example.salepoint.request.ReceiptFilterRequest;
import com.example.salepoint.response.PointListResponse;
import com.example.salepoint.response.PointResponse;
import com.example.salepoint.response.ReceiptCarInfoResponse;
import com.example.salepoint.response.ReceiptResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IReceiptDAO {

    @GET("point/{user_id}")
    Call<PointResponse> getPointByUserId(@Path("user_id") String userId);

    @GET("points")
    Call<PointListResponse> getAllPoints();

    @GET("admin/receipts")
    Call<ReceiptResponse> getAllReceiptForManager();

    @POST("receipts/new")
    Call<Void> createReceipt(@Body Receipt receipt);

    @DELETE("receipts/{id}")
    Call<Void> removeReceipt(@Path("id") String serviceId);

    @POST("receipts/filter")
    Call<ReceiptResponse> filterReceipt(@Body ReceiptFilterRequest request);
}
