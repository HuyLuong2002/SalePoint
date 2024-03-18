package com.example.salepoint.dao;

import com.example.salepoint.model.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IServiceDAO {
    @GET("services")
    Call<List<Service>> getServices();

}
