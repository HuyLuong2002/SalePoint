package com.example.salepoint.services;

import com.example.salepoint.model.Service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServicesService {
    @GET("services")
    Call<List<Service>> getServices();

}
