package com.example.salepoint.dao;


import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.Receipt;
import com.example.salepoint.response.PointResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IReceiptDAO {

    @GET("point/{user_id}")
    Call<PointResponse> getPointByUserId(@Path("user_id") String userId);

    @POST("receipt/new")
    Call<Void> createReceipt(@Body Receipt receipt);
}
