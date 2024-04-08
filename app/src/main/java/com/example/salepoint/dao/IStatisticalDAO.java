package com.example.salepoint.dao;

import com.example.salepoint.response.ServiceResponse;
import com.example.salepoint.response.Top5CustomerMaxPointResponse;
import com.example.salepoint.response.TotalRevenueResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IStatisticalDAO {

    @GET("statistical/totalRevenue")
    Call<TotalRevenueResponse> getTotalRevenue();

    @GET("statistical/topCustomer")
    Call<Top5CustomerMaxPointResponse> getTop5CustomerMaxPoint();
}
