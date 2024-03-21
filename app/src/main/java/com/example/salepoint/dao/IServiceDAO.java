package com.example.salepoint.dao;

import com.example.salepoint.model.Service;
import com.example.salepoint.response.ServiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IServiceDAO {
    @GET("admin/services")
    Call<ServiceResponse> getServices();
    @GET("services")
    Call<ServiceResponse> getClientServices();
    @POST("services/new")
    Call<Void> createService(@Body Service newService);


    @PUT("services/{id}")
    Call<Void> updateService(@Path("id") String serviceId, @Body Service updatedService);

    @DELETE("services/{id}")
    Call<Void> removeService(@Path("id") String serviceId);
}
