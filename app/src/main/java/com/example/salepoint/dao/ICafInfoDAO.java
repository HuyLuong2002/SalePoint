package com.example.salepoint.dao;

import com.example.salepoint.model.CarInfo;
import com.example.salepoint.model.Service;
import com.example.salepoint.response.CarInfoResponse;
import com.example.salepoint.response.ServiceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ICafInfoDAO {

    @GET("{user_id}/my_car_info")
    Call<CarInfoResponse> getCarsOfUser(@Path("user_id") String userId);

    @POST("car_info/new")
    Call<Void> createCarInfo(@Body CarInfo carInfo);
}
