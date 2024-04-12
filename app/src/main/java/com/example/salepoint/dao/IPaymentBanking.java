package com.example.salepoint.dao;

import com.example.salepoint.request.PaymentRequest;
import com.example.salepoint.response.PaymentResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IPaymentBanking {
    @GET("stripeapikey")
    Call<PaymentResponse> getStripeApiKey();

    @POST("payment/process")
    Call<PaymentResponse> processPayment(@Header("Authorization") String apiKey, @Body PaymentRequest paymentRequest);
}
