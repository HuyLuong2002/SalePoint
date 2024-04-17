package com.example.salepoint.dao;

import com.example.salepoint.response.HistoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IHistoryDAO {
    @GET("history/{user_id}")
    Call<HistoryResponse> getHistoryByUserId(@Path("user_id") String userId);
}
